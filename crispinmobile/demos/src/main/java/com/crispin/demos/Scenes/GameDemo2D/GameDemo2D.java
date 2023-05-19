package com.crispin.demos.Scenes.GameDemo2D;

import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_TEXTURE_2D;

import android.opengl.GLES30;

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
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Models.ModelProperties;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Shaders.LightingTextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.TwoDimensional.LightingTextureShader2D;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.InstanceRenderer;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Joystick;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.Text;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.Audio;
import com.crispin.crispinmobile.Utilities.FontCache;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.ShaderCache;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.demos.R;
import com.crispin.demos.Scenes.DemoMasterScene;
import com.crispin.demos.Util;

import java.util.ArrayList;
import java.util.Random;

public class GameDemo2D extends Scene {
    class Bullet{
        public Vec2 velocity;
        public Square sprite;
        public long spawnTime;
        public HitboxPolygon hitboxRectangle;
        public PointLight light;
    }

    private static final float PLAYER_SIZE = 128f;
    private static final int NUM_CRATES = 100;
    private static final float TEXT_PADDING = 20f;

    private Camera2D camera;
    private Camera2D uiCamera;
    private Button backButton;
    private Text ammo;

    private Joystick movementJoystick;
    private Joystick aimJoystick;

    private Player player;

    private Vec2 bulletSpawnPoint;

    private Square mapBase;

    private Building building;
    private HitboxRectangle buildingHitbox;
    private InstanceRenderer crates;
    private HitboxRectangle[] crateHitboxes;

    private ModelProperties[] crateModelProperties;
    private Random random;
    private LightGroup lightGroup;
    private PointLight light;

    private ArrayList<Bullet> bullets;
    private ArrayList<Zombie> zombies;

    private HitboxRectangle crateTest;
    private HitboxRectangle crateTest2;
    private Model shadowTest;
    private Model shadowTest2;
    private LightingTextureShader2D lightingTextureShader;
    private int framebuffer = -1;
    private int fbTexture = -1;

