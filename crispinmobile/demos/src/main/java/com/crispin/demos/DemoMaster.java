package com.crispin.demos;


import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Font;
import com.crispin.crispinmobile.UserInterface.LinearLayout;
import com.crispin.crispinmobile.UserInterface.TouchEvent;
import com.crispin.crispinmobile.Utilities.FontCache;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.demos.scenes.LightingDemo;
import com.crispin.demos.scenes.MaterialDemo;
import com.crispin.demos.scenes.ObjLoadDemo;
import com.crispin.demos.scenes.SpotLightDemo;
import com.crispin.demos.scenes.TextDemo;

class DemoMasterScene extends Scene {
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
        Crispin.setBackgroundColour(Colour.LIGHT_GREY);
        camera2D = new Camera2D(0.0f, 0.0f, SURFACE_WIDTH, SURFACE_HEIGHT);
        Font titleFont = FontCache.getFont(R.raw.aileron_regular, 72);

        linearLayout = new LinearLayout(new Vec2(0.0f, 0.0f), new Scale2D(SURFACE_WIDTH, SURFACE_HEIGHT));
        linearLayout.setPadding(new Scale2D(PADDING, PADDING));
        linearLayout.add(createDemoButton("Materials", MaterialDemo::new));
        linearLayout.add(createDemoButton("Lighting", LightingDemo::new));
        linearLayout.add(createDemoButton("Object Load", ObjLoadDemo::new));
        linearLayout.add(createDemoButton("TextDemo", TextDemo::new));
        linearLayout.add(createDemoButton("SpotLight", SpotLightDemo::new));

        selectDemoText = new com.crispin.crispinmobile.UserInterface.Text(titleFont, "Select a Demo", true, true,
                SURFACE_WIDTH);
        selectDemoText.setPosition(0.0f, SURFACE_HEIGHT - selectDemoText.getHeight() - PADDING);
    }

    private Button createDemoButton(String text, final Constructor sceneConstructor) {
        Button demoButton = new Button(FontCache.getFont(R.raw.aileron_regular, 48), text);
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

public class DemoMaster extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request that the application does not have a title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide the action bar at the top of the application
        getSupportActionBar().hide();

        // Add graphical view to frame layout
        Crispin.init(this, () -> new DemoMasterScene());
    }
}

