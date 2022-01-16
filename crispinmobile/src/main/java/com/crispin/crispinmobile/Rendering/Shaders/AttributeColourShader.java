package com.crispin.crispinmobile.Rendering.Shaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Utilities.Shader;

/**
 * AttributeColourShader is a built in shader that allows you to handle render objects containing
 * position and colour attributes. This allows you to render objects with a colour that is defined
 * per vertex.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Shader
 * @since       1.0
 */
public class AttributeColourShader extends Shader
{
    // Tag for the logger
    private static final String TAG = "AttributeColourShader";

    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.attribute_colour_vert;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.attribute_colour_frag;

    /**
     * Create the AttributeColourShader. This compiles the pre-defined vertex and fragment
     * shader's, and links the attributes to the shader base class for a common form of user
     * interaction.
     *
     * @since   1.0
     */
    public AttributeColourShader()
    {
        super(TAG, VERTEX_FILE, FRAGMENT_FILE);

        positionAttributeHandle = getAttribute("vPosition");
        colourAttributeHandle = getAttribute("vColour");
        matrixUniformHandle = getUniform("uMatrix");
        colourUniformHandle = getUniform("uColour");
    }
}
