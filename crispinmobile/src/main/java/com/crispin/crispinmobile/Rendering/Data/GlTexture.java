package com.crispin.crispinmobile.Rendering.Data;

import com.crispin.crispinmobile.Rendering.Utilities.Mesh;
import com.crispin.crispinmobile.Utilities.TextureCache;

/**
 * GlTexture is used to create a Texture object from an OpenGL texture identifier.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
 * @see TextureCache
 * @since 1.0
 */
public class GlTexture extends Texture{
    // Tag used in logging
    private static final String TAG = "GlTexture";

    private int width;
    private int height;

    public GlTexture(int glTextureID, int width, int height) {
        super.glTextureID = glTextureID;
        this.width = width;
        this.height = height;
    }

    // need a reload func passed in constructor to rebuild this object?
    @Override
    public void reload() {

    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
