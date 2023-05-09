package com.crispin.crispinmobile.Physics;

import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;

public class HitboxRectangle {
    public float x;
    public float y;
    public float w;
    public float h;
    public float rotation; // in degrees around center

    public HitboxRectangle(float x, float y, float w, float h, float rotationDegrees) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.rotation = rotationDegrees;
    }

    public HitboxRectangle(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.rotation = 0f;
    }

    public HitboxRectangle(HitboxRectangle rect) {
        this(rect.x, rect.y, rect.w, rect.h, rect.rotation);
    }

    public HitboxRectangle(Vec2 position, Scale2D size, float rotationDegrees) {
        this(position.x, position.y, size.w, size.h, rotationDegrees);
    }

    public HitboxRectangle(Vec2 position, Scale2D size) {
        this(position.x, position.y, size.w, size.h, 0f);
    }

    public Vec2 getPosition() {
        return new Vec2(x, y);
    }

    public void setPosition(Vec2 position) {
        this.x = position.x;
        this.y = position.y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Scale2D getSize() {
        return new Scale2D(w, h);
    }

    public void setSize(Scale2D size) {
        this.w = size.w;
        this.h = size.h;
    }

    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;
    }

    public float getRotation() {
        return rotation;
    }

    // set rotation in degrees
    public void setRotation(float degrees) {
        this.rotation = degrees;
    }
}