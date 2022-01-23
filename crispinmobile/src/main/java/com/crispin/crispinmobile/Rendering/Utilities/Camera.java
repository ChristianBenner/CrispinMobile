package com.crispin.crispinmobile.Rendering.Utilities;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec3;

public class Camera {
    private final Vec3 position;
    private final Vec3 target;
    private final Vec3 direction;

    public Camera() {
        position = new Vec3(0.0f, 0.0f, 3.0f);
        target = new Vec3(0.0f, 0.0f, 0.0f);
        direction = Geometry.normalize(Geometry.minus(position, target));
    }
}
