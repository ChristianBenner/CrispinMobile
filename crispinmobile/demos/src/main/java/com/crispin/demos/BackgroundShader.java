package com.crispin.demos;

import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform2f;

import com.crispin.crispinmobile.Rendering.Shaders.Shader;

public class BackgroundShader extends Shader {
    private int timeUniformHandle = UNDEFINED_HANDLE;
    private int centerUniformHandle = UNDEFINED_HANDLE;

    public BackgroundShader(int vertResource, int fragResource) {
        super("BackgroundShader", vertResource, fragResource);

        positionAttributeHandle = getAttribute("aPosition");
        matrixUniformHandle = getUniform("uMatrix");

        timeUniformHandle = getUniform("uTime");
        centerUniformHandle = getUniform("uCenter");
    }

    public void setTime(float time) {
        super.enable();
        glUniform1f(timeUniformHandle, time);
        super.disable();
    }

    public void setCenter(float x, float y) {
        super.enable();
        glUniform2f(centerUniformHandle, x, y);
        super.disable();
    }
}