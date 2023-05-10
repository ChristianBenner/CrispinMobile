package com.crispin.crispinmobile.Physics;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Models.Line;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;

public class HitboxPolygon {
    private float[] data;
    private Vec2[] transformedPoints;
    private Line[] lines;

    // Assumes two components (xy)
    public HitboxPolygon(float[] data) {
        this.data = data;

        // Allocate now so it does not need to be allocate every transform call (expensive)
        this.transformedPoints = new Vec2[data.length / 2];
        for (int i = 0; i < transformedPoints.length; i++) {
            transformedPoints[i] = new Vec2();
        }
    }


    public void transform(ModelMatrix modelMatrix) {
        float[] m = modelMatrix.getFloats();
        for (int i = 0; i < data.length; i += 2) {
            // Cheaper way to transform just 2 co-ordinates with the matrix
            transformedPoints[i / 2].x = m[0] * data[i] + m[4] * data[i + 1] + m[12];
            transformedPoints[i / 2].y = m[1] * data[i] + m[5] * data[i + 1] + m[13];
        }
    }

    public void render(Camera2D camera2D) {
        if (lines == null) {
            // Initialise lines for debugging
            this.lines = new Line[transformedPoints.length];
            for (int i = 0; i < lines.length; i++) {
                lines[i] = new Line();
                lines[i].setColour(Colour.BLUE);
            }
        }

        for (int i = 0; i < transformedPoints.length; i++) {
            int next = i == transformedPoints.length - 1 ? 0 : i + 1;
            lines[i].setPoints(transformedPoints[i].x, transformedPoints[i].y, transformedPoints[next].x, transformedPoints[next].y);
            lines[i].render(camera2D);
        }
    }

    /*
        2D collision check. Only works for convex shapes not concave. WIP but supports any convex
        polygon meaning that rotation should be supported also. The algorithm assumes that the polygons
        are convex and calculates the centroid of each polygon accordingly.
        1. Calculate the centroids of each polygon, and use them to compute the distance between the two
            polygons.
        2. If the sum of the maximum radii of the two polygons is less than the distance between the
            centroids, then there is no collision, so return false.
        3. Check if the bounding boxes of the two polygons intersect. If they don't intersect, then
            there is no collision, so return false.
        4. Find the line passing through the centroids of the two polygons, and check for intersections
            between this line and each edge of both polygons. If there are no intersections, then there
            is no collision, so return false.
        5. If there are intersections, check the distances between the centroid and each intersection
            point for both polygons. If the distance between the centroid and the intersection point for
            the second polygon is smaller than the distance between the centroid and the intersection
            point for the first polygon, then the polygons are colliding, so return true.
        6. If the distances are equal or if the first polygon is closer to the intersection point, check
            if there is an intersection between the edges of the two polygons that share the two
            intersection points. If there is an intersection, then the polygons are colliding, so return
            true. Otherwise, there is no collision, so return false.
         */
    public boolean isColliding(HitboxPolygon other) {
        Vec2 c1 = calculatePolygonCentroid(transformedPoints);
        Vec2 c2 = calculatePolygonCentroid(other.transformedPoints);

        PolygonInfo polygon1Info = getPolygonInfo(transformedPoints, c1);
        PolygonInfo polygon2Info = getPolygonInfo(other.transformedPoints, c2);

        // Comparing the max radii for each allows us to check if something is too far away to
        // consider using more expensive collision algorithm on it
        float centerLineLength = Geometry.getDistance(c1.x, c1.y, c2.x, c2.y);
        if (polygon1Info.maxRadii + polygon2Info.maxRadii < centerLineLength) {
            return false;
        }

        // Check boundary box collision. This is a rectangle that is around encompasses the entire
        // polygon. If the two rectangles do not overlap, then they cannot be colliding and it will
        // save computation of more extensive check
        if (!checkBoundCollision(polygon1Info, polygon2Info)) {
            return false;
        }

        Line2D lineCenter = new Line2D(c1.x, c1.y, c2.x, c2.y);
        Line2D[] r1lines = polygon1Info.lines;
        Line2D[] r2lines = polygon2Info.lines;

        Vec2 r1Intercept = null;
        int r1InterceptLine = 0;
        for (int i = 0; i < r1lines.length; i++) {
            // Should only intercept with one line so break when found
            r1Intercept = getIntersection(lineCenter, r1lines[i]);
            if (r1Intercept != null) {
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
                r2InterceptLine = i;
                break;
            }
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

    private class Line2D {
        private Vec2 start;
        private Vec2 end;

        public Line2D(float sx, float sy, float ex, float ey) {
            this.start = new Vec2(sx, sy);
            this.end = new Vec2(ex, ey);
        }
    }

    private class PolygonInfo {
        private Line2D[] lines;
        private float maxRadii;
        private float boundX;
        private float boundY;
        private float boundEx;
        private float boundEy;
    }

    public Vec2 getIntersection(Line2D line1, Line2D line2) {
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

    public Vec2 calculatePolygonCentroid(Vec2[] vertices) {
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

    private PolygonInfo getPolygonInfo(Vec2[] points, Vec2 centoid) {
        PolygonInfo polygonInfo = new PolygonInfo();
        polygonInfo.lines = new Line2D[points.length];
        for (int i = 0; i < points.length; i++) {
            int next = i == points.length - 1 ? 0 : i + 1;
            polygonInfo.lines[i] = new Line2D(points[i].x, points[i].y, points[next].x, points[next].y);
            polygonInfo.maxRadii = Math.max(Geometry.getDistance(centoid, points[i]), polygonInfo.maxRadii);
            polygonInfo.boundX = i == 0 ? points[i].x : Math.min(polygonInfo.boundX, points[i].x);
            polygonInfo.boundY = i == 0 ? points[i].y : Math.min(polygonInfo.boundY, points[i].y);
            polygonInfo.boundEx = i == 0 ? points[i].x : Math.max(polygonInfo.boundEx, points[i].x);
            polygonInfo.boundEy = i == 0 ? points[i].y : Math.max(polygonInfo.boundEy, points[i].y);
        }

        return polygonInfo;
    }

    // Simple rectangle same axis collision check
    private boolean checkBoundCollision(PolygonInfo box1, PolygonInfo box2) {
        return box1.boundX <= box2.boundEx && box2.boundX <= box1.boundEx &&
                box1.boundY <= box2.boundEy && box2.boundY <= box1.boundEy;
    }
}