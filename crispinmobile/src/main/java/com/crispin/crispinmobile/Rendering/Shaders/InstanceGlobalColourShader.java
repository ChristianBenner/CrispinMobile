package com.crispin.crispinmobile.Rendering.Shaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.MaterialHandles;

public class InstanceGlobalColourShader extends Shader {
        public InstanceGlobalColourShader() {
            super("Global Colour Instance Demo Shader", R.raw.instance_global_colour_vert, R.raw.instance_global_colour_frag);
            positionAttributeHandle = getAttribute("aPosition");
            modelMatrixAttributeHandle = getAttribute("aModel");

            projectionMatrixUniformHandle = getUniform("uProjection");
            viewMatrixUniformHandle = getUniform("uView");

            materialHandles = new MaterialHandles();
            materialHandles.colourUniformHandle = getUniform("uColour");
        }
    }