package com.crispin.crispinmobile.Rendering.Shaders.InstanceShaders;

import static android.opengl.GLES20.glUniform2f;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;

/**
 * InstanceShadowShader2D is a built in shader designed to render shadow mesh instances
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Shader
 * @since 1.0
 */
public class InstanceShadowShader2D extends Shader {
    // The resource ID of the vertex file
    public static final int VERTEX_FILE = R.raw.instance_shadow_vert_2d;

    // The resource ID of the fragment file
    public static final int FRAGMENT_FILE = R.raw.instance_shadow_frag_2d;

    // Tag for the logger
    private static final String TAG = "InstanceShadowShader2D";

    // Position of the light
    public int lightPositionUniformHandle = UNDEFINED_HANDLE;

    /**
     * Create the InstanceShadowShader2D. This compiles the pre-defined vertex and fragment shader's,
     * and links the attributes to the shader base class for a common form of user interaction.
     *
     * @since 1.0
     */
    public InstanceShadowShader2D() {
        super(TAG, VERTEX_FILE, FRAGMENT_FILE);
        positionAttributeHandle = getAttribute("aPosition");
        modelMatrixAttributeHandle = getAttribute("aModel");
        projectionMatrixUniformHandle = getUniform("uOrthographic");
        lightPositionUniformHandle = getUniform("uLightPos");
    }

    public void setLightPos(Vec2 lightPos) {
        super.enable();
        glUniform2f(lightPositionUniformHandle, lightPos.x, lightPos.y);
        super.disable();
    }
}
