package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Geometry.Vec2;

public class Pointer {
    private Vec2 position;
    private InteractiveElement element;
    private boolean inUse;

    public Pointer(Vec2 position) {
        this.position = new Vec2(position);
        this.element = null;
        this.inUse = false;
    }

    public void click(InteractiveElement element) {
        this.element = element;
        this.element.sendClickEvent(position);
    }

    public void release(Vec2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        if(element != null) {
            element.sendReleaseEvent(position);
        }
    }

    public void move(Vec2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        if(element != null) {
            element.sendDownEvent(position);
        }
    }

    public Vec2 getPosition() {
        return this.position;
    }
}