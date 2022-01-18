package com.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform3f;
import static com.crispin.crispinmobile.Rendering.Utilities.Shader.UNDEFINED_HANDLE;

import com.crispin.crispinmobile.Rendering.Entities.PointLight;

public class PointLightHandles {
    // Position uniform handle
    public int positionUniformHandle;

    // Colour uniform handle
    public int colourUniformHandle;

    // Diffuse strength uniform handle
    public int diffuseUniformHandle;

    // Ambience strength uniform handle
    public int ambientUniformHandle;

    // Specular strength uniform handle
    public int specularUniformHandle;

    // Attenuation constant variable uniform handle
    public int constantUniformHandle;

    // Attenuation linear variable uniform handle
    public int linearUniformHandle;

    // Attenuation quadratic variable uniform handle
    public int quadraticUniformHandle;

    public PointLightHandles() {
        positionUniformHandle = UNDEFINED_HANDLE;
        colourUniformHandle = UNDEFINED_HANDLE;
        diffuseUniformHandle = UNDEFINED_HANDLE;
        ambientUniformHandle = UNDEFINED_HANDLE;
        specularUniformHandle = UNDEFINED_HANDLE;
        constantUniformHandle = UNDEFINED_HANDLE;
        linearUniformHandle = UNDEFINED_HANDLE;
        quadraticUniformHandle = UNDEFINED_HANDLE;
    }

    public void setUniforms(PointLight light) {
        if(positionUniformHandle != UNDEFINED_HANDLE) {
            glUniform3f(positionUniformHandle, light.x, light.y, light.z);
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

        if(constantUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(constantUniformHandle, light.attenuationConstant);
        }

        if(linearUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(linearUniformHandle, light.attenuationLinear);
        }

        if(quadraticUniformHandle != UNDEFINED_HANDLE) {
            glUniform1f(quadraticUniformHandle, light.attenuationQuadratic);
        }
    }
}