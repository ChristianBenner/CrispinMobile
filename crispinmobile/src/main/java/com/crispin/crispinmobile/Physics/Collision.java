package com.crispin.crispinmobile.Physics;

import com.crispin.crispinmobile.Geometry.Vec2;

public class Collision {
    public static boolean isColliding(BoundBox2D boundBox2D, BoundBox2D other) {
        return boundBox2D.collidesWith(other);
    }

    public static boolean isColliding(HitboxCircle circle, HitboxCircle other) {
        // Get the distance between the two center points of the circles
        float distance = distance(circle.centerX, circle.centerY, other.centerX, other.centerY);
        return distance < circle.radius + other.radius;
    }

    // Return the shortest vector to translate the other circle away
    public static Vec2 isCollidingMTV(HitboxCircle circle, HitboxCircle other) {
        // Get the distance between the two center points of the circles
        float distance = distance(circle.centerX, circle.centerY, other.centerX, other.centerY);
        if (distance < circle.radius + other.radius) {
            // Scale the vector between the circles by the intercept multiplier
            float in = ((circle.radius + other.radius) - distance) / distance;
            float translateX = (other.centerX - circle.centerX) * in;
            float translateY = (other.centerY - circle.centerY) * in;
            return new Vec2(translateX, translateY);
        }
        return null;
    }

    public static boolean isColliding(HitboxPolygon polygon, HitboxCircle circle) {
        return isCollidingMTV(polygon, circle) != null;
    }

    // Is colliding with circle check. todo: calc MTV
    public static Vec2 isCollidingMTV(HitboxPolygon polygon, HitboxCircle circle) {
//        boolean collision = false;
//
//        Vec2 mtv = new Vec2();

        for (int i = 0; i < polygon.points.length; i += 2) {
            int next = (i + 2) % polygon.points.length;

            // Calculate closest point on this edge to the circle
            float edgeVectorX = polygon.points[next] - polygon.points[i];
            float edgeVectorY = polygon.points[next + 1] - polygon.points[i + 1];
            float toPointVectorX = circle.getCenter().x - polygon.points[i];
            float toPointVectorY = circle.getCenter().y - polygon.points[i + 1];
            float dotProductToPointEdge = dotProduct(toPointVectorX, toPointVectorY, edgeVectorX, edgeVectorY);
            if (dotProductToPointEdge < 0) {
                // the circle center is behind the edge, check distance to edge start point
                float distanceToStart = distance(circle.getCenter().x, circle.getCenter().y, polygon.points[i], polygon.points[i + 1]);

                if (distanceToStart < circle.getRadius()) {
//                    float distance = circle.getRadius() - distanceToStart;
//                    if(!collision || distance > mtv.getMagnitude()) {
//                        mtv.x = circle.getCenter().x - polygon.points[i];
//                        mtv.y = circle.getCenter().y - polygon.points[i + 1];
//                    }

                    // collision detected
                    return new Vec2();
                }
            } else if (dotProductToPointEdge > edgeVectorX * edgeVectorX + edgeVectorY * edgeVectorY) {
                // the circle center is past the edge end point, check distance to edge end point
                float distanceToEnd = distance(circle.getCenter().x, circle.getCenter().y, polygon.points[next], polygon.points[next + 1]);

                if (distanceToEnd < circle.getRadius()) {
                    // collision detected
                    return new Vec2();
                }
            } else {
                // the circle center is between the edge start and end points, calculate distance to edge
                float perpendicularDistance = Math.abs((toPointVectorX * edgeVectorY - toPointVectorY * edgeVectorX) / distance(edgeVectorX, edgeVectorY, 0, 0));

                if (perpendicularDistance < circle.getRadius()) {
                    // collision detected
                    return new Vec2();
                }
            }
        }

        // no collision detected
        return null;
    }

    public static boolean isColliding(HitboxPolygon polygon, HitboxPolygon other) {
        return isCollidingMTV(polygon, other) != null;
    }

