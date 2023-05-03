package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;

/**
 * Created by Christian Benner on 03/04/2018.
 */

public class Joystick {
    private Square base;
    private Button joystick;

    private Vec2 outerOffset;
    private float baseRadius;
    private float joystickRadius;

    public Joystick(Vec2 position, float size, int baseTexture, int movableTexture) {
        this.baseRadius = size / 2f;
        this.joystickRadius = baseRadius / 2f;

        base = new Square(baseTexture);
        base.setPosition(position);
        base.setScale(size);

        Vec2 movablePosition = new Vec2(position.x + baseRadius - joystickRadius,
                position.y + baseRadius - joystickRadius);
        Scale2D movableSize = new Scale2D(joystickRadius * 2.0f, joystickRadius * 2.0f);

        joystick = new Button(movableTexture);
        joystick.setPosition(movablePosition);
        joystick.setSize(movableSize);
        joystick.addTouchListener(e -> {
            switch (e.getEvent()) {
                case DOWN:
                    drag(e.getPosition());
                    break;
                case RELEASE:
                    release();
                    break;
            }
        });

        outerOffset = new Vec2(0.0f, 0.0f);
    }

    public Joystick(Vec2 position, float size) {
        this(position, size, R.drawable.joy_stick_outer, R.drawable.joy_stick_inner);
    }

    public void setJoystickOffset(Vec2 offset) {
        this.outerOffset = offset;
        joystick.setPosition(base.getPosition().x + baseRadius - joystickRadius + offset.x,
                base.getPosition().y + baseRadius - joystickRadius + offset.y);
    }

    public Vec2 getOuterOffset() {
        return this.outerOffset;
    }

    public Vec2 getDirection() {
        float xDirection = ((joystick.getPosition().x + joystickRadius) - base.getPosition().x -
                baseRadius) / baseRadius;
        float yDirection = ((joystick.getPosition().y + joystickRadius) - base.getPosition().y -
                baseRadius) / baseRadius;
        return new Vec2(xDirection, yDirection);
    }

    public void render(Camera2D camera2D) {
        base.render(camera2D);
        joystick.draw(camera2D);
    }

    public void drag(Vec2 pos) {
        // Calculate offset
        float xOffset = pos.x - base.getPosition().x - baseRadius;
        float yOffset = pos.y - base.getPosition().y - baseRadius;
        Vec2 drag = new Vec2(xOffset, yOffset);

        if (drag.length() > baseRadius) {
            Vec2 offset = Geometry.scaleVector(drag, baseRadius / drag.length());
            setJoystickOffset(offset);
        } else {
            joystick.setPosition(pos.x - joystickRadius, pos.y - joystickRadius);
        }
    }

    public void release() {
        // Set position back to original offset
        setJoystickOffset(new Vec2(0.0f, 0.0f));
    }
}