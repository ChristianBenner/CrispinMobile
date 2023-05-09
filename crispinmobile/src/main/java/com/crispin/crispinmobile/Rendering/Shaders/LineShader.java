package com.crispin.crispinmobile.Rendering.Shaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.MaterialHandles;

public class LineShader extends Shader {
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.line_vert;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.line_frag;

    // Tag for the logger
    private static final String TAG = "LineShader";

    public LineShader() {
        super(TAG, VERTEX_FILE, FRAGMENT_FILE);

        positionAttributeHandle = getAttribute("aPosition");
        matrixUniformHandle = getUniform("uMatrix");

        materialHandles = new MaterialHandles();
        materialHandles.colourUniformHandle = getUniform("uColour");
    }
}
