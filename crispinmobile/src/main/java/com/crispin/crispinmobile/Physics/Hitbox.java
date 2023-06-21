package com.crispin.crispinmobile.Physics;

import com.crispin.crispinmobile.Geometry.Vec2;

public interface Hitbox {
    public boolean isColliding(HitboxPolygon hitbox);

    public boolean isColliding(HitboxCircle hitbox);

    public Vec2 isCollidingMTV(HitboxPolygon hitbox);

    public Vec2 isCollidingMTV(HitboxCircle hitbox);
}
