package com.crispin.crispinmobile.UserInterface;

import java.util.EventObject;

import glm_.vec2.Vec2;

public class TouchEvent extends EventObject {
    public enum Event
    {
        CLICK,
        DOWN,
        RELEASE
    }

    private Event event;
    private Vec2 position;

    public TouchEvent(Object source, Event event, Vec2 position)
    {
        super(source);
        this.event = event;
        this.position = position;
    }

    public Vec2 getPosition()
    {
        return this.position;
    }

    public Event getEvent()
    {
        return this.event;
    }
}
