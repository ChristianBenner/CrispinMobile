package com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.MaterialHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;

public class InstanceColourTextureShader extends Shader {
    public InstanceColourTextureShader() {
        super("Instance Colour Texture Shader", R.raw.instance_colour_texture_vert, R.raw.instance_colour_texture_frag);
        positionAttributeHandle = getAttribute("aPosition");
        textureAttributeHandle = getAttribute("aTextureCoordinates");
        colourAttributeHandle = getAttribute("aColour");
        modelMatrixAttributeHandle = getAttribute("aModel");

        materialHandles = new MaterialHandles();
        materialHandles.textureUniformHandle = getUniform("uTexture");

        projectionMatrixUniformHandle = getUniform("uProjection");
        viewMatrixUniformHandle = getUniform("uView");
    }
}