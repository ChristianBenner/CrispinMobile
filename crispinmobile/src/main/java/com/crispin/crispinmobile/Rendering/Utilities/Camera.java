package com.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec3;

public class Camera {


    private final Vec3 DEFAULT_UP = new Vec3(0.0f, 1.0f, 0.0f);

    private final Vec3 DEFAULT_FRONT = new Vec3(0.0f, 0.f, -1.0f);

    // Default target is set to the origin (so the camera points at the center of the world space)
    private final Vec3 DEFAULT_TARGET = new Vec3(0.0f, 0.0f, 0.0f);

    private Vec3 position;
    private Vec3 direction;
    private Vec3 front;
    private Vec3 right;
    private Vec3 up;
    private float[] viewMatrix;
    private float[] perspectiveMatrix;
    private float yaw;
    private float pitch;

    public Camera() {
        position = new Vec3(0.0f, 0.0f, 3.0f);
        direction = Geometry.normalize(Geometry.minus(position, DEFAULT_TARGET));
        right = Geometry.normalize(Geometry.crossProduct(DEFAULT_UP, direction));
        up = Geometry.crossProduct(direction, right);
        front = new Vec3(DEFAULT_FRONT);

        viewMatrix = new float[16];
        perspectiveMatrix = new float[16];

        final float aspectRatio = (float) Crispin.getSurfaceWidth() / Crispin.getSurfaceHeight();

        // Generate perspective matrix
        Matrix.perspectiveM(perspectiveMatrix, 0, 59, aspectRatio, 0.1f, 100.0f);
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setPosition(Vec3 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;
    }

    public Vec3 getPosition() {
        return position;
    }

    public float[] getPerspectiveMatrix() {
        return perspectiveMatrix;
    }

    public float[] getViewMatrix() {
        Matrix.setLookAtM(viewMatrix, 0, position.x, position.y, position.z,
                position.x + front.x, position.y + front.y,
                position.z + front.z, up.x, up.y, up.z);
        return viewMatrix;
    }

    private void updateVectors() {
        Vec3 dir = new Vec3();
        dir.x = (float)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        dir.y = (float)(Math.sin(Math.toRadians(pitch)));
        dir.z = (float)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front = Geometry.normalize(dir);
        right = Geometry.normalize(Geometry.crossProduct(front, DEFAULT_UP));
        up = Geometry.normalize(Geometry.crossProduct(right, front));
    }

    public void setPitch(float degrees) {
        this.pitch = degrees;
    }

    public void setYaw(float degrees) {
        this.yaw = degrees;
    }

    public void setRotation(float pitchDegrees, float yawDegrees) {
        this.pitch = pitchDegrees;
        this.yaw = yawDegrees;
    }

    public void rotate(float pitchDegrees, float yawDegrees) {
        this.pitch += pitchDegrees;
        this.yaw += yawDegrees;
    }
}
