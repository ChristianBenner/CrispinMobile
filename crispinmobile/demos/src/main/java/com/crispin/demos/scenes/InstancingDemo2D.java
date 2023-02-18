package com.crispin.demos.scenes;

import android.view.MotionEvent;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Scale3D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.DefaultMesh.CubeMesh;
import com.crispin.crispinmobile.Rendering.DefaultMesh.SquareMesh;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
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

public class InstancingDemo2D extends Scene {
    private final int NUM_INSTANCES = 3000;

    private InstanceRenderer globalColourInstanceRenderer;

    private final Camera2D camera;

    // UI
    private Camera2D uiCamera;
    private Button homeButton;
    private Button regenButton;
    private Text instanceCount;
    private Text fpsText;

    private int instanceRenderCount = 0;

    private PointLight pointLight;

    public InstancingDemo2D() {
        Crispin.setBackgroundColour(Colour.BLACK);

        instanceRenderCount = NUM_INSTANCES;
        initUI();

        camera = new Camera2D();
        // Set position so that the center of the view is 0,0
        camera.setPosition(-Crispin.getSurfaceWidth()/2.0f, -Crispin.getSurfaceHeight()/2.0f);


        globalColourInstanceRenderer = new InstanceRenderer(new SquareMesh(true),
                generateRandomModelTransformations(), generateRandomColours());
        globalColourInstanceRenderer.setTexture(TextureCache.loadTexture(R.drawable.crate_texture));

        LightGroup lightGroup = new LightGroup();
        pointLight = new PointLight(400f, 400f);
        pointLight.setLinearAttenuation(0.0001f);
        pointLight.setQuadraticAttenuation(0.0001f);

        lightGroup.addLight(pointLight);
        globalColourInstanceRenderer.setLightGroup(lightGroup);
    }

    private ModelMatrix[] generateRandomModelTransformations() {
        Random r = new Random();
        ModelMatrix[] matrices = new ModelMatrix[NUM_INSTANCES];
        for(int i = 0; i < NUM_INSTANCES; i++) {
            float x = (-Crispin.getSurfaceWidth() / 2.0f) + r.nextFloat() * Crispin.getSurfaceWidth();
            float y = (-Crispin.getSurfaceHeight() / 2.0f) + r.nextFloat() * Crispin.getSurfaceHeight();
            float scale = r.nextFloat() * 100f;
            ModelMatrix modelMatrix = new ModelMatrix();
            modelMatrix.translate(x, y);
            modelMatrix.scale(scale, scale);
            matrices[i] = modelMatrix;
        }

        return matrices;
    }

    private Colour[] generateRandomColours() {
        Random r = new Random();
        Colour[] colours = new Colour[NUM_INSTANCES];
        for(int i = 0; i < NUM_INSTANCES; i++) {
            float red = r.nextFloat();
            float green = r.nextFloat();
            float blue = r.nextFloat();
            float alpha = 1.0f;
            colours[i] = new Colour(red, green, blue, alpha);
        }

        return colours;
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

        regenButton = new Button(homeButtonFont, "Regenerate");
        regenButton.setPosition(Crispin.getSurfaceWidth() - 10 - 200, 230);
        regenButton.setSize(200, 200);
        regenButton.setBorder(new Border(Colour.BLACK));
        regenButton.setColour(Colour.LIGHT_GREY);
        regenButton.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                globalColourInstanceRenderer.uploadModelMatrices(generateRandomModelTransformations());
                globalColourInstanceRenderer.uploadColourData(generateRandomColours());
            }
        });
    }

    private float n = 0.0f;

    @Override
    public void update(float deltaTime) {
        fpsText.setText(Crispin.getFps() + "FPS");

        float x = (float)Math.sin(n) * 200.0f;
        float y = (float)Math.cos(n) * 400.0f;
        n += 0.02f * deltaTime;
        pointLight.setPosition(x, y);
    }

    @Override
    public void render() {
        globalColourInstanceRenderer.render(camera);

        instanceCount.draw(uiCamera);
        fpsText.draw(uiCamera);
        homeButton.draw(uiCamera);
        regenButton.draw(uiCamera);
    }

    Vec2 downPos = new Vec2();
    long lastClick = 0;

    @Override
    public void touch(int type, Vec2 position) {
        switch (type) {
            case MotionEvent.ACTION_DOWN:
                long currentClick = System.currentTimeMillis();
                if(currentClick - lastClick < 500) {
                    camera.setZoom(camera.getZoom() + 0.5f);
                }
                lastClick = currentClick;

                downPos = new Vec2(position);
                break;
            case MotionEvent.ACTION_MOVE:
                if(downPos != null) {
                    camera.translate(Geometry.getVectorBetween(downPos, position).x, Geometry.getVectorBetween(downPos, position).y);
                    downPos = new Vec2(position);
                }
                break;
            case MotionEvent.ACTION_UP:
                downPos = null;
                break;
        }
    }
}