package com.games.crispin.crispinmobile.UserInterface;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.Utilities.Logger;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

public class Plane implements UIObject
{
    protected Point2D position;
    protected Scale2D size;

    protected Square plane;

    private boolean borderEnabled;
    private int disabledBorderFlags;

    public Plane(Point2D position, Scale2D size)
    {
        this.position = new Point2D();
        this.size = new Scale2D();

        this.borderEnabled = false;

        plane = new Square(true);
        disabledBorderFlags = Border.NONE;

        // Because we don't have an image on by default, ignore texel data in rendering
        plane.getMaterial().setIgnoreTexelData(true);

        setPosition(position);
        setSize(size);
    }

    public Plane(Texture texture,
                 Point2D position,
                 Scale2D size)
    {
        this.position = new Point2D();
        this.size = new Scale2D();
        this.borderEnabled = false;

        plane = new Square(true);
        setImage(texture);

        setPosition(position);
        setSize(size);
    }

    public Plane(Scale2D size)
    {
        this(new Point2D(), size);
    }

    public Plane(Texture texture)
    {
        this(texture, new Point2D(), new Scale2D());
    }

    public Plane(Texture texture,
                 Point2D position)
    {
        this(texture, position, new Scale2D());
    }

    public Plane(Texture texture,
                 Scale2D size)
    {
        this(texture, new Point2D(), size);
    }

    public Plane()
    {
        this(new Point2D(), new Scale2D());
    }

    public void setImage(Texture texture)
    {
        plane.getMaterial().setIgnoreTexelData(false);
        plane.getMaterial().setTexture(texture);
    }

    public void setImage(int resourceId)
    {
        plane.getMaterial().setIgnoreTexelData(false);
        plane.getMaterial().setTexture(new Texture(resourceId));
    }

    private Border border;

    public void setBorder(Border border)
    {
        if(border == null)
        {
            this.borderEnabled = false;
        }
        else
        {
            this.border = border;
            this.borderEnabled = true;
            this.border.updatePosition(this);
        }
    }

    public void removeBorder()
    {
        this.borderEnabled = false;
    }

    // recalculate positions
    private void updatePosition()
    {
        if(borderEnabled)
        {
            this.border.updatePosition(this);
        }

        this.plane.setScale(new Scale2D(size.x, size.y));
        this.plane.setPosition(position);
    }

    /**
     * Set the position of the user interface
     *
     * @param position  The new position for the user interface
     * @since 1.0
     */
    @Override
    public void setPosition(Point2D position)
    {
        this.position.x = position.x;
        this.position.y = position.y;
        updatePosition();
    }

    /**
     * Set the position of the user interface
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @since 1.0
     */
    @Override
    public void setPosition(float x, float y)
    {
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
    public Point2D getPosition()
    {
        return position;
    }

    /**
     * Set the width of the UI object
     *
     * @param width The new width of the object
     * @since 1.0
     */
    @Override
    public void setWidth(float width)
    {
        size.x = width;
        updatePosition();
    }

    /**
     * Get the width of the UI object
     *
     * @return The width of the UI object
     * @since 1.0
     */
    @Override
    public float getWidth()
    {
        return size.x;
    }

    /**
     * Set the height of the UI object
     *
     * @param height    The new width of the object
     * @since 1.0
     */
    @Override
    public void setHeight(float height)
    {
        size.y = height;
        updatePosition();
    }

    /**
     * Get the height of the UI object
     *
     * @return The height of the UI object
     * @since 1.0
     */
    @Override
    public float getHeight()
    {
        return size.y;
    }

    /**
     * Set the size of the UI object
     *
     * @param size  The new size of the UI object
     * @since 1.0
     */
    @Override
    public void setSize(Scale2D size)
    {
        this.setSize(size.x, size.y);
    }

    /**
     * Set the size of the UI object
     *
     * @param width     The new width for the object
     * @param height    The new height for the object
     * @since 1.0
     */
    @Override
    public void setSize(float width, float height)
    {
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
    public Scale2D getSize()
    {
        return size;
    }

    /**
     * Set the colour of the UI object
     *
     * @param colour    The new colour for the UI object
     * @since 1.0
     */
    @Override
    public void setColour(Colour colour)
    {
        this.plane.setColour(colour);
        this.setBorderAlpha(colour.getAlpha());
    }

    /**
     * Set the colour of the border. If the object doesn't have a border, one will be created.
     *
     * @param colour    The colour of the border
     * @since 1.0
     */
    public void setBorderColour(Colour colour)
    {
        // If a border does not exist yet, create one
        if(border == null)
        {
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
    public void setBorderAlpha(float alpha)
    {
        if(border != null)
        {
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
    public Colour getColour()
    {
        return this.plane.getColour();
    }

    /**
     * Set the alpha channel intensity of the UI object
     *
     * @param alpha The new alpha channel intensity for the UI object
     * @since 1.0
     */
    public void setAlpha(float alpha)
    {
        this.setOpacity(alpha);
    }

    /**
     * Set the opacity of the UI object
     *
     * @param alpha The new alpha channel value for the UI object
     * @since 1.0
     */
    @Override
    public void setOpacity(float alpha)
    {
        this.plane.setAlpha(alpha);
        this.setBorderAlpha(alpha);
    }

    /**
     * Get the opacity of the UI object
     *
     * @return  The alpha channel value of the UI object
     * @since 1.0
     */
    @Override
    public float getOpacity()
    {
        return this.plane.getMaterial().getColour().getAlpha();
    }

    /**
     * Disable specific borders on the object
     *
     * @param flags The border flags
     * @since 1.0
     */
    @Override
    public void setDisabledBorders(int flags)
    {
        this.disabledBorderFlags = flags;
    }

    /**
     * Draw function designed to be overridden
     *
     * @since 1.0
     */
    @Override
    public void draw(Camera2D camera)
    {
        glDisable(GL_DEPTH_TEST);
        if(borderEnabled)
        {
            border.draw(camera, disabledBorderFlags);
        }
        plane.render(camera);
        glEnable(GL_DEPTH_TEST);
    }
}
