package com.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glVertexAttribPointer;
import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_INVALID_INDEX;
import static android.opengl.GLES30.GL_STATIC_DRAW;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glGenVertexArrays;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;

import java.nio.FloatBuffer;

/**
 * Mesh is a base class for any graphical object. Vertex data upload to the graphics memory and
 * drawing of objects.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @since 1.0
 */
public class Mesh {
    /**
     * The attribute order type is to store the order that the vertex data elements appear. This
     * means that the data can be submitted to the GPU in the correct order.
     * POSITION: The position data element is the only one provided
     * POSITION_THEN_TEXEL: The position data is provided first, then texel data
     * POSITION_THEN_NORMAL: The position data provided first, then normal
     * POSITION_THEN_TEXEL_THEN_NORMAL: Position data first, then texel, then normal
     * POSITION_THEN_NORMAL_THEN_TEXEL: Position data first, then normal, then texel
     *
     * @since 1.0
     */
    public enum AttributeOrder_t {
        POSITION,
        POSITION_THEN_TEXEL,
        POSITION_THEN_NORMAL,
        POSITION_THEN_TEXEL_THEN_NORMAL,
        POSITION_THEN_NORMAL_THEN_TEXEL,
    }

    // Method of rendering the object. Points renders one vertex at a time and places a point. Lines
    // renders two vertex at a time and render a line between them. Triangles renders three vertices
    // at a time and uses the fragment shader to fill the middle.
    public enum RenderMethod {
        POINTS,
        LINES,
        TRIANGLES,
        NONE
    }

    // Tag used in logging output
    private static final String TAG = "Mesh";

    // The 'numVerticesPerGroup' if the vertices are not grouped
    public static final int UNGROUPED = 1;

    // The number of bytes in a float
    public static final int BYTES_PER_FLOAT = 4;

    // Value that represents an invalid OpenGL ES GLSL shader uniform handle
    private static final int INVALID_UNIFORM_HANDLE = -1;

    // The method to render the data as (e.g. triangles or lines)
    public final RenderMethod renderMethod;

    // The number of elements that are in the position data
    public final int elementsPerPosition;

    // The number of elements that are in the texel data
    public final int elementsPerTexel;

    // The number of elements that are in the direction data
    public final int elementsPerNormal;

    // The number of elements that are in the tangent data
    public int elementsPerTangent;

    // The number of elements that are in the bi-tangent data
    public int elementsPerBitangent;

    // The position index of the position data in the vertex data buffer
    public int positionDataOffset;

    // The position index of the texel data in the vertex data buffer
    public int texelDataOffset;

    // The position index of the normal data in the vertex data buffer
    public int normalDataOffset;

    // The position index of the tangent data in the vertex data buffer
    public int tangentDataOffset;

    // The position index of the bi-tangent data in the vertex data buffer
    public int bitangentDataOffset;

    // The stride between each set of data (only if the data format is ungrouped)
    public int stride;

    // Number of vertices
    public int vertexCount;

    public int vao;
    public int vbo;

