package com.crispin.demos;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.UserInterface.Border;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.TouchEvent;
import com.crispin.crispinmobile.UserInterface.TouchListener;
import com.crispin.crispinmobile.Utilities.FontCache;
import com.crispin.crispinmobile.Utilities.Scene;

public class Util {
    public static final float BACK_BUTTON_SIZE = 200f;

    public static Button createBackButton(Scene.Constructor sceneConstructor) {
        return createBackButton(sceneConstructor, 10f, 10f);
    }

    public static Button createBackButton(Scene.Constructor sceneConstructor, float x, float y) {
        Button backButton = new Button(FontCache.getFont(R.raw.aileron_regular, 48), "Back");
        backButton.setPosition(x, y);
        backButton.setSize(BACK_BUTTON_SIZE, BACK_BUTTON_SIZE);
        backButton.setBorder(new Border(new Colour(255, 255, 255, 150), 5));
        backButton.setColour(new Colour(211, 124, 65, 150));
        backButton.setTextColour(new Colour(255, 255, 255, 150));
        backButton.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                Crispin.setScene(sceneConstructor);
            }
        });
        return backButton;
    }

    public static Button createStyledButton(String text, float x, float y, TouchListener touchListener) {
        Button button = new Button(FontCache.getFont(R.raw.aileron_regular, 36), text);
        button.setPosition(x, y);
        button.setSize(200, 200);
        button.setBorder(new Border(new Colour(255, 255, 255, 150), 5));
        button.setColour(new Colour(211, 124, 65, 150));
        button.setTextColour(new Colour(255, 255, 255, 150));
        button.addTouchListener(touchListener);
        return button;
    }

    public static Button createDemoButton(float size, String text, final Scene.Constructor sceneConstructor) {
        Button demoButton = new Button(FontCache.getFont(R.raw.aileron_regular, 48), text);
        demoButton.setBorder(new Border(new Colour(50, 50, 50, 150), 5));
        demoButton.setColour(new Colour(100, 100, 100, 150));
        demoButton.setTextColour(Colour.WHITE);
        demoButton.setSize(size, size);
        demoButton.addTouchListener(e -> {
            if (e.getEvent() == TouchEvent.Event.CLICK) {
                Crispin.setScene(sceneConstructor);
            }
        });
        return demoButton;
    }
}
