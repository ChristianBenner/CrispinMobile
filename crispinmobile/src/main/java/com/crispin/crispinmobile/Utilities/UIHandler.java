package com.crispin.crispinmobile.Utilities;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.UserInterface.InteractiveUI;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.UIElement;

import java.util.HashSet;
import java.util.Set;

/**
 * UI handler is a class that supports internal behaviour of the Crispin engine. Every UI that is
 * created should register itself with the UIHandler so that the engine can check for pointer
 * interaction and send touch events.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @since 1.0
 */
public class UIHandler {
    // List of user interface elements
    private static final Set<InteractiveUI> uiElements = new HashSet<>();

    /**
     * Add an interactive UI element. Once added the engine will check for pointer interactions and
     * send touch events to it.
     *
     * @param uiElement UI element to add
     * @since 1.0
     */
    public static void add(InteractiveUI uiElement) {
        uiElements.add(uiElement);
    }

    /**
     * Remove an interactive UI element. Once removed the engine will no longer check for pointer
     * interactions and send touch events to it.
     *
     * @param uiElement UI element to remove
     * @since 1.0
     */
    public static void remove(InteractiveUI uiElement) {
        uiElements.remove(uiElement);
    }

    /**
     * Remove all interactive UI elements, meaning that no UI will be checked for pointer
     * interactions or receive touch events.
     *
     * @since 1.0
     */
    public static void removeAll() {
        uiElements.clear();
    }

    /**
     * Check if a given pointer interacts with any of the registered UI elements. If it does, it
     * will own that element and send move and release events to it when they occur. Once a pointer
     * interacts with an element, it is consumed and will not be available to react with another.
     *
     * @param pointer The pointer to check interaction with UI elements
     * @return True if the pointer was consumed (interacts with an enabled UI element), else false
     * @since 1.0
     */
    public static boolean consume(Pointer pointer) {
        for (InteractiveUI interactiveUI : uiElements) {
            if (interactiveUI.isEnabled() && interacts(interactiveUI, pointer.getPosition())) {
                pointer.click(interactiveUI);
                return true;
            }
        }

        return false;
    }

    /**
     * Check if a UI element interacts with a given position. Assumes the element is rectangular and
     * not rotated.
     *
     * @param uiElement The element to check interaction against
     * @return True if the position collides with the UI
     * @since 1.0
     */
    public static boolean interacts(UIElement uiElement, Vec2 position) {
        return position.x > uiElement.getPosition().x &&
                position.x < uiElement.getPosition().x + uiElement.getWidth() &&
                position.y < uiElement.getPosition().y + uiElement.getHeight() &&
                position.y > uiElement.getPosition().y;
    }
}
