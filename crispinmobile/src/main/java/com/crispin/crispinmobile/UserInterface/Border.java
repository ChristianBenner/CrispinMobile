package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;

import glm_.vec2.Vec2;
import glm_.vec4.Vec4;

public class Border
{
    private Vec4 colour;
    private int width;

    private static final int DEFAULT_BORDER_WIDTH_PIXELS = 5;
    public static final int NONE = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 4;
    public static final int LEFT = 8;
    public static final int ALL = 16;

    private Square borderTop;
    private Square borderRight;
    private Square borderBottom;
    private Square borderLeft;

    public Border(Vec4 colour, int width)
    {
        this.colour = colour;
        this.width = width;

        this.borderTop = new Square(false);
        this.borderRight = new Square(false);
        this.borderBottom = new Square(false);
        this.borderLeft = new Square(false);

        setColour(colour);
    }

    public Border(Vec4 colour)
    {
        this(colour, DEFAULT_BORDER_WIDTH_PIXELS);
    }

    public Border(int width)
    {
        this(new Vec4(), width);
    }

    public Border()
    {
        this(new Vec4(), DEFAULT_BORDER_WIDTH_PIXELS);
    }

    public void setColour(Vec4 colour)
    {
        this.borderTop.setColour(colour);
        this.borderRight.setColour(colour);
        this.borderBottom.setColour(colour);
        this.borderLeft.setColour(colour);
    }

    public Vec4 getColour()
    {
        return this.colour;
    }

    public void setAlpha(float alpha)
    {
        this.borderTop.setAlpha(alpha);
    }

    // Updates the position of all enabled borders around a given object (based on position and scale)
    public void updatePosition(UIObject uiObject)
    {
        final Vec2 parentPos = uiObject.getPosition();
        final Vec2 parentSize = uiObject.getSize();

        borderTop.setPosition(parentPos.x, parentPos.y + parentSize.y);
        borderRight.setPosition(parentPos.x + parentSize.x, parentPos.y - width);
        borderBottom.setPosition(parentPos.x, parentPos.y - width);
        borderLeft.setPosition(parentPos.x - width, parentPos.y - width);

        borderTop.setScale(parentSize.x, width);
        borderRight.setScale(width, parentSize.y + (width * 2.0f));
        borderBottom.setScale(parentSize.x, width);
        borderLeft.setScale(width, parentSize.y + (width * 2.0f));
    }

    public void draw(Camera2D camera2D)
    {
        // Position and draw border elements
        borderTop.render(camera2D);
        borderRight.render(camera2D);
        borderBottom.render(camera2D);
        borderLeft.render(camera2D);
    }

    public void draw(Camera2D camera2D, int disabledBorderFlags)
    {
        if((disabledBorderFlags & ALL) != ALL)
        {
            if((disabledBorderFlags & TOP) != TOP)
            {
                borderTop.render(camera2D);
            }

            if((disabledBorderFlags & RIGHT) != RIGHT)
            {
                borderRight.render(camera2D);
            }

            if((disabledBorderFlags & BOTTOM) != BOTTOM)
            {
                borderBottom.render(camera2D);
            }

            if((disabledBorderFlags & LEFT) != LEFT)
            {
                borderLeft.render(camera2D);
            }
        }
    }
}
