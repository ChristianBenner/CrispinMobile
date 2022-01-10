package com.crispin.demos.scenes;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.Geometry.Point3D;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Entities.Light;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.crispin.crispinmobile.Rendering.Utilities.Material;
import com.crispin.crispinmobile.UserInterface.Font;
import com.crispin.crispinmobile.UserInterface.Text;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.crispin.demos.R;

import java.util.HashMap;
import java.util.HashSet;

public class LightingShaderAttributesDemoScene extends Scene
{
    // Amount that the information text should fade in per cycle
    private final float INFO_TEXT_FADE_STEP_AMOUNT = 0.03f;

    private final long WELCOME_SCENE_DURATION_MS = 3000;
    private final long WELCOME_SCENE_MODEL_APPEAR_START_MS = 2000;

    private final float LIGHT_INTENSITY_STEP_AMOUNT = 0.005f;

    private final float LIGHT_INTENSITY_CEILING = 1.0f;

    private final float LIGHT_SPECULAR_STEP_AMOUNT = 0.02f;

    private final float LIGHT_SPECULAR_CEILING = 4.0f;

    private final float LIGHT_AMBIENT_STEP_AMOUNT = 0.002f;

    private final float LIGHT_AMBIENT_CEILING = 0.2f;

    private final float POSITIONING_SCENE_DURATION_MS = 4000;

    private final int STAGE_INTRODUCTION = 0;
    private final int STAGE_INTENSITY_TEST = 1;
    private final int STAGE_AMBIENT_TEST = 2;
    private final int STAGE_POSITIONING_TEST = 3;
    private final int STAGE_SPECULAR_TEST = 4;
    private final int STAGE_COLOUR_TEST = 5;
    private final int STAGE_MAX = 6;

    // A model for our torus (donut) shape
    private Model torus;

    // A model for a light bulb
    private Model lightBulb;

    // Camera for 3D model rendering
    private Camera3D modelCamera;

    // Camera for user interface rendering
    private Camera2D uiView;

    // Font for information text
    private Font informationFont;

    // Should the text fade in
    private boolean fadeInInformationText;

    // Should the text fade out
    private boolean fadeOutInformationText;

    // Light object containing position and colour data
    private Light light;

    // Group of lights as a HashSet is required to pass to the renderer for lighting. Even if there
    // is only one. This is because internally, the engines support multiple
    private HashSet<Light> lightGroup;

    private int currentStage;

    private long stageStartMs;

    private HashMap<Integer, Text> stageInfoText;

    private float lightXCount;
    private float lightZCount;
    private float redColourRad;
    private float greenColourRad;
    private float blueColourRad;
    private boolean moveToPosition;

    public LightingShaderAttributesDemoScene() {
        Crispin.setBackgroundColour(Colour.BLACK);

        currentStage = STAGE_INTRODUCTION;
        stageStartMs = System.currentTimeMillis();

        uiView = new Camera2D();
        informationFont = new Font(R.raw.aileron_regular, 72);

        stageInfoText = new HashMap<>();
        putStageText(stageInfoText, STAGE_INTRODUCTION, "Welcome to the lighting demo");
        putStageText(stageInfoText, STAGE_INTENSITY_TEST, "Intensity!");
        putStageText(stageInfoText, STAGE_SPECULAR_TEST, "Specular Glints!");
        putStageText(stageInfoText, STAGE_AMBIENT_TEST, "Ambience!");
        putStageText(stageInfoText, STAGE_COLOUR_TEST, "Colour!");
        putStageText(stageInfoText, STAGE_POSITIONING_TEST, "Positioning!");

        fadeInInformationText = true;
        fadeOutInformationText = false;

        light = new Light();
        light.setAmbienceStrength(0.0f);
        light.setIntensity(0.0f);
        light.setSpecularStrength(0.0f);
        lightXCount = 0.0f;
        lightZCount = 0.0f;
        redColourRad = (float)Math.PI / 2.0f;
        greenColourRad = 0.0f;
        blueColourRad = (float)Math.PI / 2.0f;
        moveToPosition = true;

        lightGroup = new HashSet<>();
        lightGroup.add(light);

        // Load the torus donut shape used to demo lighting
        Material torusWoodMaterial = new Material(R.drawable.tiledwood16);
        torusWoodMaterial.setSpecularMap(TextureCache.loadTexture(R.drawable.tiledwood16_specular));
        torusWoodMaterial.setUvMultiplier(16.0f, 4.0f);
        ThreadedOBJLoader.loadModel(R.raw.torus_uv, loadListener -> {
            this.torus = loadListener;
            this.torus.setMaterial(torusWoodMaterial);
            this.torus.setColour(1.0f, 0.5f, 0.31f);
            this.torus.translate(0.0f, 0.0f, 0.0f);
            this.torus.setAlpha(0.0f);
        });

        // Load the light bulb model (used to show light position)
        Material lightBulbMaterial = new Material(R.drawable.lightbulbtex);
        ThreadedOBJLoader.loadModel(R.raw.lightbulb, loadListener -> {
            this.lightBulb = loadListener;
            this.lightBulb.setMaterial(lightBulbMaterial);
            this.lightBulb.setScale(0.3f);
            this.lightBulb.setPosition(0.0f, 1.0f, 0.0f);
            this.lightBulb.setAlpha(0.0f);
        });

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera3D();
        modelCamera.setPosition(new Point3D(0.0f, 1.0f, 3.0f));
    }

