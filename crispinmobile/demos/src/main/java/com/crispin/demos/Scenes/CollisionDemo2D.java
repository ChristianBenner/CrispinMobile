package com.crispin.demos.Scenes;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Physics.Collision;
import com.crispin.crispinmobile.Physics.HitboxCircle;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.Scene;

import java.util.Random;

public class CollisionDemo2D extends Scene {
    private static final int NUM_BALLS = 50;
    private static final float FIXED_CIRCLE_RADIUS = 300f;
    private static final float BALL_RADII = 25f;
    private static final float BALL_DRAG_COEFFICIENT = 0.0002f;


    private Camera2D camera;
    private Random random;
    private HitboxCircle fixedCircle;
    private boolean spawnOnPointerLocation;
    private Vec2 pointerLocation;

    class Ball {
        HitboxCircle hitbox;
        float velocityX;
        float velocityY;
        float mass;

        public Ball(float cx, float cy, float radius) {
            hitbox = new HitboxCircle(cx, cy, radius);
            this.mass = 1f;
        }
    }

    private Ball[] balls;

    public CollisionDemo2D() {
        Crispin.setBackgroundColour(Colour.BLACK);

        camera = new Camera2D();
        random = new Random();

        fixedCircle = new HitboxCircle(Crispin.getSurfaceWidth() / 2f,
                Crispin.getSurfaceHeight() / 4f, FIXED_CIRCLE_RADIUS);
        fixedCircle.setColour(new Colour(255, 202, 175));

        balls = new Ball[NUM_BALLS];
        for (int i = 0; i < NUM_BALLS; i++) {
            balls[i] = new Ball(0f, 0f, BALL_RADII);
            resetBall(i);
        }
    }

    // Calculate and apply the drag force to the balls
    private void applyDragForce(int i, float deltaTime) {
        Vec2 velocity = new Vec2(balls[i].velocityX, balls[i].velocityY);
        float speed = velocity.getMagnitude();
        Vec2 dragForce = Geometry.scaleVector(velocity, -BALL_DRAG_COEFFICIENT * speed);
        balls[i].velocityX += dragForce.x * deltaTime;
        balls[i].velocityY += dragForce.y * deltaTime;
    }

