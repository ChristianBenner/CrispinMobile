package com.games.crispin.crispinmobile.UserInterface;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;

/**
 * Base class for user interface objects. Contains functions that should be generic to most user
 * interface objects so that there is a common way to interact with them.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public interface UIObject
{
    /**
     * Set the position of the user interface
     *
     * @param position  The new position for the user interface
     * @since 1.0
     */
    void setPosition(Point2D position);

    /**
     * Set the position of the user interface
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @since 1.0
     */
    void setPosition(float x, float y);

    /**
     * Get the user interface position
     *
     * @return The user interface position
     * @since 1.0
     */
    Point2D getPosition();

    /**
     * Set the width of the UI object
     *
     * @param width The new width of the object
     * @since 1.0
     */
    void setWidth(float width);

    /**
     * Get the width of the UI object
     *
     * @return The width of the UI object
     * @since 1.0
     */
    float getWidth();

    /**
     * Set the height of the UI object
     *
     * @param height    The new width of the object
     * @since 1.0
     */
    void setHeight(float height);

    /**
     * Get the height of the UI object
     *
     * @return The height of the UI object
     * @since 1.0
     */
    float getHeight();

    /**
     * Set the size of the UI object
     *
     * @param size  The new size of the UI object
     * @since 1.0
     */
    void setSize(Scale2D size);

    /**
     * Set the size of the UI object
     *
     * @param width     The new width for the object
     * @param height    The new height for the object
     * @since 1.0
     */
    void setSize(float width, float height);

    /**
     * Get the size of the UI object
     *
     * @return The size of the UI object
     * @since 1.0
     */
    Scale2D getSize();

    /**
     * Set the colour of the UI object
     *
     * @param colour    The new colour for the UI object
     * @since 1.0
     */
    void setColour(Colour colour);

    /**
     * Get the colour of the UI object
     *
     * @return The colour of the UI object
     * @since 1.0
     */
    Colour getColour();

    /**
     * Set the opacity of the UI object
     *
     * @param alpha The new alpha channel value for the UI object
     * @since 1.0
     */
    void setOpacity(float alpha);

    /**
     * Get the opacity of the UI object
     *
     * @return  The alpha channel value of the UI object
     * @since 1.0
     */
    float getOpacity();

    /**
     * Disable specific borders on the object
     *
     * @param flags The border flags
     * @since 1.0
     */
    void setDisabledBorders(int flags);

    /**
     * Draw function designed to be overridden
     *
     * @since 1.0
     */
    void draw(Camera2D camera);
}