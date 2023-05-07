package com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.DirectionalLightHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.MaterialHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.PointLightHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.SpotLightHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;

public class InstanceLightingTextureShader2D extends Shader {
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.instance_lighting_texture_2d_vert;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.instance_lighting_texture_2d_frag;

    // Maximum number of point lights supported by the shader
    private static final int MAX_NUM_POINT_LIGHTS = 10;

    /**
     * Create the InstanceLightingTextureShader2D. This compiles the pre-defined vertex and fragment
     * shader's, and links the attributes to the shader base class for a common form of user
     * interaction.
     *
     * @since 1.0
     */
    public InstanceLightingTextureShader2D() {
        super("Instance Lighting Texture Shader 2D", VERTEX_FILE, FRAGMENT_FILE);

        // Attributes
        positionAttributeHandle = getAttribute("aPosition");
        textureAttributeHandle = getAttribute("aTextureCoordinates");
        modelMatrixAttributeHandle = getAttribute("aModel");

        // Vertex uniforms
        projectionMatrixUniformHandle = getUniform("uProjection");
        viewMatrixUniformHandle = getUniform("uView");

        // Fragment uniforms
        numPointLightsUniformHandle = getUniform("uNumPointLights");

        // Set all the material handles
        materialHandles = new MaterialHandles();
        materialHandles.colourUniformHandle = getUniform("uColour");
        materialHandles.uvMultiplierUniformHandle = getUniform("uUvMultiplier");
        materialHandles.textureUniformHandle = getUniform("uTexture");
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