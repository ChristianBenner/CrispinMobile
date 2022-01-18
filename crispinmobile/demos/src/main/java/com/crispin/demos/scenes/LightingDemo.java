package com.crispin.demos.scenes;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.Geometry.Point3D;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Shaders.LightingShader;
import com.crispin.crispinmobile.Rendering.Shaders.LightingTextureShader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.Rendering.Utilities.Material;
import com.crispin.crispinmobile.UserInterface.Border;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Font;
import com.crispin.crispinmobile.UserInterface.Text;
import com.crispin.crispinmobile.UserInterface.TouchEvent;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.crispin.demos.R;

import java.util.ArrayList;
import java.util.HashMap;

public class LightingDemo extends Scene
{
    // Amount that the information text should fade in per cycle
    private final float INFO_TEXT_FADE_STEP_AMOUNT = 0.03f;

    private final long WELCOME_SCENE_DURATION_MS = 4000L;

    private final long WELCOME_SCENE_MODEL_APPEAR_START_MS = 2000L;

    private final long LIGHT_INTENSITY_SCENE_DURATION_MS = 3000L;

    private final float LIGHT_INTENSITY_CEILING = 1.0f;

    private final long LIGHT_AMBIENT_SCENE_DURATION_MS = 3000L;

    private final float LIGHT_AMBIENT_CEILING = 0.2f;

    private final long LIGHT_SPECULAR_SCENE_DURATION_MS = 3000L;

    private final float LIGHT_SPECULAR_CEILING = 4.0f;

    private final long POSITIONING_SCENE_DURATION_MS = 4000L;

    private final long COLOUR_SCENE_DURATION_MS = 8000L;

    private final long RESET_LIGHT_SCENE_DURATION_MS = 4000L;

    private final float BUTTON_PADDING = 20.0f;

    private final int STAGE_INTRODUCTION = 0;
    private final int STAGE_INTENSITY_TEST = 1;
    private final int STAGE_AMBIENT_TEST = 2;
    private final int STAGE_POSITIONING_TEST = 3;
    private final int STAGE_SPECULAR_TEST = 4;
    private final int STAGE_COLOUR_TEST = 5;
    private final int STAGE_RESET_POSITION = 6;
    private final int STAGE_RESET_LIGHT = 7;
    private final int STAGE_MAX = 8;

    // A model for our torus (donut) shape
    private Model torus;

    // A model for a light bulb
    private Model lightBulb;

    // Camera for 3D model rendering
    private Camera3D modelCamera;

    // Camera for user interface rendering
    private Camera2D uiView;

    // Font for the information text
    private Font informationFont;

    // Should the text fade in
    private boolean fadeInInformationText;

    // Light object containing position and colour data
    private PointLight pointLight;

    private LightGroup lightGroup;

    private int currentStage;

    private long stageStartMs;

    private HashMap<Integer, Text> stageInfoText;

    private Button toggleShader;
    private boolean useMaterialShader;

    private float lightXCount;
    private float lightZCount;
    private float redColourRad;
    private float greenColourRad;
    private float blueColourRad;
    private boolean moveToPosition;
    private boolean resetX;
    private boolean resetZ;

