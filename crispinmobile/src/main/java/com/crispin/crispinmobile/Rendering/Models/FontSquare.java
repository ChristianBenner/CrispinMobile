package com.crispin.crispinmobile.Rendering.Models;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Utilities.Material;
import com.crispin.crispinmobile.Rendering.Utilities.RenderObject;

/**
 * FontSquare class derived from the Square class. It is a square render object with some extra
 * functionality to make it more usable in rendering a text UI object.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see RenderObject
 * @see Square
 * @see com.crispin.crispinmobile.UserInterface.Text
 * @since 1.0
 */
public class FontSquare extends Square {
    // The position of the text UI object associated to the character
    private final Vec3 textPosition;

    // The offset of the character from the text position
    private final Vec2 characterOffset;

    /**
     * Construct a FontSquare object with a material and position
     *
     * @param material        The material to apply to the object
     * @param textPosition    The position of the text object. This is not the position of the
     *                        character itself, rather the position of the text UI object it is a
     *                        part of
     * @param characterOffset The position offset of the character from the text position
     * @since 1.0
     */
    public FontSquare(Material material,
                      Vec3 textPosition,
                      Vec2 characterOffset) {
        super(material);

        // Because text shouldn't have colour per vertex ignore the data if it is present
        super.material.ignoreData(Material.IGNORE_COLOUR_DATA_FLAG);
        this.textPosition = textPosition;
        this.characterOffset = characterOffset;

        updatePosition();

    }

    /**
     * Construct a FontSquare object
     *
     * @since 1.0
     */
    public FontSquare() {
        super();

        // Because text shouldn't have colour per vertex ignore the data if it is present
        super.material.ignoreData(Material.IGNORE_COLOUR_DATA_FLAG);
        textPosition = new Vec3();
        characterOffset = new Vec2();
    }

    /**
     * Update and calculate the position of the rendered object. The position is calculated from the
     * given text position and character offset.
     *
     * @since 1.0
     */
    private void updatePosition() {
        super.setPosition(textPosition.x + characterOffset.x,
                textPosition.y + characterOffset.y,
                textPosition.z);
    }

    /**
     * Set the text position. This should be done if the position of the associated text UI object
     * has been changed. This causes a position update.
     *
     * @param x The text x position
     * @param y The text y position
     * @param z The text z position
     * @since 1.0
     */
    public void setTextPosition(float x,
                                float y,
                                float z) {
        this.textPosition.x = x;
        this.textPosition.y = y;
        this.textPosition.z = z;
        updatePosition();
    }

    /**
     * Set the text position. This should be done if the position of the associated text UI object
     * has been changed. This causes a position update.
     *
     * @param x The text x position
     * @param y The text y position
     * @since 1.0
     */
    public void setTextPosition(float x, float y) {
        this.textPosition.x = x;
        this.textPosition.y = y;
        updatePosition();
    }

    /**
     * Get the text position
     *
     * @return The position of the text as a Vec3 object
     * @since 1.0
     */
    public Vec3 getTextPosition() {
        return this.textPosition;
    }

    /**
     * Set the text position. This should be done if the position of the associated text UI object
     * has been changed. This causes a position update.
     *
     * @param textPosition The new position of the text
     * @since 1.0
     */
    public void setTextPosition(Vec3 textPosition) {
        setTextPosition(textPosition.x, textPosition.y, textPosition.z);
        updatePosition();
    }

    /**
     * Set the text position. This should be done if the position of the associated text UI object
     * has been changed. This causes a position update.
     *
     * @param textPosition The new position of the text
     * @since 1.0
     */
    public void setTextPosition(Vec2 textPosition) {
        setTextPosition(textPosition.x, textPosition.y);
        updatePosition();
    }

    /**
     * Set the character offset. This should be if the character position relative to the text UI
     * objects position changes. This function causes a position update.
     *
     * @param x The character x offset
     * @param y The character y offset
     * @since 1.0
     */
    public void setCharacterOffset(float x, float y) {
        this.characterOffset.x = x;
        this.characterOffset.y = y;
        updatePosition();
    }

    /**
     * Get the character offset
     *
     * @return The character offset as a Vec2 object
     * @since 1.0
     */
    public Vec2 getCharacterOffset() {
        return new Vec2(this.characterOffset);
    }

    /**
     * Set the character offset. This should be if the character position relative to the text UI
     * objects position changes. This function causes a position update.
     *
     * @param characterOffset The new character offset
     * @since 1.0
     */
    public void setCharacterOffset(Vec2 characterOffset) {
        setCharacterOffset(characterOffset.x, characterOffset.y);
        updatePosition();
    }

    /**
     * Get the width of the font square
     *
     * @return The font square width
     * @since 1.0
     */
    public float getWidth() {
        return super.getScale().x;
    }

    /**
     * Get the height of the font square
     *
     * @return The font square height
     * @since 1.0
     */
    public float getHeight() {
        return super.getScale().y;
    }
}
