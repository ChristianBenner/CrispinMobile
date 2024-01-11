package com.crispin.demos.InstancingDemos.GameDemo2D;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Physics.HitboxPolygon;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Models.Square;

public class Bullet {
    public Vec2 velocity;
    public Square sprite;
    public long spawnTime;
    public HitboxPolygon hitboxRectangle;
    public PointLight light;
}