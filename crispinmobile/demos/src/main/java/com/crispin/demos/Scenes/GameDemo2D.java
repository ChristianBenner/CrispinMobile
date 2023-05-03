package com.crispin.demos.Scenes;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Joystick;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.demos.R;

public class GameDemo2D extends Scene {
    private static final float MAX_MOVEMENT_SPEED = 10.0f;

    private Camera2D camera;
    private Joystick joystick;
    private Square square;

    public GameDemo2D() {
        camera = new Camera2D();

        joystick = new Joystick(new Vec2(100f, 100f), 400f);

        square = new Square(R.drawable.crate_texture);
        square.setPosition(400f, 400f);
        square.setScale(64f);
    }

    @Override
    public void update(float deltaTime) {
        square.translate(Geometry.scaleVector(joystick.getDirection(), MAX_MOVEMENT_SPEED));
    }

    @Override
    public void render() {
        joystick.render(camera);
        square.render(camera);
    }

    @Override
    public void touch(int type, Vec2 position) {}
}
