package com.crispin.demos.scenes;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glUniformMatrix4fv;

import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Pair;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
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

public class IndexDemo extends Scene {
    class IndexShader extends Shader {
        public IndexShader() {
            super("Index Demo Shader", R.raw.index_vert, R.raw.index_frag);
            positionAttributeHandle = getAttribute("aPosition");
            matrixUniformHandle = getUniform("uMatrix");
        }
    }

    private final ModelMatrix modelMatrix;
    private Model torus;
    private final Camera camera;
    private Shader indexShader;

    public IndexDemo() {
        Crispin.setBackgroundColour(Colour.WHITE);

        indexShader = new IndexShader();
        ThreadedOBJLoader.loadModel(R.raw.torus_uv, model -> {
            this.torus = model;
            this.torus.useCustomShader(indexShader);
            this.torus.setRotation(15.0f, 1.0f, 0.0f, 0.0f);
        });

        camera = new Camera();
        camera.setPosition(new Vec3(0.0f, 1.0f, 5.0f));
        modelMatrix = new ModelMatrix();

    }

    @Override
    public void update(float deltaTime) {
    }

    final int NUM_VALUES_PER_VIEW_MATRIX = 16;

    @Override
    public void render() {



        modelMatrix.reset();
        modelMatrix.translate(torus.getPosition());

        if (torus.getRotation().angle != 0.0f) {
            modelMatrix.rotate(torus.getRotation());
        }

        modelMatrix.scale(torus.getScale());




        float[] modelViewMatrix = new float[16];
        Matrix.multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0,
                modelMatrix.getModelMatrix(), 0);

        float[] modelViewProjectionMatrix = new float[NUM_VALUES_PER_VIEW_MATRIX];
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, camera.getPerspectiveMatrix(), 0,
                modelViewMatrix, 0);



        indexShader.enableIt();

        glUniformMatrix4fv(indexShader.getMatrixUniformHandle(), 1, false,
                modelViewProjectionMatrix, 0);


        GLES30.glBindVertexArray(torus.vao);
        // Draw the vertex data with the specified render method
        switch (torus.renderMethod) {
            case POINTS:
                glDrawArrays(GL_POINTS, 0, torus.vertexCount);
                break;
            case LINES:
                glDrawArrays(GL_LINES, 0, torus.vertexCount);
                break;
            case TRIANGLES:
                glDrawArrays(GL_TRIANGLES, 0, torus.vertexCount);
                break;
        }
        GLES30.glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);

        indexShader.disableIt();
    }

    @Override
    public void touch(int type, Vec2 position) {

    }
}