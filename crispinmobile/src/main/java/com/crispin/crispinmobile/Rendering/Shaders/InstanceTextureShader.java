package com.crispin.crispinmobile.Rendering.Shaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.MaterialHandles;

public class InstanceTextureShader extends Shader {
    public InstanceTextureShader() {
        super("Texture Instance Shader", R.raw.instance_texture_vert, R.raw.instance_texture_frag);
        positionAttributeHandle = getAttribute("aPosition");
        modelMatrixAttributeHandle = getAttribute("aModel");
        textureAttributeHandle = getAttribute("aTextureCoordinates");

        materialHandles = new MaterialHandles();
        materialHandles.textureUniformHandle = getUniform("uTexture");
        materialHandles.colourUniformHandle = getUniform("uColour");

        projectionMatrixUniformHandle = getUniform("uProjection");
        viewMatrixUniformHandle = getUniform("uView");
    }
}