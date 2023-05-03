package com.crispin.demos.Scenes;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Models.AnimatedSquare;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Joystick;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.demos.R;

public class GameDemo2D extends Scene {
    private Camera2D camera;
    private Joystick joystick;

    private AnimatedSquare animatedSquare;

    public GameDemo2D() {
        camera = new Camera2D();

        joystick = new Joystick(new Vec2(100f, 100f), 400f, R.drawable.joy_stick_outer, R.drawable.joy_stick_inner);

        animatedSquare = new AnimatedSquare(new Material(R.drawable.crate_texture), 1, 1000);
        animatedSquare.setPosition(400f, 400f);
        animatedSquare.setScale(64f);
    }

    @Override
    public void update(float deltaTime) {
        float x = animatedSquare.getPosition().x + (10.0f * joystick.getDirection().x);
        float y = animatedSquare.getPosition().y + (10.0f * joystick.getDirection().y);
        animatedSquare.setPosition(x, y);
    }

    @Override
    public void render() {
        animatedSquare.render(camera);
        joystick.render(camera);
    }

    @Override
    public void touch(int type, Vec2 position) {

    }
}
