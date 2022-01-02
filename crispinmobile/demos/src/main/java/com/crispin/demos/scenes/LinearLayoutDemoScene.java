package com.crispin.demos.scenes;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Border;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.LinearLayout;
import com.crispin.crispinmobile.Utilities.FontCache;
import com.crispin.crispinmobile.Utilities.Scene;

public class LinearLayoutDemoScene extends Scene
{
    private LinearLayout linearLayout;
    private Button previousButton;
    private Button nextButton;

    private Camera2D camera2D;

    public LinearLayoutDemoScene()
    {
        previousButton = new Button(FontCache.getFont(R.raw.aileron_regular, 32),
                "<");
        nextButton = new Button(FontCache.getFont(R.raw.aileron_regular, 32), ">");

        linearLayout = new LinearLayout(new Point2D(200.0f, 200.0f), new Scale2D(
                Crispin.getSurfaceWidth() - 400.0f, 600.0f));
        linearLayout.setColour(Colour.BLUE);
        linearLayout.setBorder(new Border(Colour.BLACK, 4));
        linearLayout.setOrientation(LinearLayout.Orientation.VERTICAL);
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
    public void touch(int type, Point2D position)
    {

    }
}