    private void putStageText(HashMap<Integer, Text> textHashMap, int stage, String text) {
        Text stageText = new Text(informationFont, text, true, true,
                Crispin.getSurfaceWidth());
        stageText.setColour(Colour.ORANGE);
        stageText.setPosition(0.0f, Crispin.getSurfaceHeight() * 0.8f);
        stageText.setOpacity(0.0f);
        stageInfoText.put(stage, stageText);
    }

    private void nextStage() {
        currentStage++;
        if(currentStage >= STAGE_MAX) {
            currentStage = 0;
        }
        stageStartMs = System.currentTimeMillis();
        fadeInInformationText = true;

        if(stageInfoText.containsKey(currentStage)) {
            stageInfoText.get(currentStage).setOpacity(0.0f);
        }
    }

    private long currentStageDurationMs() {
        return System.currentTimeMillis() - stageStartMs;
    }

    @Override
    public void update(float deltaTime) {
        updateTextFade(deltaTime);

        switch (currentStage) {
            case STAGE_INTRODUCTION:
                if(currentStageDurationMs() >= WELCOME_SCENE_MODEL_APPEAR_START_MS) {
                    torus.setAlpha(1.0f);
                    lightBulb.setAlpha(1.0f);
                }

                if(currentStageDurationMs() >= WELCOME_SCENE_DURATION_MS) {
                    nextStage();
                }
                break;
            case STAGE_INTENSITY_TEST:
                light.setIntensity(light.getIntensity() + (LIGHT_INTENSITY_STEP_AMOUNT * deltaTime));
                if(light.getIntensity() >= LIGHT_INTENSITY_CEILING) {
                    light.setIntensity(LIGHT_INTENSITY_CEILING);
                    nextStage();
                }
                break;
            case STAGE_AMBIENT_TEST:
                light.setAmbienceStrength(light.getAmbienceStrength() +
                        (LIGHT_AMBIENT_STEP_AMOUNT * deltaTime));
                if(light.getAmbienceStrength() >= LIGHT_AMBIENT_CEILING) {
                    light.setAmbienceStrength(LIGHT_AMBIENT_CEILING);
                    nextStage();
                }
                break;
            case STAGE_POSITIONING_TEST:
                if(moveToPosition) {
                    lightZCount += 0.01f * deltaTime;
                    float lightZ = (float)Math.sin(lightZCount);
                    if(lightZCount >= (float)Math.PI / 2.0f) {
                        lightZ = 1.0f;
                        moveToPosition = false;
                    }
                    lightBulb.setPosition(0.0f, 1.0f, lightZ);
                } else {
                    updateBulbPosition(deltaTime);
                }

                if(currentStageDurationMs() > POSITIONING_SCENE_DURATION_MS) {
                    nextStage();
                }
                break;
            case STAGE_SPECULAR_TEST:
                updateBulbPosition(deltaTime);
                light.setSpecularStrength(light.getSpecularStrength() +
                        (LIGHT_SPECULAR_STEP_AMOUNT * deltaTime));
                if(light.getSpecularStrength() >= LIGHT_SPECULAR_CEILING) {
                    light.setSpecularStrength(LIGHT_SPECULAR_CEILING);
                    nextStage();
                }
                break;
            case STAGE_COLOUR_TEST:
                updateBulbPosition(deltaTime);
                redColourRad += 0.01f * deltaTime;
                greenColourRad += 0.02f * deltaTime;
                blueColourRad -= 0.01f * deltaTime;
                float redChannel = ((float)Math.sin(redColourRad) + 1.0f) * 0.7f;
                float greenChannel = ((float)Math.sin(greenColourRad) + 1.0f) * 0.7f;
                float blueChannel = ((float)Math.sin(blueColourRad) + 1.0f) * 0.7f;
                light.setColour(redChannel, greenChannel, blueChannel);
                break;
        }

        light.setPosition(lightBulb.getPosition());
    }

    @Override
    public void render() {
        if(torus != null) {
            lightBulb.render(modelCamera);
        }

        if(torus != null) {
            torus.render(modelCamera, lightGroup);
        }

        if(stageInfoText.containsKey(currentStage)) {
            stageInfoText.get(currentStage).draw(uiView);
        }
    }

    @Override
    public void touch(int type, Point2D position) {

    }

    private void updateBulbPosition(float deltaTime) {
        lightXCount += 0.03f * deltaTime;
        lightZCount += 0.03f * deltaTime;
        float lightX = (float)Math.sin(lightXCount);
        float lightZ = (float)Math.sin(lightZCount);
        lightBulb.setPosition(lightX, 1.0f, lightZ);
    }

    private void updateTextFade(float deltaTime) {
        if(stageInfoText.containsKey(currentStage)) {
            final Text currentText = stageInfoText.get(currentStage);
            if(fadeInInformationText) {
                currentText.setOpacity(currentText.getOpacity() +
                        (INFO_TEXT_FADE_STEP_AMOUNT * deltaTime));
                if(currentText.getOpacity() > 1.0f) {
                    currentText.setOpacity(1.0f);
                    fadeInInformationText = false;
                }
            } else if (fadeOutInformationText) {
                currentText.setOpacity(currentText.getOpacity() -
                        (INFO_TEXT_FADE_STEP_AMOUNT * deltaTime));
                if(currentText.getOpacity() < 0.0f) {
                    currentText.setOpacity(0.0f);
                    fadeOutInformationText = false;
                }
            }
        }
    }
}
