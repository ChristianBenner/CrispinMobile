package com.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES20.GL_STREAM_DRAW;
import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_CULL_FACE;
import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glDrawArraysInstanced;
import static android.opengl.GLES30.glEnable;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glVertexAttribDivisor;
import static android.opengl.GLES30.glVertexAttribPointer;

import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Data.Texture;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Entities.SpotLight;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceColourLightingShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceColourLightingTextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceColourShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceColourTextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceGlobalColourShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceLightingShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceLightingTextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceTextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;
import com.crispin.crispinmobile.Utilities.Logger;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class InstanceRendererFinalise {
    private static final String TAG = "InstanceRenderer";

    protected final int NUM_BYTES_FLOAT = 4;
    protected final int NUM_FLOATS_MATRIX = 16;
    protected final int NUM_BYTES_MATRIX = NUM_FLOATS_MATRIX * NUM_BYTES_FLOAT;
    protected final int NUM_FLOATS_VEC4 = 4;
    protected final int NUM_BYTES_VEC4 = NUM_FLOATS_VEC4 * NUM_BYTES_FLOAT;
    protected final int NUM_FLOATS_COLOUR = 4;
    protected final int NUM_BYTES_COLOUR = NUM_FLOATS_COLOUR * NUM_BYTES_FLOAT;

    private Mesh mesh;
    private int matricesVBO;
    private int colourVBO;
    private Shader shader;
    private Material material;
    private LightGroup lightGroup;
    private int instances;
    private boolean instancedColour;

    public InstanceRendererFinalise(Mesh mesh, boolean instancedColour) {
        this.mesh = mesh;
        this.instancedColour = instancedColour;

        material = new Material();

        // Generate virtual buffer object for matrices instances
        int[] matricesVBO = new int[1];
        glGenBuffers(1, matricesVBO, 0);
        this.matricesVBO = matricesVBO[0];

        if(instancedColour) {
            // Generate virtual buffer object for colour instances
            int[] colourVBO = new int[1];
            glGenBuffers(1, colourVBO, 0);
            this.colourVBO = colourVBO[0];
        }

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

    public InstanceRendererFinalise(Mesh mesh, ModelMatrix[] modelMatrices) {
        this(mesh, false);
        uploadModelMatrices(modelMatrices);
    }

    public InstanceRendererFinalise(Mesh mesh, FloatBuffer buffer, int instances) {
        this(mesh, false);
        uploadModelMatrices(buffer, instances);
    }

    public InstanceRendererFinalise(Mesh mesh, float[] modelMatrices) {
        this(mesh, false);
        uploadModelMatrices(modelMatrices);
    }

    public InstanceRendererFinalise(Mesh mesh, ModelMatrix[] modelMatrices, Colour[] colours) {
        this(mesh, true);
        uploadModelMatrices(modelMatrices);
        uploadColourData(colours);
    }

    public InstanceRendererFinalise(Mesh mesh, FloatBuffer modelMatrixBuffer, FloatBuffer colourBuffer, int instances) {
        this(mesh, true);
        uploadModelMatrices(modelMatrixBuffer, instances);
        uploadColourData(colourBuffer, instances);
    }

    public InstanceRendererFinalise(Mesh mesh, float[] modelMatrices, float[] colours) {
        this(mesh, true);
        uploadModelMatrices(modelMatrices);
        uploadColourData(colours);
    }

    public void uploadModelMatrices(FloatBuffer buffer, int instances) {
        this.instances = instances;
        buffer.position(0);

        glBindBuffer(GL_ARRAY_BUFFER, matricesVBO);
        glBufferData(GL_ARRAY_BUFFER, NUM_BYTES_MATRIX * instances, buffer, GL_STREAM_DRAW);

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

    public void uploadModelMatrices(float[] modelMatrices) {
        int count = modelMatrices.length / NUM_FLOATS_MATRIX;
        uploadModelMatrices(FloatBuffer.wrap(modelMatrices), count);
    }

    public void uploadModelMatrices(ModelMatrix[] modelMatrices) {
        int count = modelMatrices.length;

        FloatBuffer buffer = FloatBuffer.allocate(NUM_FLOATS_MATRIX * count);
        for(int i = 0; i < count; i++) {
            buffer.put(modelMatrices[i].getFloats());
        }

        uploadModelMatrices(buffer, count);
    }

    public void uploadColourData(FloatBuffer colourBuffer, int instances) {
        // If instanced colour is not yet enabled, then generate the buffer, load the correct shader and enable it
        if(!instancedColour) {
            Logger.error(TAG, "Colour per-instance rendering not enabled but colour buffer provided");
            return;
        }

        colourBuffer.position(0);

        // Upload data
        glBindBuffer(GL_ARRAY_BUFFER, colourVBO);
        glBufferData(GL_ARRAY_BUFFER, NUM_BYTES_COLOUR * instances, colourBuffer, GL_STREAM_DRAW);

        // Bind vertex attribs
        glBindVertexArray(mesh.vao);

        int h = shader.colourAttributeHandle;
        glEnableVertexAttribArray(h);
        glVertexAttribPointer(h, 4, GL_FLOAT, false, NUM_BYTES_COLOUR, 0);
        glVertexAttribDivisor(h, 1);
        glBindVertexArray(0);
    }

    public void uploadColourData(float[] colours) {
        int count = colours.length / NUM_FLOATS_COLOUR;
        uploadColourData(FloatBuffer.wrap(colours), count);
    }

    public void uploadColourData(Colour[] colours) {
        int count = colours.length;
        FloatBuffer colourBuffer = FloatBuffer.allocate(NUM_FLOATS_COLOUR * count);
        for(int i = 0; i < count; i++) {
            colourBuffer.put(colours[i].red);
            colourBuffer.put(colours[i].green);
            colourBuffer.put(colours[i].blue);
            colourBuffer.put(colours[i].alpha);
        }

        uploadColourData(colourBuffer, count);
    }

    public void setLightGroup(LightGroup lightGroup) {
        this.lightGroup = lightGroup;
    }

    public void setGlobalColour(Colour colour) {
        this.material.colour = new Colour(colour);
    }

    public void setTexture(Texture texture) {
        this.material.texture = texture;
    }

    public void setMaterial(Material material) {
        this.material = material;
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

        // Set view matrices
        glUniformMatrix4fv(shader.getProjectionMatrixUniformHandle(),1,false, camera.getPerspectiveMatrix(), 0);
        glUniformMatrix4fv(shader.getViewMatrixUniformHandle(), 1, false, camera.getViewMatrix(), 0);

        // Draw instances
        glBindVertexArray(mesh.vao);
        glDrawArraysInstanced(GL_TRIANGLES, 0, mesh.vertexCount, instances);
        glBindVertexArray(0);

        shader.disable();
    }

    protected int getMeshVAO() {
        return mesh.vao;
    }

    protected Shader getShader() {
        return shader;
    }

    protected Shader determineShader(Mesh mesh) {
        if(instancedColour) {
            if(mesh.supportsTexture() && mesh.supportsLighting()) {
                return new InstanceColourLightingTextureShader();
            } else if(mesh.supportsTexture()) {
                return new InstanceColourTextureShader();
            }else if(mesh.supportsLighting()) {
                return new InstanceColourLightingShader();
            }

            return new InstanceColourShader();
        }

        if(mesh.supportsTexture() && mesh.supportsLighting()) {
            return new InstanceLightingTextureShader();
        } else if(mesh.supportsTexture()) {
            return new InstanceTextureShader();
        }else if(mesh.supportsLighting()) {
            return new InstanceLightingShader();
        }

        // Texture and lighting not supported, use uniform colour
        return new InstanceGlobalColourShader();
    }
}
