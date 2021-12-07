package com.crispin.crispinmobile.Demos;

import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.AbsoluteLayout;
import com.crispin.crispinmobile.UserInterface.Border;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Font;
import com.crispin.crispinmobile.UserInterface.Text;
import com.crispin.crispinmobile.UserInterface.UIObject;
import com.crispin.crispinmobile.Utilities.FontCache;
import com.crispin.crispinmobile.Utilities.Scene;

public class AbsoluteLayoutDemoScene extends Scene
{
    private AbsoluteLayout absoluteLayout;
    private Button button;
    private Text text;

    private Camera2D camera;

    public AbsoluteLayoutDemoScene()
    {
        absoluteLayout = new AbsoluteLayout();
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
    public void update(float deltaTime)
    {

    }

    @Override
    public void render()
    {
        absoluteLayout.draw(camera);
    }

    @Override
    public void touch(int type, Point2D position)
    {

    }
}