    /**
     * Create an object with vertex data comprised of multiple buffers containing different forms of
     * vertex data. For the buffers that you are not providing data for, put <code>null</code>.
     *
     * @param positionBuffer      Float buffer containing the position data
     * @param texelBuffer         Float buffer containing the texel data, or <code>null</code> if
     *                            no texel data is being provided
     * @param normalBuffer        Float buffer containing the normal data, or <code>null</code> if
     *                            no normal data is being provided
     * @param renderMethod        The method to render the data (e.g. triangles or quads)
     * @param elementsPerPosition The number of components the position data is comprised of
     * @param elementsPerTexel    The number of components that the texel data is comprised of
     * @param elementsPerNormal   The number components that the normal data is comprised of
     * @since 1.0
     */
    public Mesh(float[] positionBuffer, float[] texelBuffer, float[] normalBuffer,
                Mesh.RenderMethod renderMethod, int elementsPerPosition,
                int elementsPerTexel, int elementsPerNormal) {
        this.renderMethod = renderMethod;
        this.elementsPerPosition = positionBuffer == null ? 0 : elementsPerPosition;
        this.elementsPerTexel = texelBuffer == null ? 0 : elementsPerTexel;
        this.elementsPerNormal = normalBuffer == null ? 0 : elementsPerNormal;

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        // todo: wip, resolve tangent and bi-tangent
        // Normal maps require tangent and bi-tangents to be calculated for each face in order to
        // point the normals on the map in the correct direction. To have a functioning normal map
        // you must have texture co-ordinates provided and normals on the mesh.

        float[] tangents = null;
        float[] bitangents = null;

        // todo:       if(elementsPerTexel > 0 && elementsPerNormal > 0) {
        if(elementsPerPosition == 3 && elementsPerTexel == 2 && elementsPerNormal == 3) { // temporary
            // todo: below example for 3D and TRIANGLES
            final int POINTS_PER_FACE = 3; // for triangles
            int numVertices = positionBuffer.length / elementsPerPosition;
            int numFaces = positionBuffer.length / (elementsPerPosition * POINTS_PER_FACE);
            elementsPerTangent = 3;
            elementsPerBitangent = 3;
            tangents = new float[numVertices * elementsPerTangent];
            bitangents = new float[numVertices * elementsPerBitangent];

            // For each face, calculate the tangent and bi-tangent
            for(int n = 0; n < numFaces; n++) {
                int pi = n * (elementsPerPosition * POINTS_PER_FACE);
                Vec3 p1 = new Vec3(positionBuffer[pi], positionBuffer[pi+1], positionBuffer[pi+2]);
                Vec3 p2 = new Vec3(positionBuffer[pi+3], positionBuffer[pi+4], positionBuffer[pi+5]);
                Vec3 p3 = new Vec3(positionBuffer[pi+6], positionBuffer[pi+7], positionBuffer[pi+8]);

                int ti = n * (elementsPerTexel * POINTS_PER_FACE);
                Vec2 uv1 = new Vec2(texelBuffer[ti], texelBuffer[ti+1]);
                Vec2 uv2 = new Vec2(texelBuffer[ti+2], texelBuffer[ti+3]);
                Vec2 uv3 = new Vec2(texelBuffer[ti+4], texelBuffer[ti+5]);

                Vec3 edge1 = Geometry.minus(p2, p1);
                Vec3 edge2 = Geometry.minus(p3, p1);
                Vec2 deltaUV1 = Geometry.minus(uv2, uv1);
                Vec2 deltaUV2 = Geometry.minus(uv3, uv1);

                float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);

                int ri = n * (elementsPerTangent * POINTS_PER_FACE);
                tangents[ri] = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
                tangents[ri+1] = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
                tangents[ri+2] = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
                tangents[ri+3] = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
                tangents[ri+4] = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
                tangents[ri+5] = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
                tangents[ri+6] = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
                tangents[ri+7] = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
                tangents[ri+8] = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);

                int bi = n * (elementsPerBitangent * POINTS_PER_FACE);
                bitangents[bi] = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
                bitangents[bi+1] = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
                bitangents[bi+2] = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
                bitangents[bi+3] = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
                bitangents[bi+4] = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
                bitangents[bi+5] = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
                bitangents[bi+6] = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
                bitangents[bi+7] = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
                bitangents[bi+8] = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        stride = (elementsPerPosition + elementsPerTexel + elementsPerNormal + elementsPerTangent + elementsPerBitangent) * BYTES_PER_FLOAT;

        // Turn the individual component data into vertex data
        float[] vertexData = toVertexData(positionBuffer, texelBuffer, normalBuffer, tangents,
                bitangents, elementsPerPosition, elementsPerTexel, elementsPerNormal,
                elementsPerTangent, elementsPerBitangent);
        vertexCount = vertexData.length / (elementsPerPosition + elementsPerTexel +
                elementsPerNormal + elementsPerTangent + elementsPerBitangent);
        createGLObjects(vertexData);

        // Resolve the attribute order
        AttributeOrder_t attributeOrder;
        if (texelBuffer != null && normalBuffer != null) {
            attributeOrder = AttributeOrder_t.POSITION_THEN_TEXEL_THEN_NORMAL;
        } else if (texelBuffer != null) {
            attributeOrder = AttributeOrder_t.POSITION_THEN_TEXEL;
        } else if (normalBuffer != null) {
            attributeOrder = AttributeOrder_t.POSITION_THEN_NORMAL;
        } else {
            attributeOrder = AttributeOrder_t.POSITION;
        }
        resolveAttributeOffsets(attributeOrder);
    }

    /**
     * Check if the mesh supports a texture
     *
     * @return True if the mesh supports textures, else false
     * @since 1.0
     */
    public boolean supportsTexture() {
        return elementsPerTexel != 0;
    }

    /**
     * Check if the mesh supports lighting. A mesh is treated as having lighting support if it has
     * normal data/elements or only has two components per position (is 2D)
     *
     * @return True if the mesh supports lighting, else false
     * @since 1.0
     */
    public boolean supportsLighting() {
        return elementsPerPosition == 2 || elementsPerNormal != 0;
    }

