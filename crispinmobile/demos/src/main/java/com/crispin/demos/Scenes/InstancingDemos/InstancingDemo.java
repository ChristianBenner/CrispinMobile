package com.crispin.demos.Scenes.InstancingDemos;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Scale3D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.DefaultMesh.CubeMesh;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.InstanceRenderer;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.UserInterface.Border;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Font;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.Text;
import com.crispin.crispinmobile.UserInterface.TouchEvent;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.demos.R;
import com.crispin.demos.Scenes.DemoMasterScene;

import java.util.Random;

public class InstancingDemo extends Scene {
    private final int NUM_INSTANCES = 100000;
    private final Scale3D GENERATION_AREA_SIZE = new Scale3D(30.0f, 30.0f, 100.0f);

    private InstanceRenderer textureInstanceRenderer;
    private InstanceRenderer globalColourInstanceRenderer;
    private final LightGroup lightGroup;

    private final Camera camera;
    private float cameraZCount = 0.0f;

    // Different render modes
    private boolean renderTextureInstances;
    private boolean renderGlobalColourInstances;

    // UI
    private Camera2D uiCamera;
    private Button toggleTextureInstances;
    private Button toggleGlobalColourInstances;
    private Button homeButton;
    private Text instanceCount;
    private Text fpsText;

    private int instanceRenderCount = 0;

    private PointLight[] zipLights;

    private Random random;

    public InstancingDemo() {
        Crispin.setBackgroundColour(Colour.BLACK);
        renderTextureInstances = false;
        renderGlobalColourInstances = true;
        instanceRenderCount = NUM_INSTANCES;
        initUI();

        camera = new Camera();
       // camera.setPosition(new Vec3(0.0f, 0f, GENERATION_AREA_SIZE.z));
        float cameraZ = (((float)Math.sin(cameraZCount)) * (GENERATION_AREA_SIZE.l /2f)) + 10.0f;
        camera.setPosition(0.0f, 0.0f, cameraZ);

        lightGroup = new LightGroup();
        zipLights = new PointLight[10];

        random = new Random();
        for(int i = 0; i < 10; i++) {
            PointLight pointLight = new PointLight();
            zipLights[i] = pointLight;

            setRandomSpawn(pointLight);

            pointLight.setAmbientStrength(0.0f);
            pointLight.setDiffuseStrength(2f);
            pointLight.setSpecularStrength(0.5f);
            pointLight.setLinearAttenuation(1.5f);
            pointLight.setColour(Colour.WHITE);

            lightGroup.addLight(pointLight);
        }

        textureInstanceRenderer = new InstanceRenderer(new CubeMesh(true, true), true, generateRandomModelTransformations());
        textureInstanceRenderer.setTexture(TextureCache.loadTexture(R.drawable.crate_texture));
        textureInstanceRenderer.setGlobalColour(Colour.CYAN);
        textureInstanceRenderer.setLightGroup(lightGroup);

        globalColourInstanceRenderer = new InstanceRenderer(new CubeMesh(false, true), true, generateRandomModelTransformations());
        globalColourInstanceRenderer.setGlobalColour(Colour.GREEN);
        globalColourInstanceRenderer.setLightGroup(lightGroup);
    }

    private void setRandomSpawn(PointLight pointLight) {
        float rx = (random.nextFloat() * 18.0f) - 9.0f;
        float ry = (random.nextFloat() * 10.0f) - 5.0f;
        float rz = 40.0f + random.nextFloat() * (200.0f - 40.0f);
        pointLight.setPosition(rx, ry, rz);
    }

    private ModelMatrix[] generateRandomModelTransformations() {
        Random r = new Random();
        ModelMatrix[] matrices = new ModelMatrix[NUM_INSTANCES];
        for(int i = 0; i < NUM_INSTANCES; i++) {
            float x = (r.nextFloat() * GENERATION_AREA_SIZE.w) - (GENERATION_AREA_SIZE.w /2.0f);
            float y = (r.nextFloat() * GENERATION_AREA_SIZE.h) - (GENERATION_AREA_SIZE.h /2.0f);
            float z = (r.nextFloat() * GENERATION_AREA_SIZE.l) - (GENERATION_AREA_SIZE.l /2.0f);
            float rotateAngle = r.nextFloat() * 360.0f;
            float scale = (1.0f + r.nextFloat()) / 32f;

            ModelMatrix modelMatrix = new ModelMatrix();
            modelMatrix.translate(x, y, z);
            modelMatrix.rotate(rotateAngle, 0.4f, 0.6f, 0.8f);
            modelMatrix.scale(scale);
            matrices[i] = modelMatrix;
        }

        return matrices;
    }