    public GameDemo2D() {
        Audio.getInstance().initMusicChannel();
        Audio.getInstance().initSoundChannel(8);
        Audio.getInstance().playMusic(R.raw.demo_music);

        camera = new Camera2D();
        uiCamera = new Camera2D();
        random = new Random();
        lightingTextureShader = new LightingTextureShader2D();

        backButton = Util.createBackButton(DemoMasterScene::new, 10f, Crispin.getSurfaceHeight() - 10f - Util.BACK_BUTTON_SIZE);

        movementJoystick = new Joystick(new Vec2(100f, 100f), 400f);

        aimJoystick = new Joystick(new Vec2(Crispin.getSurfaceWidth() - 500f, 100f), 400f);

        player = new Player(5000f, 5000f, PLAYER_SIZE);

        lightGroup = new LightGroup();
        light = new PointLight();
        lightGroup.addLight(light);
        light.setConstantAttenuation(1f);
        light.setQuadraticAttenuation(0.00007f);
        light.setLinearAttenuation(0.000005f);
        light.setAmbientStrength(0f);

      // atten = 1.0 / (light.constant + (light.linear * distance) + (light.quadratic * (distance * distance)));

//
        bulletSpawnPoint = new Vec2(31f/32f, 9f/32f);

        Material grassRepeatMaterial = new Material(R.drawable.dirt_tile);
        grassRepeatMaterial.setUvMultiplier(100f, 100f);
        mapBase = new Square(grassRepeatMaterial);
        mapBase.setScale(10000f);
        mapBase.setShader(lightingTextureShader);

        crateModelProperties = generateRandomModelProperties(NUM_CRATES, 100f, 200f);
        crates = new InstanceRenderer(new SquareMesh(true), false, crateModelProperties);
        crates.setTexture(TextureCache.loadTexture(R.drawable.crate_texture));
        crateHitboxes = new HitboxRectangle[NUM_CRATES];
        for(int i = 0; i < NUM_CRATES; i++) {
            crateHitboxes[i] = new HitboxRectangle();
            crateHitboxes[i].transform(crateModelProperties[i].getModelMatrix());
        }


        ModelMatrix modelMatrix = new ModelMatrix();
        modelMatrix.reset();
        modelMatrix.translate(4800f, 4800f);
        modelMatrix.scale(200f, 200f);
        crateTest = new HitboxRectangle();
        crateTest.transform(modelMatrix);

        ModelMatrix modelMatrix2 = new ModelMatrix();
        modelMatrix2.reset();
        modelMatrix2.translate(5000f, 4400f);
        modelMatrix2.scale(200f, 200f);
        crateTest2 = new HitboxRectangle();
        crateTest2.transform(modelMatrix2);

        building = new Building(new Vec2(5200f, 5200f), new Scale2D(750f, 400f));
        building.setShader(lightingTextureShader);
        buildingHitbox = new HitboxRectangle();
        buildingHitbox.transform(building.getModelMatrix());

        bullets = new ArrayList<>();

        zombies = new ArrayList<>();
        spawnZombies(100);

        ammo = new Text(FontCache.getFont(R.raw.aileron_bold, 56), String.format("Ammo %d/%d", player.getAmmo(), player.getMaxAmmo()));
        ammo.setPosition(Crispin.getSurfaceWidth() - ammo.getWidth() - TEXT_PADDING, Crispin.getSurfaceHeight() - TEXT_PADDING - ammo.getHeight());

        shadowTest = new Model(null);
        shadowTest2 = new Model(null);


        int[] genBufferTemp = new int[1];
        GLES30.glGenFramebuffers(1, genBufferTemp, 0);
        framebuffer = genBufferTemp[0];

        // Generate texture to render into
        GLES30.glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        int[] genTextureTemp = new int[1];
        GLES30.glGenTextures(1, genTextureTemp, 0);
        fbTexture = genTextureTemp[0];
        GLES30.glBindTexture(GL_TEXTURE_2D, fbTexture);
        GLES30.glTexImage2D(GL_TEXTURE_2D, 0, GLES30.GL_RGB, Crispin.getSurfaceWidth(),
                Crispin.getSurfaceHeight(), 0, GLES30.GL_RGB, GLES30.GL_UNSIGNED_BYTE,
                null);
        GLES30.glTexParameteri(GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glBindTexture(GL_TEXTURE_2D, 0);
        GLES30.glFramebufferTexture2D(GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, fbTexture, 0);
    }

    public void spawnZombies(int count) {
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
                lightGroup.removePointLight(bullet.light);
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
                lightGroup.removePointLight(bullet.light);
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
                lightGroup.removePointLight(bullet.light);
                continue;
            }

            if(building.isColliding(bullet.hitboxRectangle) != null) {
                bullets.remove(i);
                lightGroup.removePointLight(bullet.light);
                continue;
            }
        }

