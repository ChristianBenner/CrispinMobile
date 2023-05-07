package com.crispin.demos.Scenes.GameDemo2D;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.DefaultMesh.SquareMesh;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.InstanceRenderer;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.UserInterface.Joystick;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.demos.R;

import java.util.Random;

public class GameDemo2D extends Scene {
    private static final float PLAYER_SIZE = 128f;

    private Camera2D camera;
    private Camera2D uiCamera;

    private Joystick movementJoystick;
    private Joystick aimJoystick;

    private Player player;

    private Square mapBase;

    private InstanceRenderer cactus;
    private InstanceRenderer rock;

    public GameDemo2D() {
//        GLES30.glDisable(GLES30.GL_DEPTH_TEST);
//        GLES30.glDisable(GLES30.GL_DEPTH);
////        GLES30.glDepthMask(false);

        cactus = new InstanceRenderer(new SquareMesh(true), false, generateRandomModelTransformations(100, 100f, 200f));
        cactus.setTexture(TextureCache.loadTexture(R.drawable.cactus));

        rock = new InstanceRenderer(new SquareMesh(true), false, generateRandomModelTransformations(300, 50f, 250f));
        rock.setTexture(TextureCache.loadTexture(R.drawable.rock));

        camera = new Camera2D();
        uiCamera = new Camera2D();

        movementJoystick = new Joystick(new Vec2(100f, 100f), 400f);

        aimJoystick = new Joystick(new Vec2(Crispin.getSurfaceWidth() - 500f, 100f), 400f);

        player = new Player(5000f, 5000f, PLAYER_SIZE);

        Material grassRepeatMaterial = new Material(R.drawable.dirt_center);
        grassRepeatMaterial.setUvMultiplier(100f, 100f);
        mapBase = new Square(grassRepeatMaterial);
        mapBase.setScale(10000f);
    }

    private ModelMatrix[] generateRandomModelTransformations(int count, float minScale, float maxScale) {
        Random r = new Random();
        ModelMatrix[] matrices = new ModelMatrix[count];
        for(int i = 0; i < count; i++) {
            float x = r.nextFloat() * 10000f;
            float y = r.nextFloat() * 10000f;
            float scale = minScale + (r.nextFloat() * (maxScale - minScale));
            ModelMatrix modelMatrix = new ModelMatrix();
            modelMatrix.translate(x, y);
            modelMatrix.scale(scale, scale);
            matrices[i] = modelMatrix;
        }

        return matrices;
    }

    @Override
    public void update(float deltaTime) {
        player.update(movementJoystick.getDirection(), aimJoystick.getDirection());
        camera.setPosition(getCenteredCameraPosition());
    }

    private Vec2 getCenteredCameraPosition() {
        return Geometry.minus(new Vec2(player.getPosition()), new Vec2(
                (Crispin.getSurfaceWidth() / 2f) - (player.getSize().x / 2f),
                (Crispin.getSurfaceHeight() / 2f) - (player.getSize().y / 2f)));
    }

    @Override
    public void render() {
        mapBase.render(camera);
        rock.render(camera);
        cactus.render(camera);
        player.render(camera);

        // UI
        movementJoystick.render(uiCamera);
        aimJoystick.render(uiCamera);
    }

    @Override
    public void touch(int type, Vec2 position) {}
}
