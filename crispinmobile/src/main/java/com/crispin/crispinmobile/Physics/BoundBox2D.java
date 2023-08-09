package com.crispin.crispinmobile.Physics;

import android.opengl.Matrix;

import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;

public class BoundBox2D {
    public float x;
    public float y;
    public float w;
    public float h;

    public BoundBox2D(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public BoundBox2D(Vec2 position, Scale2D scale) {
        this.x = position.x;
        this.y = position.y;
        this.w = scale.w;
        this.h = scale.h;
    }

    public boolean collidesWith(BoundBox2D other) {
        return x < other.x + other.w &&
                x + w > other.x &&
                y < other.y + other.h &&
                y + h > other.y;
    }

    public BoundBox2D transform(ModelMatrix modelMatrix) {
        float[] vertices = new float[] {
                x, y, 0f, 1f,
                x + w, y, 0f, 1f,
                x + w, y + h, 0f, 1f,
                x, y + h, 0f, 1f
        };

        float[] matrix = modelMatrix.getFloats();
        float[] transformedVertices = new float[16];
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;

        for(int i = 0; i < 4; i++) {
            int index = i * 4;
            Matrix.multiplyMV(transformedVertices, index, matrix, 0, vertices, index);
            float x = transformedVertices[index];
            float y = transformedVertices[index + 1];
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }

        return new BoundBox2D(minX, minY, maxX - minX, maxY - minY);
    }
}
