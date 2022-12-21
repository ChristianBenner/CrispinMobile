package com.crispin.demos.scenes;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES30.glBindVertexArray;

import static com.crispin.crispinmobile.Rendering.Utilities.RenderObject.BYTES_PER_FLOAT;

import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Pair;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Entities.SpotLight;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Models.ModelProperties;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.MaterialHandles;
import com.crispin.crispinmobile.Rendering.Shaders.LightingShader;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;
import com.crispin.crispinmobile.Rendering.Shaders.UniformColourShader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.Rendering.Utilities.RenderBatch;
import com.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.crispin.crispinmobile.Utilities.OBJModelLoader;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.crispin.demos.R;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL;

public class IndexDemo extends Scene {
    class IndexShader extends Shader {
        // Attribute for the instances of model matrices
        int modelMatrixInstanceAttributeHandle;

        public IndexShader() {
            super("Index Demo Shader", R.raw.index_vert, R.raw.index_frag);
            positionAttributeHandle = getAttribute("aPosition");
            modelMatrixInstanceAttributeHandle = getAttribute("aModel");
            projectionMatrixUniformHandle = getUniform("uProjection");
            viewMatrixUniformHandle = getUniform("uView");
        }

        public void bindModelMatrixInstanceVAO(int instanceVAO) {
            GLES30.glBindVertexArray(instanceVAO);

            glEnableVertexAttribArray(modelMatrixInstanceAttributeHandle);
            glVertexAttribPointer(modelMatrixInstanceAttributeHandle, 4, GL_FLOAT, false, stride, 0);
            glEnableVertexAttribArray(modelMatrixInstanceAttributeHandle + 1);
            glVertexAttribPointer(modelMatrixInstanceAttributeHandle + 1, 4, GL_FLOAT, false, stride, vec4Size);
            glEnableVertexAttribArray(modelMatrixInstanceAttributeHandle + 2);
            glVertexAttribPointer(modelMatrixInstanceAttributeHandle + 2, 4, GL_FLOAT, false, stride, 2 * vec4Size);
            glEnableVertexAttribArray(modelMatrixInstanceAttributeHandle + 3);
            glVertexAttribPointer(modelMatrixInstanceAttributeHandle + 3, 4, GL_FLOAT, false, stride, 3 * vec4Size);

            GLES30.glVertexAttribDivisor(modelMatrixInstanceAttributeHandle, 1);
            GLES30.glVertexAttribDivisor(modelMatrixInstanceAttributeHandle + 1, 1);
            GLES30.glVertexAttribDivisor(modelMatrixInstanceAttributeHandle + 2, 1);
            GLES30.glVertexAttribDivisor(modelMatrixInstanceAttributeHandle + 3, 1);

            GLES30.glBindVertexArray(0);
        }
    }

    private Model torus;
    private final Camera camera;
    private IndexShader indexShader;
    private float cameraZCount = 0.0f;
    private int instanceVBO;

    final int NUM_INSTANCES = 5000;
    final int vec4Size = BYTES_PER_FLOAT * 4;
    final int stride = 4 * vec4Size; // we want to stride of 4x vec4's (jump a mat4 num bytes each time)
    final float boxSize = 30.0f;
    final int numMatrices = NUM_INSTANCES;
    final int numFloatsPerMatrix = 16;
    final int totalNumFloats = numMatrices * numFloatsPerMatrix;
    final int totalBytes = totalNumFloats * 4;
    final int numBytesPerMatrix = numFloatsPerMatrix * 4;

    public IndexDemo() {
        Crispin.setBackgroundColour(Colour.WHITE);

        indexShader = new IndexShader();
        torus = OBJModelLoader.readObjFile(R.raw.torus_uv);
        torus.useCustomShader(indexShader);

        camera = new Camera();
        camera.setPosition(new Vec3(0.0f, 0f, boxSize));

        FloatBuffer modelMatrixBuffer = FloatBuffer.allocate(totalNumFloats);

        Random r = new Random();
        for(int i = 0; i < NUM_INSTANCES; i++) {
            float x = (r.nextFloat() * boxSize) - (boxSize/2.0f);
            float y = (r.nextFloat() * boxSize) - (boxSize/2.0f);
            float z = (r.nextFloat() * boxSize) - (boxSize/2.0f);
            float rotateAngle = r.nextFloat() * 360.0f;
            float scale = (1.0f + r.nextFloat()) / 4f;

            ModelMatrix modelMatrix = new ModelMatrix();
            modelMatrix.translate(x, y, z);
            modelMatrix.rotate(rotateAngle, 0.4f, 0.6f, 0.8f);
            modelMatrix.scale(scale);
            modelMatrixBuffer.put(modelMatrix.getModelMatrix());
        }
        modelMatrixBuffer.position(0);

        int[] instanceVBO = new int[1];
        GLES30.glGenBuffers(1, instanceVBO, 0);
        this.instanceVBO = instanceVBO[0];
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, instanceVBO[0]);
        GLES30.glBufferData(GL_ARRAY_BUFFER, totalBytes, modelMatrixBuffer, GL_STATIC_DRAW);
        indexShader.bindModelMatrixInstanceVAO(torus.vao);

        setPosition(1, 0.0f, -1.0f, 35.0f);
    }

    // An example of how to set the position/model matrix of just one of the objects in the group,
    // meaning that each object can still have on-demand position, scale and rotation changes
    private void setPosition(int index, float x, float y, float z) {
        FloatBuffer modelMatrixBuffer = FloatBuffer.allocate(numFloatsPerMatrix);
        ModelMatrix modelMatrix = new ModelMatrix();
        modelMatrix.translate(x, y, z);
        modelMatrixBuffer.put(modelMatrix.getModelMatrix());
        modelMatrixBuffer.position(0);

        GLES30.glBindBuffer(GL_ARRAY_BUFFER, instanceVBO);
        GLES30.glBufferSubData(GL_ARRAY_BUFFER,index * numBytesPerMatrix, numBytesPerMatrix, modelMatrixBuffer);
    }

    @Override
    public void update(float deltaTime) {
        cameraZCount += 0.01f * deltaTime;
        float cameraZ = ((float)Math.sin(cameraZCount) * boxSize * 1.4f) + boxSize;
        camera.setPosition(0.0f, 0.0f, cameraZ);
    }

    @Override
    public void render() {
        indexShader.enableIt();
        glUniformMatrix4fv(indexShader.getProjectionMatrixUniformHandle(),1,false, camera.getPerspectiveMatrix(), 0);
        glUniformMatrix4fv(indexShader.getViewMatrixUniformHandle(), 1, false, camera.getViewMatrix(), 0);
        GLES30.glBindVertexArray(torus.vao);
        GLES30.glDrawArraysInstanced(GL_TRIANGLES, 0, torus.vertexCount, NUM_INSTANCES);
        GLES30.glBindVertexArray(0);
        indexShader.disableIt();
    }

    @Override
    public void touch(int type, Vec2 position) {

    }
}