    public LightingDemo() {
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
        putStageText(stageInfoText, STAGE_RESET_POSITION, "Resetting Position");
        putStageText(stageInfoText, STAGE_RESET_LIGHT, "Turning the lights off...");

        fadeInInformationText = true;

        pointLight = new PointLight();
        pointLight.setAmbientStrength(0.0f);
        pointLight.setDiffuseStrength(0.0f);
        pointLight.setSpecularStrength(0.0f);
        resetLightingValues();

        lightGroup = new LightGroup();
        lightGroup.addPointLight(pointLight);

        // Load the torus donut shape used to demo lighting
        Material torusWoodMaterial = new Material(R.drawable.tiledwood16);
        useMaterialShader = true;
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
        Material lightBulbMaterial = new Material(R.drawable.lightbulb_texture);
        lightBulbMaterial.setSpecularMap(TextureCache.loadTexture(R.drawable.lightbulb_specular));
        ThreadedOBJLoader.loadModel(R.raw.lightbulb_flipped_normals, loadListener -> {
            this.lightBulb = loadListener;
            this.lightBulb.setMaterial(lightBulbMaterial);
            this.lightBulb.setScale(0.3f);
            this.lightBulb.setPosition(0.0f, 1.0f, 0.0f);
            this.lightBulb.setAlpha(0.0f);
        });

        toggleShader = new Button(new Font(R.raw.aileron_regular, 48),
                "Toggle Shader");
        toggleShader.setPosition(Crispin.getSurfaceWidth() - BUTTON_PADDING -
                toggleShader.getWidth(), Crispin.getSurfaceHeight() - BUTTON_PADDING -
                toggleShader.getHeight());
        toggleShader.setAlpha(0.0f);
        toggleShader.setTextColour(Colour.ORANGE);
        toggleShader.setColour(Colour.BLACK);
        toggleShader.setBorder(new Border(Colour.ORANGE, 5));
        toggleShader.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                useMaterialShader = !useMaterialShader;
                if(torus != null) {
                    if(useMaterialShader) {
                        torus.useCustomShader(new LightingTextureShader());
                    } else {
                        torus.useCustomShader(new LightingShader());
                    }
                }

                if(lightBulb != null) {
                    if(useMaterialShader) {
                        lightBulb.useCustomShader(new LightingTextureShader());
                    } else {
                        lightBulb.useCustomShader(new LightingShader());
                    }
                }
            }
        });

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera3D();
        modelCamera.setPosition(new Point3D(0.0f, 1.0f, 3.0f));
    }

    private void putStageText(HashMap<Integer, Text> textHashMap, int stage, String text) {
        Text stageText = new Text(informationFont, text,
                true, true, Crispin.getSurfaceWidth());
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
                pointLight.setDiffuseStrength(((float)currentStageDurationMs() /
                        (float)LIGHT_INTENSITY_SCENE_DURATION_MS) * LIGHT_INTENSITY_CEILING);

                if(currentStageDurationMs() >= LIGHT_INTENSITY_SCENE_DURATION_MS) {
                    pointLight.setDiffuseStrength(LIGHT_INTENSITY_CEILING);
                    nextStage();
                }
                break;
            case STAGE_AMBIENT_TEST:
                pointLight.setAmbientStrength(((float)currentStageDurationMs() /
                        (float)LIGHT_AMBIENT_SCENE_DURATION_MS) * LIGHT_AMBIENT_CEILING);

                if(currentStageDurationMs() >= LIGHT_AMBIENT_SCENE_DURATION_MS) {
                    pointLight.setAmbientStrength(LIGHT_AMBIENT_CEILING);
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
                pointLight.setSpecularStrength(((float)currentStageDurationMs() /
                        (float)LIGHT_SPECULAR_SCENE_DURATION_MS) * LIGHT_SPECULAR_CEILING);

                if(currentStageDurationMs() >= LIGHT_SPECULAR_SCENE_DURATION_MS) {
                    pointLight.setSpecularStrength(LIGHT_SPECULAR_CEILING);
                    nextStage();
                }
                break;
            case STAGE_COLOUR_TEST:
                updateBulbPosition(deltaTime);
                redColourRad += 0.01f * deltaTime;
                if(redColourRad % Math.PI > 0.0f) {
                    greenColourRad += 0.03f * deltaTime;
                }
                if(redColourRad % Math.PI / 2.0f > 0.0f) {
                    blueColourRad += 0.02f * deltaTime;
                }
                float redChannel = ((float)Math.cos(redColourRad) + 1.0f) / 2.0f;
                float greenChannel = ((float)Math.cos(greenColourRad) + 1.0f) / 2.0f;
                float blueChannel = ((float)Math.cos(blueColourRad) + 1.0f) / 2.0f;
                pointLight.setColour(redChannel, greenChannel, blueChannel);

                if(currentStageDurationMs() >= COLOUR_SCENE_DURATION_MS) {
                    nextStage();
                }
                break;
            case STAGE_RESET_POSITION:
                if(resetX) {
                    if(lightXCount % (float)Math.PI < (float)Math.PI / 2.0f) {
                        lightXCount -= 0.01f * deltaTime;
                        if((float)Math.sin(lightXCount) <= 0.0f) {
                            resetX = false;
                            lightXCount = 0.0f;
                        }
                    } else {
                        lightXCount += 0.01f * deltaTime;
                        if((float)Math.sin(lightXCount) >= 0.0f) {
                            resetX = false;
                            lightXCount = 0.0f;
                        }
                    }
                }

                if(resetZ) {
                    if(lightZCount % (float)Math.PI < (float)Math.PI / 2.0f) {
                        lightZCount -= 0.01f * deltaTime;
                        if((float)Math.sin(lightZCount) <= 0.0f) {
                            resetZ = false;
                            lightZCount = 0.0f;
                        }
                    } else {
                        lightZCount += 0.01f * deltaTime;
                        if((float)Math.sin(lightZCount) >= 0.0f) {
                            resetZ = false;
                            lightZCount = 0.0f;
                        }
                    }
                }

                if(lightXCount == 0.0f && lightZCount == 0.0f) {
                    nextStage();
                }

                lightBulb.setPosition((float)Math.sin(lightXCount), 1.0f,
                        (float)Math.sin(lightZCount));
                break;
            case STAGE_RESET_LIGHT:
                pointLight.setDiffuseStrength(LIGHT_INTENSITY_CEILING - (((float)currentStageDurationMs() /
                        (float)LIGHT_INTENSITY_SCENE_DURATION_MS) * LIGHT_INTENSITY_CEILING));
                pointLight.setAmbientStrength(LIGHT_AMBIENT_CEILING -
                        (((float)currentStageDurationMs() /
                                (float)LIGHT_AMBIENT_SCENE_DURATION_MS) * LIGHT_AMBIENT_CEILING));
                pointLight.setSpecularStrength(LIGHT_SPECULAR_CEILING -
                        (((float)currentStageDurationMs() /
                        (float)LIGHT_SPECULAR_SCENE_DURATION_MS) * LIGHT_SPECULAR_CEILING));

                if(currentStageDurationMs() >= RESET_LIGHT_SCENE_DURATION_MS) {
                    resetLightingValues();
                    pointLight.setDiffuseStrength(0.0f);
                    pointLight.setAmbientStrength(0.0f);
                    pointLight.setSpecularStrength(0.0f);
                    pointLight.setColour(1.0f, 1.0f, 1.0f);
                    nextStage();
                }
                break;
        }

        pointLight.setPosition(lightBulb.getPosition());
    }

    @Override
    public void render() {
        if(torus != null) {
            lightBulb.render(modelCamera, lightGroup);
        }

        if(torus != null) {
            torus.render(modelCamera, lightGroup);
        }

        if(stageInfoText.containsKey(currentStage)) {
            stageInfoText.get(currentStage).draw(uiView);
        }

        toggleShader.draw(uiView);
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
            }
        }
    }

    private void resetLightingValues() {
        lightXCount = 0.0f;
        lightZCount = 0.0f;
        redColourRad = 0.0f;
        greenColourRad = 0.0f;
        blueColourRad = 0.0f;
        moveToPosition = true;
        resetX = true;
        resetZ = true;
    }
}
