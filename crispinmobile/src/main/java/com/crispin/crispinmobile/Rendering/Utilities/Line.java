package com.crispin.crispinmobile.Rendering.Utilities;

import com.crispin.crispinmobile.Rendering.Shaders.LineShader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_LINES;
import static android.opengl.GLES30.glDisableVertexAttribArray;
import static android.opengl.GLES30.glDrawArrays;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glLineWidth;
import static android.opengl.GLES30.glUniform4f;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glVertexAttribPointer;
import static com.crispin.crispinmobile.Rendering.Utilities.RenderObject.BYTES_PER_FLOAT;

import glm_.vec2.Vec2;
import glm_.vec4.Vec4;

public class Line
{
    // todo:
    // - Create a line shader
    // - Upload line vertices
    // - Allow positioning of vertices
    // - Line width
    // - Line colour

    //

    // The number of elements in a 4x4 view matrix
    private final int NUM_VALUES_PER_VIEW_MATRIX = 16;

    // Number of uniform elements to upload in a GLSL uniform upload
    private static final int UNIFORM_UPLOAD_COUNT = 1;

    private static final int NUM_DIMS = 2;
    private static final int NUM_VERTICES_PER_LINE = 2;

    private Vec2 pointOne;
    private Vec2 pointTwo;
    private float lineWidth;
    private float[] positionBuffer = new float[NUM_DIMS * NUM_VERTICES_PER_LINE];

    // Float buffer that holds all the line co-ordinate data
    private FloatBuffer vertexBuffer;

    private LineShader lineShader;

    private Material material;

    public Line()
    {
        pointOne = new Vec2();
        pointTwo = new Vec2();
        lineWidth = 1.0f;

        lineShader = new LineShader();
        material = new Material();

        setPoints(new Vec2(0.0f, 0.0f), new Vec2(0.0f, 0.0f));
    }

    public Vec2 getPointOne()
    {
        return pointOne;
    }

    public Vec2 getPointTwo()
    {
        return pointTwo;
    }

    public void setColour(Vec4 colour) {
        material.setColour(colour);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setWidth(float width)
    {
        this.lineWidth = width;
    }

    public void setPoints(float x1, float y1, float x2, float y2)
    {
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
    }

    public void setPoints(Vec2 p1, Vec2 p2)
    {
        setPoints(p1.x, p1.y, p2.x, p2.y);
    }

    public void render(Camera2D camera)
    {
        lineShader.enableIt();
        glLineWidth(lineWidth);

        glUniform4f(lineShader.materialHandles.colourUniformHandle, material.colour.x,
                material.colour.y, material.colour.z, material.colour.w);

        glUniformMatrix4fv(lineShader.getMatrixUniformHandle(), UNIFORM_UPLOAD_COUNT, false,
                camera.getOrthoMatrix(), 0);

        vertexBuffer.position(0);
        glEnableVertexAttribArray(lineShader.getPositionAttributeHandle());
        glVertexAttribPointer(lineShader.getPositionAttributeHandle(),
                2,
                GL_FLOAT,
                true,
                0,
                vertexBuffer);
        vertexBuffer.position(0);
        glDrawArrays(GL_LINES, 0, 2);
        glDisableVertexAttribArray(lineShader.getPositionAttributeHandle());

        glLineWidth(1.0f);
        lineShader.disableIt();
    }
}
