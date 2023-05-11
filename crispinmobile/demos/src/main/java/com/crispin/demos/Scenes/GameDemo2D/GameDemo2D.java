package com.crispin.demos.Scenes.GameDemo2D;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Physics.HitboxPolygon;
import com.crispin.crispinmobile.Physics.HitboxRectangle;
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
    private static final int NUM_CRATES = 100;

    private Camera2D camera;
    private Camera2D uiCamera;

    private Joystick movementJoystick;
    private Joystick aimJoystick;

    private Player player;
    private HitboxRectangle playerHitbox;

    private Square mapBase;

    private Building building;
    private HitboxRectangle buildingHitbox;
    private InstanceRenderer crates;
    private HitboxRectangle[] crateHitboxes;
    private HitboxPolygon hitboxPolygon;

    public GameDemo2D() {
        camera = new Camera2D();
        uiCamera = new Camera2D();

        movementJoystick = new Joystick(new Vec2(100f, 100f), 400f);

        aimJoystick = new Joystick(new Vec2(Crispin.getSurfaceWidth() - 500f, 100f), 400f);

        player = new Player(0f, 0f, PLAYER_SIZE);
        playerHitbox = new HitboxRectangle();

        Material grassRepeatMaterial = new Material(R.drawable.grass_tile);
        grassRepeatMaterial.setUvMultiplier(100f, 100f);
        mapBase = new Square(grassRepeatMaterial);
        mapBase.setScale(10000f);

        ModelMatrix[] crateModelMatrixes = generateRandomModelTransformations(NUM_CRATES, 100f, 200f);
        crates = new InstanceRenderer(new SquareMesh(true), false, crateModelMatrixes);
        crates.setTexture(TextureCache.loadTexture(R.drawable.crate_texture));
        crateHitboxes = new HitboxRectangle[NUM_CRATES];
        for(int i = 0; i < NUM_CRATES; i++) {
            crateHitboxes[i] = new HitboxRectangle();
            crateHitboxes[i].transform(crateModelMatrixes[i]);
        }

        building = new Building(new Vec2(5200f, 5200f), new Scale2D(750f, 400f));
        buildingHitbox = new HitboxRectangle();

        hitboxPolygon = new HitboxPolygon(new float[]{
                300f, 300f,
                400f, 400f,
                500f, 450f,
                600f, 400f,
                500f, 250f,
                400f, 250f,
        });

        ModelMatrix modelMatrix = new ModelMatrix();
        modelMatrix.scale(4f);
        hitboxPolygon.transform(modelMatrix);
    }

    @Override
    public void update(float deltaTime) {
        player.update(movementJoystick.getDirection(), aimJoystick.getDirection());

        buildingHitbox.transform(building.getModelMatrix());

        camera.setPosition(getCenteredCameraPosition());
    }

    @Override
    public void render() {
        mapBase.render(camera);
        crates.render(camera);
        building.render(camera);
        player.render(camera);

        // at the moment the model matrix for the player only gets updated on render
        // call. Should add a member variable called modelMatrixUpdate to Model class that gets set
        // to true on all operations that may require a model matrix update. Only update on render
        // when needed, and also update on retrieval of model matrix if needed.
        playerHitbox.transform(player.getModelMatrix());
        mapBase.setColour(Colour.WHITE);
        if(playerHitbox.isCollidingSAT(buildingHitbox)) {
            mapBase.setColour(Colour.RED);
        }

        for(int i = 0; i < NUM_CRATES; i++) {
            if(playerHitbox.isCollidingSAT(crateHitboxes[i])) {
                mapBase.setColour(Colour.RED);
            }
        }

        if(playerHitbox.isCollidingSAT(hitboxPolygon)) {
            mapBase.setColour(Colour.RED);
        }
        hitboxPolygon.render(camera);

        playerHitbox.render(camera);

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
            modelMatrix.rotate(r.nextFloat() * 360f, 0f, 0f, 1f);
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
