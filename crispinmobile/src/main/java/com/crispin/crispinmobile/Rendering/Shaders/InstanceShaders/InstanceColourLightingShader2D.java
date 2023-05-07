package com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.DirectionalLightHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.MaterialHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.PointLightHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.SpotLightHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;

public class InstanceColourLightingShader2D extends Shader {
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.instance_colour_lighting_2d_vert;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.instance_colour_lighting_2d_frag;

    // Maximum number of point lights supported by the shader
    private static final int MAX_NUM_POINT_LIGHTS = 10;

    /**
     * Create the NormalShader. This compiles the pre-defined vertex and fragment
     * shader's, and links the attributes to the shader base class for a common form of user
     * interaction.
     *
     * @since 1.0
     */
    public InstanceColourLightingShader2D() {
        super("Instance Colour Lighting Shader 2D", VERTEX_FILE, FRAGMENT_FILE);

        // Attributes
        positionAttributeHandle = getAttribute("aPosition");
        colourAttributeHandle = getAttribute("aColour");
        modelMatrixAttributeHandle = getAttribute("aModel");

        // Vertex uniforms
        projectionMatrixUniformHandle = getUniform("uProjection");
        viewMatrixUniformHandle = getUniform("uView");

        // Fragment uniforms
        numPointLightsUniformHandle = getUniform("uNumPointLights");

        // Set all the material handles
        materialHandles = new MaterialHandles();
        materialHandles.ambientUniformHandle = getUniform("uMaterial.ambient");
        materialHandles.diffuseUniformHandle = getUniform("uMaterial.diffuse");
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
            pointLightHandles.constantUniformHandle = getUniform(parent + "constant");
            pointLightHandles.linearUniformHandle = getUniform(parent + "linear");
            pointLightHandles.quadraticUniformHandle = getUniform(parent + "quadratic");
            super.pointLightHandles[i] = pointLightHandles;
        }
    }
}