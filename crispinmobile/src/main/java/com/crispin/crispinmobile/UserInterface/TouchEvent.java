package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Geometry.Vec2;

import java.util.EventObject;

public class TouchEvent extends EventObject {
    private final Event event;
    private final Vec2 position;
    public TouchEvent(Object source, Event event, Vec2 position) {
        super(source);
        this.event = event;
        this.position = position;
    }

    public Vec2 getPosition() {
        return this.position;
    }

    public Event getEvent() {
        return this.event;
    }

    public enum Event {
        CLICK,
        DOWN,
        RELEASE
    }
}
