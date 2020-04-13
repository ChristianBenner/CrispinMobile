package com.games.crispin.crispinmobile.UserInterface;

import android.opengl.GLES20;
import android.util.Pair;

import com.games.crispin.crispinmobile.Geometry.Geometry;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;

import java.util.ArrayList;

public class AbsoluteLayout extends Plane
{
    private ArrayList<UIObject> uiObjects;

    public AbsoluteLayout()
    {
        uiObjects = new ArrayList<>();
    }

    public void add(UIObject uiObject)
    {
        uiObjects.add(uiObject);
    }

    public void remove(UIObject uiObject)
    {
        uiObjects.remove(uiObject);
    }

    @Override
    public void draw(Camera2D camera)
    {
        super.draw(camera);

        for(int i = 0; i < uiObjects.size(); i++)
        {
            // Calculate the objects new position based on the position of the layout
            final Point2D oldPos = new Point2D(uiObjects.get(i).getPosition());
            uiObjects.get(i).setPosition(Geometry.translate(oldPos, super.getPosition()));
            uiObjects.get(i).draw(camera);
            uiObjects.get(i).setPosition(oldPos);
        }
    }
}
