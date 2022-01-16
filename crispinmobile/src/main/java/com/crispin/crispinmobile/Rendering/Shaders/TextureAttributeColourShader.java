package com.crispin.crispinmobile.Rendering.Shaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Utilities.Shader;

/**
 * TextureAttributeColourShader is a built in shader designed to render objects with position,
 * colour and texture attributes.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Shader
 * @since       1.0
 */
public class TextureAttributeColourShader extends Shader
{
    // Tag for the logger
    private static final String TAG = "TextureAttributeColourShader";

    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.texture_attribute_colour_vert;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.texture_attribute_colour_frag;

    /**
     * Create the TextureAttributeColourShader. This compiles the pre-defined vertex and fragment
     * shader's, and links the attributes to the shader base class for a common form of user
     * interaction.
     *
     * @since   1.0
     */
    public TextureAttributeColourShader()
    {
        super(TAG, VERTEX_FILE, FRAGMENT_FILE);

        positionAttributeHandle = getAttribute("vPosition");
        colourAttributeHandle = getAttribute("vColour");
        colourUniformHandle = getUniform("uColour");
        textureAttributeHandle = getAttribute("vTextureCoordinates");
        matrixUniformHandle = getUniform("uMatrix");
        uvMultiplierUniformHandle = getUniform("uUvMultiplier");
        textureUniformHandle = getUniform("uTexture");
    }
}
