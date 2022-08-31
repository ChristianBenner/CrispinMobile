package com.crispin.demos.scenes;

import android.opengl.GLES30;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;
import com.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.demos.R;

import java.nio.FloatBuffer;

public class VAODemo extends Scene {
    class VaoShader extends Shader {
        public VaoShader() {
            super("VAO Demo Shader", R.raw.vao_vert, R.raw.vao_frag);
            positionAttributeHandle = getAttribute("aPosition");
        }
    }

//    float vertices[] = {
//            -0.5f, -0.5f, 0.0f,
//            0.0f, -0.5f, 0.0f,
//            0.0f, 0.0f, 0.0f,
//            0.5f, 0.5f, 0.0f,
//            0.0f, 0.5f, 0.0f,
//            0.0f, 0.0f, 0.0f,
//    };


    int vao;
    VaoShader vaoShader;
    Model model;

    // Position vertex data that contains XY components
    private static final float[] POSITION_DATA =
            {
                    0.0f, 0.5f,
                    0.0f, 0.0f,
                    0.5f, 0.0f,
                    0.5f, 0.5f,
                    0.0f, 0.5f,
                    0.5f, 0.0f
            };

    private static final float[] TEXEL_DATA =
            {
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 0.0f,
                    1.0f, 1.0f
            };

    public VAODemo() {
        Crispin.setBackgroundColour(Colour.BLACK);
        vaoShader = new VaoShader();

//        // Generate VAO
//        int[] vaoTemp = new int[1];
//        GLES30.glGenVertexArrays(1, vaoTemp, 0);
//        vao = vaoTemp[0];
//
//        // Generate VBO
//        int[] vboTemp = new int[1];
//        GLES30.glGenBuffers(1, vboTemp, 0);
//        int vbo = vboTemp[0];
//
//        // Setup VBO inside the bound VAO
//        GLES30.glBindVertexArray(vao);
//        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
//        FloatBuffer vertexData = FloatBuffer.allocate(vertices.length);
//        vertexData.put(vertices);
//        vertexData.position(0);
//        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.length * 4, vertexData, GLES30.GL_STATIC_DRAW);
//        GLES30.glVertexAttribPointer(vaoShader.getPositionAttributeHandle(),  3, GLES30.GL_FLOAT, false, 3 * 4, 0);
//        GLES30.glEnableVertexAttribArray(vaoShader.getPositionAttributeHandle());

        model = new Model(POSITION_DATA, TEXEL_DATA, null, RenderObject.RenderMethod.TRIANGLES, 2, 2, 0);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render() {
        vaoShader.enableIt();

        GLES30.glBindVertexArray(model.vao);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, model.vertexCount);

        vaoShader.disableIt();
    }

    @Override
    public void touch(int type, Vec2 position) {

    }

    //        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
//        byteBuffer.order(ByteOrder.nativeOrder());
//        vertexData = byteBuffer.asFloatBuffer();
}
