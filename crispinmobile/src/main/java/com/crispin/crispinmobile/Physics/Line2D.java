package com.crispin.crispinmobile.Physics;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Models.Line;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;

public class Line2D {
    Vec2 start;
    Vec2 end;
    Vec2 direction;
    Line line;

    public Line2D(Vec2 start, Vec2 end) {
        this.start = new Vec2(start);
        this.end = new Vec2(end);
        this.direction = Geometry.normalize(Geometry.minus(end, start));
        line = new Line();
        line.setPoints(start, end);
    }

    public Line2D(float sx, float sy, float ex, float ey) {
        this.start = new Vec2(sx, sy);
        this.end = new Vec2(ex, ey);
        this.direction = Geometry.normalize(Geometry.minus(end, start));
        line = new Line();
        line.setPoints(start, end);
    }

    public void render(Camera2D camera2D) {
        line.render(camera2D);
    }

    public void setColour(Colour colour) {
        line.setColour(colour);
    }

    @Override
    public String toString() {
        return "sx:" + start.x + ",sy:" + start.y + ",ex:" + end.x + ",ey:" + end.y;
    }
}