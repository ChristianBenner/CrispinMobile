package com.games.crispin.crispinmobile.UserInterface;

import com.games.crispin.crispinmobile.Rendering.Data.FreeTypeCharData;
import com.games.crispin.crispinmobile.Rendering.Utilities.TextureOptions;
import com.games.crispin.crispinmobile.Utilities.FileResourceReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES30.GL_LUMINANCE;

/**
 * Font is a class designed to load a set of characters from a font resource file. The class will
 * load a series of characters that can be used by UI components in the library such as Text.
 *
 * @see         com.games.crispin.crispinmobile.UserInterface.Text
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Font
{
    // The index of characters to start loading from
    private static final char ASCII_START_INDEX = 0;

    // The index of characters to load up to
    private static final char ASCII_END_INDEX = 128;

    // Map of characters (ASCII char associated to FreeTypeCharData object)
    private Map<Character, FreeTypeCharData> characters;

    // The size of the loaded font
    private int size;

    /**
     * Load a font using the resource ID of the font file. Also load a list of special characters
     * that are outside of the default range
     *
     * @param resourceId        The font file resource ID
     * @param size              The size of font to load. The larger the size, the higher detail the
     *                          font is, however this uses more video memory (larger textures).
     * @param specialCharacters A list of extra characters to load outside of the default range
     * @since   1.0
     */
    public Font(int resourceId, int size, ArrayList<Character> specialCharacters)
    {
        characters = new HashMap<>();
        this.size = size;

        byte[] sixtyTest;
        sixtyTest = FileResourceReader.readRawResource(resourceId);

        // Texture options of the character textures
        TextureOptions textureOptions = new TextureOptions();
        textureOptions.internalFormat = GL_LUMINANCE;
        textureOptions.monochrome = true;
        textureOptions.format = GL_LUMINANCE;
        textureOptions.minFilter = GL_NEAREST;
        textureOptions.magFilter = GL_NEAREST;

        // Iterate through the ASCII range loading each character into the FreeTypeCharData map
        for(char i = ASCII_START_INDEX; i < ASCII_END_INDEX; i++)
        {
            // Load the character and store in the map
            characters.put(i, new FreeTypeCharData(sixtyTest, size, (byte)i, textureOptions));
        }

        // If special characters have been provided, load them
        if(specialCharacters != null)
        {
            // Load the special characters
            for(Character specialCharacter : specialCharacters)
            {
                // Load the character and store in the map
                characters.put(specialCharacter, new FreeTypeCharData(sixtyTest, size,
                        (byte)((char)specialCharacter), textureOptions));
            }
        }
    }

    /**
     * Load a font using the resource ID of the font file
     *
     * @param resourceId    The font file resource ID
     * @param size          The size of font to load. The larger the size, the higher detail the
     *                      font is, however this uses more video memory (larger textures).
     * @since   1.0
     */
    public Font(int resourceId, int size)
    {
        this(resourceId, size, null);
    }

    /**
     * Get the size of the loaded font
     *
     * @return  The size of the font
     * @since   1.0
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Get a FreeTypeCharData for a specified character
     *
     * @return  The ASCII character to retrieve the FreeTypeCharData of
     * @since   1.0
     */
    public FreeTypeCharData getCharacter(char character)
    {
        return characters.get(character);
    }
}
