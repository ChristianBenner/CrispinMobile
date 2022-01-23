package com.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform3f;
import static com.crispin.crispinmobile.Rendering.Utilities.Shader.UNDEFINED_HANDLE;

import com.crispin.crispinmobile.Rendering.Entities.PointLight;

/**
 * PointLightHandles stores all GLSL shader uniform handles associated to PointLight properties such
 * as position, diffuse strength and attenuation values. The class acts as a data only object - all
 * fields are publicly accessible. The second responsibility of the class is to upload uniform data
 * to OpenGL.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see PointLight
 * @since 1.0
 */
public class PointLightHandles {
    // Position uniform handle
    public int positionUniformHandle = UNDEFINED_HANDLE;

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

    /**
     * Upload uniform data to each uniform handle from the properties of a given PointLight
     *
     * @param light PointLight to upload properties from
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void setUniforms(PointLight light) {
        if (positionUniformHandle != UNDEFINED_HANDLE) {
            glUniform3f(positionUniformHandle, light.x, light.y, light.z);
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
            glUniform1f(constantUniformHandle, light.attenuationConstant);
        }

        if (linearUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(linearUniformHandle, light.attenuationLinear);
        }

        if (quadraticUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(quadraticUniformHandle, light.attenuationQuadratic);
        }
    }
}