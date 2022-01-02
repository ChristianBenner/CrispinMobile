package com.crispin.crispinmobile.UserInterface;

import android.graphics.PointF;

import com.crispin.crispinmobile.Geometry.Point2D;

import java.util.EventObject;

public class TouchEvent extends EventObject {
    public enum Event
    {
        CLICK,
        DOWN,
        RELEASE
    }

    private Event event;
    private Point2D position;

    public TouchEvent(Object source, Event event, Point2D position)
    {
        super(source);
        this.event = event;
        this.position = position;
    }

    public Point2D getPosition()
    {
        return this.position;
    }

    public Event getEvent()
    {
        return this.event;
    }
}
