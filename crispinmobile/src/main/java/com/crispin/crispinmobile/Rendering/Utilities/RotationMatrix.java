package com.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.crispin.crispinmobile.Geometry.Vec3;

public class RotationMatrix {
    private final float[] rotationMatrix;
    private final float[] invertedRotation;

    public RotationMatrix() {
        rotationMatrix = new float[16];
        invertedRotation = new float[16];
        reset();
    }

    public void applyRotation(Vec3 rotationAxis, float angle) {
        float[] calcAxis = new float[4];
        float[] axisOfRotation = {rotationAxis.x, rotationAxis.y, rotationAxis.z, 1.0f};
        Matrix.invertM(invertedRotation, 0, rotationMatrix, 0);
        Matrix.multiplyMV(calcAxis, 0, invertedRotation, 0, axisOfRotation, 0);
        Matrix.rotateM(rotationMatrix, 0, angle, calcAxis[0], calcAxis[1], calcAxis[2]);
    }

    public void reset() {
        Matrix.setIdentityM(rotationMatrix, 0);
    }

    public float[] getRotationMatrix() {
        return this.rotationMatrix;
    }
}
