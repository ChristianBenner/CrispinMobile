package com.crispin.crispinmobile.Rendering.Utilities;

import glm_.vec3.Vec3;

public class Camera {
    Vec3 cameraPos = new Vec3(0.0f, 0.0f, 3.0f);
    Vec3 cameraRight = new Vec3(1.0f, 0.0f, 0.0f);
    Vec3 cameraUp = new Vec3(0.0f, 1.0f, 0.0f);
    Vec3 cameraDirection = cameraUp.cross(cameraRight);

    public Camera() {
        Vec3 test = new Vec3(3.0f, 0.0f, 0.0f);
        float dotProduct = cameraPos.dot(test);
        System.out.println("DOT PRODUCT: " + dotProduct);
        double angleRad = Math.acos(dotProduct);
        double angleDeg = Math.toDegrees(angleRad);
        System.out.println("ANGLE: " + angleDeg);
        System.out.println("CAMERA DIRECTION: " + cameraDirection);
    }
}
