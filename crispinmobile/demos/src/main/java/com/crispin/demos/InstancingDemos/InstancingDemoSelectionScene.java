package com.crispin.demos.InstancingDemos;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Font;
import com.crispin.crispinmobile.UserInterface.LinearLayout;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.Text;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.demos.BackgroundShader;
import com.crispin.demos.R;
import com.crispin.demos.DemoMasterScene;
import com.crispin.demos.Util;

public class InstancingDemoSelectionScene extends Scene {
    private final float SURFACE_WIDTH = Crispin.getSurfaceWidth();
    private final float SURFACE_HEIGHT = Crispin.getSurfaceHeight();
    private final float PADDING = 20.0f;
    private final float NUM_BUTTONS_ROW = 3;
    private final float BUTTON_SPACE = SURFACE_WIDTH - ((NUM_BUTTONS_ROW + 1) * PADDING);
    private final float BUTTON_SIZE = BUTTON_SPACE / NUM_BUTTONS_ROW;

    private final Camera2D camera2D;
    private final LinearLayout linearLayout;
    private final Text selectDemoText;
    private Square background;
    private BackgroundShader backgroundShader;
    private float time;
    private final Square titleBackground;

    public InstancingDemoSelectionScene() {
        Crispin.setBackgroundColour(Colour.DARK_GREY);
        camera2D = new Camera2D(0.0f, 0.0f, SURFACE_WIDTH, SURFACE_HEIGHT);

        backgroundShader = new BackgroundShader(R.raw.background_vert, R.raw.background_two_frag);
        backgroundShader.setCenter(Crispin.getSurfaceWidth() / 2f, Crispin.getSurfaceHeight() / 2f);
        time = 0.0f;

        background = new Square();
        background.setPosition(0f, 0f);
        background.setScale(Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());
        background.setShader(backgroundShader);

        linearLayout = new LinearLayout(new Vec2(0.0f, 0.0f), new Scale2D(SURFACE_WIDTH, SURFACE_HEIGHT));
        linearLayout.setPadding(new Scale2D(PADDING, PADDING));
        linearLayout.add(Util.createDemoButton(BUTTON_SIZE, "Back", DemoMasterScene::new));
        linearLayout.add(Util.createDemoButton(BUTTON_SIZE, "Instance Rendering", InstancingDemo::new));
        linearLayout.add(Util.createDemoButton(BUTTON_SIZE, "Instance Vs Batch Rendering", InstancingVsBatchDemo::new));
        linearLayout.add(Util.createDemoButton(BUTTON_SIZE, "Instance Rendering 2D", InstancingDemo2D::new));
        linearLayout.add(Util.createDemoButton(BUTTON_SIZE, "Instance Rendering 3D", InstancingDemo3D::new));
        linearLayout.add(Util.createDemoButton(BUTTON_SIZE, "Instance Lighting Test", InstancingLightingTest::new));

        Font titleFont = new Font(R.raw.aileron_bold, 72);
        selectDemoText = new Text(titleFont, "Instance Rendering Demos", true, true, SURFACE_WIDTH);
        selectDemoText.setColour(Colour.WHITE);
        selectDemoText.setPosition(0.0f, SURFACE_HEIGHT - selectDemoText.getHeight() - PADDING - PADDING);

        titleBackground = new Square(false);
        titleBackground.setColour(new Colour(0f, 0f, 0f, 0.3f));
        titleBackground.setPosition((Crispin.getSurfaceWidth() / 2f) - (selectDemoText.getWidth() / 2f) - PADDING, selectDemoText.getPosition().y - PADDING);
        titleBackground.setScale(selectDemoText.getWidth() + PADDING + PADDING, selectDemoText.getHeight() + PADDING + PADDING);
    }

    @Override
    public void update(float deltaTime) {
        time += 0.01f * deltaTime;
        backgroundShader.setTime(time);
    }

    @Override
    public void render() {
        background.render(camera2D);
        linearLayout.draw(camera2D);
        titleBackground.render(camera2D);
        selectDemoText.draw(camera2D);
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {

    }
}