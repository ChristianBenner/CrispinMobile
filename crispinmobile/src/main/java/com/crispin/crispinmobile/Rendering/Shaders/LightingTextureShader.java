package com.crispin.crispinmobile.Rendering.Shaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Utilities.Shader;

/**
 * NormalTextureShader is a built in shader that allows you to handle render objects containing position
 * and normal attributes. It also supports a texture. This allows you to render objects with diffuse
 * lighting.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Shader
 * @since       1.0
 */
public class LightingTextureShader extends Shader
{
    // Tag for the logger
    private static final String TAG = "LightingTextureShader";

    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.lighting_texture_vert;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.lighting_texture_frag;

    /**
     * Create the NormalTextureShader. This compiles the pre-defined vertex and fragment
     * shader's, and links the attributes to the shader base class for a common form of user
     * interaction.
     *
     * @since   1.0
     */
    public LightingTextureShader()
    {
        super(TAG, VERTEX_FILE, FRAGMENT_FILE);

        lightingShader = true;

        // Attributes
        positionAttributeHandle = getAttribute("aPosition");
        normalAttributeHandle = getAttribute("aNormal");
        textureAttributeHandle = getAttribute("aTextureCoordinates");

        // Vertex uniforms
        projectionMatrixUniformHandle = getUniform("uProjection");
        viewMatrixUniformHandle = getUniform("uView");
        modelMatrixUniformHandle = getUniform("uModel");
        uvMultiplierUniformHandle = getUniform("uUvMultiplier");

        // Fragment uniforms
        colourUniformHandle = getUniform("uColour");
        viewPositionUniformHandle = getUniform("uViewPosition");
        textureUniformHandle = getUniform("uTexture");
        specularMapUniformHandle = getUniform("uSpecularMap");
        normalMapUniformHandle = getUniform("uNormalMap");
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