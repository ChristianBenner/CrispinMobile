package com.crispin.crispinmobile.Rendering.Models;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_STREAM_DRAW;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glBufferSubData;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES30.GL_INVALID_INDEX;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glGenVertexArrays;
import static com.crispin.crispinmobile.Rendering.Utilities.Mesh.BYTES_PER_FLOAT;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Shaders.LineShader;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Utilities.ShaderCache;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Line {
    // todo:
    // - Create a line shader
    // - Upload line vertices
    // - Allow positioning of vertices
    // - Line width
    // - Line colour

    //

    // Number of uniform elements to upload in a GLSL uniform upload
    private static final int UNIFORM_UPLOAD_COUNT = 1;
    private static final int NUM_DIMS = 2;
    private static final int NUM_VERTICES_PER_LINE = 2;
    // The number of elements in a 4x4 view matrix
    private final int NUM_VALUES_PER_VIEW_MATRIX = 16;
    private final Vec2 pointOne;
    private final Vec2 pointTwo;
    private final float[] positionBuffer = new float[NUM_DIMS * NUM_VERTICES_PER_LINE];
    private final Shader lineShader;
    private float lineWidth;
    // Float buffer that holds all the line co-ordinate data
    private FloatBuffer vertexBuffer;
    private Material material;

    private int vao;
    private int vbo;

    public Line() {
        pointOne = new Vec2();
        pointTwo = new Vec2();
        lineWidth = 1.0f;

        if(ShaderCache.existsInCache(R.raw.line_vert, R.raw.line_frag)) {
            lineShader = ShaderCache.getShader(R.raw.line_vert, R.raw.line_frag);
        } else {
            lineShader = new LineShader();
        }

        material = new Material();

        // Generate VAO
        int[] vaoTemp = new int[1];
        glGenVertexArrays(1, vaoTemp, 0);
        vao = vaoTemp[0];

        // Generate VBO
        int[] vboTemp = new int[1];
        glGenBuffers(1, vboTemp, 0);
        vbo = vboTemp[0];

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(lineShader.getPositionAttributeHandle(), 2, GL_FLOAT, false, 2 * BYTES_PER_FLOAT, 0);
        glEnableVertexAttribArray(lineShader.getPositionAttributeHandle());
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        setPoints(new Vec2(0.0f, 0.0f), new Vec2(0.0f, 0.0f));
    }

    public Vec2 getPointOne() {
        return pointOne;
    }

    public Vec2 getPointTwo() {
        return pointTwo;
    }

    public void setColour(Colour colour) {
        material.setColour(colour);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setWidth(float width) {
        this.lineWidth = width;
    }

    public void setPoints(float x1, float y1, float x2, float y2) {
        pointOne.x = x1;
        pointOne.y = y1;
        pointTwo.x = x2;
        pointTwo.y = y2;

        positionBuffer[0] = x1;
        positionBuffer[1] = y1;
        positionBuffer[2] = x2;
        positionBuffer[3] = y2;

        // Initialise a vertex byte buffer for the shape float array
        final ByteBuffer VERTICES_BYTE_BUFFER = ByteBuffer.allocateDirect(
                positionBuffer.length * BYTES_PER_FLOAT);

        // Use the devices hardware's native byte order
        VERTICES_BYTE_BUFFER.order(ByteOrder.nativeOrder());

        // Create a Float buffer from the ByteBuffer
        vertexBuffer = VERTICES_BYTE_BUFFER.asFloatBuffer();

        // Add the array of floats to the buffer
        vertexBuffer.put(positionBuffer);

        // Set buffer to read the first co-ordinate
        vertexBuffer.position(0);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, positionBuffer.length * BYTES_PER_FLOAT, vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void setPoints(Vec2 p1, Vec2 p2) {
        setPoints(p1.x, p1.y, p2.x, p2.y);
    }

    public void render(Camera2D camera) {
        // Check if depth is enabled, and disable it
        final boolean DEPTH_ENABLED = GLES30.glIsEnabled(GL_DEPTH_TEST);
        if(DEPTH_ENABLED) {
            GLES30.glDisable(GL_DEPTH_TEST);
        }

        lineShader.enable();
        glLineWidth(10);

        glUniform4f(lineShader.materialHandles.colourUniformHandle, material.colour.red,
                material.colour.green, material.colour.blue, material.colour.alpha);

        glUniformMatrix4fv(lineShader.getMatrixUniformHandle(), UNIFORM_UPLOAD_COUNT, false,
                camera.getOrthoMatrix(), 0);

        vertexBuffer.position(0);

        GLES30.glBindVertexArray(vao);
        glDrawArrays(GL_LINES, 0, 2);
        GLES30.glBindVertexArray(0);

        glLineWidth(1.0f);
        lineShader.disable();

        // If depth was enabled before calling the function then re-enable it
        if (DEPTH_ENABLED) {
            glEnable(GL_DEPTH_TEST);
        }
    }
}
