package com.crispin.demos.scenes;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRONT_AND_BACK;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

import static com.crispin.crispinmobile.Rendering.Utilities.RenderObject.BYTES_PER_FLOAT;

import android.opengl.GLES30;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Scale3D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Models.Cube;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.Utilities.OBJModelLoader;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.SceneManager;
import com.crispin.demos.R;

import java.nio.FloatBuffer;
import java.util.Random;

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

    final int NUM_INSTANCES = 200000;
    final int vec4Size = BYTES_PER_FLOAT * 4;
    final int stride = 4 * vec4Size; // we want to stride of 4x vec4's (jump a mat4 num bytes each time)
    final Scale3D boxSize = new Scale3D(30.0f, 30.0f, 100.0f);
    final int numMatrices = NUM_INSTANCES;
    final int numFloatsPerMatrix = 16;
    final int totalNumFloats = numMatrices * numFloatsPerMatrix;
    final int totalBytes = totalNumFloats * 4;
    final int numBytesPerMatrix = numFloatsPerMatrix * 4;

    public IndexDemo() {
        Crispin.setBackgroundColour(Colour.WHITE);

        indexShader = new IndexShader();
        torus = new Cube(false, false, false);
    //    torus = OBJModelLoader.readObjFile(R.raw.monkey);
        torus.useCustomShader(indexShader);

        camera = new Camera();
        camera.setPosition(new Vec3(0.0f, 0f, boxSize.z));

        FloatBuffer modelMatrixBuffer = FloatBuffer.allocate(totalNumFloats);

        Random r = new Random();
        for(int i = 0; i < NUM_INSTANCES; i++) {
            float x = (r.nextFloat() * boxSize.x) - (boxSize.x/2.0f);
            float y = (r.nextFloat() * boxSize.y) - (boxSize.y/2.0f);
            float z = (r.nextFloat() * boxSize.z) - (boxSize.z/2.0f);
            float rotateAngle = r.nextFloat() * 360.0f;
            float scale = (1.0f + r.nextFloat()) / 32f;

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

        setPosition(1, 0.0f, -5.0f, 35.0f);

        Crispin.setCullFaceState(true);
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
        float cameraZ = (((float)Math.sin(cameraZCount)) * (boxSize.z/2f)) + 10.0f;
        camera.setPosition(0.0f, 0.0f, cameraZ);
    }

    @Override
    public void render() {
        indexShader.enable();
        glUniformMatrix4fv(indexShader.getProjectionMatrixUniformHandle(),1,false, camera.getPerspectiveMatrix(), 0);
        glUniformMatrix4fv(indexShader.getViewMatrixUniformHandle(), 1, false, camera.getViewMatrix(), 0);
        GLES30.glBindVertexArray(torus.vao);
        GLES30.glDrawArraysInstanced(GL_TRIANGLES, 0, torus.vertexCount, NUM_INSTANCES);
        GLES30.glBindVertexArray(0);
        indexShader.disable();
    }

    @Override
    public void touch(int type, Vec2 position) {

    }
}