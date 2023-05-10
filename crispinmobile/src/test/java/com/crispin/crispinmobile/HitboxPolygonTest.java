package com.crispin.crispinmobile;

import org.junit.Test;

import static org.junit.Assert.*;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Physics.HitboxPolygon;

public class HitboxPolygonTest {
    private static final int NUM_RUNS = 100000;

    private final HitboxPolygon polygon1;
    private final HitboxPolygon polygon2;
    private final HitboxPolygon polygon3; // collides with polygon1
    private final HitboxPolygon circle1;
    private final HitboxPolygon circle2;
    private final HitboxPolygon circle3; // collides with circle1

    public HitboxPolygonTest() {
        polygon1 = new HitboxPolygon(new float[]{-150f, 0f, 150f, 0f, 150f, 300f, -150f, 500f});
        polygon2 = new HitboxPolygon(new float[]{450f, 450f, 750f, 450f, 750f, 750f, 450f, 750f});
        polygon3 = new HitboxPolygon(new float[]{0f, 100f, 300f, 100f, 300f, 400f, 0f, 400f});

        circle1 = new HitboxPolygon(generateCirclePolygon(new Vec2(100f, 100f), 300f, 200));
        circle2 = new HitboxPolygon(generateCirclePolygon(new Vec2(700f, 700f), 300f, 200));
        circle3 = new HitboxPolygon(generateCirclePolygon(new Vec2(400f, 100f), 300f, 200));
    }

    private float[] generateCirclePolygon(Vec2 center, float radius, int numPoints) {
        float[] polygon = new float[numPoints * 2];
        float angleIncrement = (float)(2f * Math.PI / (double)numPoints);
        for (int i = 0; i < numPoints; i++) {
            float angle = i * angleIncrement;
            polygon[i] = center.x + radius * (float) Math.cos(angle);
            polygon[i + 1] = center.y + radius * (float) Math.sin(angle);
        }
        return polygon;
    }

    @Test
    public void CollisionPerformanceCheck() {
        System.out.println("LOW POLY RECTANGLE:");
        CustomNonCollisionPerformanceCheck(polygon1, polygon2);
        CustomCollisionPerformanceCheck(polygon1, polygon3);
//        SATNonCollisionPerformanceCheck(polygon1, polygon2);
//        SATCollisionPerformanceCheck(polygon1, polygon3);

        System.out.println("HIGH POLY CIRCLE:");
        CustomNonCollisionPerformanceCheck(circle1, circle2);
        CustomCollisionPerformanceCheck(circle1, circle3);
//        SATNonCollisionPerformanceCheck(circle1, circle2);
//        SATCollisionPerformanceCheck(circle1, circle3);
    }

//    public void SATCollisionPerformanceCheck(HitboxPolygon p1, HitboxPolygon p2) {
//        long timeStart = System.currentTimeMillis();
//        for(int i = 0; i < NUM_RUNS; i++) {
//            assertTrue(SAT.checkCollision(p1, p2));
//        }
//        long timeEnd = System.currentTimeMillis();
//        System.out.println("\tSATCollisionPerformanceCheck MS: " + (timeEnd - timeStart));
//    }
//
//    public void SATNonCollisionPerformanceCheck(HitboxPolygon p1, HitboxPolygon p2) {
//        long timeStart = System.currentTimeMillis();
//        for(int i = 0; i < NUM_RUNS; i++) {
//            assertFalse(SAT.checkCollision(p1, p2));
//        }
//        long timeEnd = System.currentTimeMillis();
//        System.out.println("\tSATNonCollisionPerformanceCheck MS: " + (timeEnd - timeStart));
//    }

    public void CustomCollisionPerformanceCheck(HitboxPolygon p1, HitboxPolygon p2) {
        long timeStart = System.currentTimeMillis();
        for(int i = 0; i < NUM_RUNS; i++) {
            assertTrue(p1.isColliding(p2));
        }
        long timeEnd = System.currentTimeMillis();
        System.out.println("\tCustomCollisionPerformanceCheck MS: " + (timeEnd - timeStart));
    }

    public void CustomNonCollisionPerformanceCheck(HitboxPolygon p1, HitboxPolygon p2) {
        long timeStart = System.currentTimeMillis();
        for(int i = 0; i < NUM_RUNS; i++) {
            assertFalse(p1.isColliding(p2));
        }
        long timeEnd = System.currentTimeMillis();
        System.out.println("\tCustomNonCollisionPerformanceCheck MS: " + (timeEnd - timeStart));
    }
}