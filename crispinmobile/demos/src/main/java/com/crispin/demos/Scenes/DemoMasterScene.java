package com.crispin.demos.Scenes;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Border;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Font;
import com.crispin.crispinmobile.UserInterface.LinearLayout;
import com.crispin.crispinmobile.UserInterface.Text;
import com.crispin.crispinmobile.UserInterface.TouchEvent;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.demos.R;
import com.crispin.demos.Scenes.InstancingDemos.InstancingDemoSelectionScene;

public class DemoMasterScene extends Scene {
    private final float SURFACE_WIDTH = Crispin.getSurfaceWidth();
    private final float SURFACE_HEIGHT = Crispin.getSurfaceHeight();
    private final float PADDING = 20.0f;
    private final float NUM_BUTTONS_ROW = 3;
    private final float BUTTON_SPACE = SURFACE_WIDTH - ((NUM_BUTTONS_ROW + 1) * PADDING);
    private final float BUTTON_SIZE = BUTTON_SPACE / NUM_BUTTONS_ROW;

    private final Camera2D camera2D;
    private final LinearLayout linearLayout;
    private final com.crispin.crispinmobile.UserInterface.Text selectDemoText;

    public DemoMasterScene() {
        Crispin.setBackgroundColour(Colour.DARK_GREY);
        camera2D = new Camera2D(0.0f, 0.0f, SURFACE_WIDTH, SURFACE_HEIGHT);
        Font titleFont = new Font(R.raw.aileron_regular, 72);

        linearLayout = new LinearLayout(new Vec2(0.0f, 0.0f), new Scale2D(SURFACE_WIDTH, SURFACE_HEIGHT));
        linearLayout.setPadding(new Scale2D(PADDING, PADDING));
        linearLayout.add(createDemoButton("Materials", MaterialDemo::new));
        linearLayout.add(createDemoButton("Lighting", LightingDemo::new));
        linearLayout.add(createDemoButton("Object Load", ObjLoadDemo::new));
        linearLayout.add(createDemoButton("Text", TextDemo::new));
        linearLayout.add(createDemoButton("SpotLight", SpotLightDemo::new));
        linearLayout.add(createDemoButton("RenderBatch", RenderBatchDemo::new));
        linearLayout.add(createDemoButton("Instance Rendering Demos", InstancingDemoSelectionScene::new));
        linearLayout.add(createDemoButton("2D Demo", Demo2D::new));
        linearLayout.add(createDemoButton("Normal Map Demo", NormalMapDemo::new));

        selectDemoText = new Text(titleFont, "Select a Demo", true, true,
                SURFACE_WIDTH);
        selectDemoText.setColour(Colour.WHITE);
        selectDemoText.setPosition(0.0f, SURFACE_HEIGHT - selectDemoText.getHeight() - PADDING);
    }

    private Button createDemoButton(String text, final Constructor sceneConstructor) {
        Button demoButton = new Button(new Font(R.raw.aileron_regular, 48), text);
        demoButton.setBorder(new Border(Colour.WHITE, 5));
        demoButton.setColour(new Colour(211, 124, 65));
        demoButton.setTextColour(Colour.WHITE);
        demoButton.setSize(BUTTON_SIZE, BUTTON_SIZE);
        demoButton.addTouchListener(e -> {
            if (e.getEvent() == TouchEvent.Event.CLICK) {
                Crispin.setScene(sceneConstructor);
            }
        });
        return demoButton;
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void render() {
        linearLayout.draw(camera2D);
        selectDemoText.draw(camera2D);
    }

    @Override
    public void touch(int type, Vec2 position) {

    }
}