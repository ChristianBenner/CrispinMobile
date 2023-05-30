package com.crispin.crispinmobile.Rendering.Shaders.TwoDimensional;

import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES30.GL_TEXTURE_2D_ARRAY;

import android.opengl.GLES30;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.DirectionalLightHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.MaterialHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.PointLightHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;

/**
 * TextureShader is a built in shader designed to render objects with position and texture
 * attributes.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Shader
 * @since 1.0
 */
public class LightingShadowTextureShader2D extends Shader {
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.lighting_texture_vert_2d;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.lighting_shadows_texture_frag_2d;

    // Maximum number of point lights supported by the shader
    private static final int MAX_NUM_POINT_LIGHTS = 10;

    // Tag for the logger
    private static final String TAG = "LightingShadowTextureShader2D";

    /**
     * Create the TextureShader. This compiles the pre-defined vertex and fragment shader's, and
     * links the attributes to the shader base class for a common form of user interaction.
     *
     * @since 1.0
     */
    public LightingShadowTextureShader2D() {
        super(TAG, VERTEX_FILE, FRAGMENT_FILE);
        positionAttributeHandle = getAttribute("aPosition");
        textureAttributeHandle = getAttribute("aTextureCoordinates");

        materialHandles = new MaterialHandles();
        materialHandles.colourUniformHandle = getUniform("uColour");
        materialHandles.uvMultiplierUniformHandle = getUniform("uUvMultiplier");
        materialHandles.uvOffsetUniformHandle = getUniform("uUVOffset");
        materialHandles.textureUniformHandle = getUniform("uTexture");
        materialHandles.ambientUniformHandle = getUniform("uMaterial.ambient");
        materialHandles.diffuseUniformHandle = getUniform("uMaterial.diffuse");

        viewMatrixUniformHandle = getUniform("uView");
        viewDimensionUniformHandle = getUniform("uViewDimension");
        modelMatrixUniformHandle = getUniform("uModel");

        shadowTextureUniformHandle = getUniform("uShadow");

        // Fragment uniforms
        numPointLightsUniformHandle = getUniform("uNumPointLights");

        final String parent = "uDirectionalLight.";
        DirectionalLightHandles directionalLightHandles = new DirectionalLightHandles();
        directionalLightHandles.directionUniformHandle = getUniform(parent + "direction");
        directionalLightHandles.colourUniformHandle = getUniform(parent + "colour");
        directionalLightHandles.ambientUniformHandle = getUniform(parent + "ambient");
        directionalLightHandles.diffuseUniformHandle = getUniform(parent + "diffuse");
        directionalLightHandles.specularUniformHandle = getUniform(parent + "specular");
        super.directionalLightHandles = directionalLightHandles;
        initPointLightHandles();
    }

    public void setShadowTexture(int textureHandle) {
        super.enable();
        glActiveTexture(GLES30.GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D_ARRAY, textureHandle);
        glUniform1i(shadowTextureUniformHandle, 3);
        super.disable();
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
