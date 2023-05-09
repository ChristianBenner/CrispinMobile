package com.crispin.crispinmobile.Physics;

import com.crispin.crispinmobile.Geometry.Vec2;

public class HitboxCircle {
        private Vec2 center;
        private float radius;

        public HitboxCircle(Vec2 center, float radius) {
            this.center = new Vec2(center);
            this.radius = radius;
        }

        public Vec2 getCenter() {
            return center;
        }

        public void setCenter(Vec2 center) {
            this.center.x = center.x;
            this.center.y = center.y;
        }

        public void setCenter(float x, float y) {
            this.center.x = x;
            this.center.y = y;
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
            return (float)(Math.PI * radius * radius);
        }

        public float getCircumference() {
            return (float)(Math.PI * radius * 2f);
        }
    }