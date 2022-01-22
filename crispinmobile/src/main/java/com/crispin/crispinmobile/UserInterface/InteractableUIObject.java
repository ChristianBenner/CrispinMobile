package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Utilities.UIHandler;

import java.util.ArrayList;

import glm_.vec2.Vec2;

public abstract class InteractableUIObject implements UIObject
{
    private ArrayList<TouchListener> touchListeners = new ArrayList<>();

    private boolean clicked = false;
    private boolean enabled = true;

    public void setEnabled(boolean state)
    {
        if(enabled != state)
        {
            enabled = state;

            if(enabled)
            {
                enabled();
            }
            else
            {
                disabled();
            }
        }
    }

    public void removeAllTouchListeners()
    {
        touchListeners.clear();

        UIHandler.removeUI(this);
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void addTouchListener(TouchListener listener)
    {

        touchListeners.add(listener);

        if(touchListeners.size() == 1)
        {
            // Add the piece of UI to the UIHandler
            UIHandler.addUI(this);
        }
    }

    public void removeTouchListener(TouchListener listener)
    {
        touchListeners.remove(listener);

        if(touchListeners.isEmpty())
        {
            // Remove the UI from the UIHandler
            UIHandler.removeUI(this);
        }
    }

    public boolean isClicked()
    {
        return clicked;
    }

    public void sendClickEvent(Vec2 position)
    {
        if(enabled)
        {
            clicked = true;

            clickEvent(position);

            final TouchEvent CLICK_EVENT = new TouchEvent(this, TouchEvent.Event.CLICK, position);

            for (final TouchListener buttonListener : touchListeners)
            {
                buttonListener.touchEvent(CLICK_EVENT);
            }
        }
    }

    public void sendReleaseEvent(Vec2 position)
    {
        clicked = false;

        releaseEvent(position);

        final TouchEvent RELEASE_EVENT = new TouchEvent(this, TouchEvent.Event.RELEASE, position);
        for(final TouchListener buttonListener : touchListeners)
        {
            buttonListener.touchEvent(RELEASE_EVENT);
        }
    }

    public void sendDownEvent(Vec2 position)
    {
        if(enabled)
        {
            dragEvent(position);

            // Send the drag touch event
            final TouchEvent DOWN_EVENT = new TouchEvent(this, TouchEvent.Event.DOWN, position);
            for(final TouchListener buttonListener : touchListeners)
            {
                buttonListener.touchEvent(DOWN_EVENT);
            }
        }
    }

    // This is something that an interactable UI needs to inherit and control
    protected abstract void clickEvent(Vec2 pointerPosition);
    protected abstract void dragEvent(Vec2 pointerPosition);
    protected abstract void releaseEvent(Vec2 pointerPosition);
    protected abstract void enabled();
    protected abstract void disabled();

    public static boolean interacts(UIObject uiObject, Vec2 pointer)
    {
        Vec2 pos = new Vec2();
        pos.x = pointer.x;
        pos.y = pointer.y;

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
