package com.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES30.GL_LINES;
import static android.opengl.GLES30.GL_POINTS;
import static android.opengl.GLES30.glDrawArrays;
import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_STATIC_DRAW;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.glUniform3f;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glVertexAttribPointer;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glVertexAttribDivisor;

import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Entities.SpotLight;
import com.crispin.crispinmobile.Rendering.Models.ModelProperties;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashSet;

public class RenderBatch {
    // Number of uniform elements to upload in a single GLSL uniform upload
    private static final int UNIFORM_UPLOAD_COUNT_SINGLE = 1;

    private RenderObject renderObject;
    private HashSet<ModelProperties> batch;

    private Shader shader;
    private Camera camera;
    private LightGroup lightGroup;

    public RenderBatch() {
        batch = new HashSet<>();
    }

    public void setRenderObject(RenderObject renderObject) {
        this.renderObject = renderObject;
    }

    public void add(ModelProperties modelProperties) {
        batch.add(modelProperties);
    }

    public void remove(ModelProperties modelProperties) {
        batch.remove(modelProperties);
    }

    public void clear() {
        batch.clear();
    }

    public void setShader(Shader shader) {
        this.shader = shader;
        renderObject.setAttributePointers(shader.positionAttributeHandle,
                shader.textureAttributeHandle, shader.normalAttributeHandle);
       // instanceStuff();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setLightGroup(LightGroup lightGroup) {
        this.lightGroup = lightGroup;
    }

    private void instanceStuff() {
        int[] instanceVBO = new int[1];
        glGenBuffers(1, instanceVBO, 0);
        glBindBuffer(GL_ARRAY_BUFFER, instanceVBO[0]);

        // Mat is 4x4 of floats (4 bytes each) so each mat = 4*4*4 bytes
        int modelMatNumFloats = 4 * 4;
        int modelMatNumBytes = modelMatNumFloats * 4;

        FloatBuffer md = FloatBuffer.allocate(batch.size() * modelMatNumFloats);
        for(ModelProperties modelProperties : batch) {
            md.put(modelProperties.modelMatrix.getModelMatrix());
        }
        md.position(0);

        glBufferData(GL_ARRAY_BUFFER, batch.size() * modelMatNumBytes, md, GL_STATIC_DRAW);



        int aLoc = shader.instanceMatrixAttributeHandle;
        glBindVertexArray(renderObject.vao);
        glEnableVertexAttribArray(aLoc);
        glVertexAttribPointer(aLoc, 4, GL_FLOAT, false, modelMatNumBytes, 0);
        glEnableVertexAttribArray(aLoc + 1);
        glVertexAttribPointer(aLoc + 1, 4, GL_FLOAT, false, modelMatNumBytes, 4 * 1 * 4);
        glEnableVertexAttribArray(aLoc + 2);
        glVertexAttribPointer(aLoc + 2, 4, GL_FLOAT, false, modelMatNumBytes, 4 * 2 * 4);
        glEnableVertexAttribArray(aLoc + 3);
        glVertexAttribPointer(aLoc + 3, 4, GL_FLOAT, false, modelMatNumBytes, 4 * 3 * 4);
        glVertexAttribDivisor(aLoc, 1);
        glVertexAttribDivisor(aLoc + 1, 1);
        glVertexAttribDivisor(aLoc + 2, 1);
        glVertexAttribDivisor(aLoc + 3, 1);
        glBindVertexArray(0);
    }

    public void render() {
        shader.enable();

        if (lightGroup != null) {
            final DirectionalLight directionalLight = lightGroup.getDirectionalLight();
            if (directionalLight != null) {
                shader.setDirectionalLightUniforms(directionalLight);
            }

            final ArrayList<PointLight> pointLights = lightGroup.getPointLights();
            if (shader.validHandle(shader.getNumPointLightsUniformHandle())) {
                glUniform1i(shader.getNumPointLightsUniformHandle(), pointLights.size());
            }

            // Iterate through point lights, uploading each to the shader
            for (int i = 0; i < pointLights.size() && i < shader.getMaxPointLights(); i++) {
                final PointLight pointLight = pointLights.get(i);
                shader.setPointLightUniforms(i, pointLight);
            }

            final ArrayList<SpotLight> spotLights = lightGroup.getSpotLights();
            if (shader.validHandle(shader.getNumSpotLightsUniformHandle())) {
                glUniform1i(shader.getNumSpotLightsUniformHandle(), spotLights.size());
            }

            // Iterate through spot lights, uploading each to the shader
            for (int i = 0; i < spotLights.size() && i < shader.getMaxSpotLights(); i++) {
                final SpotLight spotLight = spotLights.get(i);
                shader.setSpotLightUniforms(i, spotLight);
            }
        }

        if (shader.validHandle(shader.getViewPositionUniformHandle())) {
            final Vec3 cameraPos = camera.getPosition();
            glUniform3f(shader.getViewPositionUniformHandle(), cameraPos.x, cameraPos.y,
                    cameraPos.z);
        }

        if (shader.validHandle(shader.getProjectionMatrixUniformHandle())) {
            glUniformMatrix4fv(shader.getProjectionMatrixUniformHandle(),
                    UNIFORM_UPLOAD_COUNT_SINGLE,
                    false,
                    camera.getPerspectiveMatrix(),
                    0);
        }

        if (shader.validHandle(shader.getViewMatrixUniformHandle())) {
            glUniformMatrix4fv(shader.getViewMatrixUniformHandle(),
                    UNIFORM_UPLOAD_COUNT_SINGLE,
                    false,
                    camera.getViewMatrix(),
                    0);
        }

        glBindVertexArray(renderObject.vao);
     //   glDrawArraysInstanced(GL_TRIANGLES, 0, renderObject.vertexCount, batch.size());

        for(ModelProperties properties : batch) {
            properties.updateModelMatrix();

            if (shader.validHandle(shader.getModelMatrixUniformHandle())) {
                glUniformMatrix4fv(shader.getModelMatrixUniformHandle(),
                        UNIFORM_UPLOAD_COUNT_SINGLE,
                        false,
                        properties.modelMatrix.getModelMatrix(),
                        0);
            }

            // Set all material uniforms
            shader.setMaterialUniforms(properties.material);

            // Draw the vertex data with the specified render method
            switch (renderObject.renderMethod) {
                case POINTS:
                    glDrawArrays(GL_POINTS, 0, renderObject.vertexCount);
                    break;
                case LINES:
                    glDrawArrays(GL_LINES, 0, renderObject.vertexCount);
                    break;
                case TRIANGLES:
                    glDrawArrays(GL_TRIANGLES, 0, renderObject.vertexCount);
                    break;
            }
        }
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
        shader.disable();
    }
}
