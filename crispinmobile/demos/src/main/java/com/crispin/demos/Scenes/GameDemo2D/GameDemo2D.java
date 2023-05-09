package com.crispin.demos.Scenes.GameDemo2D;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Physics.Collision;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.DefaultMesh.SquareMesh;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.InstanceRenderer;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.UserInterface.Joystick;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.TouchType;
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
    private Building building;

    public GameDemo2D() {
        camera = new Camera2D();
        uiCamera = new Camera2D();

        movementJoystick = new Joystick(new Vec2(100f, 100f), 400f);

        aimJoystick = new Joystick(new Vec2(Crispin.getSurfaceWidth() - 500f, 100f), 400f);

        player = new Player(5000f, 5000f, PLAYER_SIZE);

        Material grassRepeatMaterial = new Material(R.drawable.grass_tile);
        grassRepeatMaterial.setUvMultiplier(100f, 100f);
        mapBase = new Square(grassRepeatMaterial);
        mapBase.setScale(10000f);

        cactus = new InstanceRenderer(new SquareMesh(true), false, generateRandomModelTransformations(100, 100f, 200f));
        cactus.setTexture(TextureCache.loadTexture(R.drawable.cactus));

        rock = new InstanceRenderer(new SquareMesh(true), false, generateRandomModelTransformations(300, 50f, 250f));
        rock.setTexture(TextureCache.loadTexture(R.drawable.rock));

        building = new Building(new Vec2(5200f, 5200f), new Scale2D(750f, 400f));
    }

    @Override
    public void update(float deltaTime) {
        player.update(movementJoystick.getDirection(), aimJoystick.getDirection());

//        if(building.isColliding(player)) {
//            Vec2 undoMovement = Geometry.scaleVector(Geometry.invert(movementJoystick.getDirection()), player.getMovementSpeed());
//            player.translate(undoMovement);
//        }
        camera.setPosition(getCenteredCameraPosition());
    }

    @Override
    public void render() {
        mapBase.render(camera);
        rock.render(camera);
        cactus.render(camera);
        building.render(camera);
        player.render(camera);


//        if(Collision.checkCollisionRotation(
//                new HitboxRectangle(player.getPosition(), new Scale2D(player.getSize().x, player.getSize().y)),
//                new HitboxRectangle(building.getPosition(), building.getSize()), camera)) {
//            mapBase.setColour(Colour.RED);
//        }

        mapBase.setColour(Colour.WHITE);
        Vec2[] triangle = new Vec2[8];
        triangle[0] = new Vec2(4000f, 4000f);
        triangle[1] = new Vec2(4250f, 4350f);
        triangle[2] = new Vec2(4500f, 4500f);
        triangle[3] = new Vec2(4750f, 4350f);
        triangle[4] = new Vec2(5000f, 4000f);
        triangle[5] = new Vec2(4750f, 3650f);
        triangle[6] = new Vec2(4500f, 3500f);
        triangle[7] = new Vec2(4250f, 3650f);

        Vec2[] playerPolygon = new Vec2[4];
        playerPolygon[0] = new Vec2(player.getPosition().x, player.getPosition().y);
        playerPolygon[1] = new Vec2(player.getPosition().x, player.getPosition().y + player.getSize().y);
        playerPolygon[2] = new Vec2(player.getPosition().x + player.getSize().x, player.getPosition().y + player.getSize().y);
        playerPolygon[3] = new Vec2(player.getPosition().x + player.getSize().x, player.getPosition().y);
        if(Collision.checkCollision(triangle, playerPolygon, camera)) {
            mapBase.setColour(Colour.RED);
        }

        // UI
        movementJoystick.render(uiCamera);
        aimJoystick.render(uiCamera);
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {}

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

    private Vec2 getCenteredCameraPosition() {
        return Geometry.minus(new Vec2(player.getPosition()), new Vec2(
                (Crispin.getSurfaceWidth() / 2f) - (player.getSize().x / 2f),
                (Crispin.getSurfaceHeight() / 2f) - (player.getSize().y / 2f)));
    }
}
