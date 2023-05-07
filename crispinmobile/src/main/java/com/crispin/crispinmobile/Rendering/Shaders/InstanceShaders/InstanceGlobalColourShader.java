package com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.MaterialHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;

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