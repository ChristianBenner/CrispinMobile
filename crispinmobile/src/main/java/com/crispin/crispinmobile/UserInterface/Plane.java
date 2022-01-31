package com.crispin.crispinmobile.UserInterface;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Data.Texture;

public class Plane implements UIObject {
    protected Vec2 position;
    protected Scale2D size;

    protected Square plane;

    private boolean borderEnabled;
    private int disabledBorderFlags;
    private Border border;

    public Plane(Vec2 position, Scale2D size) {
        this.position = new Vec2();
        this.size = new Scale2D();

        this.borderEnabled = false;

        plane = new Square(true);
        disabledBorderFlags = Border.NONE;

        // Because we don't have an image on by default, ignore texel data in rendering
        plane.getMaterial().setIgnoreTexelData(true);

        setPosition(position);
        setSize(size);
    }

    public Plane(Texture texture, Vec2 position, Scale2D size) {
        this.position = new Vec2();
        this.size = new Scale2D();
        this.borderEnabled = false;

        plane = new Square(true);
        setImage(texture);

        setPosition(position);
        setSize(size);
    }

    public Plane(Scale2D size) {
        this(new Vec2(), size);
    }

    public Plane(Texture texture) {
        this(texture, new Vec2(), new Scale2D());
    }

    public Plane(Texture texture,
                 Vec2 position) {
        this(texture, position, new Scale2D());
    }

    public Plane(Texture texture,
                 Scale2D size) {
        this(texture, new Vec2(), size);
    }

    public Plane() {
        this(new Vec2(), new Scale2D());
    }

    public void setImage(Texture texture) {
        plane.getMaterial().setIgnoreTexelData(false);
        plane.getMaterial().setTexture(texture);
    }

    public void setImage(int resourceId) {
        plane.getMaterial().setIgnoreTexelData(false);
        plane.getMaterial().setTexture(new Texture(resourceId));
    }

    public void setBorder(Border border) {
        if (border == null) {
            this.borderEnabled = false;
        } else {
            this.border = border;
            this.borderEnabled = true;
            this.border.updatePosition(this);
        }
    }

    public void removeBorder() {
        this.borderEnabled = false;
    }

    // recalculate positions
    private void updatePosition() {
        if (borderEnabled) {
            this.border.updatePosition(this);
        }

        this.plane.setScale(new Scale2D(size.x, size.y));
        this.plane.setPosition(position);
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
        this.position.x = x;
        this.position.y = y;
        updatePosition();
    }

    /**
     * Get the user interface position
     *
     * @return The user interface position
     * @since 1.0
     */
    @Override
    public Vec2 getPosition() {
        return position;
    }

    /**
     * Set the position of the user interface
     *
     * @param position The new position for the user interface
     * @since 1.0
     */
    @Override
    public void setPosition(Vec2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        updatePosition();
    }

    /**
     * Get the width of the UI object
     *
     * @return The width of the UI object
     * @since 1.0
     */
    @Override
    public float getWidth() {
        return size.x;
    }

    /**
     * Set the width of the UI object
     *
     * @param width The new width of the object
     * @since 1.0
     */
    @Override
    public void setWidth(float width) {
        size.x = width;
        updatePosition();
    }

    /**
     * Get the height of the UI object
     *
     * @return The height of the UI object
     * @since 1.0
     */
    @Override
    public float getHeight() {
        return size.y;
    }

    /**
     * Set the height of the UI object
     *
     * @param height The new width of the object
     * @since 1.0
     */
    @Override
    public void setHeight(float height) {
        size.y = height;
        updatePosition();
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
        this.size.x = width;
        this.size.y = height;
        updatePosition();
    }

    /**
     * Get the size of the UI object
     *
     * @return The size of the UI object
     * @since 1.0
     */
    @Override
    public Scale2D getSize() {
        return size;
    }

    /**
     * Set the size of the UI object
     *
     * @param size The new size of the UI object
     * @since 1.0
     */
    @Override
    public void setSize(Scale2D size) {
        this.setSize(size.x, size.y);
    }

    /**
     * Set the colour of the border. If the object doesn't have a border, one will be created.
     *
     * @param colour The colour of the border
     * @since 1.0
     */
    public void setBorderColour(Colour colour) {
        // If a border does not exist yet, create one
        if (border == null) {
            border = new Border();
            border.updatePosition(this);
        }

        this.border.setColour(colour);
    }

    /**
     * Set the alpha channel intensity of the border
     *
     * @param alpha The alpha channel intensity
     * @since 1.0
     */
    public void setBorderAlpha(float alpha) {
        if (border != null) {
            this.border.setAlpha(alpha);
        }
    }

    /**
     * Get the colour of the UI object
     *
     * @return The colour of the UI object
     * @since 1.0
     */
    @Override
    public Colour getColour() {
        return this.plane.getColour();
    }

    /**
     * Set the colour of the UI object
     *
     * @param colour The new colour for the UI object
     * @since 1.0
     */
    @Override
    public void setColour(Colour colour) {
        this.plane.setColour(colour);
        this.setBorderAlpha(colour.alpha);
    }

    /**
     * Set the alpha channel intensity of the UI object
     *
     * @param alpha The new alpha channel intensity for the UI object
     * @since 1.0
     */
    public void setAlpha(float alpha) {
        this.setOpacity(alpha);
    }

    /**
     * Get the opacity of the UI object
     *
     * @return The alpha channel value of the UI object
     * @since 1.0
     */
    @Override
    public float getOpacity() {
        return this.plane.getMaterial().colour.alpha;
    }

    /**
     * Set the opacity of the UI object
     *
     * @param alpha The new alpha channel value for the UI object
     * @since 1.0
     */
    @Override
    public void setOpacity(float alpha) {
        this.plane.setAlpha(alpha);
        this.setBorderAlpha(alpha);
    }

    /**
     * Disable specific borders on the object
     *
     * @param flags The border flags
     * @since 1.0
     */
    @Override
    public void setDisabledBorders(int flags) {
        this.disabledBorderFlags = flags;
    }

    /**
     * Draw function designed to be overridden
     *
     * @since 1.0
     */
    @Override
    public void draw(Camera2D camera) {
        glDisable(GL_DEPTH_TEST);
        if (borderEnabled) {
            border.draw(camera, disabledBorderFlags);
        }
        plane.render(camera);
        glEnable(GL_DEPTH_TEST);
    }
}