    private void resetBall(int i) {
        float radiusMin = BALL_RADII;
        float radiusMax = BALL_RADII * 2f;
        float radius = (radiusMin + (random.nextFloat() * (radiusMax - radiusMin)));
        float mass = (float)(4.0 * Math.PI * radius * radius); // mass is the area of a sphere
        balls[i].hitbox.setRadius(radius);
        balls[i].mass = mass;

        if (spawnOnPointerLocation) {
            balls[i].hitbox.setCenter(pointerLocation);
            balls[i].velocityX = 0f;
            balls[i].velocityY = 0f;
            balls[i].hitbox.setColour(new Colour(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            return;
        }

        float minX = fixedCircle.getCenter().x - FIXED_CIRCLE_RADIUS - BALL_RADII;
        float maxX = fixedCircle.getCenter().x + FIXED_CIRCLE_RADIUS + BALL_RADII;
        float minY = Crispin.getSurfaceHeight() + BALL_RADII;
        float maxY = Crispin.getSurfaceHeight() * 1.5f;
        balls[i].hitbox.setCenter(minX + (random.nextFloat() * (maxX - minX)),
                minY + (random.nextFloat() * (maxY - minY)));
        balls[i].velocityX = 0f;
        balls[i].velocityY = 0f;
        balls[i].hitbox.setColour(new Colour(random.nextFloat(), random.nextFloat(), random.nextFloat()));
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < NUM_BALLS; i++) {
            // Check if the ball has gone off the screen and if so, reset it
            if (balls[i].hitbox.getCenter().y < 0 - BALL_RADII ||
                    balls[i].hitbox.getCenter().x + BALL_RADII < 0 ||
                    balls[i].hitbox.getCenter().x - BALL_RADII > Crispin.getSurfaceWidth()) {
                resetBall(i);
            }

            applyDragForce(i, deltaTime);

            balls[i].velocityY -= 0.25f * deltaTime; // gravity
            balls[i].hitbox.translate(balls[i].velocityX, balls[i].velocityY);

            for (int n = 0; n < NUM_BALLS; n++) {
                if (n == i) {
                    continue;
                }

                handleCollision(balls[i], balls[n]);
            }

            // How far the ball needs to be moved away from fixed circle to no longer be colliding
            Vec2 collisionVec = Collision.isColliding(fixedCircle, balls[i].hitbox);
            if (collisionVec != null) {
                // Move the ball out of the fixed circle
                balls[i].hitbox.translate(collisionVec.x, collisionVec.y);

                // Reflect the velocity of the ball
                Vec2 normal = Geometry.normalize(collisionVec);
                float dotProduct = normal.getDotProduct(new Vec2(balls[i].velocityX, balls[i].velocityY));
                Vec2 reflectionVector = new Vec2(balls[i].velocityX, balls[i].velocityY);
                reflectionVector.subtract(Geometry.scaleVector(normal, 2f * dotProduct));
                balls[i].velocityX = reflectionVector.x * 0.8f;
                balls[i].velocityY = reflectionVector.y * 0.8f;
            }
        }
    }

    public static void handleCollision(Ball ball1, Ball ball2) {
        Vec2 collisionVector = Collision.isColliding(ball1.hitbox, ball2.hitbox);
        if (collisionVector != null) {
            // Move the balls out of each-other first
            ball1.hitbox.translate(-collisionVector.x / 2f, -collisionVector.y / 2f);
            ball2.hitbox.translate(collisionVector.x / 2f, collisionVector.y / 2f);

            // Calculate collision normal
            float collisionAngle = (float) Math.atan2(collisionVector.y, collisionVector.x);
            float angle1 = (float) Math.atan2(ball1.velocityY, ball1.velocityX);
            float angle2 = (float) Math.atan2(ball2.velocityY, ball2.velocityX);

            // Calculate the angle with respect to the collision angle
            float direction1 = angle1 - collisionAngle;
            float direction2 = angle2 - collisionAngle;

            float magnitude1 = (float) Math.sqrt((ball1.velocityX * ball1.velocityX) + (ball1.velocityY * ball1.velocityY));
            float magnitude2 = (float) Math.sqrt((ball2.velocityX * ball2.velocityX) + (ball2.velocityY * ball2.velocityY));

            // The velocities of each ball in the direction of the collision normal. We work this
            // out so that we can determine how much velocity each ball transfers to the other on
            // collision.
            float velocityX1 = (float) (magnitude1 * Math.cos(direction1));
            float velocityY1 = (float) (magnitude1 * Math.sin(direction1));
            float velocityX2 = (float) (magnitude2 * Math.cos(direction2));
            float velocityY2 = (float) (magnitude2 * Math.sin(direction2));

            // Calculate the transfer for velocity between each object taking into account the masses of each
            float finalVelocityX1 = ((ball1.mass - ball2.mass) * velocityX1 + (ball2.mass + ball2.mass) * velocityX2) / (ball1.mass + ball2.mass);
            float finalVelocityX2 = ((ball1.mass + ball1.mass) * velocityX1 + (ball2.mass - ball1.mass) * velocityX2) / (ball1.mass + ball2.mass);
            float finalVelocityY1 = velocityY1;
            float finalVelocityY2 = velocityY2;

            // Convert the final velocities back into velocity components
            ball1.velocityX = (float) (Math.cos(collisionAngle) * finalVelocityX1 + Math.cos(collisionAngle + Math.PI / 2) * finalVelocityY1);
            ball1.velocityY = (float) (Math.sin(collisionAngle) * finalVelocityX1 + Math.sin(collisionAngle + Math.PI / 2) * finalVelocityY1);
            ball2.velocityX = (float) (Math.cos(collisionAngle) * finalVelocityX2 + Math.cos(collisionAngle + Math.PI / 2) * finalVelocityY2);
            ball2.velocityY = (float) (Math.sin(collisionAngle) * finalVelocityX2 + Math.sin(collisionAngle + Math.PI / 2) * finalVelocityY2);
        }
    }

    @Override
    public void render() {
        fixedCircle.render(camera);

        for (int i = 0; i < NUM_BALLS; i++) {
            balls[i].hitbox.render(camera);
        }
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {
        if (touchType == TouchType.DOWN || touchType == TouchType.MOVE) {
            spawnOnPointerLocation = true;
            pointerLocation = pointer.getPosition();
        }

        if (touchType == TouchType.UP) {
            spawnOnPointerLocation = false;
        }
    }
}
