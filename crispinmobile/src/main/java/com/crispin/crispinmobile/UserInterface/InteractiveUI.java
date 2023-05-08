package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Utilities.UIHandler;

import java.util.ArrayList;

public abstract class InteractiveUI implements UIElement, InteractiveElement {
    private final ArrayList<TouchListener> touchListeners = new ArrayList<>();

    private boolean clicked;
    private boolean enabled;

    public InteractiveUI() {
        this.clicked = false;
        this.enabled = true;
    }

    public boolean interacts(Vec2 position) {
        return position.x > getPosition().x && position.x < getPosition().x + getWidth() &&
                position.y < getPosition().y + getHeight() && position.y > getPosition().y;
    }

    public void removeAllTouchListeners() {
        touchListeners.clear();

        UIHandler.remove(this);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean state) {
        if (enabled != state) {
            enabled = state;

            if (enabled) {
                enabled();
            } else {
                disabled();
            }
        }
    }

    public void addTouchListener(TouchListener listener) {
        if(touchListeners.isEmpty()) {
            UIHandler.add(this);
        }

        touchListeners.add(listener);
    }

    public void removeTouchListener(TouchListener listener) {
        touchListeners.remove(listener);

        if (touchListeners.isEmpty()) {
            UIHandler.remove(this);
        }
    }

    public boolean isClicked() {
        return clicked;
    }

    public void sendClickEvent(Vec2 position) {
        if (enabled) {
            clicked = true;

            clickEvent(position);

            final TouchEvent CLICK_EVENT = new TouchEvent(this, TouchEvent.Event.CLICK, position);

            for (final TouchListener buttonListener : touchListeners) {
                buttonListener.touchEvent(CLICK_EVENT);
            }
        }
    }

    public void sendReleaseEvent(Vec2 position) {
        clicked = false;

        releaseEvent(position);

        final TouchEvent RELEASE_EVENT = new TouchEvent(this, TouchEvent.Event.RELEASE, position);
        for (final TouchListener buttonListener : touchListeners) {
            buttonListener.touchEvent(RELEASE_EVENT);
        }
    }

    public void sendDownEvent(Vec2 position) {
        if (enabled) {
            dragEvent(position);

            // Send the drag touch event
            final TouchEvent DOWN_EVENT = new TouchEvent(this, TouchEvent.Event.DOWN, position);
            for (final TouchListener buttonListener : touchListeners) {
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
}
