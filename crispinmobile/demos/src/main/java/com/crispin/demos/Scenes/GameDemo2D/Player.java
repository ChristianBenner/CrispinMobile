package com.crispin.demos.Scenes.GameDemo2D;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Models.AnimatedSquare;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.demos.R;

public class Player {
    private static final float MAX_MOVEMENT_SPEED = 10.0f;

    private static final int ANIMATION_RUN = 0;
    private static final int ANIMATION_SIDESTEP_UP = 1;
    private static final int ANIMATION_SIDESTEP_DOWN = 2;
    private Square torsoSprite;
    private AnimatedSquare legsSprite;
    private AnimatedSquare legsSidestepUpSprite;
    private AnimatedSquare legsSidestepDownSprite;
    private float size;
    private float movementSpeed;

    private int animation;

    public Player(float x, float y, float size) {
        this.size = size;
        this.animation = ANIMATION_RUN;

        torsoSprite = new Square(TextureCache.loadTexture(R.drawable.player_top_down));
        torsoSprite.setPosition(x, y);
        torsoSprite.setScale(size, size);

        legsSprite = new AnimatedSquare(R.drawable.player_top_down_legs, 32, 1000);
        legsSprite.setPosition(x, y);
        legsSprite.setScale(size, size);

        legsSidestepUpSprite = new AnimatedSquare(R.drawable.player_top_down_legs_sidestep_up, 32, 500);
        legsSidestepUpSprite.setPosition(x, y);
        legsSidestepUpSprite.setScale(size, size);

        legsSidestepDownSprite = new AnimatedSquare(R.drawable.player_top_down_legs_sidestep_down, 32, 500);
        legsSidestepDownSprite.setPosition(x, y);
        legsSidestepDownSprite.setScale(size, size);
    }

    public ModelMatrix getModelMatrix() {
        return torsoSprite.getModelMatrix();
    }

    public void update(Vec2 movement, Vec2 aim) {
        // In any further math we don't want to alter the actual joystick vector, so use new vectors
        Vec2 moveCalc = new Vec2(movement);
        Vec2 aimCalc = new Vec2(aim);

        if(moveCalc.getMagnitude() == 0.0f) {
            // Use right direction as movement vector
            moveCalc.x = 1.0f;
            moveCalc.y = 0.0f;
        }

        if(aimCalc.getMagnitude() == 0.0f) {
            // Not aiming, so use the movement direction
            aimCalc.x = moveCalc.x;
            aimCalc.y = moveCalc.y;
        }

        float dotProduct = aimCalc.getDotProduct(moveCalc);
        float cosTheta = dotProduct / (aimCalc.getMagnitude() * moveCalc.getMagnitude());
        double angleMoveAimRads = Math.acos(cosTheta);
        double angleMoveAimDegrees = angleMoveAimRads * (180.0 / Math.PI);

        // While the angle is unsigned, calculate the movement speed. If the player is facing
        // forward enough, full movement speed is applied. Otherwise its decreased. Forward is
        // considered to be within 60 degrees either way of the aim direction.
        movementSpeed = MAX_MOVEMENT_SPEED;
        if(angleMoveAimDegrees > 60.0) {
            movementSpeed = MAX_MOVEMENT_SPEED * 0.5f;

            // Slow down the run animation as the player is backing up and can only walk
            legsSprite.setAnimationDurationMs(2000);
        } else {
            legsSprite.setAnimationDurationMs(1000);
        }

        // The cross product Z component sign can help us determine the sign of the angle
        if(Geometry.crossProduct(new Vec3(aimCalc, 0f), new Vec3(moveCalc, 0f)).z < 0f) {
            angleMoveAimDegrees = -angleMoveAimDegrees;
        }

        double legAngleRads = Math.atan2(moveCalc.y, moveCalc.x);
        double legAngleDegrees = legAngleRads * (180 / Math.PI);

        if(angleMoveAimDegrees > 60.0 && angleMoveAimDegrees < 120.0) {
            animation = ANIMATION_SIDESTEP_UP;
            legsSidestepUpSprite.setRotationAroundPoint(size / 2f, size / 2f, 0f, (float)legAngleDegrees, 0.0f, 0.0f, 1.0f);
            if(movement.getMagnitude() > 0f) {
                legsSidestepUpSprite.updateAnimation();
            }
        } else if(angleMoveAimDegrees > -120.0 && angleMoveAimDegrees < -60.0) {
            animation = ANIMATION_SIDESTEP_DOWN;
            legsSidestepDownSprite.setRotationAroundPoint(size / 2f, size / 2f, 0f, (float)legAngleDegrees, 0.0f, 0.0f, 1.0f);
            if(movement.getMagnitude() > 0f) {
                legsSidestepDownSprite.updateAnimation();
            }
        } else {
            animation = ANIMATION_RUN;
            legsSprite.setRotationAroundPoint(size / 2f, size / 2f, 0f, (float)legAngleDegrees, 0.0f, 0.0f, 1.0f);
            if(movement.getMagnitude() > 0f) {
                legsSprite.updateAnimation();
            }
        }

        double torsoAngleRads = Math.atan2(aimCalc.y, aimCalc.x);
        double torsoAngleDegrees = torsoAngleRads * (180 / Math.PI);
        torsoSprite.setRotationAroundPoint(size / 2f, size / 2f, 0f, (float)torsoAngleDegrees, 0.0f, 0.0f, 1.0f);

        Vec2 translate = Geometry.scaleVector(movement, movementSpeed);
        legsSprite.translate(translate);
        legsSidestepUpSprite.translate(translate);
        legsSidestepDownSprite.translate(translate);
        torsoSprite.translate(translate);
    }

    public void render(Camera2D camera) {
        switch (animation) {
            case ANIMATION_RUN:
                legsSprite.render(camera);
                break;
            case ANIMATION_SIDESTEP_UP:
                legsSidestepUpSprite.render(camera);
                break;
            case ANIMATION_SIDESTEP_DOWN:
                legsSidestepDownSprite.render(camera);
                break;
        }

        torsoSprite.render(camera);
    }

    public Vec2 getPosition() {
        return new Vec2(torsoSprite.getPosition());
    }

    public void setPosition(Vec2 position) {
        legsSprite.setPosition(position);
        legsSidestepUpSprite.setPosition(position);
        legsSidestepDownSprite.setPosition(position);
        torsoSprite.setPosition(position);
    }

    public void translate(Vec2 translation) {
        legsSprite.translate(translation);
        legsSidestepUpSprite.translate(translation);
        legsSidestepDownSprite.translate(translation);
        torsoSprite.translate(translation);
    }

    public Vec2 getSize() {
        return new Vec2(size, size);
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }
}