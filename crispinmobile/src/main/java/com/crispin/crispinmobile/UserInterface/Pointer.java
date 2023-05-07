package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Geometry.Vec2;

public class Pointer {
    private int pointerId;
    private boolean inUse;
    private Vec2 position;
    private InteractiveElement element;

    public Pointer(int pointerId, Vec2 position) {
        this.pointerId = pointerId;
        this.position = position;
    }

    public void setControlOver(InteractiveElement element) {
        this.element = element;
        this.element.sendClickEvent(position);
    }

    public void releaseControl() {
        // The pointer may have never had a UI element
        if (this.element != null) {
            this.element.sendReleaseEvent(position);
        }
    }

    public void handleDrag() {
        if (this.element != null) {
            this.element.sendDownEvent(position);
        }
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    public boolean isInUse() {
        return inUse;
    }

    public int getPointerId() {
        return pointerId;
    }

    public void setInUse(boolean state) {
        this.inUse = state;
    }

    public void setPointerId(int pointerId) {
        this.pointerId = pointerId;
    }
}