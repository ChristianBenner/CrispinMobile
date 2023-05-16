package com.crispin.crispinmobile.Physics;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Models.Line;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;

public class HitboxPolygon {
    private float[] data;
    private float[] transformedPoints;

    // These lines are only initialised when attempting to render for the first time. Rendering will
    // likely only occur in debugging scenarios to show hitboxes in development.
    private Line[] lines;

    // A reference to the points that should be used (either data or transformedPoints)
    float[] points;
    float[] axes;

    float centerX;
    float centerY;
    int numPoints;

    // Assumes two components (xy)
    public HitboxPolygon(float[] data) {
        this.data = data;
        this.points = data;
        this.numPoints = data.length / 2;

        // Allocate now so it does not need to be allocate every collision calculation
        this.axes = new float[data.length];
        calculateAxes();
    }

    public Vec2 isCollidingMTV(HitboxPolygon other) {
        return Collision.isCollidingMTV(this, other);
    }

    public boolean isCollidingMTV(HitboxCircle circle) {
        return Collision.isCollidingMTV(this, circle);
    }

    public void transform(ModelMatrix modelMatrix) {
        if (transformedPoints == null) {
            // Allocate now so it does not need to be allocate every transform call (expensive)
            this.transformedPoints = new float[data.length];
            this.points = transformedPoints;
        }

        centerX = 0f;
        centerY = 0f;

        float[] m = modelMatrix.getFloats();
        for (int i = 0; i < data.length; i += 2) {
            // Cheaper way to transform just 2 co-ordinates with the matrix
            transformedPoints[i] = m[0] * data[i] + m[4] * data[i + 1] + m[12];
            transformedPoints[i + 1] = m[1] * data[i] + m[5] * data[i + 1] + m[13];

            // Sum the vertex co-ordinates for center calculation
            centerX += transformedPoints[i];
            centerY += transformedPoints[i + 1];
        }

        // Divide by the number of vertices to get the average
        centerX /= numPoints;
        centerY /= numPoints;

        calculateAxes();
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

    private void calculateAxes() {
        // Calculate axes for this polygon
        for (int i = 0; i < points.length; i += 2) {
            int next = (i + 2) % points.length;
            float edgeX = points[next] - points[i];
            float edgeY = -(points[next + 1] - points[i + 1]);

            // Normalise the axis
            float length = (float) Math.sqrt(Math.abs((edgeY * edgeY) + (edgeX * edgeX)));
            axes[i] = edgeY / length;
            axes[i + 1] = edgeX / length;
        }
    }
}