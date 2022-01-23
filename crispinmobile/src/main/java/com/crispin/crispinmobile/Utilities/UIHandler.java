package com.crispin.crispinmobile.Utilities;

import android.view.MotionEvent;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.UserInterface.InteractableUIObject;

import java.util.ArrayList;

public class UIHandler {
    private static final ArrayList<InteractableUIObject> uiObjects = new ArrayList();

    public static void addUI(InteractableUIObject uiObject) {
        uiObjects.add(uiObject);
    }

    public static void removeUI(InteractableUIObject uiObject) {
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
    public static void sendTouchEvent(int type, Vec2 position) {
        for (int i = 0; i < uiObjects.size(); i++) {
            if (uiObjects.get(i).isEnabled()) {
                switch (type) {
                    case MotionEvent.ACTION_DOWN:
                        if (InteractableUIObject.interacts(uiObjects.get(i), position)) {
                            uiObjects.get(i).sendClickEvent(position);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (uiObjects.get(i).isClicked()) {
                            uiObjects.get(i).sendReleaseEvent(position);
                        }
                        break;
                }
            }
        }
    }
}
