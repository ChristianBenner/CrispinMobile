package com.crispin.demos.scenes;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Border;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.Utilities.FontCache;
import com.crispin.crispinmobile.Utilities.Scene;

import glm_.vec2.Vec2;

public class LinearLayoutDemo extends Scene
{
    private com.crispin.crispinmobile.UserInterface.LinearLayout linearLayout;
    private Button previousButton;
    private Button nextButton;

    private Camera2D camera2D;

    public LinearLayoutDemo()
    {
        previousButton = new Button(FontCache.getFont(R.raw.aileron_regular, 32),
                "<");
        nextButton = new Button(FontCache.getFont(R.raw.aileron_regular, 32), ">");

        linearLayout = new com.crispin.crispinmobile.UserInterface.LinearLayout(new Vec2(200.0f, 200.0f), new Vec2(
                Crispin.getSurfaceWidth() - 400.0f, 600.0f));
        linearLayout.setColour(Colour.BLUE);
        linearLayout.setBorder(new Border(Colour.BLACK, 4));
        linearLayout.setOrientation(com.crispin.crispinmobile.UserInterface.LinearLayout.Orientation.VERTICAL);
        linearLayout.add(previousButton);
        linearLayout.add(nextButton);

        camera2D = new Camera2D();
    }

    @Override
    public void update(float deltaTime)
    {

    }

    @Override
    public void render()
    {
        linearLayout.draw(camera2D);
    }

    @Override
    public void touch(int type, Vec2 position)
    {

    }
}
