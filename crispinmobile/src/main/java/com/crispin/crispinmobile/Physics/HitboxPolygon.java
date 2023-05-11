package com.crispin.crispinmobile.Physics;

import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Models.Line;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;

public class HitboxPolygon {
    private float[] data;
    private float[] transformedPoints;
    private float[] axes;

    // A reference to the points that should be used (either data or transformedPoints)
    private float[] points;

    // These lines are only initialised when attempting to render for the first time. Rendering will
    // likely only occur in debugging scenarios to show hitboxes in development.
    private Line[] lines;

    // Assumes two components (xy)
    public HitboxPolygon(float[] data) {
        this.data = data;
        this.points = data;

        // Allocate now so it does not need to be allocate every collision calculation
        this.axes = new float[data.length];
        calculateAxes();
    }

    public void transform(ModelMatrix modelMatrix) {
        if(transformedPoints == null) {
            // Allocate now so it does not need to be allocate every transform call (expensive)
            this.transformedPoints = new float[data.length];
            this.points = transformedPoints;
        }

        float[] m = modelMatrix.getFloats();
        for (int i = 0; i < data.length; i += 2) {
            // Cheaper way to transform just 2 co-ordinates with the matrix
            transformedPoints[i] = m[0] * data[i] + m[4] * data[i + 1] + m[12];
            transformedPoints[i + 1] = m[1] * data[i] + m[5] * data[i + 1] + m[13];
        }

        calculateAxes();
    }

    private void calculateAxes() {
        // Calculate axes for this polygon
        for (int i = 0; i < points.length; i += 2) {
            int next = (i + 2) % points.length;
            float edgeX = points[next] - points[i];
            float edgeY = -(points[next + 1] - points[i + 1]);
            float length = (float) Math.sqrt(Math.abs((edgeY * edgeY) + (edgeX * edgeX)));
            axes[i] = edgeY / length;
            axes[i + 1] = edgeX / length;
        }
    }

    public void render(Camera2D camera2D) {
        if (lines == null) {
            // Initialise lines for debugging
            this.lines = new Line[points.length / 2];
            for (int i = 0; i < lines.length; i++) {
                lines[i] = new Line();
                lines[i].setColour(Colour.BLUE);
            }
        }

        for (int i = 0; i < points.length; i += 2) {
            int next = (i + 2) % points.length;
            lines[i / 2].setPoints(points[i], points[i + 1], points[next], points[next + 1]);
            lines[i / 2].render(camera2D);
        }
    }

    // Using SAT collision algorithm
    public boolean isCollidingSAT(HitboxPolygon other) {
        float[] projectionMyAxis = new float[2];
        float[] projectionOtherAxis = new float[2];

        // Collision check using this polygons axes
        for (int i = 0; i < axes.length; i += 2) {
            projectPolygon(projectionMyAxis, axes[i], axes[i + 1], points);
            projectPolygon(projectionOtherAxis, axes[i], axes[i + 1], other.points);
            if (!overlap(projectionMyAxis, projectionOtherAxis)) {
                return false;
            }
        }

        // Collision check using other polygons axes
        for (int i = 0; i < other.axes.length; i += 2) {
            projectPolygon(projectionMyAxis, other.axes[i], other.axes[i + 1], points);
            projectPolygon(projectionOtherAxis, other.axes[i], other.axes[i + 1], other.points);
            if (!overlap(projectionMyAxis, projectionOtherAxis)) {
                return false;
            }
        }

        return true;
    }

    private void projectPolygon(float[] projectionOut, float axisX, float axisY, float[] polygon) {
        float min = (axisX * polygon[0]) + (axisY * polygon[1]); // dot product
        float max = min;
        for (int i = 2; i < polygon.length; i += 2) {
            float dot = (axisX * polygon[i]) + (axisY * polygon[i + 1]); // dot product
            if (dot < min) {
                min = dot;
            } else if (dot > max) {
                max = dot;
            }
        }

        projectionOut[0] = min;
        projectionOut[1] = max;
    }

    private boolean overlap(float[] proj1, float[] proj2) {
        return !(proj1[1] < proj2[0] || proj2[1] < proj1[0]);
    }
}