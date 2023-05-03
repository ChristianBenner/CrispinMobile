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
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.InstanceRenderer;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.UserInterface.Border;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Font;
import com.crispin.crispinmobile.UserInterface.Text;
import com.crispin.crispinmobile.UserInterface.TouchEvent;
import com.crispin.crispinmobile.Utilities.OBJModelLoader;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.demos.R;

import java.util.Random;

public class InstancingLightingTest extends Scene {
    private final int TEXT_PADDING = 10;

    // Rendering
    private InstanceRenderer renderer;
    private Camera camera;

    // UI
    private Camera2D uiCamera;
//    private Button homeButton;
//    private Text fpsText;
    private Vec2 touchDownPos;
    private Model light;

    public InstancingLightingTest() {
        Crispin.setBackgroundColour(Colour.BLACK);
        touchDownPos = new Vec2();

        initUI();

        camera = new Camera();
        camera.setPosition(0.0f, 0.0f, 7.0f);

        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, 0.0f);
        Matrix.rotateM(modelMatrix, 0, 45, 1f, 1f, 1f);
        Matrix.scaleM(modelMatrix, 0, 1f, 1f, 1f);

        float[] colourData = new float[]{
            1.0f,
            1.0f,
            1.0f,
            1.0f
        };

        renderer = new InstanceRenderer(new CubeMesh(true, true), true, modelMatrix, colourData);
        Material material = new Material();
        material.setTexture(TextureCache.loadTexture(R.drawable.floor_base));
       // material.setDiffuseMap(TextureCache.loadTexture(R.drawable.stacked_rock_cliff_albedo));
        material.setSpecularMap(TextureCache.loadTexture(R.drawable.floor_spec));
        material.setNormalMap(TextureCache.loadTexture(R.drawable.floor_normal));
        renderer.setMaterial(material);

        LightGroup lightGroup = new LightGroup();
        PointLight pl = new PointLight(4.0f, 0.0f, -1.0f);
        pl.setSpecularStrength(3.0f);
        pl.setDiffuseStrength(1.3f);
        pl.setAmbientStrength(1.0f);
        lightGroup.addLight(pl);
        renderer.setLightGroup(lightGroup);


        light = new Model(new CubeMesh(false, false));
        light.setColour(Colour.YELLOW);
        light.setScale(0.02f);
        light.setPosition(pl.getPosition());
    }

    private void initUI() {
        uiCamera = new Camera2D();

        Font font = new Font(R.raw.aileron_regular, 48);
        int textOffset = TEXT_PADDING + font.getSize();

//        fpsText = new Text(font, Crispin.getFps() + "FPS", false, true, Crispin.getSurfaceWidth() - 20);
//        fpsText.setPosition(10, Crispin.getSurfaceHeight() - (textOffset));
//        fpsText.setColour(Colour.WHITE);
//
//        Font homeButtonFont = new Font(R.raw.aileron_regular, 36);
//
//        homeButton = new Button(homeButtonFont, "Back");
//        homeButton.setPosition(Crispin.getSurfaceWidth() - 10 - 200, 10);
//        homeButton.setSize(200, 200);
//        homeButton.setBorder(new Border(Colour.BLACK));
//        homeButton.setColour(Colour.LIGHT_GREY);
//        homeButton.addTouchListener(e -> {
//            if(e.getEvent() == TouchEvent.Event.CLICK) {
//                Crispin.setScene(InstancingDemoSelectionScene::new);
//            }
//        });
    }

    float angle = 45.0f;

    @Override
    public void update(float deltaTime) {
//        fpsText.setText(Crispin.getFps() + "FPS");

        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, 0.0f);
        Matrix.rotateM(modelMatrix, 0, angle, 1f, 1f, 1f);
        Matrix.scaleM(modelMatrix, 0, 1f, 1f, 1f);
        angle += 0.2f * deltaTime;
        renderer.uploadModelMatrices(modelMatrix);
    }

    @Override
    public void render() {
        renderer.render(camera);
        light.render(camera);
//        fpsText.draw(uiCamera);
//        homeButton.draw(uiCamera);
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