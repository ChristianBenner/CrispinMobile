package com.crispin.crispinmobile.Physics;

public class HitboxRectangle extends HitboxPolygon {
    private static final float[] POSITION_DATA =
            {
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,
            };


    public HitboxRectangle() {
        super(POSITION_DATA);
    }

    public HitboxRectangle(float x, float y, float w, float h) {
        super(new float[]{
                x, y,
                x, y + h,
                x + w, y + h,
                x + w, y
        });
    }
}