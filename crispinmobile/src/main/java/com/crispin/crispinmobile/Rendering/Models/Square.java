package com.crispin.crispinmobile.Rendering.Models;

import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;

/**
 * Square class is a default render object model that allows you to render a 2-dimensional square.
 * It contains position and texture data.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
 * @see Square
 * @see com.crispin.crispinmobile.UserInterface.Text
 * @since 1.0
 */
public class Square extends Model {
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

    // Normal vertex data that contains XYZ components
    private static final float[] NORMAL_DATA =
            {

            };

    /**
     * Create a square with specifically allowed data types. This allows you to limit what data is
     * uploaded to the GPU and rendered. This means that a vertex buffer of the minimum size is
     * created when the render object is freeTypeInitialised, minimising the time it takes to a construct a
     * square. This can prove efficient when making high performance software such as particle
     * engines as it allows you to prevent the handling of un-required data.
     *
     * @param material     A material to apply to the rendered object
     * @param renderTexels True if the model is allowed to use texel data, else false
     * @since 1.0
     */
    public Square(Material material, boolean renderTexels) {
        super(POSITION_DATA, renderTexels ? TEXEL_DATA : null, null,
                Mesh.RenderMethod.TRIANGLES, NUMBER_POSITION_COMPONENTS,
                renderTexels ? NUMBER_TEXEL_COMPONENTS : 0,
                (byte)0, material);
    }

    /**
     * Create a square with specifically allowed data types. This allows you to limit what data is
     * uploaded to the GPU and rendered. This means that a vertex buffer of the minimum size is
     * created when the render object is freeTypeInitialised, minimising the time it takes to a construct a
     * square. This can prove efficient when making high performance software such as particle
     * engines as it allows you to prevent the handling of un-required data.
     *
     * @param renderTexels True if the model is allowed to use texel data, else false
     * @since 1.0
     */
    public Square(boolean renderTexels) {
        this(new Material(), renderTexels);
    }

    /**
     * Create a square render object. By default the object supports textures, meaning that texel
     * data is included in the vertex buffer construction. If not using textures on the object it
     * may be more efficient to construct an object without texel data using a different square
     * constructor.
     *
     * @param material A material to apply to the rendered object
     * @since 1.0
     */
    public Square(Material material) {
        this(material, material.hasTexture());
    }

    /**
     * Create a square render object with a texture resource
     *
     * @param textureResource Texture resource handle
     * @since 1.0
     */
    public Square(int textureResource) {
        this(new Material(textureResource), true);
    }

    /**
     * Create a square render object. By default the object supports textures, meaning that texel
     * data is included in the vertex buffer construction. If not using textures on the object it
     * may be more efficient to construct an object without texel data using a different square
     * constructor.
     *
     * @since 1.0
     */
    public Square() {
        this(new Material(), true);
    }
}
