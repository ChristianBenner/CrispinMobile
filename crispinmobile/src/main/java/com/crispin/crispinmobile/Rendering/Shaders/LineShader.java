package com.crispin.crispinmobile.Rendering.Shaders;

import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Utilities.MaterialHandles;
import com.crispin.crispinmobile.Rendering.Utilities.Shader;

import static android.opengl.GLES20.glUniform1i;

public class LineShader extends Shader
{
    // Tag for the logger
    private static final String TAG = "LineShader";

    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.line_vert;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.line_frag;

    public LineShader()
    {
        super(TAG, VERTEX_FILE, FRAGMENT_FILE);

        positionAttributeHandle = getAttribute("vPosition");
        matrixUniformHandle = getUniform("uMatrix");

        materialHandles = new MaterialHandles();
        materialHandles.colourUniformHandle = getUniform("uColour");
    }
}
