package com.crispin.demos.scenes;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class IndexDemo extends Scene {
    class IndexShader extends Shader {
        private int[] matrixUniformHandles = new int[NUM_INSTANCES];

        public IndexShader() {
            super("Index Demo Shader", R.raw.index_vert, R.raw.index_frag);
            positionAttributeHandle = getAttribute("aPosition");
            matrixUniformHandle = getUniform("uMatrix");

            for(int i = 0; i < NUM_INSTANCES; i++) {
                matrixUniformHandles[i] = getUniform("uMatrixArr[" + i + "]");
            }
        }

        /**
         * Set the matrix for an index
         *
         * @since 1.0
         */
        public void setMatrix(int index, float[] matrix) {
            glUniformMatrix4fv(matrixUniformHandles[index], 1, false, matrix, 0);
        }
    }

    private Model torus;
    private final Camera camera;
    private IndexShader indexShader;
    final int NUM_INSTANCES = 625;
    float cameraZCount;

    public IndexDemo() {
        Crispin.setBackgroundColour(Colour.WHITE);

        indexShader = new IndexShader();
        ThreadedOBJLoader.loadModel(R.raw.torus_uv, model -> {
            this.torus = model;
            this.torus.useCustomShader(indexShader);
            this.torus.setRotation(15.0f, 1.0f, 0.0f, 0.0f);
        });

        camera = new Camera();
        camera.setPosition(new Vec3(0.0f, 0f, 50.0f));
        cameraZCount = (float)Math.PI / 2.0f;

        Random r = new Random();
        indexShader.enableIt();

        int max = 25;

        for(int y = 0; y < max; y++) {
            for(int x = 0; x < max; x++) {
                ModelMatrix modelMatrix = new ModelMatrix();

                // Create the model matrix that contains all position, rotation data etc
                modelMatrix.reset();
                modelMatrix.translate(x - (max / 2.0f), y - (max/ 2.0f), 0.0f);
                modelMatrix.rotate(r.nextFloat() * 180.0f, 1.0f, 0.0f, 0.0f);
                modelMatrix.scale(0.3f);

                float[] modelViewProjectionMatrix = new float[NUM_VALUES_PER_VIEW_MATRIX];
                float[] modelViewMatrix = new float[16];
                Matrix.multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, modelMatrix.getModelMatrix(), 0);
                Matrix.multiplyMM(modelViewProjectionMatrix, 0, camera.getPerspectiveMatrix(), 0, modelViewMatrix, 0);

                indexShader.setMatrix((max*y)+x, modelViewProjectionMatrix);
            }
        }
        indexShader.disableIt();
    }

    @Override
    public void update(float deltaTime) {
        cameraZCount += 0.03f * deltaTime;
        float cameraZ = (float)Math.sin(cameraZCount);
        camera.setPosition(0.0f, 1.0f, cameraZ);
    }

    final int NUM_VALUES_PER_VIEW_MATRIX = 16;

    @Override
    public void render() {
        indexShader.enableIt();
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