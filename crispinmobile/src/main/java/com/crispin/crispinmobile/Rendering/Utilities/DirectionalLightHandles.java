package com.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform3f;
import static com.crispin.crispinmobile.Rendering.Utilities.Shader.UNDEFINED_HANDLE;

import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;

public class DirectionalLightHandles {
    // Direction uniform handle
    public int directionUniformHandle;

    // Colour uniform handle
    public int colourUniformHandle;

    // Diffuse strength uniform handle
    public int diffuseUniformHandle;

    // Ambience strength uniform handle
    public int ambientUniformHandle;

    // Specular strength uniform handle
    public int specularUniformHandle;

    public DirectionalLightHandles() {
        directionUniformHandle = UNDEFINED_HANDLE;
        colourUniformHandle = UNDEFINED_HANDLE;
        diffuseUniformHandle = UNDEFINED_HANDLE;
        ambientUniformHandle = UNDEFINED_HANDLE;
        specularUniformHandle = UNDEFINED_HANDLE;
    }

    public void setUniforms(DirectionalLight light) {
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