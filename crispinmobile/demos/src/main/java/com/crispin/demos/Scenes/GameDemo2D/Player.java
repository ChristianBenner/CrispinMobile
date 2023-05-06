package com.crispin.demos.Scenes.GameDemo2D;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Texture;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.demos.R;

public class Player {
    private static final float MAX_MOVEMENT_SPEED = 10.0f;
    private static final long FRAME_DURATION_MS = 250;
    private static final int NUM_BACK_FRAMES = 2;
    private static final int NUM_FRONT_FRAMES = 2;
    private static final int NUM_RIGHT_FRAMES = 2;
    private static final int NUM_LEFT_FRAMES = 2;

    private Texture[] textureBack;
    private Texture[] textureFront;
    private Texture[] textureLeft;
    private Texture[] textureRight;

    private Square sprite;

    private Geometry.Direction2D direction;

    private int frame;
    private long lastFrameStartMs;

    public Player(float x, float y, float size) {
        this.direction = Geometry.Direction2D.DOWN;
        this.frame = 0;
        this.lastFrameStartMs = System.currentTimeMillis();

        textureBack = new Texture[NUM_BACK_FRAMES];
        textureBack[0] = new Texture(R.drawable.player_back_1 );
        textureBack[1] = new Texture(R.drawable.player_back_2);

        textureFront = new Texture[NUM_FRONT_FRAMES];
        textureFront[0] = new Texture(R.drawable.player_front_1);
        textureFront[1] = new Texture(R.drawable.player_front_2);

        textureRight = new Texture[NUM_RIGHT_FRAMES];
        textureRight[0] = new Texture(R.drawable.player_right_1);
        textureRight[1] = new Texture(R.drawable.player_right_2);

        textureLeft = new Texture[NUM_LEFT_FRAMES];
        textureLeft[0] = new Texture(R.drawable.player_left_1);
        textureLeft[1] = new Texture(R.drawable.player_left_2);

        sprite = new Square(true);
        sprite.setPosition(x, y);
        sprite.setScale(size, size);
        sprite.setTexture(textureFront[0]);
    }

    public void setDirection(Geometry.Direction2D direction) {
        if (this.direction != direction) {
            this.direction = direction;
            lastFrameStartMs = 0;
            frame = 0;
            updateTexture();
        }
    }

    private void updateTexture() {
        if (System.currentTimeMillis() - lastFrameStartMs < FRAME_DURATION_MS) {
            return;
        }

        switch (direction) {
            case UP:
                if(frame >= NUM_BACK_FRAMES) {
                    frame = 0;
                }
                sprite.setTexture(textureBack[frame]);
                break;
            case DOWN:
            case NONE:
                if(frame >= NUM_FRONT_FRAMES) {
                    frame = 0;
                }
                sprite.setTexture(textureFront[frame]);
                break;
            case RIGHT:
                if(frame >= NUM_RIGHT_FRAMES) {
                    frame = 0;
                }
                sprite.setTexture(textureRight[frame]);
                break;
            case LEFT:
                if(frame >= NUM_LEFT_FRAMES) {
                    frame = 0;
                }
                sprite.setTexture(textureLeft[frame]);
                break;
        }

        lastFrameStartMs = System.currentTimeMillis();
        frame++;
    }

    public void update(Vec2 movement) {
        setDirection(Geometry.getDirection(movement));
        sprite.translate(Geometry.scaleVector(movement, MAX_MOVEMENT_SPEED));
        if(movement.getMagnitude() > 0f) {
            updateTexture();
        }
    }

    public void render(Camera2D camera) {
        sprite.render(camera);
    }

    public Vec2 getPosition() {
        return new Vec2(sprite.getPosition());
    }

    public Vec2 getSize() {
        return new Vec2(sprite.getScale().x, sprite.getScale().y);
    }
}