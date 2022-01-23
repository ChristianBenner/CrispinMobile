package com.crispin.crispinmobile.Rendering.Shaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Utilities.MaterialHandles;
import com.crispin.crispinmobile.Rendering.Utilities.Shader;
import com.crispin.crispinmobile.UserInterface.Text;

/**
 * TextShader is a built in shader designed to render the built in text user interface object. The
 * shader supports position and texture attributes.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Text
 * @see Shader
 * @since 1.0
 */
public class TextShader extends Shader {
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.text_vert;
    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.text_frag;
    // Tag for the logger
    private static final String TAG = "TextShader";

    /**
     * Create the TextShader. This compiles the pre-defined vertex and fragment shader's, and links
     * the attributes to the shader base class for a common form of user interaction.
     *
     * @since 1.0
     */
    public TextShader() {
        super(TAG, VERTEX_FILE, FRAGMENT_FILE);

        positionAttributeHandle = getAttribute("vPosition");
        textureAttributeHandle = getAttribute("vTextureCoordinates");
        matrixUniformHandle = getUniform("uMatrix");

        materialHandles = new MaterialHandles();
        materialHandles.colourUniformHandle = getUniform("uColour");
        materialHandles.textureUniformHandle = getUniform("uTexture");
    }
}
