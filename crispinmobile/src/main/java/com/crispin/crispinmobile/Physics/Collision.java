package com.crispin.crispinmobile.Physics;

import com.crispin.crispinmobile.Geometry.Vec2;

public class Collision {
    // Return the shortest vector to translate the other circle away
    public static Vec2 isColliding(HitboxCircle circle, HitboxCircle other) {
        // Get the distance between the two center points of the circles
        float distance = distance(circle.centerX, circle.centerY, other.centerX, other.centerY);
        if(distance < circle.radius + other.radius) {
            // Scale the vector between the circles by the intercept multiplier
            float in = ((circle.radius + other.radius) - distance) / distance;
            float translateX = (other.centerX - circle.centerX) * in;
            float translateY = (other.centerY - circle.centerY) * in;
            return new Vec2(translateX, translateY);
        }
        return null;
    }

    // Is colliding with circle check
    public static boolean isColliding(HitboxPolygon polygon, HitboxCircle circle) {
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
                    // collision detected
                    return true;
                }
            } else if (dotProductToPointEdge > edgeVectorX * edgeVectorX + edgeVectorY * edgeVectorY) {
                // the circle center is past the edge end point, check distance to edge end point
                float distanceToEnd = distance(circle.getCenter().x, circle.getCenter().y, polygon.points[next], polygon.points[next + 1]);

                if (distanceToEnd < circle.getRadius()) {
                    // collision detected
                    return true;
                }
            } else {
                // the circle center is between the edge start and end points, calculate distance to edge
                float perpendicularDistance = Math.abs((toPointVectorX * edgeVectorY - toPointVectorY * edgeVectorX) / distance(edgeVectorX, edgeVectorY, 0, 0));

                if (perpendicularDistance < circle.getRadius()) {
                    // collision detected
                    return true;
                }
            }
        }

        // no collision detected
        return false;
    }

    // Using SAT collision algorithm
    public static boolean isColliding(HitboxPolygon polygon, HitboxPolygon other) {
        float[] projectionMyAxis = new float[2];
        float[] projectionOtherAxis = new float[2];

        // Collision check using this polygons axes
        for (int i = 0; i < polygon.axes.length; i += 2) {
            projectPolygon(projectionMyAxis, polygon.axes[i], polygon.axes[i + 1], polygon.points);
            projectPolygon(projectionOtherAxis, polygon.axes[i], polygon.axes[i + 1], other.points);
            if (!overlap(projectionMyAxis, projectionOtherAxis)) {
                return false;
            }
        }

        // Collision check using other polygons axes
        for (int i = 0; i < other.axes.length; i += 2) {
            projectPolygon(projectionMyAxis, other.axes[i], other.axes[i + 1], polygon.points);
            projectPolygon(projectionOtherAxis, other.axes[i], other.axes[i + 1], other.points);
            if (!overlap(projectionMyAxis, projectionOtherAxis)) {
                return false;
            }
        }

        return true;
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
        return !(proj1[1] < proj2[0] || proj2[1] < proj1[0]);
    }

    private static float dotProduct(float x1, float y1, float x2, float y2) {
        return (x1 * x2) + (y1 * y2);
    }

    private static float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}
