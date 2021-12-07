package com.crispin.crispinmobile.Rendering.Shaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Utilities.Shader;

/**
 * NormalShader is a built in shader that allows you to handle render objects containing position
 * and normal attributes. This allows you to render objects with diffuse lighting.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Shader
 * @since       1.0
 */
public class NormalShader extends Shader
{
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.normal_vert;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.normal_frag;

    /**
     * Create the NormalShader. This compiles the pre-defined vertex and fragment
     * shader's, and links the attributes to the shader base class for a common form of user
     * interaction.
     *
     * @since   1.0
     */
    public NormalShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);

        lightingShader = true;

        positionAttributeHandle = getAttribute("aPosition");
        normalAttributeHandle = getAttribute("aNormal");
        colourUniformHandle = getUniform("uColour");
        projectionMatrixUniformHandle = getUniform("uProjection");
        viewMatrixUniformHandle = getUniform("uView");
        modelMatrixUniformHandle = getUniform("uModel");
    }
}