package com.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glGenVertexArrays;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec3;

import java.nio.FloatBuffer;

public class DebugMesh extends Mesh {
    private final boolean DEBUG_NORMALS = true;

    public int normalLinesVao;
    public int normalLinesVbo;
    public int normalLinesVertexCount;

    public DebugMesh(float[] positionBuffer, float[] texelBuffer, float[] normalBuffer,
                     RenderMethod renderMethod, int elementsPerPosition, int elementsPerTexel,
                     int elementsPerNormal) {
        super(positionBuffer, texelBuffer, normalBuffer, renderMethod, elementsPerPosition,
                elementsPerTexel, elementsPerNormal);
        calcDebug(positionBuffer, normalBuffer);
    }

    private void calcDebug(float[] positionBuffer, float[] normalBuffer) {
        if(DEBUG_NORMALS) {
            // Calculate the center of the face
            if(renderMethod == Mesh.RenderMethod.TRIANGLES) {
                final int POINTS_PER_FACE = 3; // for triangles
                int numFaces = positionBuffer.length / (elementsPerPosition * POINTS_PER_FACE);

                final int POINTS_PER_LINE = 2;
                normalLinesVertexCount = numFaces * POINTS_PER_LINE;
                float[] normalLinesVertexData = new float[elementsPerPosition * normalLinesVertexCount];

                for(int i = 0; i < numFaces; i++) {
                    int pi = i * (elementsPerPosition * POINTS_PER_FACE);
                    Vec3 p1 = new Vec3(positionBuffer[pi], positionBuffer[pi+1], positionBuffer[pi+2]);
                    Vec3 p2 = new Vec3(positionBuffer[pi+3], positionBuffer[pi+4], positionBuffer[pi+5]);
                    Vec3 p3 = new Vec3(positionBuffer[pi+6], positionBuffer[pi+7], positionBuffer[pi+8]);

                    int nli = i * POINTS_PER_LINE * elementsPerPosition;
                    // Set the first vertex of the line to the center of the face (triangle)
                    normalLinesVertexData[nli] = (p1.x + p2.x + p3.x) / 3;
                    normalLinesVertexData[nli + 1] = (p1.y + p2.y + p3.y) / 3;
                    normalLinesVertexData[nli + 2] = (p1.z + p2.z + p3.z) / 3;

                    // Set the second vertex of the line to normal, which means it should be pointing this way
                    Vec3 normalizedNormal = Geometry.normalize(new Vec3(normalBuffer[pi], normalBuffer[pi+1], normalBuffer[pi+2]));
                    normalLinesVertexData[nli + 3] = normalizedNormal.x;
                    normalLinesVertexData[nli + 4] = normalizedNormal.y;
                    normalLinesVertexData[nli + 5] = normalizedNormal.z;
                }

                // Generate VAO
                int[] vaoTemp = new int[1];
                glGenVertexArrays(1, vaoTemp, 0);
                normalLinesVao = vaoTemp[0];

                // Generate VBO
                int[] vboTemp = new int[1];
                glGenBuffers(1, vboTemp, 0);
                normalLinesVbo = vboTemp[0];

                FloatBuffer vd = FloatBuffer.allocate(normalLinesVertexData.length);
                vd.put(normalLinesVertexData);
                vd.position(0);
                glBindVertexArray(normalLinesVao);
                glBindBuffer(GL_ARRAY_BUFFER, normalLinesVbo);
                glBufferData(GL_ARRAY_BUFFER, normalLinesVertexData.length * BYTES_PER_FLOAT, vd, GL_STATIC_DRAW);
                glBindVertexArray(0);
                glBindBuffer(GL_ARRAY_BUFFER, 0);
            }
        }
    }
}
