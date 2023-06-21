package com.crispin.crispinmobile.Rendering.Utilities;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;

public class Util {
    private static Vec2[] calculateBezierCurveOffsetPoints(Vec2 current, Vec2 previous, Vec2 next, float width) {
        Vec2 line = Geometry.minus(next, previous);
        Vec2 perpendicular = new Vec2(line.y, -line.x);
        Vec2 normalised = Geometry.normalize(perpendicular);
        Vec2 scaled = Geometry.scaleVector(normalised, width / 2f);
        Vec2 out1 = Geometry.plus(current, scaled);
        Vec2 out2 = Geometry.minus(current, scaled);
        return new Vec2[]{
                out1,
                out2,
        };
    }

    public static float[] bezierCurveToMeshPositionComponents(Vec2[] curvePoints, float width) {
        float[] mesh = new float[curvePoints.length * 4];

        Vec2[] startOffsetPoints = calculateBezierCurveOffsetPoints(curvePoints[0], curvePoints[0], curvePoints[1], width);
        mesh[0] = startOffsetPoints[0].x;
        mesh[1] = startOffsetPoints[0].y;
        mesh[2] = startOffsetPoints[1].x;
        mesh[3] = startOffsetPoints[1].y;

        // Calculate the points for width
        for (int i = 1; i < curvePoints.length - 1; i++) {
            Vec2[] offsetPoints = calculateBezierCurveOffsetPoints(curvePoints[i], curvePoints[i - 1], curvePoints[i + 1], width);
            mesh[(i * 4) + 0] = offsetPoints[0].x;
            mesh[(i * 4) + 1] = offsetPoints[0].y;
            mesh[(i * 4) + 2] = offsetPoints[1].x;
            mesh[(i * 4) + 3] = offsetPoints[1].y;
        }

        int lastIndex = curvePoints.length - 1;
        Vec2[] endOffsetPoints = calculateBezierCurveOffsetPoints(curvePoints[lastIndex], curvePoints[lastIndex - 1], curvePoints[lastIndex], width);
        mesh[(lastIndex * 4) + 0] = endOffsetPoints[0].x;
        mesh[(lastIndex * 4) + 1] = endOffsetPoints[0].y;
        mesh[(lastIndex * 4) + 2] = endOffsetPoints[1].x;
        mesh[(lastIndex * 4) + 3] = endOffsetPoints[1].y;
        return mesh;
    }
}
