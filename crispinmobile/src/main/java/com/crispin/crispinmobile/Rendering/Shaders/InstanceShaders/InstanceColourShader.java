package com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;

public class InstanceColourShader extends Shader {
    public InstanceColourShader() {
        super("Instance Colour Shader", R.raw.instance_colour_vert, R.raw.instance_colour_frag);
        positionAttributeHandle = getAttribute("aPosition");
        colourAttributeHandle = getAttribute("aColour");
        modelMatrixAttributeHandle = getAttribute("aModel");

        projectionMatrixUniformHandle = getUniform("uProjection");
        viewMatrixUniformHandle = getUniform("uView");
    }
}