package com.crispin.crispinmobile.Rendering.Models;

import com.crispin.crispinmobile.Rendering.Utilities.Mesh;

public class ShadowMeshUtil {
    public static Mesh createShadowMesh2D(float[] positionData) {
        return new Mesh(ShadowMeshUtil.createShadowVertices2D(positionData), null,
                null, Mesh.RenderMethod.TRIANGLES, 3, 0,
                0);
    }

    // positionBuffer must be 2D (x, y co-ordinates)
    public static float[] createShadowVertices2D(float[] positionBuffer) {
        // With the position data that's come in, create multiple vertices, one will be used to
        // calculate the far side of the shadow. The z buffer of '1' represents the point is the far
        // side of the shadow so that the vertex shader can calculate the shadow and therefor it is
        // hardware accelerated. For example:
        //  Vec3 point = new Vec3(positionBuffer[0], positionBuffer[1], 0f);
        //  Vec3 farPoint = new Vec3(positionBuffer[0], positionBuffer[1], 1f);


        // First we need to work out each points for each line segment on the polygon
        // (e.g. A->B,B->C) but then for each line segment upload a point that will represent the
        // far co-ordinate that the vertex shader will calculate the shadow for. The amount of data
        // needed is higher, for each line segment to we need to draw 2 triangles. A triangle is
        // 3 points * 2 components (6 floats per

        // There are as many lines as there are points
        // lines = positionBuffer.length / 2
        // There are 3 components, xy for position and z to imply if the points is shadow far or not
        // Each line needs 2 triangles. A triangle is 3 points * 3 components so
        // total components = 2 * 3 * 3 = 18 floats per line
        // Num of lines * 18
        // bufferSize = (18 * positionBuffer.length) / 2 or
        // bufferSize = 9 * positionBuffer.length
        float[] shadowMeshPositionBuffer = new float[(positionBuffer.length) * 9];
        for (int i = 0; i < positionBuffer.length; i += 2) {
            float ax = positionBuffer[i];
            float ay = positionBuffer[i + 1];

            int nextIndex = (i + 2) % positionBuffer.length;
            float bx = positionBuffer[nextIndex];
            float by = positionBuffer[nextIndex + 1];

            int destIndex = i * 9;

            // Triangle 1
            // Vertex 1
            shadowMeshPositionBuffer[destIndex + 0] = ax;
            shadowMeshPositionBuffer[destIndex + 1] = ay;
            shadowMeshPositionBuffer[destIndex + 2] = 0f;
            // Vertex 2
            shadowMeshPositionBuffer[destIndex + 3] = ax;
            shadowMeshPositionBuffer[destIndex + 4] = ay;
            shadowMeshPositionBuffer[destIndex + 5] = 1f;
            // Vertex 3
            shadowMeshPositionBuffer[destIndex + 6] = bx;
            shadowMeshPositionBuffer[destIndex + 7] = by;
            shadowMeshPositionBuffer[destIndex + 8] = 0f;

            // Triangle 2
            // Vertex 1
            shadowMeshPositionBuffer[destIndex +  9] = bx;
            shadowMeshPositionBuffer[destIndex + 10] = by;
            shadowMeshPositionBuffer[destIndex + 11] = 0f;
            // Vertex 2
            shadowMeshPositionBuffer[destIndex + 12] = ax;
            shadowMeshPositionBuffer[destIndex + 13] = ay;
            shadowMeshPositionBuffer[destIndex + 14] = 1f;
            // Vertex 3
            shadowMeshPositionBuffer[destIndex + 15] = bx;
            shadowMeshPositionBuffer[destIndex + 16] = by;
            shadowMeshPositionBuffer[destIndex + 17] = 1f;
        }

        return shadowMeshPositionBuffer;
    }

//    public ShadowModel(float[] positionBuffer, Material material) {
//        // Create the shadow model
//        super(new Mesh(createShadowMesh2D(positionBuffer), null, null, Mesh.RenderMethod.TRIANGLES,
//                2, 0, 0), material);
//
//    }
//
//    public ShadowModel(Mesh mesh, Material material) {
//        // Create the shadow model
//        super(new Mesh(createShadowMesh2D(mesh.), null, null, Mesh.RenderMethod.TRIANGLES,
//                2, 0, 0), material);
//
//    }
}
