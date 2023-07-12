package com.crispin.crispinmobile.Rendering.Shaders.Handles;

import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform3f;
import static com.crispin.crispinmobile.Rendering.Shaders.Shader.UNDEFINED_HANDLE;

import com.crispin.crispinmobile.Rendering.Entities.EmissiveEdge;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;

/**
 * EmissiveEdgeHandles stores all GLSL shader uniform handles associated to EmissiveEdge properties
 * such as position, diffuse strength and attenuation values. The class acts as a data only object -
 * all fields are publicly accessible. The second responsibility of the class is to upload uniform
 * data to OpenGL.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see PointLight
 * @since 1.0
 */
public class EmissiveEdgeHandles {
    // Position for point A uniform handle
    public int positionPointAUniformHandle = UNDEFINED_HANDLE;

    // Position for point B uniform handle
    public int positionPointBUniformHandle = UNDEFINED_HANDLE;

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
     * Upload uniform data to each uniform handle from the properties of a given EmissiveEdge
     *
     * @param emissiveEdge EmissiveEdge to upload properties from
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void setUniforms(EmissiveEdge emissiveEdge) {
        if (positionPointAUniformHandle != UNDEFINED_HANDLE) {
            glUniform3f(positionPointAUniformHandle, emissiveEdge.ax, emissiveEdge.ay, emissiveEdge.az);
        }

        if (positionPointBUniformHandle != UNDEFINED_HANDLE) {
            glUniform3f(positionPointBUniformHandle, emissiveEdge.bx, emissiveEdge.by, emissiveEdge.bz);
        }

        if (colourUniformHandle != UNDEFINED_HANDLE) {
            glUniform3f(colourUniformHandle, emissiveEdge.red, emissiveEdge.green, emissiveEdge.blue);
        }

        if (ambientUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(ambientUniformHandle, emissiveEdge.ambientStrength);
        }

        if (diffuseUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(diffuseUniformHandle, emissiveEdge.diffuseStrength);
        }

        if (specularUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(specularUniformHandle, emissiveEdge.specularStrength);
        }

        if (constantUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(constantUniformHandle, emissiveEdge.constantAttenuation);
        }

        if (linearUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(linearUniformHandle, emissiveEdge.linearAttenuation);
        }

        if (quadraticUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(quadraticUniformHandle, emissiveEdge.quadraticAttenuation);
        }
    }
}