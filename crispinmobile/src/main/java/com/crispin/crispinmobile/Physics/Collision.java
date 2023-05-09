package com.crispin.crispinmobile.Physics;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;

public class Collision {
    public static Vec2 getIntersection(Line2D line1, Line2D line2) {
        Vec2 p1 = line1.start;
        Vec2 p2 = line1.end;
        Vec2 p3 = line2.start;
        Vec2 p4 = line2.end;
        float det = (p2.x - p1.x) * (p4.y - p3.y) - (p4.x - p3.x) * (p2.y - p1.y);
        if (det == 0) {
            return null; // Lines are parallel
        } else {
            float ua = ((p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x)) / det;
            float ub = ((p2.x - p1.x) * (p1.y - p3.y) - (p2.y - p1.y) * (p1.x - p3.x)) / det;
            if (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1) {
                // Calculate intersection point
                float x = p1.x + ua * (p2.x - p1.x);
                float y = p1.y + ua * (p2.y - p1.y);
                return new Vec2(x, y);
            } else {
                return null; // Lines do not intersect within their defined range
            }
        }
    }

    public static Vec2 calculatePolygonCentroid(Vec2[] vertices) {
        float cx = 0, cy = 0;
        float area = 0;
        int n = vertices.length;

        for (int i = 0; i < n; i++) {
            Vec2 p1 = vertices[i];
            Vec2 p2 = vertices[(i + 1) % n];
            double cross = p1.x * p2.y - p2.x * p1.y;
            area += cross;
            cx += (p1.x + p2.x) * cross;
            cy += (p1.y + p2.y) * cross;
        }

        area /= 2;
        cx /= (6 * area);
        cy /= (6 * area);

        return new Vec2(cx, cy);
    }

    // 2D collision check. Only works for convex shapes not concave
    public static boolean checkCollision(HitboxRectangle rect1, HitboxRectangle rect2, Camera2D camera) {
        // Copy rectangle objects to not edit originals
        HitboxRectangle r1 = new HitboxRectangle(rect1);
        HitboxRectangle r2 = new HitboxRectangle(rect2);

        Vec2[] r1v = new Vec2[4];
        r1v[0] = new Vec2(r1.x, r1.y);
        r1v[1] = new Vec2(r1.x, r1.y + r1.h);
        r1v[2] = new Vec2(r1.x + r1.w, r1.y + r1.h);
        r1v[3] = new Vec2(r1.x + r1.w, r1.y);

        Vec2[] r2v = new Vec2[4];
        r2v[0] = new Vec2(r2.x, r2.y);
        r2v[1] = new Vec2(r2.x, r2.y + r2.h);
        r2v[2] = new Vec2(r2.x + r2.w, r2.y + r2.h);
        r2v[3] = new Vec2(r2.x + r2.w, r2.y);
        return checkCollision(r1v, r2v, camera);
    }

    private static Line2D[] getLines(Vec2[] polygon) {
        Line2D[] lines = new Line2D[polygon.length];
        for (int i = 0; i < polygon.length; i++) {
            int next = i == polygon.length - 1 ? 0 : i + 1;
            lines[i] = new Line2D(polygon[i].x, polygon[i].y, polygon[next].x, polygon[next].y);
        }

        return lines;
    }

    // 2D collision check. Only works for convex shapes not concave. WIP but supports any convex
    // polygon meaning that rotation should be supported also.
    public static boolean checkCollision(Vec2[] polygon1, Vec2[] polygon2, Camera2D camera) {
        Vec2 c1 = calculatePolygonCentroid(polygon1);
        Vec2 c2 = calculatePolygonCentroid(polygon2);

        Line2D lineCenter = new Line2D(c1.x, c1.y, c2.x, c2.y);
        Line2D[] r1lines = getLines(polygon1);
        Line2D[] r2lines = getLines(polygon2);

        Vec2 r1Intercept = null;
        int r1InterceptLine = 0;
        for (int i = 0; i < r1lines.length; i++) {
            // Should only intercept with one line so break when found
            r1Intercept = getIntersection(lineCenter, r1lines[i]);
            if (r1Intercept != null) {
                r1lines[i].setColour(Colour.RED);
                r1InterceptLine = i;
                break;
            }
        }

        Vec2 r2Intercept = null;
        int r2InterceptLine = 0;
        for (int i = 0; i < r2lines.length; i++) {
            // Should only intercept with one line so break when found
            r2Intercept = getIntersection(lineCenter, r2lines[i]);
            if (r2Intercept != null) {
                r2lines[i].setColour(Colour.RED);
                r2InterceptLine = i;
                break;
            }
        }


        // todo: REMOVE - Render for debugging
        lineCenter.render(camera);
        for (int i = 0; i < r1lines.length; i++) {
            r1lines[i].render(camera);
        }
        for (int i = 0; i < r2lines.length; i++) {
            r2lines[i].render(camera);
        }

        // There is one or no intercepts so they must be colliding (one center point must be within
        // the others lines)
        if (r1Intercept == null || r2Intercept == null) {
            return true;
        }

        float c1r1InterceptDistance = Geometry.minus(r1Intercept, c1).getMagnitude();
        float c1r2InterceptDistance = Geometry.minus(r2Intercept, c1).getMagnitude();
        if (c1r2InterceptDistance < c1r1InterceptDistance) {
            return true;
        }

        if (getIntersection(r1lines[r1InterceptLine], r2lines[r2InterceptLine]) != null) {
            return true;
        }

        return false;
    }
}