package com.crispin.crispinmobile.Rendering.Shaders.Handles;

import static android.opengl.GLES30.glUniform1f;
import static android.opengl.GLES30.glUniform3f;
import static com.crispin.crispinmobile.Rendering.Shaders.Shader.UNDEFINED_HANDLE;

import com.crispin.crispinmobile.Rendering.Entities.SpotLight;

/**
 * SpotLightHandles stores all GLSL shader uniform handles associated to SpotLight properties such
 * as direction, size and attenuation values. The class acts as a data only object - all fields are
 * publicly accessible. The second responsibility of the class is to upload uniform data to OpenGL.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see SpotLight
 * @since 1.0
 */
public class SpotLightHandles {
    // Position uniform handle
    public int positionUniformHandle = UNDEFINED_HANDLE;

    // Direction uniform handle
    public int directionUniformHandle = UNDEFINED_HANDLE;

    // Colour uniform handle
    public int colourUniformHandle = UNDEFINED_HANDLE;

    // Diffuse strength uniform handle
    public int diffuseUniformHandle = UNDEFINED_HANDLE;

    // Ambience strength uniform handle
    public int ambientUniformHandle = UNDEFINED_HANDLE;

    // Specular strength uniform handle
    public int specularUniformHandle = UNDEFINED_HANDLE;

    // Attenuation constant variable uniform handle
    public int constantUniformHandle = UNDEFINED_HANDLE;

    // Attenuation linear variable uniform handle
    public int linearUniformHandle = UNDEFINED_HANDLE;

    // Attenuation quadratic variable uniform handle
    public int quadraticUniformHandle = UNDEFINED_HANDLE;

    // Size of the light (when to begin fading out the light radius of the light)
    public int sizeUniformHandle = UNDEFINED_HANDLE;

    // Outer size of the light (when to end fading out the light radius of the light)
    public int outerSizeUniformHandle = UNDEFINED_HANDLE;

    /**
     * Upload uniform data to each uniform handle from the properties of a given SpotLight
     *
     * @param light SpotLight to upload properties from
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void setUniforms(SpotLight light) {
        if (positionUniformHandle != UNDEFINED_HANDLE) {
            glUniform3f(positionUniformHandle, light.x, light.y, light.z);
        }

        if (directionUniformHandle != UNDEFINED_HANDLE) {
            glUniform3f(directionUniformHandle, light.dx, light.dy, light.dz);
        }

        if (colourUniformHandle != UNDEFINED_HANDLE) {
            glUniform3f(colourUniformHandle, light.red, light.green, light.blue);
        }

        if (ambientUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(ambientUniformHandle, light.ambientStrength);
        }

        if (diffuseUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(diffuseUniformHandle, light.diffuseStrength);
        }

        if (specularUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(specularUniformHandle, light.specularStrength);
        }

        if (constantUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(constantUniformHandle, light.constantAttenuation);
        }

        if (linearUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(linearUniformHandle, light.linearAttenuation);
        }

        if (quadraticUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(quadraticUniformHandle, light.quadraticAttenuation);
        }

        if (sizeUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(sizeUniformHandle, light.size);
        }

        if (outerSizeUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(outerSizeUniformHandle, light.outerSize);
        }
    }
}