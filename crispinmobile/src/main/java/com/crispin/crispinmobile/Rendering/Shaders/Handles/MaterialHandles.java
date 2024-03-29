package com.crispin.crispinmobile.Rendering.Shaders.Handles;

import static android.opengl.GLES30.GL_TEXTURE0;
import static android.opengl.GLES30.GL_TEXTURE1;
import static android.opengl.GLES30.GL_TEXTURE2;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.glActiveTexture;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glUniform1f;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.glUniform2f;
import static android.opengl.GLES30.glUniform3f;
import static android.opengl.GLES30.glUniform4f;
import static com.crispin.crispinmobile.Rendering.Shaders.Shader.UNDEFINED_HANDLE;

import com.crispin.crispinmobile.Rendering.Data.Material;


/**
 * MaterialHandles stores all GLSL shader uniform handles associated to material properties such as
 * colour, texture and shininess. The class acts as a data only object - all fields are publicly
 * accessible. The second responsibility of the class is to upload uniform data to OpenGL.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Material
 * @since 1.0
 */
public class MaterialHandles {
    // Colour uniform handle
    public int colourUniformHandle = UNDEFINED_HANDLE;

    // UV multiplier uniform handle
    public int uvMultiplierUniformHandle = UNDEFINED_HANDLE;

    // UV offset uniform handle
    public int uvOffsetUniformHandle = UNDEFINED_HANDLE;

    // Texture uniform handle
    public int textureUniformHandle = UNDEFINED_HANDLE;

    // Diffuse map uniform handle
    public int diffuseMapUniformHandle = UNDEFINED_HANDLE;

    // Specular map uniform handle
    public int specularMapUniformHandle = UNDEFINED_HANDLE;

    // Normal map uniform handle
    public int normalMapUniformHandle = UNDEFINED_HANDLE;

    // Diffuse strength uniform handle
    public int diffuseUniformHandle = UNDEFINED_HANDLE;

    // Ambience strength uniform handle
    public int ambientUniformHandle = UNDEFINED_HANDLE;

    // Specular strength uniform handle
    public int specularUniformHandle = UNDEFINED_HANDLE;

    // Shininess uniform handle
    public int shininessUniformHandle = UNDEFINED_HANDLE;

    /**
     * Upload uniform data to each uniform handle from the properties of a given material
     *
     * @param material Material to upload properties from
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void setUniforms(final Material material) {
        if (colourUniformHandle != UNDEFINED_HANDLE) {
            glUniform4f(colourUniformHandle, material.colour.red, material.colour.green,
                    material.colour.blue, material.colour.alpha);
        }

        if (uvMultiplierUniformHandle != UNDEFINED_HANDLE) {
            glUniform2f(uvMultiplierUniformHandle, material.uvMultiplier.w,
                    material.uvMultiplier.h);
        }

        if (uvOffsetUniformHandle != UNDEFINED_HANDLE) {
            glUniform2f(uvOffsetUniformHandle, material.uvOffset.x, material.uvOffset.y);
        }

        if (textureUniformHandle != UNDEFINED_HANDLE && material.hasTexture()) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, material.texture.getGlTextureID());
            glUniform1i(textureUniformHandle, 0);
        }

        if (diffuseMapUniformHandle != UNDEFINED_HANDLE && material.hasDiffuseMap()) {
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, material.diffuseMap.getGlTextureID());
            glUniform1i(diffuseMapUniformHandle, 1);
        }

        if (specularMapUniformHandle != UNDEFINED_HANDLE && material.hasSpecularMap()) {
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, material.specularMap.getGlTextureID());
            glUniform1i(specularMapUniformHandle, 1);
        }

        if (normalMapUniformHandle != UNDEFINED_HANDLE && material.hasNormalMap()) {
            glActiveTexture(GL_TEXTURE2);
            glBindTexture(GL_TEXTURE_2D, material.normalMap.getGlTextureID());
            glUniform1i(normalMapUniformHandle, 2);
        }

        if (ambientUniformHandle != UNDEFINED_HANDLE) {
            glUniform3f(ambientUniformHandle, material.ambientStrength.red,
                    material.ambientStrength.green, material.ambientStrength.blue);
        }

        if (diffuseUniformHandle != UNDEFINED_HANDLE) {
            glUniform3f(diffuseUniformHandle, material.diffuseStrength.red,
                    material.diffuseStrength.green, material.diffuseStrength.blue);
        }

        if (specularUniformHandle != UNDEFINED_HANDLE) {
            glUniform3f(specularUniformHandle, material.specularStrength.red,
                    material.specularStrength.green, material.specularStrength.blue);
        }

        if (shininessUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(shininessUniformHandle, material.shininess);
        }
    }
}
