package com.crispin.demos.Scenes.InstancingDemos;

import android.opengl.Matrix;
import android.view.MotionEvent;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Scale3D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.DefaultMesh.CubeMesh;
import com.crispin.crispinmobile.Rendering.DefaultMesh.SquareMesh;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Models.ModelProperties;
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

public class InstancingDemo3D extends Scene {
    private final Scale3D GENERATION_AREA_SIZE = new Scale3D(30.0f, 30.0f, 30.0f);
    private final int NUM_FLOATS_MATRIX = 16;
    private final int NUM_FLOATS_COLOUR = 4;

    private final int TEXT_PADDING = 10;
    private final int NUM_INSTANCES = 30000;

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
    private Camera camera;
    private int instanceRenderCount;
    private int rendererIndex;
    private PointLight[] zipLights;
    private Random random;

    // UI
    private Camera2D uiCamera;
    private Button homeButton;
    private Button regenButton;
    private Button nextDataSetButton;
    private Text instanceCount;
    private Text typeText;
    private Text fpsText;
    private Vec2 touchDownPos;

    public InstancingDemo3D() {
        Crispin.setBackgroundColour(Colour.BLACK);

        instanceRenderCount = NUM_INSTANCES;
        rendererIndex = RENDER_UNIQUE_COLOUR_TEXTURE_LIGHTING;
        touchDownPos = new Vec2();

        initUI();

        camera = new Camera();

        float[] modelTransformations = generateRandomModelTransformations();
        float[] colourData = generateRandomColours();

        renderers = new InstanceRenderer[NUM_RENDERERS];
        // Initialise the lighting renderers
        renderers[RENDER_UNIQUE_COLOUR_TEXTURE_LIGHTING] = new InstanceRenderer(new CubeMesh(true, true), true, modelTransformations, colourData);
        Material material = new Material();
        material.setTexture(TextureCache.loadTexture(R.drawable.crate_texture));
//        material.setDiffuseMap(TextureCache.loadTexture(R.drawable.crate_hd_texture));
//        material.setSpecularMap(TextureCache.loadTexture(R.drawable.crate_hd_specular));
        renderers[RENDER_UNIQUE_COLOUR_TEXTURE_LIGHTING].setMaterial(material);

        renderers[RENDER_TEXTURE_LIGHTING] = new InstanceRenderer(new CubeMesh(true, true), true, modelTransformations);
        renderers[RENDER_TEXTURE_LIGHTING].setTexture(TextureCache.loadTexture(R.drawable.crate_texture));
        renderers[RENDER_TEXTURE_LIGHTING].setGlobalColour(Colour.RED);

        renderers[RENDER_COLOUR_LIGHTING] = new InstanceRenderer(new CubeMesh(false, true), true, modelTransformations);
        renderers[RENDER_COLOUR_LIGHTING].setGlobalColour(Colour.BLUE);

        renderers[RENDER_UNIQUE_COLOUR_LIGHTING] = new InstanceRenderer(new CubeMesh(false, true), true, modelTransformations, colourData);

        // Initialise the non-lighting renderers
        renderers[RENDER_UNIQUE_COLOUR_TEXTURE] = new InstanceRenderer(new CubeMesh(true, false), false, modelTransformations, colourData);
        renderers[RENDER_UNIQUE_COLOUR_TEXTURE].setTexture(TextureCache.loadTexture(R.drawable.crate_texture));

        renderers[RENDER_TEXTURE] = new InstanceRenderer(new CubeMesh(true, false), false, modelTransformations);
        renderers[RENDER_TEXTURE].setTexture(TextureCache.loadTexture(R.drawable.crate_texture));
        renderers[RENDER_TEXTURE].setGlobalColour(Colour.RED);

        renderers[RENDER_COLOUR] = new InstanceRenderer(new CubeMesh(false, false), false, modelTransformations);
        renderers[RENDER_COLOUR].setGlobalColour(Colour.BLUE);

        renderers[RENDER_UNIQUE_COLOUR] = new InstanceRenderer(new CubeMesh(false, false), false, modelTransformations, colourData);

        LightGroup lightGroup = new LightGroup();
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

        renderers[RENDER_UNIQUE_COLOUR_TEXTURE_LIGHTING].setLightGroup(lightGroup);
        renderers[RENDER_TEXTURE_LIGHTING].setLightGroup(lightGroup);
        renderers[RENDER_COLOUR_LIGHTING].setLightGroup(lightGroup);
        renderers[RENDER_UNIQUE_COLOUR_LIGHTING].setLightGroup(lightGroup);
    }

    private float[] generateRandomModelTransformations() {
        Random r = new Random();
        float[] modelMatrices = new float[NUM_FLOATS_MATRIX * NUM_INSTANCES];
        for(int i = 0; i < NUM_INSTANCES; i++) {
            int matrixOffset = NUM_FLOATS_MATRIX * i;
            float x = (r.nextFloat() * GENERATION_AREA_SIZE.x) - (GENERATION_AREA_SIZE.x/2.0f);
            float y = (r.nextFloat() * GENERATION_AREA_SIZE.y) - (GENERATION_AREA_SIZE.y/2.0f);
            float z = (r.nextFloat() * GENERATION_AREA_SIZE.z) - (GENERATION_AREA_SIZE.z/2.0f);
            float rotateAngle = r.nextFloat() * 360.0f;
            float scale = (1.0f + r.nextFloat()) / 32f;

            Matrix.setIdentityM(modelMatrices, matrixOffset);
            Matrix.translateM(modelMatrices, matrixOffset, x, y, z);
            Matrix.rotateM(modelMatrices, matrixOffset, rotateAngle, 0.4f, 0.6f, 0.8f);
            Matrix.scaleM(modelMatrices, matrixOffset, scale, scale, scale);
        }

        return modelMatrices;
    }

    private float[] generateRandomColours() {
        Random r = new Random();
        float[] colourData = new float[NUM_FLOATS_COLOUR * NUM_INSTANCES];
        for(int i = 0; i < NUM_INSTANCES; i++) {
            int colourOffset = NUM_FLOATS_COLOUR * i;
            colourData[colourOffset] = r.nextFloat();
            colourData[colourOffset + 1] = r.nextFloat();
            colourData[colourOffset + 2] = r.nextFloat();
            colourData[colourOffset + 3] = 1.0f;
        }

        return colourData;
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
                float[] modelTransformations = generateRandomModelTransformations();
                float[] colourData = generateRandomColours();
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
//        float z = ((float)Math.cos(n + (Math.PI / 2.0f)) * 5.0f) - 5.0f;
//        n += 0.02f * deltaTime;
//        pointLight.setPosition(0.0f, 0.0f, z);
        for(int i = 0; i < 10; i++) {
            zipLights[i].translate(0.0f, 0.0f, -0.6f);
            if(zipLights[i].getPosition().z <= -100.0f) {
                setRandomSpawn(zipLights[i]);
            }
        }
    }

    private void setRandomSpawn(PointLight pointLight) {
        float rx = (random.nextFloat() * 18.0f) - 9.0f;
        float ry = (random.nextFloat() * 10.0f) - 5.0f;
        float rz = 40.0f + (random.nextFloat() * 40.0f);
        pointLight.setPosition(rx, ry, rz);
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
                    camera.translate(0.0f, 0.0f, Geometry.getVectorBetween(touchDownPos, position).y / 50.0f);
                    touchDownPos = new Vec2(position);
                }
                break;
            case MotionEvent.ACTION_UP:
                touchDownPos = null;
                break;
        }
    }
}