package com.crispin.crispinmobile.Rendering.Shaders;

import static android.opengl.GLES20.glGetUniformfv;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Utilities.DirectionalLightHandles;
import com.crispin.crispinmobile.Rendering.Utilities.MaterialHandles;
import com.crispin.crispinmobile.Rendering.Utilities.PointLightHandles;
import com.crispin.crispinmobile.Rendering.Utilities.Shader;
import com.crispin.crispinmobile.Rendering.Utilities.SpotLightHandles;

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

    // Maximum number of point lights supported by the shader
    private static final int MAX_NUM_POINT_LIGHTS = 10;

    // Maximum number of spot lights supported by the shader
    private static final int MAX_NUM_SPOT_LIGHTS = 5;

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
        viewPositionUniformHandle = getUniform("uViewPosition");
        numPointLightsUniformHandle = getUniform("uNumPointLights");
        numSpotLightsUniformHandle = getUniform("uNumSpotLights");

        // Set all the material handles
        materialHandles = new MaterialHandles();
        materialHandles.colourUniformHandle = getUniform("uColour");
        materialHandles.ambientUniformHandle = getUniform("uMaterial.ambient");
        materialHandles.diffuseUniformHandle = getUniform("uMaterial.diffuse");
        materialHandles.specularUniformHandle = getUniform("uMaterial.specular");
        materialHandles.shininessUniformHandle = getUniform("uMaterial.shininess");

        initDirectionalLightHandles();
        initPointLightHandles();
        initSpotLightHandles();
    }

    private void initDirectionalLightHandles() {
        final String parent = "uDirectionalLight.";
        DirectionalLightHandles directionalLightHandles = new DirectionalLightHandles();
        directionalLightHandles.directionUniformHandle = getUniform(parent + "direction");
        directionalLightHandles.colourUniformHandle = getUniform(parent + "colour");
        directionalLightHandles.ambientUniformHandle = getUniform(parent + "ambient");
        directionalLightHandles.diffuseUniformHandle = getUniform(parent + "diffuse");
        directionalLightHandles.specularUniformHandle = getUniform(parent + "specular");
        super.directionalLightHandles = directionalLightHandles;
    }

    private void initPointLightHandles() {
        super.pointLightHandles = new PointLightHandles[MAX_NUM_POINT_LIGHTS];
        for(int i = 0; i < MAX_NUM_POINT_LIGHTS; i++) {
            final String parent = "uPointLights[" + i + "].";
            PointLightHandles pointLightHandles = new PointLightHandles();
            pointLightHandles.positionUniformHandle = getUniform(parent + "position");
            pointLightHandles.colourUniformHandle = getUniform(parent + "colour");
            pointLightHandles.ambientUniformHandle = getUniform(parent + "ambient");
            pointLightHandles.diffuseUniformHandle = getUniform(parent + "diffuse");
            pointLightHandles.specularUniformHandle = getUniform(parent + "specular");
            pointLightHandles.constantUniformHandle = getUniform(parent + "constant");
            pointLightHandles.linearUniformHandle = getUniform(parent + "linear");
            pointLightHandles.quadraticUniformHandle = getUniform(parent + "quadratic");
            super.pointLightHandles[i] = pointLightHandles;
        }
    }

    private void initSpotLightHandles() {
        super.spotLightHandles = new SpotLightHandles[MAX_NUM_SPOT_LIGHTS];
        for(int i = 0; i < MAX_NUM_SPOT_LIGHTS; i++) {
            final String parent = "uSpotLights[" + i + "].";
            SpotLightHandles spotLightHandles = new SpotLightHandles();
            spotLightHandles.positionUniformHandle = getUniform(parent + "position");
            spotLightHandles.directionUniformHandle = getUniform(parent + "direction");
            spotLightHandles.colourUniformHandle = getUniform(parent + "colour");
            spotLightHandles.ambientUniformHandle = getUniform(parent + "ambient");
            spotLightHandles.diffuseUniformHandle = getUniform(parent + "diffuse");
            spotLightHandles.specularUniformHandle = getUniform(parent + "specular");
            spotLightHandles.constantUniformHandle = getUniform(parent + "constant");
            spotLightHandles.linearUniformHandle = getUniform(parent + "linear");
            spotLightHandles.quadraticUniformHandle = getUniform(parent + "quadratic");
            spotLightHandles.sizeUniformHandle = getUniform(parent + "size");
            spotLightHandles.outerSizeUniformHandle = getUniform(parent + "outerSize");
            super.spotLightHandles[i] = spotLightHandles;
        }
    }
}