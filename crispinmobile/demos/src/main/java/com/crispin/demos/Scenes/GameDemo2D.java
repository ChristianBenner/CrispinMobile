package com.crispin.demos.Scenes;

import android.opengl.GLES30;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Models.AnimatedSquare;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Joystick;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.demos.R;

public class GameDemo2D extends Scene {
    private static final float MAX_MOVEMENT_SPEED = 10.0f;

    private Camera2D camera;
    private Camera2D uiCamera;

    private Joystick joystick;

    private AnimatedSquare playerMoving;
    private Square playerStatic;
    private boolean playerRunning;

    private Square mapBase;

    public GameDemo2D() {
        GLES30.glDisable(GLES30.GL_DEPTH_TEST);
        GLES30.glDisable(GLES30.GL_DEPTH);
        GLES30.glDepthMask(false);

        camera = new Camera2D();
        uiCamera = new Camera2D();

        joystick = new Joystick(new Vec2(100f, 100f), 400f);

        playerMoving = new AnimatedSquare(TextureCache.loadTexture(R.drawable.goblin_run), 16, 1000);
        playerMoving.setPosition(5000f, 5000f);
        playerMoving.setScale(256f, 256f);

        playerStatic = new Square(TextureCache.loadTexture(R.drawable.goblin_static));
        playerStatic.setPosition(5000f, 5000f);
        playerStatic.setScale(256f, 256f);

        Material grassRepeatMaterial = new Material(R.drawable.grass_tile);
        grassRepeatMaterial.setUvMultiplier(100f, 100f);
        mapBase = new Square(grassRepeatMaterial);
        mapBase.setScale(10000f);
    }

    @Override
    public void update(float deltaTime) {
        if(joystick.getDirection().x > 0f) {
            playerMoving.getMaterial().setUvMultiplier(1f, playerMoving.getMaterial().getUvMultiplier().y);
        } else if (joystick.getDirection().x < 0f) {
            playerMoving.getMaterial().setUvMultiplier(-1f, playerMoving.getMaterial().getUvMultiplier().y);
        }

        if(joystick.getDirection().getMagnitude() > 0f) {
            playerRunning = true;
            playerMoving.updateAnimation();
        } else {
            playerRunning = false;
        }

        playerMoving.translate(Geometry.scaleVector(joystick.getDirection(), MAX_MOVEMENT_SPEED));
        playerStatic.setPosition(playerMoving.getPosition());
        camera.setPosition(getCenteredCameraPosition());
    }

    private Vec2 getCenteredCameraPosition() {
        return Geometry.minus(new Vec2(playerMoving.getPosition()), new Vec2(
                (Crispin.getSurfaceWidth() / 2f) - (playerMoving.getScale().x / 2f),
                (Crispin.getSurfaceHeight() / 2f) - (playerMoving.getScale().y / 2f)));
    }

    @Override
    public void render() {
        mapBase.render(camera);

        if(playerRunning) {
            playerMoving.render(camera);
        } else {
            playerStatic.render(camera);
        }

        joystick.render(uiCamera);
    }

    @Override
    public void touch(int type, Vec2 position) {}
}
