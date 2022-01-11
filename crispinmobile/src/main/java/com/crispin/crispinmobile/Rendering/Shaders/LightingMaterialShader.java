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
public class LightingMaterialShader extends Shader
{
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.lighting_material_vert;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.lighting_material_frag;

    /**
     * Create the NormalTextureShader. This compiles the pre-defined vertex and fragment
     * shader's, and links the attributes to the shader base class for a common form of user
     * interaction.
     *
     * @since   1.0
     */
    public LightingMaterialShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);

        lightingShader = true;

        positionAttributeHandle = getAttribute("aPosition");
        normalAttributeHandle = getAttribute("aNormal");
        textureAttributeHandle = getAttribute("aTextureCoordinates");
        colourUniformHandle = getUniform("uColour");
        lightPositionUniformHandle = getUniform("uLightPosition");
        viewPositionUniformHandle = getUniform("uViewPosition");
        lightColourUniformHandle = getUniform("uLightColour");
        projectionMatrixUniformHandle = getUniform("uProjection");
        viewMatrixUniformHandle = getUniform("uView");
        modelMatrixUniformHandle = getUniform("uModel");
        textureUniformHandle = getUniform("uTexture");
        uvMultiplierUniformHandle = getUniform("uUvMultiplier");
        specularMapUniformHandle = getUniform("uSpecularMap");
        normalMapUniformHandle = getUniform("uNormalMap");
        lightIntensityUniformHandle = getUniform("uLightIntensity");
        lightAmbienceStrengthHandle = getUniform("uLightAmbienceStrength");
        lightSpecularStrengthHandle = getUniform("uSpecularStrength");
    }
}