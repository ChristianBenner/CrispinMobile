package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;

import java.util.ArrayList;

public class AbsoluteLayout extends Plane {
    private final ArrayList<UIObject> uiObjects;

    public AbsoluteLayout() {
        uiObjects = new ArrayList<>();
    }

    public void add(UIObject uiObject) {
        uiObjects.add(uiObject);
    }

    public void remove(UIObject uiObject) {
        uiObjects.remove(uiObject);
    }

    @Override
    public void draw(Camera2D camera) {
        super.draw(camera);

        for (int i = 0; i < uiObjects.size(); i++) {
            // Calculate the objects new position based on the position of the layout
            final Vec2 oldPos = new Vec2(uiObjects.get(i).getPosition());
            uiObjects.get(i).setPosition(Geometry.translate(oldPos, super.getPosition()));
            uiObjects.get(i).draw(camera);
            uiObjects.get(i).setPosition(oldPos);
        }
    }
}
