package com.crispin.crispinmobile.Rendering.DefaultMesh;

import com.crispin.crispinmobile.Rendering.Utilities.Mesh;

/**
 * Square class is a default render object model that allows you to render a 2-dimensional square.
 * It contains position and texture data.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
 * @see com.crispin.crispinmobile.UserInterface.Text
 * @since 1.0
 */
public class SquareMesh extends Mesh {
    // The number of position components in the position data (2 because its XYZ)
    private static final byte NUMBER_POSITION_COMPONENTS = 2;

    // The number of texel components in the texel data (2 because its ST)
    private static final byte NUMBER_TEXEL_COMPONENTS = 2;

    // Position vertex data that contains XY components
    private static final float[] POSITION_DATA =
            {
                    0.0f, 1.0f,
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f
            };

    // Texel vertex data that contains ST components
    private static final float[] TEXEL_DATA =
            {
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 0.0f,
                    1.0f, 1.0f
            };

    /**
     * Create a square with specifically allowed data types. This allows you to limit what data is
     * uploaded to the GPU and rendered. This means that a vertex buffer of the minimum size is
     * created when the render object is freeTypeInitialised, minimising the time it takes to a construct a
     * square. This can prove efficient when making high performance software such as particle
     * engines as it allows you to prevent the handling of un-required data.
     *
     * @param textureSupport  True to generate the mesh with texel data, else false
     * @since 1.0
     */
    public SquareMesh(boolean textureSupport) {
        super(POSITION_DATA, textureSupport ? TEXEL_DATA : null, null,
                RenderMethod.TRIANGLES, NUMBER_POSITION_COMPONENTS,
                textureSupport ? NUMBER_TEXEL_COMPONENTS : 0, 0);
    }
}