    private void initUI() {
        uiCamera = new Camera2D();

        Font instanceCountFont = new Font(R.raw.aileron_regular, 48);
        instanceCount = new Text(instanceCountFont, "Instances Rendered: " + instanceRenderCount, false, true, Crispin.getSurfaceWidth() - 20);
        instanceCount.setPosition(10, Crispin.getSurfaceHeight() - 58);
        instanceCount.setColour(Colour.WHITE);

        fpsText = new Text(instanceCountFont, Crispin.getFps() + "FPS", false, true, Crispin.getSurfaceWidth() - 20);
        fpsText.setPosition(10, Crispin.getSurfaceHeight() - 58 - 10 - instanceCountFont.getSize());
        fpsText.setColour(Colour.WHITE);

        Font buttonFont = new Font(R.raw.aileron_regular, 24);
        Font homeButtonFont = new Font(R.raw.aileron_regular, 36);

        homeButton = new Button(homeButtonFont, "Home");
        homeButton.setPosition(Crispin.getSurfaceWidth() - 10 - 200, 10);
        homeButton.setSize(200, 200);
        homeButton.setBorder(new Border(Colour.BLACK));
        homeButton.setColour(Colour.LIGHT_GREY);
        homeButton.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                Crispin.setScene(DemoMasterScene::new);
            }
        });

        toggleTextureInstances = new Button(buttonFont, "Toggle Texture Instances");
        toggleTextureInstances.setPosition(10, 10);
        toggleTextureInstances.setSize(200, 200);
        toggleTextureInstances.setBorder(new Border(Colour.BLACK));
        toggleTextureInstances.setColour(Colour.LIGHT_GREY);
        toggleTextureInstances.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                renderTextureInstances = !renderTextureInstances;
                if(renderTextureInstances){
                    instanceRenderCount += NUM_INSTANCES;
                } else {
                    instanceRenderCount -= NUM_INSTANCES;
                }
                instanceCount.setText("Instances Rendered: " + instanceRenderCount);
            }
        });

        toggleGlobalColourInstances = new Button(buttonFont, "Toggle Global Colour Instances");
        toggleGlobalColourInstances.setPosition(10, 230);
        toggleGlobalColourInstances.setSize(200, 200);
        toggleGlobalColourInstances.setBorder(new Border(Colour.BLACK));
        toggleGlobalColourInstances.setColour(Colour.LIGHT_GREY);
        toggleGlobalColourInstances.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                renderGlobalColourInstances = !renderGlobalColourInstances;
                if(renderGlobalColourInstances){
                    instanceRenderCount += NUM_INSTANCES;
                } else {
                    instanceRenderCount -= NUM_INSTANCES;
                }
                instanceCount.setText("Instances Rendered: " + instanceRenderCount);
            }
        });
    }

    @Override
    public void update(float deltaTime) {
        fpsText.setText(Crispin.getFps() + "FPS");
        // Update camera position to move in out
       // cameraZCount += 0.005f * deltaTime;
       // float cameraZ = (((float)Math.sin(cameraZCount)) * (GENERATION_AREA_SIZE.z/2f)) + 10.0f;
       // camera.setPosition(0.0f, 0.0f, cameraZ);

      //  globalColourInstanceRenderer.updateAll();
        for(int i = 0; i < 10; i++) {
            zipLights[i].translate(0.0f, 0.0f, -0.6f);
            if(zipLights[i].getPosition().z <= -100.0f) {
                setRandomSpawn(zipLights[i]);
            }
        }
    }

    @Override
    public void render() {
        if(renderTextureInstances) {
            textureInstanceRenderer.render(camera);
        }

        if(renderGlobalColourInstances) {
            globalColourInstanceRenderer.render(camera);
        }

        instanceCount.draw(uiCamera);
        fpsText.draw(uiCamera);
        toggleTextureInstances.draw(uiCamera);
        toggleGlobalColourInstances.draw(uiCamera);
        homeButton.draw(uiCamera);
    }

    Vec2 downPos = new Vec2();
    @Override
    public void touch(TouchType touchType, Pointer pointer) {
        Vec2 position = new Vec2(pointer.getPosition());

        switch (touchType) {
            case DOWN:
                downPos = position;
                break;
            case MOVE:
                camera.translate(0.0f, 0.0f, Geometry.getVectorBetween(downPos, position).y/50.0f);
                downPos = position;
                break;
        }
    }
}