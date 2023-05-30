package com.crispin.demos;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Scale3D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.Scene;

public class Camera2DTest extends Scene {
    private Camera2D uiCamera;
    private Camera2D gameplayCamera;
    private Button backButton;
    private Square square;
    private Square square2;
    private Square square3;

    public Camera2DTest() {
        Crispin.setBackgroundColour(Colour.BLACK);
        uiCamera = new Camera2D();

        // Desired width of the view in world-space co-ordinates. This means that the amount of
        // world visible on every device should be the same despite the resolution
        double desiredWidth = 10.0;
        double viewRatio = (double)Crispin.getSurfaceWidth() / (double)Crispin.getSurfaceHeight();
        // The resulting height in world-space co-ordinates adjusted with the view ratio so that it
        // does not appear stretched
        double resultingHeight = desiredWidth / viewRatio;

        gameplayCamera = new Camera2D(0f, 0f, (float)desiredWidth, (float)resultingHeight);
        backButton = Util.createBackButton(DemoMasterScene::new);

        square = new Square();
        square.setScale(3f);
        square.setColour(Colour.RED);

        square2 = new Square();
        square2.setPosition(square.getPosition().x - 3f, square.getPosition().y - 3f);
        square2.setScale(3f);
        square2.setColour(Colour.GREEN);

        square3 = new Square();
        square3.setPosition(square.getPosition().x + 3f, square.getPosition().y + 3f);
        square3.setScale(3f);
        square3.setColour(Colour.BLUE);
    }

    @Override
    public void update(float deltaTime) {
        gameplayCamera.setPosition(getCenteredCameraPosition(square.getPosition(), square.getScale()));
    }

    @Override
    public void render() {
        square.render(gameplayCamera);
        square2.render(gameplayCamera);
        square3.render(gameplayCamera);

        backButton.draw(uiCamera);
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {

    }

    private Vec2 getCenteredCameraPosition(Vec3 pos, Scale3D scale) {
        return Geometry.minus(new Vec2(pos), new Vec2(
                (gameplayCamera.getRight() / 2f) - (scale.w / 2f),
                (gameplayCamera.getTop() / 2f) - (scale.h / 2f)));
    }
}
