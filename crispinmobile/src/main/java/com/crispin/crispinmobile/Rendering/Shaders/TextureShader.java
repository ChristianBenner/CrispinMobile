package com.crispin.crispinmobile.Rendering.Shaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Utilities.Shader;

/**
 * TextureShader is a built in shader designed to render objects with position and texture
 * attributes.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Shader
 * @since       1.0
 */
public class TextureShader extends Shader
{
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.texture_vert;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.texture_frag;

    /**
     * Create the TextureShader. This compiles the pre-defined vertex and fragment shader's, and
     * links the attributes to the shader base class for a common form of user interaction.
     *
     * @since   1.0
     */
    public TextureShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);

        positionAttributeHandle = getAttribute("aPosition");
        textureAttributeHandle = getAttribute("aTextureCoordinates");
        colourUniformHandle = getUniform("uColour");
        projectionMatrixUniformHandle = getUniform("uProjection");
        viewMatrixUniformHandle = getUniform("uView");
        modelMatrixUniformHandle = getUniform("uModel");
        textureUniformHandle = getUniform("uTexture");
        uvOffsetUniformHandle = getUniform("uUVOffset");
        uvMultiplierUniformHandle = getUniform("uUvMultiplier");
    }
}
