package com.crispin.crispinmobile.Rendering.Shaders.TwoDimensional;

import static android.opengl.GLES20.glUniform2f;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;

/**
 * TextureShader is a built in shader designed to render objects with position and texture
 * attributes.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Shader
 * @since 1.0
 */
public class ShadowLengthShader2D extends Shader {
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.shadow_with_length_vert_2d;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.shadow_with_length_frag_2d;

    // Tag for the logger
    private static final String TAG = "ShadowLengthShader2D";

    // Position of the light
    public int lightPositionUniformHandle = UNDEFINED_HANDLE;

    /**
     * Create the TextureShader. This compiles the pre-defined vertex and fragment shader's, and
     * links the attributes to the shader base class for a common form of user interaction.
     *
     * @since 1.0
     */
    public ShadowLengthShader2D() {
        super(TAG, VERTEX_FILE, FRAGMENT_FILE);
        positionAttributeHandle = getAttribute("aPosition");
        viewMatrixUniformHandle = getUniform("uView");
        modelMatrixUniformHandle = getUniform("uModel");
        lightPositionUniformHandle = getUniform("uLightPos");
    }

    public void setLightPos(Vec2 lightPos) {
        super.enable();
        glUniform2f(lightPositionUniformHandle, lightPos.x, lightPos.y);
        super.disable();
    }
}
