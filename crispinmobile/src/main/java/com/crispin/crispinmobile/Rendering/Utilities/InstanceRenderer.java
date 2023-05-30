package com.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_STREAM_DRAW;
import static android.opengl.GLES20.glBufferSubData;
import static android.opengl.GLES20.glUniform2f;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_CULL_FACE;
import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_INVALID_INDEX;
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
import static com.crispin.crispinmobile.Rendering.Shaders.Shader.UNDEFINED_HANDLE;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Data.Texture;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Entities.SpotLight;
import com.crispin.crispinmobile.Rendering.Models.ModelProperties;
import com.crispin.crispinmobile.Rendering.Models.ShadowModel;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceColourLightingShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceColourLightingShader2D;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceColourLightingTextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceColourLightingTextureShader2D;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceColourShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceColourTextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceGlobalColourShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceLightingShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceLightingShader2D;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceLightingTextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceLightingTextureShader2D;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceTextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;
import com.crispin.crispinmobile.Utilities.Logger;
import com.crispin.crispinmobile.Utilities.TextureCache;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class InstanceRenderer {
    private static final String TAG = "InstanceRenderer";

    protected final int NUM_BYTES_FLOAT = 4;
    protected final int NUM_FLOATS_MATRIX = 16;
    protected final int NUM_BYTES_MATRIX = NUM_FLOATS_MATRIX * NUM_BYTES_FLOAT;
    protected final int NUM_FLOATS_VEC4 = 4;
    protected final int NUM_BYTES_VEC4 = NUM_FLOATS_VEC4 * NUM_BYTES_FLOAT;
    protected final int NUM_FLOATS_COLOUR = 4;
    protected final int NUM_BYTES_COLOUR = NUM_FLOATS_COLOUR * NUM_BYTES_FLOAT;

    protected Mesh mesh;
    protected boolean instancedColour;
    protected boolean lightingSupport;
    protected Shader shader;
    protected Material material;
    protected LightGroup lightGroup;
    protected int instances;

    private float[] default2DViewMatrix;
    private int matricesVBO;
    private int colourVBO;
    private boolean customShader;

    public InstanceRenderer(Mesh mesh, boolean lightingSupport, boolean instancedColour) {
        this.mesh = mesh;
        if(lightingSupport) {
            if(mesh.supportsLighting()) {
                this.lightingSupport = true;
            } else {
                Logger.error(TAG, "Lighting cannot be enabled for a mesh that does not support lighting");
            }
        }
        this.instancedColour = instancedColour;

        default2DViewMatrix = new float[NUM_FLOATS_MATRIX];
        Matrix.setIdentityM(default2DViewMatrix, 0);

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

        determineShader();
        setVertexAttributeArrays();
    }

    public InstanceRenderer(Mesh mesh, boolean lightingSupport, ModelMatrix[] modelMatrices) {
        this(mesh, lightingSupport, false);
        uploadModelMatrices(modelMatrices);
    }

    public InstanceRenderer(Mesh mesh, boolean lightingSupport, ModelProperties[] modelProperties) {
        this(mesh, lightingSupport, false);
        uploadModelMatrices(modelProperties);
    }

    public InstanceRenderer(Mesh mesh, boolean lightingSupport, FloatBuffer buffer, int instances) {
        this(mesh, lightingSupport, false);
        uploadModelMatrices(buffer, instances);
    }

    public InstanceRenderer(Mesh mesh, boolean lightingSupport, float[] modelMatrices) {
        this(mesh, lightingSupport, false);
        uploadModelMatrices(modelMatrices);
    }

    public InstanceRenderer(Mesh mesh, boolean lightingSupport, ModelMatrix[] modelMatrices, Colour[] colours) {
        this(mesh, lightingSupport, true);
        uploadModelMatrices(modelMatrices);
        uploadColourData(colours);
    }

    public InstanceRenderer(Mesh mesh, boolean lightingSupport, FloatBuffer modelMatrixBuffer, FloatBuffer colourBuffer, int instances) {
        this(mesh, lightingSupport, true);
        uploadModelMatrices(modelMatrixBuffer, instances);
        uploadColourData(colourBuffer, instances);
    }

    public InstanceRenderer(Mesh mesh, boolean lightingSupport, float[] modelMatrices, float[] colours) {
        this(mesh, lightingSupport, true);
        uploadModelMatrices(modelMatrices);
        uploadColourData(colours);
    }

    public void setShader(Shader shader) {
        customShader = true;
        this.shader = shader;
        setVertexAttributeArrays();
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

    public void uploadModelMatrices(ModelProperties[] modelProperties) {
        int count = modelProperties.length;

        FloatBuffer buffer = FloatBuffer.allocate(NUM_FLOATS_MATRIX * count);
        for(int i = 0; i < count; i++) {
            buffer.put(modelProperties[i].getModelMatrix().getFloats());
        }

        uploadModelMatrices(buffer, count);
    }

    public void uploadModelMatrix(ModelMatrix modelMatrix, int index) {
        FloatBuffer buffer = FloatBuffer.allocate(NUM_FLOATS_MATRIX);
        buffer.put(modelMatrix.getFloats());
        buffer.position(0);

        glBindBuffer(GL_ARRAY_BUFFER, matricesVBO);
        glBufferSubData(GL_ARRAY_BUFFER, index * NUM_BYTES_MATRIX, NUM_BYTES_MATRIX, buffer);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
//        glBindVertexArray(mesh.vao);
//        int h = shader.modelMatrixAttributeHandle;
//        glEnableVertexAttribArray(h);
//        glVertexAttribPointer(h, 4, GL_FLOAT, false, NUM_BYTES_MATRIX, 0);
//        glEnableVertexAttribArray(h + 1);
//        glVertexAttribPointer(h + 1, 4, GL_FLOAT, false, NUM_BYTES_MATRIX, NUM_BYTES_VEC4);
//        glEnableVertexAttribArray(h + 2);
//        glVertexAttribPointer(h + 2, 4, GL_FLOAT, false, NUM_BYTES_MATRIX, 2 * NUM_BYTES_VEC4);
//        glEnableVertexAttribArray(h + 3);
//        glVertexAttribPointer(h + 3, 4, GL_FLOAT, false, NUM_BYTES_MATRIX, 3 * NUM_BYTES_VEC4);
//
//        glVertexAttribDivisor(h, 1);
//        glVertexAttribDivisor(h + 1, 1);
//        glVertexAttribDivisor(h + 2, 1);
//        glVertexAttribDivisor(h + 3, 1);

        glBindVertexArray(0);
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

    public void setTexture(int texture) {
        this.material.texture = TextureCache.loadTexture(texture);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void render(Camera camera) {
        shader.enable();

        glEnable(GL_CULL_FACE);

        setUniforms(camera.getPerspectiveMatrix(), camera.getViewMatrix(), camera.getPosition());

        // Draw instances
        glBindVertexArray(mesh.vao);
        glDrawArraysInstanced(GL_TRIANGLES, 0, mesh.vertexCount, instances);
        glBindVertexArray(0);

        shader.disable();
    }

    public void render(Camera2D camera2D) {
        // Check if depth is enabled, and disable it
        final boolean DEPTH_ENABLED = GLES30.glIsEnabled(GL_DEPTH_TEST);
        if(DEPTH_ENABLED) {
            GLES30.glDisable(GL_DEPTH_TEST);
        }

        shader.enable();

        setUniforms(camera2D.getOrthoMatrix(), default2DViewMatrix);

        // Draw instances
        glBindVertexArray(mesh.vao);
        glDrawArraysInstanced(GL_TRIANGLES, 0, mesh.vertexCount, instances);
        glBindVertexArray(0);

        shader.disable();

        // If depth was enabled before calling the function then re-enable it
        if (DEPTH_ENABLED) {
            glEnable(GL_DEPTH_TEST);
        }
    }

    protected int getMeshVAO() {
        return mesh.vao;
    }

    protected Shader getShader() {
        return shader;
    }

    protected void determineShader() {
        if(customShader) {
            return;
        }

        if(instancedColour) {
            if(mesh.supportsTexture() && lightingSupport) {
                if(mesh.elementsPerPosition == 2) {
                    this.shader = new InstanceColourLightingTextureShader2D();
                } else {
                    this.shader = new InstanceColourLightingTextureShader();
                }
            } else if(mesh.supportsTexture()) {
                this.shader = new InstanceColourTextureShader();
            } else if(lightingSupport) {
                if(mesh.elementsPerPosition == 2) {
                    this.shader = new InstanceColourLightingShader2D();
                } else {
                    this.shader = new InstanceColourLightingShader();
                }
            } else {
                this.shader = new InstanceColourShader();
            }
        } else {
            if(mesh.supportsTexture() && lightingSupport) {
                if(mesh.elementsPerPosition == 2) {
                    this.shader = new InstanceLightingTextureShader2D();
                } else {
                    this.shader = new InstanceLightingTextureShader();
                }
            } else if(mesh.supportsTexture()) {
                this.shader = new InstanceTextureShader();
            } else if(lightingSupport) {
                if(mesh.elementsPerPosition == 2) {
                    this.shader = new InstanceLightingShader2D();
                } else {
                    this.shader = new InstanceLightingShader();
                }
            } else {
                // Texture and lighting not supported, use uniform colour
                this.shader = new InstanceGlobalColourShader();
            }
        }
    }

    private void setUniforms(float[] projectionMatrix, float[] viewMatrix, Vec3 viewPosition) {
        if (shader.validHandle(shader.getViewPositionUniformHandle())) {
            final Vec3 cameraPos = viewPosition;
            glUniform3f(shader.getViewPositionUniformHandle(), cameraPos.x, cameraPos.y,
                    cameraPos.z);
        }

        setUniforms(projectionMatrix, viewMatrix);
    }

    private void setUniforms(float[] projectionMatrix, float[] viewMatrix) {
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

        // View dimension should be set to the view width, height
        if (shader.validHandle(shader.getViewDimensionUniformHandle())) {
            glUniform2f(shader.getViewDimensionUniformHandle(), Crispin.getSurfaceWidth(),
                    Crispin.getSurfaceHeight());
        }

        // Set all material uniforms
        shader.setMaterialUniforms(material);

        // Set view matrices
        glUniformMatrix4fv(shader.getProjectionMatrixUniformHandle(),1,false, projectionMatrix, 0);
        glUniformMatrix4fv(shader.getViewMatrixUniformHandle(), 1, false, viewMatrix, 0);
    }

    private void setVertexAttributeArrays() {
        glBindVertexArray(mesh.vao);
        glBindBuffer(GL_ARRAY_BUFFER, mesh.vbo);

        if(shader.textureAttributeHandle != UNDEFINED_HANDLE && mesh.elementsPerTexel != 0) {
            glVertexAttribPointer(shader.textureAttributeHandle , mesh.elementsPerTexel, GL_FLOAT,
                    false, mesh.stride, mesh.texelDataOffset * NUM_BYTES_FLOAT);
            glEnableVertexAttribArray(shader.textureAttributeHandle);
        }

        if(shader.normalAttributeHandle != UNDEFINED_HANDLE && lightingSupport) {
            glVertexAttribPointer(shader.normalAttributeHandle, mesh.elementsPerNormal, GL_FLOAT,
                    false, mesh.stride, mesh.normalDataOffset * NUM_BYTES_FLOAT);
            glEnableVertexAttribArray(shader.normalAttributeHandle);
        }

        if(shader.tangentAttributeHandle != UNDEFINED_HANDLE && mesh.elementsPerTangent != 0) {
            glVertexAttribPointer(shader.tangentAttributeHandle, mesh.elementsPerTangent, GL_FLOAT,
                    false, mesh.stride, mesh.tangentDataOffset * NUM_BYTES_FLOAT);
            glEnableVertexAttribArray(shader.tangentAttributeHandle);
        }

        if(shader.bitangentAttributeHandle != UNDEFINED_HANDLE && mesh.elementsPerBitangent != 0) {
            glVertexAttribPointer(shader.bitangentAttributeHandle, mesh.elementsPerBitangent,
                    GL_FLOAT, false, mesh.stride, mesh.bitangentDataOffset * NUM_BYTES_FLOAT);
            glEnableVertexAttribArray(shader.bitangentAttributeHandle);
        }

        // Enable position regardless of shader choice (they all support position)
        glVertexAttribPointer(shader.positionAttributeHandle,  mesh.elementsPerPosition, GL_FLOAT,
                false, mesh.stride, mesh.positionDataOffset * NUM_BYTES_FLOAT);
        glEnableVertexAttribArray(shader.positionAttributeHandle);

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
