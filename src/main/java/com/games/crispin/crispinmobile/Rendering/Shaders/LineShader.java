package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

import static android.opengl.GLES20.glUniform1i;

public class LineShader extends Shader
{
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.line_vert;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.line_frag;

    public LineShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);

        positionAttributeHandle = getAttribute("vPosition");
        matrixUniformHandle = getUniform("uMatrix");
        colourUniformHandle = getUniform("uColour");
    }
}
