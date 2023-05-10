package com.crispin.demos.Scenes.GameDemo2D;

import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;

public class Building {
    private static final float WALL_THICKNESS = 15f;

    private Square[] walls;
    private Square floor;
    private Scale2D size;

    public Building(Vec2 position, Scale2D size) {
        this.size = size;

        walls = new Square[4];

        // left vertical wall
        walls[0] = new Square(false);
        walls[0].setScale(new Scale2D(WALL_THICKNESS, size.h));
        walls[0].setColour(Colour.GREY);

        // right vertical wall
        walls[1] = new Square(false);
        walls[1].setScale(new Scale2D(WALL_THICKNESS, size.h));
        walls[1].setColour(Colour.GREY);

        // bottom horizontal wall
        walls[2] = new Square(false);
        walls[2].setScale(new Scale2D(size.w, WALL_THICKNESS));
        walls[2].setColour(Colour.GREY);

        // top horizontal wall
        walls[3] = new Square(false);
        walls[3].setScale(new Scale2D(size.w, WALL_THICKNESS));
        walls[3].setColour(Colour.GREY);

        floor = new Square(false);
        floor.setScale(size);
        floor.setColour(Colour.LIGHT_GREY);

        setPosition(position);
    }

    public ModelMatrix getModelMatrix() {
        return floor.getModelMatrix();
    }

    public void setPosition(Vec2 position) {
        walls[0].setPosition(position);
        walls[1].setPosition(position.x + size.w - WALL_THICKNESS, position.y);
        walls[2].setPosition(position);
        walls[3].setPosition(position.x, position.y + size.h - WALL_THICKNESS);
        floor.setPosition(position);
    }

    public Vec2 getPosition() {
        return new Vec2(floor.getPosition());
    }

    public Scale2D getSize() {
        return size;
    }

    public void render(Camera2D camera2D) {
        floor.render(camera2D);
        for (int i = 0; i < walls.length; i++) {
            walls[i].render(camera2D);
        }
    }

    public boolean isColliding(Player player) {
        for (int i = 0; i < walls.length; i++) {
            if(checkCollision(player, walls[i])) {
                return true;
            }
        }
        return false;
    }

    // Simple rectangle same axis collision check (no rotation support)
    public boolean checkCollision(Player player, Square wall) {
        Vec2 pPos = player.getPosition();
        Vec2 pSize = player.getSize();
        Vec2 wPos = wall.getPosition().toVec2();
        Vec2 wSize = new Vec2(wall.getScale());

        return ((wPos.x > pPos.x && wPos.x < pPos.x + pSize.x) ||
                (wPos.x + wSize.x > pPos.x && wPos.x + wSize.x < pPos.x + pSize.x)) &&
                ((wPos.y > pPos.y && wPos.y < pPos.y + pSize.y) ||
                        (wPos.y + wSize.y > pPos.y && wPos.y + wSize.y < pPos.y + pSize.y));
    }
}
