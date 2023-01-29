package com.crispin.demos.scenes;

import android.opengl.Matrix;
import android.view.MotionEvent;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Scale3D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.DefaultMesh.CubeMesh;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Models.ModelProperties;
import com.crispin.crispinmobile.Rendering.Shaders.LightingTextureShader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.InstanceRenderer;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.Rendering.Utilities.RenderBatch;
import com.crispin.crispinmobile.UserInterface.Border;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Font;
import com.crispin.crispinmobile.UserInterface.Text;
import com.crispin.crispinmobile.UserInterface.TouchEvent;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.demos.R;

import java.util.Random;

public class InstancingVsBatchDemo extends Scene {
    private final int NUM_INSTANCES = 60000;
    private final int NUM_RENDER_BATCH_OBJECTS = 10000;
    private final Scale3D GENERATION_AREA_SIZE = new Scale3D(30.0f, 30.0f, 100.0f);
    private final int NUM_FLOATS_MATRIX = 16;
    private final int NUM_FLOATS_COLOUR = 4;

    private InstanceRenderer instanceRenderer;
    private RenderBatch renderBatch;

    private final LightGroup lightGroup;

    private final Camera camera;

    // Different render modes
    private boolean renderAsInstances;
    private boolean staticRender;

    // UI
    private Camera2D uiCamera;
    private Button toggleRenderingTechnique;
    private Button toggleStaticRender;
    private Button homeButton;
    private Text fpsText;
    private Text typeText;
    private Text objectCountText;

    float[] modelMatrices;
    float[] colourData;
    ModelProperties[] modelProperties;

    public InstancingVsBatchDemo() {
        Crispin.setBackgroundColour(Colour.BLACK);
        renderAsInstances = true;
        initUI();
        this.modelMatrices = new float[NUM_FLOATS_MATRIX * NUM_INSTANCES];

        camera = new Camera();
        camera.setPosition(new Vec3(0.0f, 0f, GENERATION_AREA_SIZE.z));

        lightGroup = new LightGroup();
        DirectionalLight directionalLight = new DirectionalLight(0.0f, -1.0f, 0.0f);
        directionalLight.setAmbientStrength(0.4f);
        directionalLight.setDiffuseStrength(1.0f);
        directionalLight.setSpecularStrength(0.4f);
        lightGroup.addLight(directionalLight);

        this.modelMatrices = new float[NUM_FLOATS_MATRIX * NUM_INSTANCES];
        this.colourData = new float[NUM_FLOATS_COLOUR * NUM_INSTANCES];

        renderBatch = new RenderBatch();
        renderBatch.setCamera(camera);
        renderBatch.setRenderObject(new CubeMesh(true, true));
        renderBatch.setLightGroup(lightGroup);

        modelProperties = new ModelProperties[NUM_RENDER_BATCH_OBJECTS];
        Random r = new Random();
        for(int i = 0; i < NUM_INSTANCES; i++) {
            int matrixOffset = NUM_FLOATS_MATRIX * i;
            int colourOffset = NUM_FLOATS_COLOUR * i;
            float x = (r.nextFloat() * GENERATION_AREA_SIZE.x) - (GENERATION_AREA_SIZE.x/2.0f);
            float y = (r.nextFloat() * GENERATION_AREA_SIZE.y) - (GENERATION_AREA_SIZE.y/2.0f);
            float z = (r.nextFloat() * GENERATION_AREA_SIZE.z) - (GENERATION_AREA_SIZE.z/2.0f);
            float rotateAngle = r.nextFloat() * 360.0f;
            float scale = (1.0f + r.nextFloat()) / 32f;
            float red = r.nextFloat();
            float green = r.nextFloat();
            float blue = r.nextFloat();
            float alpha = 1.0f;

            Matrix.setIdentityM(modelMatrices, matrixOffset);
            Matrix.translateM(modelMatrices, matrixOffset, x, y, z);
            Matrix.rotateM(modelMatrices, matrixOffset, rotateAngle, 0.4f, 0.6f, 0.8f);
            Matrix.scaleM(modelMatrices, matrixOffset, scale, scale, scale);
            colourData[colourOffset] = red;
            colourData[colourOffset + 1] = green;
            colourData[colourOffset + 2] = blue;
            colourData[colourOffset + 3] = alpha;

            if(i < NUM_RENDER_BATCH_OBJECTS) {
                ModelProperties mp = new ModelProperties(new Material(TextureCache.loadTexture(R.drawable.crate_texture), new Colour(red, green, blue, alpha)));
                mp.setPosition(x, y, z);
                mp.setRotation(rotateAngle, 0.4f, 0.6f, 0.8f);
                mp.setScale(scale);
                renderBatch.add(mp);
                modelProperties[i] = mp;
            }
        }

        instanceRenderer = new InstanceRenderer(new CubeMesh(true, true), modelMatrices, colourData);
        instanceRenderer.setTexture(TextureCache.loadTexture(R.drawable.crate_texture));
        instanceRenderer.setLightGroup(lightGroup);

        LightingTextureShader shader = new LightingTextureShader();
        renderBatch.setShader(shader);
    }

