package com.crispin.crispinmobile.Rendering.Data;

import static android.opengl.GLES30.GL_INVALID_VALUE;
import static android.opengl.GLES30.glDeleteTextures;
import static android.opengl.GLES30.glGenTextures;

import com.crispin.crispinmobile.Rendering.Utilities.Mesh;
import com.crispin.crispinmobile.Utilities.TextureCache;

/**
 * Texture class contains texture data and information about that texture such as width and height.
 * It is used around CrispinEngine such as in RenderObjects so that you can render models or UI with
 * textures. It interacts with the TextureCache class so that the same resource isn't loaded more
 * than once.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
 * @see TextureCache
 * @since 1.0
 */
public abstract class Texture {
    // Tag used in logging
    private static final String TAG = "Texture";

    // The texture ID provided by OpenGL ES
    protected int glTextureID;

//    /**
//     * Create a texture object using a GL texture ID
//     *
//     * @param glTextureID The OpenGLES texture ID
//     * @since 1.0
//     */
//    public Texture(int glTextureID) {
//        this.glTextureID = glTextureID;
//    }
//
//    /**
//     * Default constructor
//     *
//     * @since 1.0
//     */
//    public Texture() {
//        this.glTextureID = GL_INVALID_VALUE;
//    }

    // Do not allow construction of this object, must be inherited
    protected Texture(){}

    /**
     * Get the texture ID provided by OpenGL ES
     *
     * @return The texture ID provided by OpenGL ES
     * @since 1.0
     */
    public int getGlTextureID() {
        return glTextureID;
    }

    /**
     * Remove the texture from video memory. The texture will no longer be in use and therefore not
     * visible on any bound objects.
     *
     * @since 1.0
     */
    public void destroy() {
        int[] TEMP_BUFFER = new int[1];
        TEMP_BUFFER[0] = glTextureID;

        // Remove the texture from video memory
        glDeleteTextures(1, TEMP_BUFFER, 0);

        glTextureID = GL_INVALID_VALUE;
    }

    /**
     * Re-load the texture using its file resource ID. This can be necessary because OpenGL ES
     * controlled memory gets destroyed on an Android activity resume, so textures must be reloaded
     * to be seen again. To be implemented by inheriting classes as it may differ how different
     * texture types are reloaded
     *
     * @since 1.0
     */
    public abstract void reload();

    /**
     * Get the texture width in pixels
     *
     * @return The width of the texture in pixels
     * @since 1.0
     */
    public abstract int getWidth();

    /**
     * Get the texture height in pixels
     *
     * @return The height of the texture in pixels
     * @since 1.0
     */
    public abstract int getHeight();
}
