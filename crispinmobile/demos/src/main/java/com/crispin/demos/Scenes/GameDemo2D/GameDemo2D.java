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
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Models.ModelProperties;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders.InstanceShadowShader2D;
import com.crispin.crispinmobile.Rendering.Shaders.TwoDimensional.LightingTextureShader2D;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.InstanceRenderer;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.Rendering.Utilities.ShadowMap;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Joystick;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.Text;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.Audio;
import com.crispin.crispinmobile.Utilities.FontCache;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.demos.R;
import com.crispin.demos.Scenes.DemoMasterScene;
import com.crispin.demos.Util;

import java.util.ArrayList;
import java.util.Random;

public class GameDemo2D extends Scene {
    // Constants
    private static final float PLAYER_SIZE = 128f;
    // World space co-ordinates
    private static final Vec2 PLAYER_SPAWN_POINT = new Vec2(5000f, 5000f);
    private static final int NUM_CRATES = 100;
    private static final float TEXT_PADDING = 20f;
    private static int WEAPON_FIRE_RATE_HZ = 8;
    private static int WEAPON_FIRE_MIN_WAIT_MS = 1000 / WEAPON_FIRE_RATE_HZ;
    // Relative to the vertex positions defined for the player mesh (square 0-1 in both x and y)
    private static final Vec2 BULLET_SPAWN_POINT = new Vec2(31f/32f, 9f/32f);

    // UI
    private Camera2D camera;
    private Camera2D uiCamera;
    private Button backButton;
    private Text ammo;
    private Joystick movementJoystick;
    private Joystick aimJoystick;

    // Shaders
    private LightingTextureShader2D lightingTextureShader;
    private InstanceShadowShader2D shadowShader2D;

    // Entities
    private Player player;
    private Building building;
    private ArrayList<Bullet> bullets;
    private ArrayList<Zombie> zombies;

    // Rendering
    private Square mapBase;
    private InstanceRenderer crates;
    private InstanceRenderer crateShadows;
    private ShadowMap shadowMap;

    // Lights
    private LightGroup lightGroup;
    private PointLight light;

    // Properties
    private ModelProperties[] crateModelProperties;

    // Hitboxes
    private HitboxRectangle buildingHitbox;
    private HitboxRectangle[] crateHitboxes;

    // Other
    private Random random;

    private long lastShotTimeMs = System.currentTimeMillis();

    public GameDemo2D() {
        Audio.getInstance().initMusicChannel();
        Audio.getInstance().initSoundChannel(8);
        Audio.getInstance().playMusic(R.raw.demo_music);

        camera = new Camera2D();
        uiCamera = new Camera2D();
        random = new Random();
        lightingTextureShader = new LightingTextureShader2D();

        System.out.println(Crispin.getSurfaceWidth() + ", " + Crispin.getSurfaceHeight());
        backButton = Util.createBackButton(DemoMasterScene::new, 10f, Crispin.getSurfaceHeight() - 10f - Util.BACK_BUTTON_SIZE);

        movementJoystick = new Joystick(new Vec2(100f, 100f), 400f);

        aimJoystick = new Joystick(new Vec2(Crispin.getSurfaceWidth() - 500f, 100f), 400f);

        player = new Player(PLAYER_SPAWN_POINT.x, PLAYER_SPAWN_POINT.y, PLAYER_SIZE);

        lightGroup = new LightGroup();
        light = new PointLight();
        lightGroup.add(light);
        light.setConstantAttenuation(800f);
        light.setLinearAttenuation(1f);
        light.setQuadraticAttenuation(10f);
        light.setAmbientStrength(0.0f);
        light.setColour(Colour.GREEN);

        Material grassRepeatMaterial = new Material(R.drawable.dirt_tile);
        grassRepeatMaterial.setUvMultiplier(100f, 100f);
        mapBase = new Square(grassRepeatMaterial);
        mapBase.setScale(10000f);
        mapBase.setShader(lightingTextureShader);

        crateModelProperties = generateRandomModelProperties(NUM_CRATES, 100f, 200f);
        crates = new InstanceRenderer(new SquareMesh(true), false, crateModelProperties);
        crates.setTexture(TextureCache.loadTexture(R.drawable.crate_texture));

        shadowShader2D = new InstanceShadowShader2D();
        crateShadows = new InstanceRenderer(Square.getShadowMesh(), false, crateModelProperties);
        crateShadows.setShader(shadowShader2D);

        crateHitboxes = new HitboxRectangle[NUM_CRATES];

        for(int i = 0; i < NUM_CRATES; i++) {
            crateHitboxes[i] = new HitboxRectangle();
            crateHitboxes[i].transform(crateModelProperties[i].getModelMatrix());
        }

        building = new Building(new Vec2(5200f, 5200f), new Scale2D(750f, 400f));
        building.setShader(lightingTextureShader);
        buildingHitbox = new HitboxRectangle();
        buildingHitbox.transform(building.getModelMatrix());

        bullets = new ArrayList<>();

        zombies = new ArrayList<>();
        spawnZombies(100);

        ammo = new Text(FontCache.getFont(R.raw.aileron_bold, 56), String.format("Ammo %d/%d", player.getAmmo(), player.getMaxAmmo()));
        ammo.setPosition(Crispin.getSurfaceWidth() - ammo.getWidth() - TEXT_PADDING, Crispin.getSurfaceHeight() - TEXT_PADDING - ammo.getHeight());
        ammo.setColour(Colour.WHITE);

        shadowMap = new ShadowMap();
    }