    /**
     * Creates OpenGL objects such as virtual buffer object (VBO) to store the vertex data in VRAM
     * and a virtual array object (VAO) to specify how OpenGL should utilise that data on draw.
     *
     * @param vertexData    Float buffer containing the vertex data
     * @since 1.0
     */
    private void createGLObjects(float[] vertexData) {
        // Generate VAO
        int[] vaoTemp = new int[1];
        glGenVertexArrays(1, vaoTemp, 0);
        vao = vaoTemp[0];

        // Generate VBO
        int[] vboTemp = new int[1];
        glGenBuffers(1, vboTemp, 0);
        vbo = vboTemp[0];

        FloatBuffer vd = FloatBuffer.allocate(vertexData.length);
        vd.put(vertexData);
        vd.position(0);
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexData.length * BYTES_PER_FLOAT, vd, GL_STATIC_DRAW);
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    /**
     * Turn position, texel and normal buffers into one vertex data buffer. Provide null to buffers
     * that you do not wish to provide.
     *
     * @param positionBuffer        Array of floats containing position data (or null if not in use)
     * @param texelBuffer           Array of floats containing texel data (or null if not in use)
     * @param normalBuffer          Array of floats containing normal data (or null if not in use)
     * @param tangentBuffer         Array of floats containing tangent data (or null if not in use)
     * @param bitangentBuffer       Array of floats containing bi-tangent data (or null if not in use)
     * @param elementsPerPosition   Number of elements per position data e.g. XY = 2 and XYZ = 3
     * @param elementsPerTexel      Number of elements per texel data e.g. ST = 2
     * @param elementsPerNormal     Number of elements per normal data e.g. XY = 2 and XYZ = 3
     * @param elementsPerTangent    Number of elements per tangent data e.g. XY = 2 and XYZ = 3
     * @param elementsPerBiTangent  Number of elements per bi-tangent data e.g. XY = 2 and XYZ = 3
     * @return                      Array of floats representing vertex data. The data structure of
     *                              the array is:
     *                                  v0: posX, posY, posZ, texU, texV, normX, normY, normZ, tangentX, tangentY, tangentZ, bitangentX, bitangentY, bitangentZ
     *                                  v1: posX, posY, posZ, texU, texV, normX, normY, normZ, tangentX, tangentY, tangentZ, bitangentX, bitangentY, bitangentZ
     *                                  v2: ...
     * @since                       1.0
     */
    private float[] toVertexData(float[] positionBuffer, float[] texelBuffer, float[] normalBuffer,
                                 float[] tangentBuffer, float[] bitangentBuffer,
                                 int elementsPerPosition, int elementsPerTexel,
                                 int elementsPerNormal, int elementsPerTangent,
                                 int elementsPerBiTangent) {
        final int posBufferLength = positionBuffer == null ? 0 : positionBuffer.length;
        final int texBufferLength = texelBuffer == null ? 0 : texelBuffer.length;
        final int norBufferLength = normalBuffer == null ? 0 : normalBuffer.length;
        final int tanBufferLength = tangentBuffer == null ? 0 : tangentBuffer.length;
        final int btnBufferLength = bitangentBuffer == null ? 0 : bitangentBuffer.length;
        final int vertexBufferLength = posBufferLength + texBufferLength + norBufferLength + tanBufferLength + btnBufferLength;

        // Copy the data into the vertex data buffer. This should result in a data structure of:
        // v0: posX, posY, posZ, texU, texV, normX, normY, normZ, tangentX, tangentY, tangentZ, bitangentX, bitangentY, bitangentZ
        // v1: posX, posY, posZ, texU, texV, normX, normY, normZ, tangentX, tangentY, tangentZ, bitangentX, bitangentY, bitangentZ
        float[] vertexData = new float[vertexBufferLength];
        int stride = elementsPerPosition + elementsPerTexel + elementsPerNormal + elementsPerTangent + elementsPerBiTangent;
        int posBufferIndex = 0;
        int texBufferIndex = 0;
        int norBufferIndex = 0;
        int tanBufferIndex = 0;
        int btnBufferIndex = 0;

        for(int i = 0; i < vertexBufferLength; i += stride) {
            int offset = 0;
            if(positionBuffer != null) {
                for (int pi = 0; pi < elementsPerPosition; pi++) {
                    vertexData[i + pi] = positionBuffer[posBufferIndex];
                    posBufferIndex++;
                }
                offset += elementsPerPosition;
            }

            if(texelBuffer != null) {
                for(int ti = 0; ti < elementsPerTexel; ti++) {
                    vertexData[i + offset + ti] = texelBuffer[texBufferIndex];
                    texBufferIndex++;
                }
                offset += elementsPerTexel;
            }

            if(normalBuffer != null) {
                for (int ni = 0; ni < elementsPerNormal; ni++) {
                    vertexData[i + offset + ni] = normalBuffer[norBufferIndex];
                    norBufferIndex++;
                }
                offset += elementsPerNormal;
            }

            if(tangentBuffer != null) {
                for (int ri = 0; ri < elementsPerTangent; ri++) {
                    vertexData[i + offset + ri] = tangentBuffer[tanBufferIndex];
                    tanBufferIndex++;
                }
                offset += elementsPerTangent;
            }

            if(bitangentBuffer != null) {
                for (int bi = 0; bi < elementsPerBiTangent; bi++) {
                    vertexData[i + offset + bi] = bitangentBuffer[btnBufferIndex];
                    btnBufferIndex++;
                }
                offset += elementsPerBiTangent;
            }
        }

        return vertexData;
    }

