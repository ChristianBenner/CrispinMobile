package com.crispin.demos.Scenes.GameDemo2D;

import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Physics.Collision;
import com.crispin.crispinmobile.Physics.HitboxPolygon;
import com.crispin.crispinmobile.Physics.HitboxRectangle;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.demos.R;

public class Building {
    private static final float WALL_THICKNESS = 15f;
    private static final float DOOR_WIDTH = 150f;

    private Square[] walls;
    private HitboxRectangle[] wallHitbox;
    private Square floor;
    private Scale2D size;

    public Building(Vec2 position, Scale2D size) {
        this.size = size;

        walls = new Square[5];

        // left vertical wall
        walls[0] = new Square(R.drawable.crate_texture);
        walls[0].setScale(new Scale2D(WALL_THICKNESS, size.h));

        // right vertical wall
        walls[1] = new Square(R.drawable.crate_texture);
        walls[1].setScale(new Scale2D(WALL_THICKNESS, size.h));

        // bottom left horizontal wall
        walls[2] = new Square(R.drawable.crate_texture);
        walls[2].setScale(new Scale2D((size.w / 2f) - (DOOR_WIDTH / 2f), WALL_THICKNESS));

        // bottom right horizontal wall
        walls[3] = new Square(R.drawable.crate_texture);
        walls[3].setScale(new Scale2D((size.w / 2f) - (DOOR_WIDTH / 2f), WALL_THICKNESS));

        // top horizontal wall
        walls[4] = new Square(R.drawable.crate_texture);
        walls[4].setScale(new Scale2D(size.w, WALL_THICKNESS));

        wallHitbox = new HitboxRectangle[walls.length];
        for(int i = 0; i < walls.length; i++) {
            wallHitbox[i] = new HitboxRectangle();
        }

        floor = new Square(R.drawable.brick_tile);
        floor.getMaterial().setUvMultiplier(size.w / 64f, size.h / 64f);
        floor.setScale(size);

        setPosition(position);
    }

    public ModelMatrix getModelMatrix() {
        return floor.getModelMatrix();
    }

    public void setPosition(Vec2 position) {
        walls[0].setPosition(position);
        walls[1].setPosition(position.x + size.w - WALL_THICKNESS, position.y);
        walls[2].setPosition(position);
        walls[3].setPosition(position.x + (size.w / 2f) + (DOOR_WIDTH / 2f), position.y);
        walls[4].setPosition(position.x, position.y + size.h - WALL_THICKNESS);

        // Rotate all the walls around the center of the building
        for(int i = 0; i < walls.length; i++) {
            float rx = -(walls[i].getPosition().x - position.x);
            float ry = -(walls[i].getPosition().y - position.y);
            walls[i].setRotationAroundPoint(rx, ry, 0f, 45f, 0f, 0f, 1f);
        }
        floor.setRotation(45f, 0f, 0f, 1f);

        for(int i = 0; i < walls.length; i++) {
            wallHitbox[i].transform(walls[i].getModelMatrix());
        }
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

    public Vec2 isColliding(HitboxPolygon player) {
        // Sum the translation vectors to get the total translation away from all walls
        Vec2 finalMtv = null;
        for (int i = 0; i < walls.length; i++) {
            Vec2 mtv = Collision.isCollidingMTV(player, wallHitbox[i]);
            if(mtv != null) {
                if(finalMtv == null) {
                    finalMtv = mtv;
                } else {
                    finalMtv.x += mtv.x;
                    finalMtv.y += mtv.y;
                }
            }
        }

        return finalMtv;
    }

//    public Vec2 isColliding(HitboxPolygon player) {
//        boolean colliding = false;
//        Vec2 mtvOfAllWalls = null;
//
//        for (int i = 0; i < walls.length; i++) {
//            Vec2 mtv = Collision.isColliding(player, wallHitbox[i]);
//            if(mtv != null) {
//                if(mtvOfAllWalls == null) {
//                    mtvOfAllWalls = mtv;
//                } else {
//                    if(mtvOfAllWalls.getMagnitude() > mtv.getMagnitude()) {
//                        mtvOfAllWalls = mtv;
//                    }
//                }
//            }
//        }
//
//        return mtvOfAllWalls;
//    }

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
