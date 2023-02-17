package com.crispin.demos.scenes;

import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glUniformMatrix4fv;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.DefaultMesh.SquareMesh;
import com.crispin.crispinmobile.Rendering.Shaders.UniformColourShader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.Utilities.Scene;

public class Demo2D extends Scene {
    private Camera2D camera;

    private Mesh mesh;
    private ModelMatrix modelMatrix;
    private Material material;
    private UniformColourShader shader;

    public Demo2D() {
        Crispin.setBackgroundColour(Colour.RED);

        camera = new Camera2D(0.0f, 0.0f, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());

        mesh = new SquareMesh(false, false);
        modelMatrix = new ModelMatrix();
        modelMatrix.reset();
        modelMatrix.translate(300, 300);
        modelMatrix.scale(200, 200);

        material = new Material(Colour.BLUE);

        shader = new UniformColourShader();
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render() {
        shader.enable();
        mesh.setAttributePointers(shader.positionAttributeHandle, shader.textureAttributeHandle, shader.normalAttributeHandle);

        float[] modelViewMatrix = new float[16];
        Matrix.multiplyMM(modelViewMatrix, 0, camera.getOrthoMatrix(), 0, modelMatrix.getFloats(), 0);
        glUniformMatrix4fv(shader.getMatrixUniformHandle(), 1, false, modelViewMatrix, 0);
        shader.setMaterialUniforms(material);
        GLES30.glBindVertexArray(mesh.vao);
        glDrawArrays(GL_TRIANGLES, 0, mesh.vertexCount);
        GLES30.glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
        shader.disable();
    }

    @Override
    public void touch(int type, Vec2 position) {

    }
}