    private void initUI() {
        uiCamera = new Camera2D();

        Font instanceCountFont = new Font(R.raw.aileron_regular, 48);
        int heightAndPadding = 10 + instanceCountFont.getSize();
        typeText = new Text(instanceCountFont, "Instance Rendering Active" + (staticRender ? " (Static)" : " (Update)"), false, true, Crispin.getSurfaceWidth() - 20);
        typeText.setPosition(10, Crispin.getSurfaceHeight() - heightAndPadding);
        typeText.setColour(Colour.WHITE);

        objectCountText = new Text(instanceCountFont, "Objects: " + NUM_INSTANCES, false, true, Crispin.getSurfaceWidth() - 20);
        objectCountText.setPosition(10, Crispin.getSurfaceHeight() - (heightAndPadding * 2));
        objectCountText.setColour(Colour.WHITE);

        fpsText = new Text(instanceCountFont, Crispin.getFps() + "FPS", false, true, Crispin.getSurfaceWidth() - 20);
        fpsText.setPosition(10, Crispin.getSurfaceHeight() - (heightAndPadding * 3));
        fpsText.setColour(Colour.WHITE);

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

        toggleRenderingTechnique = new Button(homeButtonFont, "Toggle Renderer");
        toggleRenderingTechnique.setPosition(10, 10);
        toggleRenderingTechnique.setSize(200, 200);
        toggleRenderingTechnique.setBorder(new Border(Colour.BLACK));
        toggleRenderingTechnique.setColour(Colour.LIGHT_GREY);
        toggleRenderingTechnique.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                renderAsInstances = !renderAsInstances;
                updateRenderInfoText();
            }
        });

        toggleStaticRender = new Button(homeButtonFont, "Toggle Update");
        toggleStaticRender.setPosition(10, 220);
        toggleStaticRender.setSize(200, 200);
        toggleStaticRender.setBorder(new Border(Colour.BLACK));
        toggleStaticRender.setColour(Colour.LIGHT_GREY);
        toggleStaticRender.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                staticRender = !staticRender;
                updateRenderInfoText();
            }
        });
    }

    private void updateRenderInfoText() {
        String suffix = staticRender ? " (Static)" : " (Update)";
        if(renderAsInstances) {
            typeText.setText("Instance Rendering Active" + suffix);
            objectCountText.setText("Objects: " + NUM_INSTANCES);
        } else {
            typeText.setText("RenderBatch Active" + suffix);
            objectCountText.setText("Objects: " + NUM_RENDER_BATCH_OBJECTS);
        }
    }

    float angle = 0.0f;
    @Override
    public void update(float deltaTime) {
        fpsText.setText(Crispin.getFps() + "FPS");

        if(!staticRender) {
            if(renderAsInstances) {
                for (int i = 0; i < NUM_INSTANCES; i++) {
                    int offset = NUM_FLOATS_MATRIX * i;
                    Matrix.rotateM(modelMatrices, offset, deltaTime, 0.4f, 0.6f, 0.8f);
                }
                instanceRenderer.uploadModelMatrices(modelMatrices);
            } else {
                for (int i = 0; i < NUM_RENDER_BATCH_OBJECTS; i++) {
                    modelProperties[i].setRotation(angle, 0.4f, 0.6f, 0.8f);
                }
                angle += 1f * deltaTime;
            }
        }
    }

    @Override
    public void render() {
        if(renderAsInstances) {
            instanceRenderer.render(camera);
        } else {
            renderBatch.render();
        }

        typeText.draw(uiCamera);
        objectCountText.draw(uiCamera);
        fpsText.draw(uiCamera);
        toggleRenderingTechnique.draw(uiCamera);
        toggleStaticRender.draw(uiCamera);
        homeButton.draw(uiCamera);
    }

    Vec2 downPos = new Vec2();
    @Override
    public void touch(int type, Vec2 position) {
        switch (type) {
            case MotionEvent.ACTION_DOWN:
                downPos = new Vec2(position);
                break;
            case MotionEvent.ACTION_MOVE:
                camera.translate(0.0f, 0.0f, Geometry.getVectorBetween(downPos, position).y/50.0f);
                downPos = new Vec2(position);
                break;
        }
    }
}