        // one bullet per second but only if the aim joystick is pulled all the way out
        if(aimJoystick.getDirection().getMagnitude() > 0.7f && System.currentTimeMillis() - timeMs > 100) {
            if(player.spendAmmo()) {
                timeMs = System.currentTimeMillis();

                // Spawn bullet
                //  Vec2 bulletStart = player.getModelMatrix().transformPoint(new Vec2(bulletSpawnPoint.x - 0.1f, bulletSpawnPoint.y));
                Vec2 bulletSpawnTransformed = player.getModelMatrix().transformPoint(bulletSpawnPoint);
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
                bullet.light.setConstantAttenuation(1f);
                bullet.light.setQuadraticAttenuation(0.00007f);
                bullet.light.setLinearAttenuation(0.000005f);
                bullet.light.setAmbientStrength(0f);
                lightGroup.addLight(bullet.light);

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

    long timeMs = System.currentTimeMillis();

    private Mesh calcMaskMesh(HitboxPolygon polygon) {
        // For each edge calculate
        float[] points = polygon.getTransformedPoints();
        float cx = player.getPosition().x + (player.getSize().x / 2f);
        float cy = player.getPosition().y + (player.getSize().y / 2f);

        float[] positionBuffer = new float[points.length * 6];

        for(int i = 0; i < points.length; i += 2) {
            int next = (i + 2) % points.length;
            float x = points[i];
            float y = points[i + 1];
            float x2 = points[next];
            float y2 = points[next + 1];

            // Get vector player -> point
            Vec2 maskPoint1 = Geometry.scaleVector(new Vec2(x - cx, y - cy), 3f);
            maskPoint1.x += x;
            maskPoint1.y += y;
            Vec2 maskPoint2 = Geometry.scaleVector(new Vec2(x2 - cx, y2 - cy), 3f);
            maskPoint2.x += x2;
            maskPoint2.y += y2;

            int mi = i * 6;

            // Triangle 1
            positionBuffer[mi] = x;
            positionBuffer[mi + 1] = y;
            positionBuffer[mi + 2] = maskPoint1.x;
            positionBuffer[mi + 3] = maskPoint1.y;
            positionBuffer[mi + 4] = x2;
            positionBuffer[mi + 5] = y2;

            // Triangle 2
            positionBuffer[mi + 6] = x2;
            positionBuffer[mi + 7] = y2;
            positionBuffer[mi + 8] = maskPoint1.x;
            positionBuffer[mi + 9] = maskPoint1.y;
            positionBuffer[mi + 10] = maskPoint2.x;
            positionBuffer[mi + 11] = maskPoint2.y;
        }

        return new Mesh(positionBuffer, null, null, Mesh.RenderMethod.TRIANGLES,
                2, 0, 0);
    }

    private HitboxPolygon[] calcMask(HitboxPolygon polygon) {
        // For each edge calculate
        float[] points = polygon.getTransformedPoints();
        HitboxPolygon[] masks = new HitboxPolygon[points.length / 2];
        Mesh shadowMesh = null;
        float cx = player.getPosition().x + (player.getSize().x / 2f);
        float cy = player.getPosition().y + (player.getSize().y / 2f);

        float[] positionBuffer = new float[points.length * 4];

        for(int i = 0; i < points.length; i += 2) {
            int next = (i + 2) % points.length;
            float x = points[i];
            float y = points[i + 1];
            float x2 = points[next];
            float y2 = points[next + 1];

            // Get vector player -> point
            Vec2 maskPoint1 = Geometry.scaleVector(new Vec2(x - cx, y - cy), 3f);
            maskPoint1.x += x;
            maskPoint1.y += y;
            Vec2 maskPoint2 = Geometry.scaleVector(new Vec2(x2 - cx, y2 - cy), 3f);
            maskPoint2.x += x2;
            maskPoint2.y += y2;

            masks[i / 2] = new HitboxPolygon(new float[]{
                    x, y,
                    x2, y2,
                    maskPoint2.x, maskPoint2.y,
                    maskPoint1.x, maskPoint2.y,
            });

            int mi = i * 2;
            positionBuffer[mi] = x;
            positionBuffer[mi + 1] = y;
            positionBuffer[mi + 2] = x;
            positionBuffer[mi + 3] = y;
            positionBuffer[mi + 4] = maskPoint2.x;
            positionBuffer[mi + 5] = maskPoint2.y;
            positionBuffer[mi + 6] = maskPoint1.x;
            positionBuffer[mi + 7] = maskPoint2.y;
        }

        shadowMesh = new Mesh(positionBuffer, null, null,
                Mesh.RenderMethod.TRIANGLES, 2,
                0, 0);
        return masks;
    }

    @Override
    public void render() {
        GLES30.glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        GLES30.glBindTexture(GL_TEXTURE_2D, fbTexture);
        GLES30.glClearColor(0f, 0f, 0f, 1f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        shadowTest.setMesh(calcMaskMesh(crateTest));
        shadowTest.setColour(Colour.WHITE);
        shadowTest.render(camera);

        shadowTest2.setMesh(calcMaskMesh(crateTest2));
        shadowTest2.setColour(Colour.WHITE);
        shadowTest2.render(camera);

        lightingTextureShader.setShadowTexture(fbTexture);
        GLES30.glBindFramebuffer(GL_FRAMEBUFFER, 0);

        // todo: we need to pass in the texture we want to render too, because this is one texture per shadow approach at the moment
//        shadowTest2.setMesh(calcMaskMesh(crateTest2));
//        shadowTest2.setColour(Colour.WHITE);
//        int id2 = shadowTest2.renderToTextureTest(camera);
//        lightingTextureShader.setShadowTexture(id2);

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

//        crateTest.render(camera);



//        shadowTest.render(camera);


//        HitboxPolygon[] polygons = calcMask(crateTest);
//        for(HitboxPolygon polygon : polygons) {
//            polygon.render(camera);
//        }


    //    playerHitbox.render(camera);

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
