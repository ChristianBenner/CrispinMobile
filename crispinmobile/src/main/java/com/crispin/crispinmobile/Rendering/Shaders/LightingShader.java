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
public class LightingShader extends Shader
{
    // Tag for the logger
    private static final String TAG = "LightingShader";

    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.lighting_vert;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.lighting_frag;

    /**
     * Create the NormalShader. This compiles the pre-defined vertex and fragment
     * shader's, and links the attributes to the shader base class for a common form of user
     * interaction.
     *
     * @since   1.0
     */
    public LightingShader()
    {
        super(TAG, VERTEX_FILE, FRAGMENT_FILE);

        lightingShader = true;

        // Attributes
        positionAttributeHandle = getAttribute("aPosition");
        normalAttributeHandle = getAttribute("aNormal");

        // Vertex uniforms
        projectionMatrixUniformHandle = getUniform("uProjection");
        viewMatrixUniformHandle = getUniform("uView");
        modelMatrixUniformHandle = getUniform("uModel");

        // Fragment uniforms
        colourUniformHandle = getUniform("uColour");
        viewPositionUniformHandle = getUniform("uViewPosition");
        materialAmbientUniformHandle = getUniform("uMaterial.ambient");
        materialDiffuseUniformHandle = getUniform("uMaterial.diffuse");
        materialSpecularUniformHandle = getUniform("uMaterial.specular");
        materialShininessUniformHandle = getUniform("uMaterial.shininess");
        lightPositionUniformHandle = getUniform("uLight.position");
        lightColourUniformHandle = getUniform("uLight.colour");
        lightIntensityUniformHandle = getUniform("uLight.intensity");
        lightAmbienceStrengthHandle = getUniform("uLight.ambient");
        lightSpecularStrengthHandle = getUniform("uLight.specular");
    }
}