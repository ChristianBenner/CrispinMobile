package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Geometry.Vec2;

public interface InteractiveElement {
    void sendClickEvent(Vec2 position);
    void sendReleaseEvent(Vec2 position);
    void sendDownEvent(Vec2 position);
}