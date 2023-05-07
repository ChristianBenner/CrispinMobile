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

    // send a touch event to all the UI in the current scene
    public static void sendTouchEvent(int type, Pointer pointer) {
        if(type == MotionEvent.ACTION_DOWN) {
            Vec2 position = pointer.getPosition();
            for (int i = 0; i < uiObjects.size(); i++) {
                if (uiObjects.get(i).isEnabled() && InteractiveUIObject.interacts(uiObjects.get(i), position)) {
                    pointer.setControlOver(uiObjects.get(i));
                    break;
                }
            }
        }
    }
}
