package com.crispin.demos.scenes;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Scale3D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.DefaultMesh.CubeMesh;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.InstanceRenderer;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.UserInterface.Border;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Font;
import com.crispin.crispinmobile.UserInterface.Text;
import com.crispin.crispinmobile.UserInterface.TouchEvent;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.demos.R;

import java.util.Random;

public class InstancingEngineDemo extends Scene {
    private final int NUM_INSTANCES = 200000;
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
    private Text instanceCount;

    private int instanceRenderCount = 0;

    public InstancingEngineDemo() {
        Crispin.setBackgroundColour(Colour.BLACK);
        renderTextureInstances = true;
        instanceRenderCount = NUM_INSTANCES;
        initUI();

        camera = new Camera();
        camera.setPosition(new Vec3(0.0f, 0f, GENERATION_AREA_SIZE.z));

        PointLight pointLight = new PointLight();
        pointLight.setPosition(1.0f, 0.0f, 1.0f);
        pointLight.setAmbientStrength(0.5f);
        pointLight.setDiffuseStrength(1.0f);
        pointLight.setSpecularStrength(1f);
        pointLight.setColour(Colour.ORANGE);

        DirectionalLight directionalLight = new DirectionalLight(0.0f, -1.0f, 0.0f);

        lightGroup = new LightGroup();
        lightGroup.addLight(pointLight);
        lightGroup.addLight(directionalLight);

        textureInstanceRenderer = new InstanceRenderer(new CubeMesh(true, false));
        textureInstanceRenderer.setTexture(TextureCache.loadTexture(R.drawable.crate_texture));
        textureInstanceRenderer.add(generateRandomModelTransformations());
        textureInstanceRenderer.setGlobalColour(Colour.BLUE);

        globalColourInstanceRenderer = new InstanceRenderer(new CubeMesh(false, true));
        globalColourInstanceRenderer.add(generateRandomModelTransformations());
        globalColourInstanceRenderer.setGlobalColour(Colour.GREEN);
        globalColourInstanceRenderer.setLightGroup(lightGroup);
    }

    private ModelMatrix[] generateRandomModelTransformations() {
        Random r = new Random();
        ModelMatrix[] matrices = new ModelMatrix[NUM_INSTANCES];
        for(int i = 0; i < NUM_INSTANCES; i++) {
            float x = (r.nextFloat() * GENERATION_AREA_SIZE.x) - (GENERATION_AREA_SIZE.x/2.0f);
            float y = (r.nextFloat() * GENERATION_AREA_SIZE.y) - (GENERATION_AREA_SIZE.y/2.0f);
            float z = (r.nextFloat() * GENERATION_AREA_SIZE.z) - (GENERATION_AREA_SIZE.z/2.0f);
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

        Font buttonFont = new Font(R.raw.aileron_regular, 24);
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
        // Update camera position to move in out
        cameraZCount += 0.005f * deltaTime;
        float cameraZ = (((float)Math.sin(cameraZCount)) * (GENERATION_AREA_SIZE.z/2f)) + 10.0f;
        camera.setPosition(0.0f, 0.0f, cameraZ);
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
        toggleTextureInstances.draw(uiCamera);
        toggleGlobalColourInstances.draw(uiCamera);
    }

    @Override
    public void touch(int type, Vec2 position) {

    }
}