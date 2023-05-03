package com.crispin.demos.Scenes.InstancingDemos;

import android.view.MotionEvent;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
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
    private final int TEXT_PADDING = 10;
    private final int NUM_INSTANCES = 3000;

    private final int NUM_RENDERERS = 8;

    private final int RENDER_UNIQUE_COLOUR_TEXTURE_LIGHTING = 0;
    private final int RENDER_TEXTURE_LIGHTING = 1;
    private final int RENDER_COLOUR_LIGHTING = 2;
    private final int RENDER_UNIQUE_COLOUR_LIGHTING = 3;
    private final int RENDER_UNIQUE_COLOUR_TEXTURE = 4;
    private final int RENDER_TEXTURE = 5;
    private final int RENDER_COLOUR = 6;
    private final int RENDER_UNIQUE_COLOUR = 7;

    // Rendering
    private InstanceRenderer[] renderers;
    private Camera2D camera;
    private PointLight pointLight;
    private int instanceRenderCount;
    private int rendererIndex;

    // UI
    private Camera2D uiCamera;
    private Button homeButton;
    private Button regenButton;
    private Button nextDataSetButton;
    private Text instanceCount;
    private Text typeText;
    private Text fpsText;
    private Vec2 touchDownPos;

    public InstancingDemo2D() {
        Crispin.setBackgroundColour(Colour.BLACK);

        instanceRenderCount = NUM_INSTANCES;
        rendererIndex = RENDER_UNIQUE_COLOUR_TEXTURE_LIGHTING;
        touchDownPos = new Vec2();

        initUI();

        camera = new Camera2D();
        // Set position so that the center of the view is 0,0
        camera.setPosition(-Crispin.getSurfaceWidth()/2.0f, -Crispin.getSurfaceHeight()/2.0f);

        ModelMatrix[] modelTransformations = generateRandomModelTransformations();
        Colour[] colourData = generateRandomColours();

        renderers = new InstanceRenderer[NUM_RENDERERS];
        // Initialise the lighting renderers
        renderers[RENDER_UNIQUE_COLOUR_TEXTURE_LIGHTING] = new InstanceRenderer(new SquareMesh(true), true, modelTransformations, colourData);
        renderers[RENDER_UNIQUE_COLOUR_TEXTURE_LIGHTING].setTexture(TextureCache.loadTexture(R.drawable.crate_texture));

        renderers[RENDER_TEXTURE_LIGHTING] = new InstanceRenderer(new SquareMesh(true), true, modelTransformations);
        renderers[RENDER_TEXTURE_LIGHTING].setTexture(TextureCache.loadTexture(R.drawable.crate_texture));
        renderers[RENDER_TEXTURE_LIGHTING].setGlobalColour(Colour.RED);

        renderers[RENDER_COLOUR_LIGHTING] = new InstanceRenderer(new SquareMesh(false), true, modelTransformations);
        renderers[RENDER_COLOUR_LIGHTING].setGlobalColour(Colour.BLUE);

        renderers[RENDER_UNIQUE_COLOUR_LIGHTING] = new InstanceRenderer(new SquareMesh(false), true, modelTransformations, colourData);

        // Initialise the non-lighting renderers
        renderers[RENDER_UNIQUE_COLOUR_TEXTURE] = new InstanceRenderer(new SquareMesh(true), false, modelTransformations, colourData);
        renderers[RENDER_UNIQUE_COLOUR_TEXTURE].setTexture(TextureCache.loadTexture(R.drawable.crate_texture));

        renderers[RENDER_TEXTURE] = new InstanceRenderer(new SquareMesh(true), false, modelTransformations);
        renderers[RENDER_TEXTURE].setTexture(TextureCache.loadTexture(R.drawable.crate_texture));
        renderers[RENDER_TEXTURE].setGlobalColour(Colour.RED);

        renderers[RENDER_COLOUR] = new InstanceRenderer(new SquareMesh(false), false, modelTransformations);
        renderers[RENDER_COLOUR].setGlobalColour(Colour.BLUE);

        renderers[RENDER_UNIQUE_COLOUR] = new InstanceRenderer(new SquareMesh(false), false, modelTransformations, colourData);

        LightGroup lightGroup = new LightGroup();
        pointLight = new PointLight(400f, 400f);
        pointLight.setLinearAttenuation(0.00001f);
        pointLight.setQuadraticAttenuation(0.00001f);
        pointLight.setConstantAttenuation(1f);

        lightGroup.addLight(pointLight);
        renderers[RENDER_UNIQUE_COLOUR_TEXTURE_LIGHTING].setLightGroup(lightGroup);
        renderers[RENDER_TEXTURE_LIGHTING].setLightGroup(lightGroup);
        renderers[RENDER_COLOUR_LIGHTING].setLightGroup(lightGroup);
        renderers[RENDER_UNIQUE_COLOUR_LIGHTING].setLightGroup(lightGroup);
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

        Font font = new Font(R.raw.aileron_regular, 48);
        int textOffset = TEXT_PADDING + font.getSize();

        typeText = new Text(font, "Renderer: " + dataTypeToString(rendererIndex), false, true, Crispin.getSurfaceWidth() - 20);
        typeText.setPosition(10, Crispin.getSurfaceHeight() - textOffset);
        typeText.setColour(Colour.WHITE);

        instanceCount = new Text(font, "Instances Rendered: " + instanceRenderCount, false, true, Crispin.getSurfaceWidth() - 20);
        instanceCount.setPosition(10, Crispin.getSurfaceHeight() - (textOffset * 2f));
        instanceCount.setColour(Colour.WHITE);

        fpsText = new Text(font, Crispin.getFps() + "FPS", false, true, Crispin.getSurfaceWidth() - 20);
        fpsText.setPosition(10, Crispin.getSurfaceHeight() - (textOffset * 3f));
        fpsText.setColour(Colour.WHITE);

        Font homeButtonFont = new Font(R.raw.aileron_regular, 36);

        homeButton = new Button(homeButtonFont, "Back");
        homeButton.setPosition(Crispin.getSurfaceWidth() - 10 - 200, 10);
        homeButton.setSize(200, 200);
        homeButton.setBorder(new Border(Colour.BLACK));
        homeButton.setColour(Colour.LIGHT_GREY);
        homeButton.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                Crispin.setScene(InstancingDemoSelectionScene::new);
            }
        });

        regenButton = new Button(homeButtonFont, "Regenerate");
        regenButton.setPosition(Crispin.getSurfaceWidth() - 10 - 200, 230);
        regenButton.setSize(200, 200);
        regenButton.setBorder(new Border(Colour.BLACK));
        regenButton.setColour(Colour.LIGHT_GREY);
        regenButton.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                ModelMatrix[] modelTransformations = generateRandomModelTransformations();
                Colour[] colourData = generateRandomColours();
                for(int i = 0; i < NUM_RENDERERS; i++) {
                    renderers[i].uploadModelMatrices(modelTransformations);
                }

                renderers[RENDER_UNIQUE_COLOUR_TEXTURE_LIGHTING].uploadColourData(colourData);
                renderers[RENDER_COLOUR_LIGHTING].uploadColourData(colourData);
                renderers[RENDER_UNIQUE_COLOUR_TEXTURE].uploadColourData(colourData);
                renderers[RENDER_COLOUR].uploadColourData(colourData);
                renderers[RENDER_UNIQUE_COLOUR].uploadColourData(colourData);
            }
        });

        nextDataSetButton = new Button(homeButtonFont, "Next Data Set");
        nextDataSetButton.setPosition(Crispin.getSurfaceWidth() - 10 - 200, 450);
        nextDataSetButton.setSize(200, 200);
        nextDataSetButton.setBorder(new Border(Colour.BLACK));
        nextDataSetButton.setColour(Colour.LIGHT_GREY);
        nextDataSetButton.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                rendererIndex++;
                if(rendererIndex >= NUM_RENDERERS) {
                    rendererIndex = 0;
                }
                typeText.setText("Renderer: " + dataTypeToString(rendererIndex));
            }
        });
    }

    private String dataTypeToString(int dataSet) {
        switch (dataSet) {
            case RENDER_UNIQUE_COLOUR_TEXTURE_LIGHTING:
                return "Lighting, Texture, Unique Colour";
            case RENDER_TEXTURE_LIGHTING:
                return "Lighting, Texture";
            case RENDER_COLOUR_LIGHTING:
                return "Lighting, Colour";
            case RENDER_UNIQUE_COLOUR_LIGHTING:
                return "Lighting, Unique Colour";
            case RENDER_UNIQUE_COLOUR_TEXTURE:
                return "Texture, Unique Colour";
            case RENDER_TEXTURE:
                return "Texture only";
            case RENDER_COLOUR:
                return "Colour only";
            case RENDER_UNIQUE_COLOUR:
                return "Unique Colour only";
        }
        return "Unknown";
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
        renderers[rendererIndex].render(camera);

        typeText.draw(uiCamera);
        instanceCount.draw(uiCamera);
        fpsText.draw(uiCamera);
        homeButton.draw(uiCamera);
        regenButton.draw(uiCamera);
        nextDataSetButton.draw(uiCamera);
    }

    @Override
    public void touch(int type, Vec2 position) {
        switch (type) {
            case MotionEvent.ACTION_DOWN:
                touchDownPos = new Vec2(position);
                break;
            case MotionEvent.ACTION_MOVE:
                if(touchDownPos != null) {
                    camera.translate(Geometry.getVectorBetween(touchDownPos, position).x, Geometry.getVectorBetween(touchDownPos, position).y);
                    touchDownPos = new Vec2(position);
                }
                break;
            case MotionEvent.ACTION_UP:
                touchDownPos = null;
                break;
        }
    }
}