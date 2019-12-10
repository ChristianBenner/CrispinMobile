package com.games.crispin.crispinmobile.UserInterface;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Utilities.UIHandler;

import java.util.ArrayList;

public abstract class InteractableUIObject implements UIObject
{
    private ArrayList<TouchListener> touchListeners = new ArrayList<>();

    private boolean clicked = false;

    public void addTouchListener(TouchListener listener)
    {
        touchListeners.add(listener);

        // Add the piece of UI to the UIHandler
        UIHandler.addUI(this);
    }

    public void removeTouchListener(TouchListener listener)
    {
        touchListeners.remove(listener);

        // Remove the UI from the UIHandler
        UIHandler.removeUI(this);
    }

    public boolean isClicked()
    {
        return clicked;
    }

    public void sendClickEvent(Point2D position)
    {
        clicked = true;
        final TouchEvent CLICK_EVENT = new TouchEvent(this, TouchEvent.Event.CLICK, position);
        for(final TouchListener buttonListener : touchListeners)
        {
            buttonListener.touchEvent(CLICK_EVENT);
        }
    }

    public void sendReleaseEvent(Point2D position)
    {
        clicked = false;
        final TouchEvent RELEASE_EVENT = new TouchEvent(this, TouchEvent.Event.RELEASE, position);
        for(final TouchListener buttonListener : touchListeners)
        {
            buttonListener.touchEvent(RELEASE_EVENT);
        }
    }

    public void sendDownEvent(Point2D position)
    {
        // Send the drag touch event
        final TouchEvent DOWN_EVENT = new TouchEvent(this, TouchEvent.Event.DOWN, position);
        for(final TouchListener buttonListener : touchListeners)
        {
            buttonListener.touchEvent(DOWN_EVENT);
        }
    }

    public static boolean interacts(UIObject uiObject, Point2D pointer)
    {
        Point2D pos = new Point2D();
        pos.x = pointer.x;
        pos.y = Crispin.getSurfaceHeight() - pointer.y;

        // Check if the pointer is inside the button
        if(pos.x > uiObject.getPosition().x && pos.x < uiObject.getPosition().x +
                uiObject.getWidth() &&
                pos.y < uiObject.getPosition().y + uiObject.getHeight() && pos.y >
                uiObject.getPosition().y)
        {
            return true;
        }

        return false;
    }
}
