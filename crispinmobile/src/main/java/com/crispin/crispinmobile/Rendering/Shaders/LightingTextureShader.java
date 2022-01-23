package com.crispin.crispinmobile.Rendering.Shaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Utilities.MaterialHandles;
import com.crispin.crispinmobile.Rendering.Utilities.PointLightHandles;
import com.crispin.crispinmobile.Rendering.Utilities.Shader;

/**
 * NormalTextureShader is a built in shader that allows you to handle render objects containing position
 * and normal attributes. It also supports a texture. This allows you to render objects with diffuse
 * lighting.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Shader
 * @since 1.0
 */
public class LightingTextureShader extends Shader {
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.lighting_texture_vert;
    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.lighting_texture_frag;
    // Tag for the logger
    private static final String TAG = "LightingTextureShader";
    // Maximum number of point lights supported by the shader
    private static final int MAX_NUM_POINT_LIGHTS = 4;

    /**
     * Create the NormalTextureShader. This compiles the pre-defined vertex and fragment
     * shader's, and links the attributes to the shader base class for a common form of user
     * interaction.
     *
     * @since 1.0
     */
    public LightingTextureShader() {
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

        // Fragment uniforms
        viewPositionUniformHandle = getUniform("uViewPosition");
        numPointLightsUniformHandle = getUniform("uNumPointLights");

        // Set all the material handles
        materialHandles = new MaterialHandles();
        materialHandles.colourUniformHandle = getUniform("uColour");
        materialHandles.uvMultiplierUniformHandle = getUniform("uUvMultiplier");
        materialHandles.textureUniformHandle = getUniform("uTexture");
        materialHandles.specularMapUniformHandle = getUniform("uSpecularMap");
        materialHandles.normalMapUniformHandle = getUniform("uNormalMap");
        materialHandles.ambientUniformHandle = getUniform("uMaterial.ambient");
        materialHandles.diffuseUniformHandle = getUniform("uMaterial.diffuse");
        materialHandles.specularUniformHandle = getUniform(
                "uMaterial.specular");
        materialHandles.shininessUniformHandle = getUniform(
                "uMaterial.shininess");

        initPointLightHandles();
    }

    private void initPointLightHandles() {
        super.pointLightHandles = new PointLightHandles[MAX_NUM_POINT_LIGHTS];
        for (int i = 0; i < MAX_NUM_POINT_LIGHTS; i++) {
            final String parent = "uPointLights[" + i + "].";
            PointLightHandles pointLightHandles = new PointLightHandles();
            pointLightHandles.positionUniformHandle = getUniform(parent + "position");
            pointLightHandles.colourUniformHandle = getUniform(parent + "colour");
            pointLightHandles.ambientUniformHandle = getUniform(parent + "ambient");
            pointLightHandles.diffuseUniformHandle = getUniform(parent + "diffuse");
            pointLightHandles.specularUniformHandle = getUniform(parent + "specular");
            super.pointLightHandles[i] = pointLightHandles;
        }
    }
}