    private void spawnZombies(int count) {
        for(int i = 0; i < count; i++) {
            float x = random.nextBoolean() ? random.nextFloat() * 4000f : 6000f + random.nextFloat() * 4000f;
            float y = random.nextBoolean() ? random.nextFloat() * 4000f : 6000f + random.nextFloat() * 4000f;
            Zombie z = new Zombie(x, y, PLAYER_SIZE);
            z.setShader(lightingTextureShader);
            zombies.add(z);
        }
    }

    @Override
    public void update(float deltaTime) {
        player.update(movementJoystick.getDirection(), aimJoystick.getDirection());

        float cx = player.getPosition().x + (player.getSize().x / 2f);
        float cy = player.getPosition().y + (player.getSize().y / 2f);
        light.setPosition(cx, cy);

        ammo.setText(String.format("Ammo %d/%d", player.getAmmo(), player.getMaxAmmo()));
        ammo.setPosition(Crispin.getSurfaceWidth() - ammo.getWidth() - TEXT_PADDING, Crispin.getSurfaceHeight() - TEXT_PADDING - ammo.getHeight());

        HitboxPolygon playerHitbox = player.getHitbox();

        // Update all zombies
        for(int n = 0; n < zombies.size(); n++) {
            Zombie z = zombies.get(n);
            HitboxPolygon zombieHitbox = z.getHitbox();
            if(!z.isAlive()) {
                continue;
            }

            z.update(Geometry.normalize(Geometry.minus(player.getPosition(), z.getPosition())));

            Vec2 playerZombieMtv = zombieHitbox.isCollidingMTV(playerHitbox);
            if(playerZombieMtv != null) {
                z.translate(playerZombieMtv.x, playerZombieMtv.y);
            }

            for(int i = 0; i < zombies.size(); i++) {
                if(i == n) {
                    continue;
                }
                Zombie other = zombies.get(i);
                if(!other.isAlive()) {
                    continue;
                }

                Vec2 mtv = zombieHitbox.isCollidingMTV(other.getHitbox());
                if (mtv != null) {
                    mtv.scale(0.5f);
                    z.translate(mtv.x, mtv.y);
                    zombieHitbox.transform(z.getModelMatrix());
                    other.translate(-mtv.x, -mtv.y);
                    other.getHitbox().transform(other.getModelMatrix());
                }
            }

            for(int i = 0; i < NUM_CRATES; i++) {
                Vec2 mtv = zombieHitbox.isCollidingMTV(crateHitboxes[i]);
                if (mtv != null) {
                    mtv.scale(0.5f);
                    z.translate(mtv.x, mtv.y);
                    zombieHitbox.transform(z.getModelMatrix());
                    crateModelProperties[i].translate(-mtv.x, -mtv.y);
                    crateHitboxes[i].transform(crateModelProperties[i].getModelMatrix());
                    crates.uploadModelMatrix(crateModelProperties[i].getModelMatrix(), i);
                    crateShadows.uploadModelMatrix(crateModelProperties[i].getModelMatrix(), i);
                }
            }

            // Zombie building collision
            Vec2 zombieBuildingMtv = building.isColliding(zombieHitbox);
            if(zombieBuildingMtv != null) {
                // A collision has occurred, move the zombie away by the MTV
                z.translate(zombieBuildingMtv.x, zombieBuildingMtv.y);
                zombieHitbox.transform(z.getModelMatrix());
            }
        }

        // Player collisions
        Vec2 playerBuildingMtv = building.isColliding(playerHitbox);
        if(playerBuildingMtv != null) {
            // A collision has occurred, move the player away by the MTV
            player.translate(playerBuildingMtv.x, playerBuildingMtv.y);
            playerHitbox.transform(player.getModelMatrix());
        }

        for(int i = 0; i < NUM_CRATES; i++) {
            Vec2 mtv = playerHitbox.isCollidingMTV(crateHitboxes[i]);
            if(mtv != null) {
//                player.translate(mtv.x, mtv.y);
//                playerHitbox.transform(player.getModelMatrix());

                crateModelProperties[i].translate(-mtv.x, -mtv.y);
                crateHitboxes[i].transform(crateModelProperties[i].getModelMatrix());
                crates.uploadModelMatrix(crateModelProperties[i].getModelMatrix(), i);
                crateShadows.uploadModelMatrix(crateModelProperties[i].getModelMatrix(), i);
            }

            for(int n = 0; n < NUM_CRATES; n++) {
                if(n == i) {
                    continue;
                }
                Vec2 crateMtv = crateHitboxes[i].isCollidingMTV(crateHitboxes[n]);
                if(crateMtv != null) {
                    crateMtv.scale(0.5f);
                    crateModelProperties[i].translate(crateMtv.x, crateMtv.y);
                    crateModelProperties[n].translate(-crateMtv.x, -crateMtv.y);
                    crateHitboxes[i].transform(crateModelProperties[i].getModelMatrix());
                    crateHitboxes[n].transform(crateModelProperties[n].getModelMatrix());
                    crates.uploadModelMatrix(crateModelProperties[i].getModelMatrix(), i);
                    crates.uploadModelMatrix(crateModelProperties[n].getModelMatrix(), n);
                    crateShadows.uploadModelMatrix(crateModelProperties[i].getModelMatrix(), i);
                    crateShadows.uploadModelMatrix(crateModelProperties[n].getModelMatrix(), n);
                }
            }
        }

        // Update bullet pos, check for collisions and check for end of life
        for(int i = bullets.size() - 1; i >= 0; --i) {
            Bullet bullet = bullets.get(i);
            bullet.sprite.translate(bullet.velocity);
            bullet.light.setPosition(bullet.sprite.getPosition());

            // Bullet should only survive for a second
            if(System.currentTimeMillis() - bullet.spawnTime > 1000) {
                bullets.remove(i);
                lightGroup.remove(bullet.light);
                continue;
            }

            boolean consumeBullet = false;

            // Collide with boxes and building
            // Update hitbox
            bullet.hitboxRectangle.transform(bullet.sprite.getModelMatrix());
            for(int n = 0; n < NUM_CRATES && !consumeBullet; n++) {
                if(bullet.hitboxRectangle.isCollidingMTV(crateHitboxes[n]) != null) {
                    consumeBullet = true;
                }
            }
            if(consumeBullet) {
                bullets.remove(i);
                lightGroup.remove(bullet.light);
                continue;
            }

            // Zombie collision
            for(int n = 0; n < zombies.size() && !consumeBullet; n++) {
                Zombie zombie = zombies.get(n);
                if(zombie.isAlive() && bullet.hitboxRectangle.isCollidingMTV(zombie.getHitbox()) != null) {
                    zombie.kill();
                    consumeBullet = true;
                }
            }
            if(consumeBullet) {
                bullets.remove(i);
                lightGroup.remove(bullet.light);
                continue;
            }

            if(building.isColliding(bullet.hitboxRectangle) != null) {
                bullets.remove(i);
                lightGroup.remove(bullet.light);
                continue;
            }
        }

        // one bullet per second but only if the aim joystick is pulled all the way out
        if(aimJoystick.getDirection().getMagnitude() > 0.7f &&
                System.currentTimeMillis() - lastShotTimeMs > WEAPON_FIRE_MIN_WAIT_MS) {
            if(player.spendAmmo()) {
                lastShotTimeMs = System.currentTimeMillis();

                // Spawn bullet
                Vec2 bulletSpawnTransformed = player.getModelMatrix().transformPoint(BULLET_SPAWN_POINT);
                Vec2 bulletDirection = Geometry.normalize(aimJoystick.getDirection());

                Bullet bullet = new Bullet();
                bullet.velocity = Geometry.scaleVector(bulletDirection, 60f);
                bullet.sprite = new Square(false);
                bullet.sprite.setColour(Colour.YELLOW);
                bullet.sprite.setScale(14f, 8f);
                bullet.sprite.setPosition(bulletSpawnTransformed.x - 7f, bulletSpawnTransformed.y - 4f);
                bullet.sprite.setRotationAroundPoint(7f, 4f, 0f, (float)Math.toDegrees(Math.atan2(bulletDirection.y, bulletDirection.x)), 0f, 0f, 1f);
                bullet.spawnTime = System.currentTimeMillis();
                bullet.hitboxRectangle = new HitboxPolygon(new float[]{
                        -2f, 0f,
                        1f, 0f,
                        1f, 1f,
                        -2f, 1f,
                });
                bullet.hitboxRectangle.transform(bullet.sprite.getModelMatrix());

                bullet.light = new PointLight();
                bullet.light.setPosition(bullet.sprite.getPosition());
                bullet.light.setConstantAttenuation(400f);
                bullet.light.setLinearAttenuation(1f);
                bullet.light.setQuadraticAttenuation(10f);
                bullet.light.setAmbientStrength(0f);
                bullet.light.setColour(Colour.RED);
                lightGroup.add(bullet.light);

                // todo - temp only, improve
                bullet.sprite.setShader(lightingTextureShader);
                bullets.add(bullet);

                Audio.getInstance().playSound(R.raw.pistol_shot);
            } else {
                player.reload();
            }
        }

        camera.setPosition(getCenteredCameraPosition());
    }

