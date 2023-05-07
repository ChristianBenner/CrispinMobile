package com.crispin.crispinmobile.Rendering.Shaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.MaterialHandles;

/**
 * UniformColourShader is a built in shader designed to render objects with a position attribute. It
 * also allows a uniform colour to be applied to that object - which appears as a solid colour
 * across the object.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Shader
 * @since 1.0
 */
public class UniformColourShader extends Shader {
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.uniform_colour_vert;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.uniform_colour_frag;

    // Tag for the logger
    private static final String TAG = "UniformColourShader";

    /**
     * Create the UniformColourShader. This compiles the pre-defined vertex and fragment shader's,
     * and links the attributes to the shader base class for a common form of user interaction.
     *
     * @since 1.0
     */
    public UniformColourShader() {
        super(TAG, VERTEX_FILE, FRAGMENT_FILE);

        positionAttributeHandle = getAttribute("aPosition");
        matrixUniformHandle = getUniform("uMatrix");

        materialHandles = new MaterialHandles();
        materialHandles.colourUniformHandle = getUniform("uColour");
    }
}
