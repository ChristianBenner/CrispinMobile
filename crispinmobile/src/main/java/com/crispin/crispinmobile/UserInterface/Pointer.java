package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Geometry.Vec2;

public class Pointer {
    private Vec2 position;
    private InteractiveElement element;
    private boolean consumedByUI;

    public Pointer(Vec2 position) {
        this.position = new Vec2(position);
        this.element = null;
        this.consumedByUI = false;
    }

    public void click(InteractiveElement element) {
        this.element = element;
        this.element.sendClickEvent(position);
        this.consumedByUI = true;
    }

    public void release(Vec2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        if(element != null) {
            element.sendReleaseEvent(position);
        }
        this.consumedByUI = false;
    }

    public void move(Vec2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        if(element != null) {
            element.sendDownEvent(position);
        }
    }

    public boolean isConsumedByUI() {
        return consumedByUI;
    }

    public Vec2 getPosition() {
        return this.position;
    }
}