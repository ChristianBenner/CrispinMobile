package com.crispin.demos;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Border;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.FontCache;
import com.crispin.crispinmobile.Utilities.Scene;

public class LinearLayoutDemo extends Scene {
    private final com.crispin.crispinmobile.UserInterface.LinearLayout linearLayout;
    private final Button previousButton;
    private final Button nextButton;

    private final Camera2D camera2D;

    public LinearLayoutDemo() {
        previousButton = new Button(FontCache.getFont(R.raw.aileron_regular, 32),
                "<");
        nextButton = new Button(FontCache.getFont(R.raw.aileron_regular, 32), ">");

        linearLayout = new com.crispin.crispinmobile.UserInterface.LinearLayout(new Vec2(200.0f, 200.0f), new Scale2D(
                Crispin.getSurfaceWidth() - 400.0f, 600.0f));
        linearLayout.setColour(Colour.BLUE);
        linearLayout.setBorder(new Border(Colour.BLACK, 4));
        linearLayout.setOrientation(com.crispin.crispinmobile.UserInterface.LinearLayout.Orientation.VERTICAL);
        linearLayout.add(previousButton);
        linearLayout.add(nextButton);

        camera2D = new Camera2D();
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render() {
        linearLayout.draw(camera2D);
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {

    }
}
