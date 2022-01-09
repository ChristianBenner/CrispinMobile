package com.crispin.crispinmobile.Rendering.Shaders;

import static android.opengl.GLES30.glUniform3f;

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
public class NormalShaderNew extends Shader
{
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.normal_vert_new;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.normal_frag_new;

    // Light position uniform handle
    protected int lightPositionUniformHandle;

    // View position uniform handle
    protected int viewPositionUniformHandle;

    // Light colour uniform handle
    protected int lightColourUniformHandle;

    /**
     * Create the NormalShader. This compiles the pre-defined vertex and fragment
     * shader's, and links the attributes to the shader base class for a common form of user
     * interaction.
     *
     * @since   1.0
     */
    public NormalShaderNew()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);

        lightingShader = true;

        positionAttributeHandle = getAttribute("aPosition");
        normalAttributeHandle = getAttribute("aNormal");
        colourUniformHandle = getUniform("uColour");
        lightPositionUniformHandle = getUniform("uLightPosition");
        viewPositionUniformHandle = getUniform("uViewPosition");
        lightColourUniformHandle = getUniform("uLightColour");
        projectionMatrixUniformHandle = getUniform("uProjection");
        viewMatrixUniformHandle = getUniform("uView");
        modelMatrixUniformHandle = getUniform("uModel");
    }

    public void setLightPosition(float x, float y, float z) {
        glUniform3f(lightPositionUniformHandle, x, y, z);
    }

    public void setViewPosition(float x, float y, float z) {
        glUniform3f(viewPositionUniformHandle, x, y, z);
    }

    public void setLightColour(float r, float g, float b) {
        glUniform3f(lightColourUniformHandle, r, g, b);
    }
}