    /**
     * Set the vertex attribute pointer on the objects VAO. Please note that calling this often
     * defeats the purpose and optimisations of using VAO/VBOs. This should be done when changing
     * shaders to link the shaders attribute locations to the correct VAO in memory. E.g. A shader
     * program attribute is fetched with glGetAttributeLocation("name"), and attribute locations may
     * differ from shader to shader - especially if these are custom shaders.
     *
     * @param posAttribLoc          Position attribute location in shader
     * @param texelAttribLoc        Texel attribute location in shader
     * @param normalAttribLoc       Normal attribute location in shader
     * @param tangentAttribLoc      Tangent attribute location in shader
     * @param bitangentAttribLoc    Bi-tangent attribute location in shader
     * @since 1.0
     */
    public void setAttributePointers(int posAttribLoc, int texelAttribLoc, int normalAttribLoc,
                                     int tangentAttribLoc, int bitangentAttribLoc) {
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        if(elementsPerPosition > 0 && posAttribLoc != GL_INVALID_INDEX) {
            glVertexAttribPointer(posAttribLoc,  elementsPerPosition, GL_FLOAT, false,
                    stride, positionDataOffset * BYTES_PER_FLOAT);
            glEnableVertexAttribArray(posAttribLoc);
        }

        if(elementsPerTexel > 0 && texelAttribLoc != GL_INVALID_INDEX) {
            glVertexAttribPointer(texelAttribLoc, elementsPerTexel, GL_FLOAT, false,
                    stride, texelDataOffset * BYTES_PER_FLOAT);
            glEnableVertexAttribArray(texelAttribLoc);
        }

        if(elementsPerNormal > 0 && normalAttribLoc != GL_INVALID_INDEX) {
            glVertexAttribPointer(normalAttribLoc, elementsPerNormal, GL_FLOAT, false,
                    stride, normalDataOffset * BYTES_PER_FLOAT);
            glEnableVertexAttribArray(normalAttribLoc);
        }

        if(elementsPerTangent > 0 && tangentAttribLoc != GL_INVALID_INDEX) {
            glVertexAttribPointer(tangentAttribLoc, elementsPerTangent, GL_FLOAT, false,
                    stride, tangentDataOffset * BYTES_PER_FLOAT);
            glEnableVertexAttribArray(tangentAttribLoc);
        }

        if(elementsPerBitangent > 0 && bitangentAttribLoc != GL_INVALID_INDEX) {
            glVertexAttribPointer(bitangentAttribLoc, elementsPerBitangent, GL_FLOAT, false,
                    stride, bitangentDataOffset * BYTES_PER_FLOAT);
            glEnableVertexAttribArray(bitangentAttribLoc);
        }
    }

    /**
     * Resolve the data attribute offsets. This is where each data attribute starts looking at the
     * vertex data.
     *
     * @since 1.0
     */
    private void resolveAttributeOffsets(AttributeOrder_t attributeOrder) {
        // Figure out the attribute data offsets depending on the attribute order
        switch (attributeOrder) {
            case POSITION:
                positionDataOffset = 0;
                break;
            case POSITION_THEN_TEXEL:
                positionDataOffset = 0;
                texelDataOffset = elementsPerPosition;
                break;
            case POSITION_THEN_NORMAL:
                positionDataOffset = 0;
                normalDataOffset = elementsPerPosition;
                break;
            case POSITION_THEN_TEXEL_THEN_NORMAL:
                positionDataOffset = 0;
                texelDataOffset = elementsPerPosition;
                normalDataOffset = texelDataOffset + elementsPerTexel;
                tangentDataOffset = normalDataOffset + elementsPerNormal;
                bitangentDataOffset = tangentDataOffset + elementsPerTangent;
                break;
            case POSITION_THEN_NORMAL_THEN_TEXEL:
                positionDataOffset = 0;
                normalDataOffset = elementsPerPosition;
                texelDataOffset = normalDataOffset + elementsPerNormal;
                tangentDataOffset = texelDataOffset + elementsPerTexel;
                bitangentDataOffset = tangentDataOffset + elementsPerTangent;
                break;
        }
    }
}
