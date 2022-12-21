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
        // Attribute for the instances of model matrixes
        int modelMatrixInstanceAttributeHandle;

        public IndexShader() {
            super("Index Demo Shader", R.raw.index_vert, R.raw.index_frag);
            positionAttributeHandle = getAttribute("aPosition");
            modelMatrixInstanceAttributeHandle = getAttribute("aModel");

            projectionMatrixUniformHandle = getUniform("uProjection");
            viewMatrixUniformHandle = getUniform("uView");
            modelMatrixUniformHandle = getUniform("uModel");
        }

        /**
         * Set the matrix for an index
         *
         * @since 1.0
         */
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
    final int NUM_INSTANCES = 625;
    float cameraZCount;
    private ModelMatrix modelMatrix;

    final int vec4Size = BYTES_PER_FLOAT * 4;
    final int stride = 4 * vec4Size; // we want to stride of 4x vec4's (jump a mat4 num bytes each time)


    public IndexDemo() {
        Crispin.setBackgroundColour(Colour.WHITE);

        indexShader = new IndexShader();
        torus = OBJModelLoader.readObjFile(R.raw.torus_uv);
        torus.useCustomShader(indexShader);

        camera = new Camera();
        camera.setPosition(new Vec3(0.0f, 0f, 5f));

        modelMatrix = new ModelMatrix();
        modelMatrix.reset();
        modelMatrix.translate(0.0f, 0.0f, 0.0f);
        modelMatrix.rotate(35f, 1.0f, 0.0f, 0.0f);
        modelMatrix.scale(1.0f);

        int numFloats = 16;
        int numBytes = numFloats * 4;

        FloatBuffer modelMatrixBuffer = FloatBuffer.allocate(numFloats);
        modelMatrixBuffer.put(modelMatrix.getModelMatrix());
        modelMatrixBuffer.position(0);

        int[] instanceVBO = new int[1];
        GLES30.glGenBuffers(1, instanceVBO, 0);
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, instanceVBO[0]);
        GLES30.glBufferData(GL_ARRAY_BUFFER, numBytes, modelMatrixBuffer, GL_STATIC_DRAW);
        indexShader.bindModelMatrixInstanceVAO(torus.vao);

//        Random r = new Random();
//        FloatBuffer md = FloatBuffer.allocate(100 * 16);
//        int max = 25;
//        for(int y = 0; y < max; y++) {
//            for(int x = 0; x < max; x++) {
//                ModelMatrix modelMatrix = new ModelMatrix();
//
//                // Create the model matrix that contains all position, rotation data etc
//                modelMatrix.reset();
//                modelMatrix.translate(x - (max / 2.0f), y - (max/ 2.0f), 0.0f);
//                modelMatrix.rotate(r.nextFloat() * 180.0f, 1.0f, 0.0f, 0.0f);
//                modelMatrix.scale(0.3f);
//
//                md.put(modelMatrix.getModelMatrix());
//                md.position(0);
//
//            }
//        }

//        // Create instance VBO and send data
//        int[] instanceVBO = new int[1];
//        GLES30.glGenBuffers(1, instanceVBO, 0);
//        GLES30.glBindBuffer(GL_ARRAY_BUFFER, instanceVBO[0]);
//        GLES30.glBufferData(GL_ARRAY_BUFFER, vec4Size * 4 * 100, md, GL_STATIC_DRAW);
//        indexShader.bindModelMatrixInstanceVAO(torus.vao);
    }

    @Override
    public void update(float deltaTime) {

    }

    final int NUM_VALUES_PER_VIEW_MATRIX = 16;

    @Override
    public void render() {
        indexShader.enableIt();
        glUniformMatrix4fv(indexShader.getProjectionMatrixUniformHandle(),1,false, camera.getPerspectiveMatrix(), 0);
        glUniformMatrix4fv(indexShader.getViewMatrixUniformHandle(), 1, false, camera.getViewMatrix(), 0);
        glUniformMatrix4fv(indexShader.getModelMatrixUniformHandle(), 1, false, modelMatrix.getModelMatrix(), 0);

        GLES30.glBindVertexArray(torus.vao);
        // Draw the vertex data with the specified render method
        switch (torus.renderMethod) {
            case POINTS:
                GLES30.glDrawArraysInstanced(GL_POINTS, 0, torus.vertexCount, NUM_INSTANCES);
                glDrawArrays(GL_POINTS, 0, torus.vertexCount);
                break;
            case LINES:
                GLES30.glDrawArraysInstanced(GL_LINES, 0, torus.vertexCount, NUM_INSTANCES);
                glDrawArrays(GL_LINES, 0, torus.vertexCount);
                break;
            case TRIANGLES:
                GLES30.glDrawArraysInstanced(GL_TRIANGLES, 0, torus.vertexCount, NUM_INSTANCES);
                break;
        }
        GLES30.glBindVertexArray(0);
        indexShader.disableIt();
    }

    @Override
    public void touch(int type, Vec2 position) {

    }
}