    @Override
    public void render() {
        // Render GPU accelerate shadows to texture
        for(int n = 0; n < lightGroup.getPointLights().size(); n++) {
            PointLight light = lightGroup.getPointLights().get(n);
            shadowMap.start(n);
            shadowShader2D.setLightPos(light.getPosition2D());
            crateShadows.render(camera);
            shadowMap.end();
        }
        lightingTextureShader.setShadowTexture(shadowMap.getShadowMap());

        mapBase.render(camera, lightGroup);
        building.render(camera, lightGroup);

        for(int i = 0; i < bullets.size(); i++) {
            bullets.get(i).sprite.render(camera, lightGroup);
        }

        for(int i = 0; i < zombies.size(); i++) {
            zombies.get(i).render(camera, lightGroup);
        }

        player.render(camera);
        crates.render(camera);

        // UI
        movementJoystick.render(uiCamera);
        aimJoystick.render(uiCamera);
        ammo.draw(uiCamera);

        backButton.draw(uiCamera);
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {}

    private ModelProperties[] generateRandomModelProperties(int count, float minScale, float maxScale) {
        ModelProperties[] properties = new ModelProperties[count];
        for(int i = 0; i < count; i++) {
            float x = random.nextFloat() * 10000f;
            float y = random.nextFloat() * 10000f;
            float scale = minScale + (random.nextFloat() * (maxScale - minScale));
            properties[i] = new ModelProperties();
            properties[i].setPosition(x, y);
            properties[i].setScale(scale, scale);
            properties[i].setRotation(random.nextFloat() * 360f, 0f, 0f, 1f);
        }

        return properties;
    }

    private Vec2 getCenteredCameraPosition() {
        return Geometry.minus(new Vec2(player.getPosition()), new Vec2(
                (Crispin.getSurfaceWidth() / 2f) - (player.getSize().x / 2f),
                (Crispin.getSurfaceHeight() / 2f) - (player.getSize().y / 2f)));
    }
}
