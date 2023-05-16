package com.crispin.demos.Scenes.GameDemo2D;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Physics.HitboxPolygon;
import com.crispin.crispinmobile.Rendering.Models.AnimatedSquare;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.demos.R;

import java.util.Random;

public class Zombie {
    private static final float MAX_MOVEMENT_SPEED = 7.0f;
    private static final float MIN_MOVEMENT_SPEED = 4.0f;

    private Square torsoSprite;
    private AnimatedSquare legsSprite;
    private float size;
    private float movementSpeed;
    private HitboxPolygon hitbox;
    private boolean alive;
    private Random random;

    public Zombie(float x, float y, float size) {
        this.size = size;
        this.alive = true;

        torsoSprite = new Square(TextureCache.loadTexture(R.drawable.zombie_top_down));
        torsoSprite.setPosition(x, y);
        torsoSprite.setScale(size, size);

        legsSprite = new AnimatedSquare(R.drawable.zombie_top_down_legs, 32, 1000);
        legsSprite.setPosition(x, y);
        legsSprite.setScale(size, size);

        random = new Random();
        movementSpeed = MIN_MOVEMENT_SPEED + (random.nextFloat() * (MAX_MOVEMENT_SPEED - MIN_MOVEMENT_SPEED));

        hitbox = new HitboxPolygon(new float[]{
                0.4f, 0.2f, // Bottom left
                0.4f, 0.8f, // Top left
                0.6f, 0.8f, // Top right
                1.0f, 0.2f, // Bottom Right
        });
    }

    public ModelMatrix getModelMatrix() {
        return torsoSprite.getModelMatrix();
    }

    public HitboxPolygon getHitbox() {
        return hitbox;
    }

    public void kill() {
        alive = false;
        torsoSprite.setTexture(TextureCache.loadTexture(R.drawable.zombie_down));
    }

    public boolean isAlive() {
        return alive;
    }

    public void update(Vec2 movement) {
        Vec2 moveCalc = new Vec2(movement);

        double legAngleRads = Math.atan2(moveCalc.y, moveCalc.x);
        double legAngleDegrees = legAngleRads * (180 / Math.PI);

        legsSprite.setRotationAroundPoint(size / 2f, size / 2f, 0f, (float)legAngleDegrees, 0.0f, 0.0f, 1.0f);
        if(movement.getMagnitude() > 0f) {
            legsSprite.updateAnimation();
        }

        double torsoAngleRads = Math.atan2(movement.y, movement.x);
        double torsoAngleDegrees = torsoAngleRads * (180 / Math.PI);
        torsoSprite.setRotationAroundPoint(size / 2f, size / 2f, 0f, (float)torsoAngleDegrees, 0.0f, 0.0f, 1.0f);

        Vec2 translate = Geometry.scaleVector(movement, movementSpeed);
        legsSprite.translate(translate);
        torsoSprite.translate(translate);
        hitbox.transform(getModelMatrix());
    }

    public void render(Camera2D camera) {
        if(!alive) {
            legsSprite.render(camera);
        }

        torsoSprite.render(camera);
    }

    public Vec2 getPosition() {
        return new Vec2(torsoSprite.getPosition());
    }

    public void setPosition(Vec2 position) {
        legsSprite.setPosition(position);
        torsoSprite.setPosition(position);
        hitbox.transform(getModelMatrix());
    }

    public void translate(Vec2 translation) {
        translate(translation.x, translation.y);
    }

    public void translate(float x, float y) {
        legsSprite.translate(x, y);
        torsoSprite.translate(x, y);
        hitbox.transform(getModelMatrix());
    }

    public Vec2 getSize() {
        return new Vec2(size, size);
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }
}