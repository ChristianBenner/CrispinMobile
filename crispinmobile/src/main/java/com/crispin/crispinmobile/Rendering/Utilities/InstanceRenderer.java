package com.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES30.GL_INVALID_INDEX;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.GL_TEXTURE0;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.glActiveTexture;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.GL_CULL_FACE;
import static android.opengl.GLES30.glEnable;
import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glVertexAttribPointer;
import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_STATIC_DRAW;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glDrawArraysInstanced;
import static android.opengl.GLES30.glVertexAttribDivisor;

import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Data.Texture;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Entities.SpotLight;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceGlobalColourShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceLightingShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceTextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class InstanceRenderer {
    private final int NUM_BYTES_FLOAT = 4;
    private final int NUM_FLOATS_MATRIX = 16;
    private final int NUM_BYTES_MATRIX = NUM_FLOATS_MATRIX * NUM_BYTES_FLOAT;
    private final int NUM_FLOATS_VEC4 = 4;
    private final int NUM_BYTES_VEC4 = NUM_FLOATS_VEC4 * NUM_BYTES_FLOAT;
    private Mesh mesh;
    private ArrayList<ModelMatrix> modelMatrices;
    private int matricesVBO;
    private Shader shader;
    private Material material;
    private Colour globalColour = Colour.WHITE;
    private LightGroup lightGroup;

    public InstanceRenderer(Mesh mesh) {
        this.mesh = mesh;

        modelMatrices = new ArrayList<>();
        material = new Material();

        // Generate virtual buffer object for matrices instances
        int[] matricesVBO = new int[1];
        glGenBuffers(1, matricesVBO, 0);
        this.matricesVBO = matricesVBO[0];

        glBindVertexArray(mesh.vao);
        glBindBuffer(GL_ARRAY_BUFFER, mesh.vbo);

        this.shader = determineShader(mesh);

        if(mesh.supportsTexture()) {
            glVertexAttribPointer(shader.textureAttributeHandle , mesh.elementsPerTexel, GL_FLOAT,
                    false, mesh.stride, mesh.texelDataOffset * NUM_BYTES_FLOAT);
            glEnableVertexAttribArray(shader.textureAttributeHandle);
        }

        if(mesh.supportsLighting()) {
            glVertexAttribPointer(shader.normalAttributeHandle, mesh.elementsPerNormal, GL_FLOAT,
                    false, mesh.stride, mesh.normalDataOffset * NUM_BYTES_FLOAT);
            glEnableVertexAttribArray(shader.normalAttributeHandle);
        }

        // Enable position regardless of shader choice (they all support position)
        glVertexAttribPointer(shader.positionAttributeHandle,  mesh.elementsPerPosition, GL_FLOAT,
                false, mesh.stride, mesh.positionDataOffset * NUM_BYTES_FLOAT);
        glEnableVertexAttribArray(shader.positionAttributeHandle);
    }

    public void setLightGroup(LightGroup lightGroup) {
        this.lightGroup = lightGroup;
    }

    public void setGlobalColour(Colour colour) {
        this.globalColour = new Colour(colour);
    }

    private Shader determineShader(Mesh mesh) {
        if(mesh.supportsTexture() && mesh.supportsLighting()) {
            // todo: Supports lighting and texture
            //return InstanceTextureLightingShader();
        } else if(mesh.supportsTexture()) {
            return new InstanceTextureShader();
        }else if(mesh.supportsLighting()) {
            return new InstanceLightingShader();
        }

        // Texture and lighting not supported, use uniform colour
        return new InstanceGlobalColourShader();
    }

    public void setTexture(Texture texture) {
        this.material.texture = texture;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void add(ModelMatrix modelMatrix) {
        modelMatrices.add(modelMatrix);
        upload();
    }

    public void add(ModelMatrix[] modelMatrices) {
        for(int i = 0; i < modelMatrices.length; i++){
            this.modelMatrices.add(modelMatrices[i]);
        }
        upload();
    }

    public void remove(ModelMatrix modelMatrix) {
        modelMatrices.remove(modelMatrix);
        upload();
    }

    private void upload() {
        FloatBuffer modelMatrixBuffer = FloatBuffer.allocate(NUM_FLOATS_MATRIX * modelMatrices.size());
        for(int i = 0; i < modelMatrices.size(); i++) {
            modelMatrixBuffer.put(modelMatrices.get(i).getModelMatrix());
        }
        modelMatrixBuffer.position(0);

        glBindBuffer(GL_ARRAY_BUFFER, matricesVBO);
        glBufferData(GL_ARRAY_BUFFER, NUM_BYTES_MATRIX * modelMatrices.size(), modelMatrixBuffer, GL_STATIC_DRAW);

        glBindVertexArray(mesh.vao);
        int h = shader.modelMatrixAttributeHandle;
        glEnableVertexAttribArray(h);
        glVertexAttribPointer(h, 4, GL_FLOAT, false, NUM_BYTES_MATRIX, 0);
        glEnableVertexAttribArray(h + 1);
        glVertexAttribPointer(h + 1, 4, GL_FLOAT, false, NUM_BYTES_MATRIX, NUM_BYTES_VEC4);
        glEnableVertexAttribArray(h + 2);
        glVertexAttribPointer(h + 2, 4, GL_FLOAT, false, NUM_BYTES_MATRIX, 2 * NUM_BYTES_VEC4);
        glEnableVertexAttribArray(h + 3);
        glVertexAttribPointer(h + 3, 4, GL_FLOAT, false, NUM_BYTES_MATRIX, 3 * NUM_BYTES_VEC4);

        glVertexAttribDivisor(h, 1);
        glVertexAttribDivisor(h + 1, 1);
        glVertexAttribDivisor(h + 2, 1);
        glVertexAttribDivisor(h + 3, 1);

        glBindVertexArray(0);
    }

    public void render(Camera camera) {
        shader.enable();

        glEnable(GL_CULL_FACE);

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

        // Set all material uniforms
        shader.setMaterialUniforms(material);

//        if(mesh.supportsTexture()) {
//            if(texture == null) {
//                System.err.println("NULL Texture on instance renderer");
//            } else {
//                glActiveTexture(GL_TEXTURE0);
//                glBindTexture(GL_TEXTURE_2D, texture.getId());
//                glUniform1i(shader.materialHandles.textureUniformHandle, 0);
//            }
//        }

        // Set global uniform colour
        if(shader.materialHandles.colourUniformHandle != GL_INVALID_INDEX) {
            glUniform4f(shader.materialHandles.colourUniformHandle, globalColour.red, globalColour.green, globalColour.blue, globalColour.alpha);
        }

        // Set view matrices
        glUniformMatrix4fv(shader.getProjectionMatrixUniformHandle(),1,false, camera.getPerspectiveMatrix(), 0);
        glUniformMatrix4fv(shader.getViewMatrixUniformHandle(), 1, false, camera.getViewMatrix(), 0);

        // Draw instances
        glBindVertexArray(mesh.vao);
        glDrawArraysInstanced(GL_TRIANGLES, 0, mesh.vertexCount, modelMatrices.size());
        glBindVertexArray(0);

        shader.disable();
    }
}
