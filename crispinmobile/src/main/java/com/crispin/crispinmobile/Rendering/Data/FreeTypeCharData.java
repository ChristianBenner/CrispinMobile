package com.crispin.crispinmobile.Rendering.Data;

import com.crispin.crispinmobile.Native.CrispinNativeInterface;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;

/**
 * FreeTypeCharData class holds important data on FreeType character faces. This is so that a
 * RenderObject can be created, assigned a texture and positioned in the correct place in respect to
 * other characters.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
 * @since 1.0
 */
public class FreeTypeCharData {
    // The width of the character face
    private final int width;

    // The height of the character face
    private final int height;

    // The x-bearing of the character face
    private final int bearingX;

    // The y-bearing of the character face
    private final int bearingY;

    // The advance of the character face
    private final int advance;

    // The ASCII character the data represents
    private final byte ascii;

    // The texture of the character
    public Texture texture;

    /**
     * Create a FreeTypeCharData object. The constructor will interface with the native FreeType
     * library through the CrispinNativeInterface (crispinni) library to load FreeType characters
     * and obtain the necessary data.
     *
     * @param fontBytes      The font as a byte array
     * @param fontSize       The size of the font (bigger font size loads a larger texture)
     * @param asciiChar      The character to load (ASCII character)
     * @param textureOptions Options to apply to the texture data
     * @since 1.0
     */
    public FreeTypeCharData(byte[] fontBytes, int fontSize, byte asciiChar,
                            TextureOptions textureOptions) {
        // Get the character texture data. The character texture must be loaded first before the
        // other properties such as width and height can be accessed.
        final byte[] freeTypeCharTextureBytes = CrispinNativeInterface.loadCharacter(fontBytes, asciiChar,
                fontSize);

        width = CrispinNativeInterface.getFaceWidth();
        height = CrispinNativeInterface.getFaceHeight();
        bearingX = CrispinNativeInterface.getFaceBearingX();
        bearingY = CrispinNativeInterface.getFaceBearingY();
        advance = CrispinNativeInterface.getFaceAdvance();
        this.ascii = asciiChar;
        CrispinNativeInterface.freeFace();

        // Create the texture
        texture = new TextureResource(freeTypeCharTextureBytes, width, height, textureOptions);
    }

    /**
     * Get the width of the character
     *
     * @return The width of the character face
     * @since 1.0
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the character
     *
     * @return The height of the character face
     * @since 1.0
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the advance of the character
     *
     * @return The advance of the character face
     * @since 1.0
     */
    public int getAdvance() {
        return advance;
    }

    /**
     * Get the x-bearing of the character
     *
     * @return The x-bearing of the character face
     * @since 1.0
     */
    public int getBearingX() {
        return bearingX;
    }

    /**
     * Get the y-bearing of the character
     *
     * @return The y-bearing of the character face
     * @since 1.0
     */
    public int getBearingY() {
        return bearingY;
    }

    /**
     * Get the ASCII character
     *
     * @return The ASCII character
     * @since 1.0
     */
    public byte getAscii() {
        return ascii;
    }

    /**
     * Get the texture of the character
     *
     * @return The texture of the character
     * @since 1.0
     */
    public Texture getTexture() {
        return texture;
    }
}