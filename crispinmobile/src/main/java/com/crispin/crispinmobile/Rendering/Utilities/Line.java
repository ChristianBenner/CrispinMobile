package com.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Shaders.LineShader;
import com.crispin.crispinmobile.Rendering.Shaders.UniformColourShader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.crispin.crispinmobile.Rendering.Utilities.RenderObject.BYTES_PER_FLOAT;

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

    private Point2D pointOne;
    private Point2D pointTwo;
    private float lineWidth;
    private float[] positionBuffer = new float[NUM_DIMS * NUM_VERTICES_PER_LINE];

    // Float buffer that holds all the line co-ordinate data
    private FloatBuffer vertexBuffer;

    private LineShader lineShader;

    private Colour colour;

    public Line()
    {
        pointOne = new Point2D();
        pointTwo = new Point2D();
        lineWidth = 1.0f;

        lineShader = new LineShader();
        colour = new Colour();

        setPoints(new Point2D(0.0f, 0.0f), new Point2D(0.0f, 0.0f));
    }

    public Point2D getPointOne()
    {
        return pointOne;
    }

    public Point2D getPointTwo()
    {
        return pointTwo;
    }

    public void setColour(Colour colour)
    {
        this.colour = colour;
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

    public void setPoints(Point2D p1, Point2D p2)
    {
        setPoints(p1.x, p1.y, p2.x, p2.y);
    }

    public void render(Camera2D camera)
    {
        lineShader.enableIt();
        glLineWidth(lineWidth);
        glUniform4f(lineShader.getColourUniformHandle(), colour.getRed(), colour.getGreen(),
                colour.getBlue(), colour.getAlpha());

        glUniformMatrix4fv(lineShader.getMatrixUniformHandle(), UNIFORM_UPLOAD_COUNT, false,
                camera.getOrthoMatrix(), 0);

        glUniform4f(lineShader.getColourUniformHandle(),
                colour.getRed(),
                colour.getGreen(),
                colour.getBlue(),
                colour.getAlpha());

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