    // Using SAT collision algorithm
    public static Vec2 isCollidingMTV(HitboxPolygon polygon, HitboxPolygon other) {
        float[] polygonProjection = new float[2];
        float[] otherProjection = new float[2];

        float minOverlap = Float.MAX_VALUE;

        // The axis that contained the smallest overlap
        float minOverlapAxisX = 0f;
        float minOverlapAxisY = 0f;

        // Collision check using this polygons axes
        for (int i = 0; i < polygon.axes.length; i += 2) {
            projectPolygon(polygonProjection, polygon.axes[i], polygon.axes[i + 1], polygon.points);
            projectPolygon(otherProjection, polygon.axes[i], polygon.axes[i + 1], other.points);
            if (!overlap(polygonProjection, otherProjection)) {
                return null;
            } else {
                float overlap = calculateOverlap(polygonProjection, otherProjection);
                if (overlap < minOverlap) {
                    // The new smallest overlap (which is so far the shortest distance to seperate
                    // the two polygons
                    minOverlap = overlap;

                    // The axis that contained the smallest overlap
                    minOverlapAxisX = polygon.axes[i];
                    minOverlapAxisY = polygon.axes[i + 1];
                }
            }
        }

        // Collision check using other polygons axes
        for (int i = 0; i < other.axes.length; i += 2) {
            projectPolygon(polygonProjection, other.axes[i], other.axes[i + 1], polygon.points);
            projectPolygon(otherProjection, other.axes[i], other.axes[i + 1], other.points);
            if (!overlap(polygonProjection, otherProjection)) {
                return null;
            } else {
                float overlap = calculateOverlap(polygonProjection, otherProjection);
                if (overlap < minOverlap) {
                    // The new smallest overlap (which is so far the shortest distance to seperate
                    // the two polygons
                    minOverlap = overlap;

                    // The axis that contained the smallest overlap
                    minOverlapAxisX = other.axes[i];
                    minOverlapAxisY = other.axes[i + 1];
                }
            }
        }

        // Calculate the shortest vector for separation (minimum translaction vector)
        // All the axis are normalised so they represent the direction in which to move the polygon.
        // The minOverlapAxisX, Y contains the direction in which the smallest overlap occurred so
        // we can multiply the size of the smallest overlap to get the MTV;
        float minimumTranslationX = minOverlapAxisX * minOverlap;
        float minimumTranslationY = minOverlapAxisY * minOverlap;

        // Determine the correct direction for the MTV
        float centerToCenterX = polygon.centerX - other.centerX;
        float centerToCenterY = polygon.centerY - other.centerY;
        float dotProduct = centerToCenterX * minimumTranslationX + centerToCenterY * minimumTranslationY;
        if (dotProduct < 0) {
            minimumTranslationX = -minimumTranslationX;
            minimumTranslationY = -minimumTranslationY;
        }

        return new Vec2(minimumTranslationX, minimumTranslationY);
    }

    private static float calculateOverlap(float[] proj1, float[] proj2) {
        return Math.min(proj2[1] - proj1[0], proj1[1] - proj2[0]);
    }

    private static void projectPolygon(float[] projectionOut, float axisX, float axisY, float[] polygon) {
        float min = dotProduct(axisX, axisY, polygon[0], polygon[1]);
        float max = min;
        for (int i = 2; i < polygon.length; i += 2) {
            float dot = dotProduct(axisX, axisY, polygon[i], polygon[i + 1]);
            if (dot < min) {
                min = dot;
            } else if (dot > max) {
                max = dot;
            }
        }

        projectionOut[0] = min;
        projectionOut[1] = max;
    }

    private static boolean overlap(float[] proj1, float[] proj2) {
        return !(proj1[1] <= proj2[0] || proj2[1] <= proj1[0]);
    }

    private static float dotProduct(float x1, float y1, float x2, float y2) {
        return (x1 * x2) + (y1 * y2);
    }

    private static float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}
