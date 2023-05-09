package com.crispin.crispinmobile.UserInterface;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;

import java.util.ArrayList;

public class LinearLayout extends Plane {
    private static final float DEFAULT_PADDING_X = 10.0f;
    private static final float DEFAULT_PADDING_Y = 10.0f;
    private boolean vertical;
    private final ArrayList<UIElement> uiObjects;
    private float cursorX;
    private float cursorY;
    private float endX;
    private float endY;
    private final Scale2D padding;
    private final boolean automaticWidth;
    private final boolean automaticHeight;
    private boolean showBackground;

    public LinearLayout(Vec2 position, Scale2D size, boolean vertical) {
        this.padding = new Scale2D();
        this.vertical = vertical;
        this.showBackground = false;

        this.automaticWidth = size.w == 0.0f;
        this.automaticHeight = size.h == 0.0f;

        uiObjects = new ArrayList<>();

        endX = 0.0f;
        endY = 0.0f;

        cursorX = 0.0f;
        cursorY = 0.0f;

        setSize(size);
        setPosition(position);
        setPadding(new Scale2D(DEFAULT_PADDING_X, DEFAULT_PADDING_Y));
    }

    public LinearLayout(Vec2 position, Scale2D size) {
        this(position, size, false);
    }

    public LinearLayout(Vec2 position) {
        this(position, new Scale2D(0.0f, 0.0f), false);
    }

    public LinearLayout(Scale2D size) {
        this(new Vec2(), size, false);
    }

    public LinearLayout(boolean vertical) {
        this(new Vec2(), new Scale2D(0.0f, 0.0f), vertical);
    }

    public LinearLayout() {
        this(new Vec2(), new Scale2D(0.0f, 0.0f), false);
    }

    public void addUI(UIElement uiObject) {
        uiObjects.add(uiObject);
    }

    public Scale2D getPadding() {
        return padding;
    }

    public void setPadding(Scale2D padding) {
        this.padding.w = padding.w;
        this.padding.h = padding.h;

        updateUIElementPositions();
    }

    public void setOrientation(Orientation orientation) {
        vertical = orientation == Orientation.VERTICAL;

        updateUIElementPositions();
    }

    /**
     * Set the position of the user interface
     *
     * @param position The new position for the user interface
     * @since 1.0
     */
    @Override
    public void setPosition(Vec2 position) {
        super.setPosition(position);
        updateUIElementPositions();
    }

    /**
     * Set the position of the user interface
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @since 1.0
     */
    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        updateUIElementPositions();
    }

    /**
     * Set the width of the UI object
     *
     * @param width The new width of the object
     * @since 1.0
     */
    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        updateUIElementPositions();
    }

    /**
     * Set the height of the UI object
     *
     * @param height The new width of the object
     * @since 1.0
     */
    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        updateUIElementPositions();
    }

    /**
     * Set the size of the UI object
     *
     * @param size The new size of the UI object
     * @since 1.0
     */
    @Override
    public void setSize(Scale2D size) {
        super.setSize(size.w, size.h);
        updateUIElementPositions();
    }

    /**
     * Set the size of the UI object
     *
     * @param width  The new width for the object
     * @param height The new height for the object
     * @since 1.0
     */
    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        updateUIElementPositions();
    }

    @Override
    public void setColour(Colour colour) {
        this.showBackground = true;
        super.setColour(colour);
    }

    private void updateUIElementPositions() {
        endX = 0.0f;
        endY = 0.0f;
        cursorX = 0.0f;
        cursorY = 0.0f;

        if (uiObjects != null) {
            for (UIElement uiObject : uiObjects) {
                positionUIObject(uiObject);
            }
        }
    }

    private float getRemainingSpace() {
        if (vertical) {
            return getHeight() - (padding.h * 2.0f) - cursorY;
        } else {
            return getWidth() - (padding.w * 2.0f) - cursorX;
        }
    }

    private void positionUIObject(UIElement uiObject) {
        if (vertical) {
            // UI Start position
            final float POS_X = position.x + cursorX;
            final float POS_Y = position.y + cursorY + padding.h;

            cursorY += padding.h + uiObject.getHeight();

            uiObject.setPosition(padding.w + POS_X, POS_Y);
        } else {
            // Check if the object will fit on the current line
            if (size.w != 0 && cursorX + uiObject.getWidth() > size.w) {
                cursorY += endY;
                cursorX = 0.0f;
                endY = 0.0f;
            }

            // UI start position
            final float POS_X = padding.w + position.x + cursorX;
            final float POS_Y = padding.h + position.y + cursorY;

            // Set the end of the UI object as the new cursorX
            cursorX += padding.w + uiObject.getWidth();

            // If the object and width exceeds the current end X, set the new end X
            if (POS_X + uiObject.getWidth() > endX) {
                endX = POS_X + uiObject.getWidth();
            }

            // If the object and height exceeds the current end Y, set the new end Y
            if (uiObject.getHeight() + padding.h > endY) {
                endY = uiObject.getHeight() + padding.h;

                final float NEW_HEIGHT = POS_Y + endY - position.y;
                if (automaticHeight && NEW_HEIGHT > size.h) {
                    size.h = NEW_HEIGHT;
                    super.setHeight(size.h);
                }
            }

            uiObject.setPosition(POS_X, POS_Y);
        }
    }

    public void add(UIElement uiObject) {
        uiObject.setOpacity(super.getOpacity());
        this.uiObjects.add(uiObject);
        positionUIObject(uiObject);
    }

    @Override
    public float getOpacity() {
        return 0;
    }

    public void setOpacity(float alpha) {
        super.setOpacity(alpha);

        for (int i = 0; i < uiObjects.size(); i++) {
            uiObjects.get(i).setOpacity(alpha);
        }
    }

    /**
     * Disable specific borders on the object
     *
     * @param flags The border flags
     * @since 1.0
     */
    @Override
    public void setDisabledBorders(int flags) {

    }

    @Override
    public void draw(Camera2D camera) {
        glDisable(GL_DEPTH_TEST);

        if (showBackground) {
            super.draw(camera);
        }

        // Iterate through the ui objects drawing them
        for (int i = 0; i < uiObjects.size(); i++) {
            uiObjects.get(i).draw(camera);
        }

        glEnable(GL_DEPTH_TEST);
    }

    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }
}
