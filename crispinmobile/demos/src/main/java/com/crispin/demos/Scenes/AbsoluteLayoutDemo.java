package com.crispin.demos.Scenes;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Border;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.Text;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.FontCache;
import com.crispin.crispinmobile.Utilities.Scene;

public class AbsoluteLayoutDemo extends Scene {
    private final com.crispin.crispinmobile.UserInterface.AbsoluteLayout absoluteLayout;
    private final Button button;
    private final Text text;

    private final Camera2D camera;

    public AbsoluteLayoutDemo() {
        absoluteLayout = new com.crispin.crispinmobile.UserInterface.AbsoluteLayout();
        absoluteLayout.setColour(Colour.BLUE);
        absoluteLayout.setPosition(100.0f, 700.0f);
        absoluteLayout.setSize(600.0f, 600.0f);
        absoluteLayout.setBorder(new Border(Colour.BLACK, 3));

        button = new Button(FontCache.getFont(R.raw.aileron_regular, 72), "Hello");
        button.setPosition(30.0f, 30.0f);

        text = new Text(FontCache.getFont(R.raw.aileron_regular, 72), "Testing!");
        text.setPosition(30.0f, 100.0f);

        absoluteLayout.add(button);
        // absoluteLayout.add(text);

        camera = new Camera2D();
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render() {
        absoluteLayout.draw(camera);
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {

    }
}
