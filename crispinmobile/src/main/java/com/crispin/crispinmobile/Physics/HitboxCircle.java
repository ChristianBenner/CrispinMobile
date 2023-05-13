package com.crispin.crispinmobile.Physics;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;

public class HitboxCircle {
    float centerX;
    float centerY;
    float radius;

    private Square square; // use a square with a circle texture to render circle for now

    public HitboxCircle(float centerX, float centerY, float radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }

    public HitboxCircle(Vec2 center, float radius) {
        this(center.x, center.y, radius);
    }

    public Vec2 isColliding(HitboxCircle other) {
        return Collision.isColliding(this, other);
    }

    public boolean isColliding(HitboxPolygon polygon) {
        return Collision.isColliding(polygon, this);
    }

    public Vec2 getCenter() {
        return new Vec2(centerX, centerY);
    }

    public void translate(float x, float y) {
        this.centerX = this.centerX + x;
        this.centerY = this.centerY + y;
    }

    public void setCenter(Vec2 center) {
        this.centerX = center.x;
        this.centerY = center.y;
    }

    public void setCenter(float x, float y) {
        this.centerX = x;
        this.centerY = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getDiameter() {
        return radius * 2f;
    }

    public void setDiameter(float diameter) {
        this.radius = diameter / 2f;
    }

    public float getArea() {
        return (float) (Math.PI * radius * radius);
    }

    public float getCircumference() {
        return (float) (Math.PI * radius * 2f);
    }

    public void setColour(Colour colour) {
        if(square == null) {
            square = new Square(R.drawable.radii_bound_circle);
        }
        square.setColour(colour);
    }

    public void render(Camera2D camera2D) {
        if(square == null) {
            square = new Square(R.drawable.radii_bound_circle);
            square.setColour(new Colour(0.0f, 0.0f, 1.0f, 0.3f));
        }

        square.setPosition(centerX - radius, centerY - radius);
        square.setScale(radius * 2, radius * 2);
        square.render(camera2D);
    }
}