package com.crispin.crispinmobile.Utilities;

import android.view.MotionEvent;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.UserInterface.InteractiveUIObject;
import com.crispin.crispinmobile.UserInterface.Pointer;

import java.util.ArrayList;

public class UIHandler {
    private static final ArrayList<InteractiveUIObject> uiObjects = new ArrayList();

    public static void addUI(InteractiveUIObject uiObject) {
        uiObjects.add(uiObject);
    }

    public static void removeUI(InteractiveUIObject uiObject) {
        for (int i = 0; i < uiObjects.size(); i++) {
            if (uiObjects.get(i) == uiObject) {
                uiObjects.remove(i);
                break;
            }
        }
    }

    public static void removeAll() {
        for (int i = 0; i < uiObjects.size(); i++) {
            uiObjects.remove(i);
            i--;
        }
    }

    // true if consumed, else false
    public static boolean consume(Pointer pointer) {
        for (int i = 0; i < uiObjects.size(); i++) {
            InteractiveUIObject uiElement = uiObjects.get(i);
            if (uiElement.isEnabled() && InteractiveUIObject.interacts(uiElement, pointer.getPosition())) {
                pointer.click(uiElement);
                return true;
            }
        }

        return false;
    }
}
