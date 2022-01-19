package com.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform3f;
import static com.crispin.crispinmobile.Rendering.Utilities.Shader.UNDEFINED_HANDLE;

import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;

/**
 * DirectionalLightHandles stores all GLSL shader uniform handles associated to DirectionalLight
 * properties such as position, direction and colour. The class acts as a data only object - all
 * fields are publicly accessible. The second responsibility of the class is to upload uniform data
 * to OpenGL.
 *
 * @see         PointLight
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class DirectionalLightHandles {
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

    /**
     * Upload uniform data to each uniform handle from the properties of a given DirectionalLight
     *
     * @param light DirectionalLight to upload properties from
     * @author      Christian Benner
     * @version     %I%, %G%
     * @since       1.0
     */
    public void setUniforms(final DirectionalLight light) {
        if(directionUniformHandle != UNDEFINED_HANDLE) {
            glUniform3f(directionUniformHandle, light.dx, light.dy, light.dz);
        }

        if(colourUniformHandle != UNDEFINED_HANDLE) {
            glUniform3f(colourUniformHandle, light.red, light.green, light.blue);
        }

        if(ambientUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(ambientUniformHandle, light.ambientStrength);
        }

        if(diffuseUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(diffuseUniformHandle, light.diffuseStrength);
        }

        if(specularUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(specularUniformHandle, light.specularStrength);
        }
    }
}