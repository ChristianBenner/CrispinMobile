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
}