package com.crispin.crispinmobile.Rendering.Data;

import static android.opengl.GLES30.GL_INVALID_VALUE;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES30.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES30.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES30.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES30.GL_UNPACK_ALIGNMENT;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glDeleteTextures;
import static android.opengl.GLES30.glGenTextures;
import static android.opengl.GLES30.glGenerateMipmap;
import static android.opengl.GLES30.glPixelStorei;
import static android.opengl.GLES30.glTexImage2D;
import static android.opengl.GLES30.glTexParameteri;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;
import com.crispin.crispinmobile.Utilities.Logger;
import com.crispin.crispinmobile.Utilities.TextureCache;

import java.nio.ByteBuffer;

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
public class TextureResource extends Texture {
    // Tag used in logging
    private static final String TAG = "Texture";

    // Default options to apply to the texture (if none are specified)
    private static final TextureOptions DEFAULT_OPTIONS = new TextureOptions();

    // Value for no resource ID
    private static final int NO_RESOURCE_ID = -1;

    // The alignment value of unpacking monochrome texture data
    private static final int MONOCHROME_PIXEL_UNPACK_ALIGNMENT = 1;

    // The alignment value of unpacking RGBA texture data
    private static final int RGBA_PIXEL_UNPACK_ALIGNMENT = 4;

    // The width of the texture
    private final int width;

    // The height of the texture
    private final int height;

    // The resource ID of the file that the texture is loaded from
    private final int resourceId;

    // Options to apply to the texture (these are used in OpenGL ES texture parameters)
    private final TextureOptions options;

    // ByteBuffer containing the texture bitmap data
    private final ByteBuffer buffer;

    /**
     * Create a texture object using a resource ID and texture options
     *
     * @param resourceId The texture file resource ID
     * @param options    Parameters to apply to the texture
     * @since 1.0
     */
    public TextureResource(int resourceId, TextureOptions options) {
        this.resourceId = resourceId;

        final Bitmap BITMAP = resourceToBitmap(resourceId);

        width = BITMAP.getWidth();
        height = BITMAP.getHeight();
        this.options = options;

        // In the scenario of loading from a resource id (file), we don't have to store a copy of
        // the image data in memory when reloading the program. This is because we know where to
        // find the image data in storage. This is not the case when data is passed as a byte array
        // or a bitmap (in that scenario we are forced to hold onto the image data as it may not
        // be contained within storage).
        buffer = null;

        loadTexture(bitmapToBuffer(BITMAP));

        BITMAP.recycle();
    }

    /**
     * Create a texture object using a bitmap and texture options
     *
     * @param bitmap  Bitmap image to use as texture
     * @param options Parameters to apply to the texture
     * @see Bitmap
     * @since 1.0
     */
    public TextureResource(Bitmap bitmap, TextureOptions options) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        resourceId = NO_RESOURCE_ID;
        this.options = options;
        buffer = bitmapToBuffer(bitmap);

        loadTexture(buffer);

        bitmap.recycle();
    }

    /**
     * Create a texture object using texture data in the form of a byte array
     *
     * @param bytes   Texture data
     * @param width   Width of the texture
     * @param height  Height of the texture
     * @param options Parameters to apply to the texture
     * @since 1.0
     */
    public TextureResource(byte[] bytes, int width, int height, TextureOptions options) {
        this.width = width;
        this.height = height;
        resourceId = NO_RESOURCE_ID;
        this.options = options;
        buffer = arrayToBuffer(bytes);

        loadTexture(buffer);
    }

    /**
     * Create a texture object using a resource ID of an image file. Default options/parameters will
     * be used.
     *
     * @param resourceId The texture file resource ID
     * @since 1.0
     */
    public TextureResource(int resourceId) {
        this(resourceId, DEFAULT_OPTIONS);
    }

    /**
     * Create a texture object using a bitmap. Default options/parameters will be used.
     *
     * @param bitmap Bitmap image to use as texture
     * @see Bitmap
     * @since 1.0
     */
    public TextureResource(Bitmap bitmap) {
        this(bitmap, DEFAULT_OPTIONS);
    }

    /**
     * Create a texture object using texture data in the form of a byte array. Default
     * options/parameters will be used.
     *
     * @param bytes  Texture data
     * @param width  Width of the texture
     * @param height Height of the texture
     * @since 1.0
     */
    public TextureResource(byte[] bytes, int width, int height) {
        this(bytes, width, height, DEFAULT_OPTIONS);
    }

    /**
     * Create a ByteBuffer object from a bitmap object
     *
     * @param bitmap A bitmap object containing the image
     * @return A ByteBuffer containing the texture data
     * @see ByteBuffer
     * @see Bitmap
     * @since 1.0
     */
    private static ByteBuffer bitmapToBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        byteBuffer.rewind();
        return byteBuffer;
    }

    /**
     * Create a ByteBuffer object from a byte array
     *
     * @param byteArray Texture data in the form of a byte array
     * @return A ByteBuffer containing the texture data
     * @see ByteBuffer
     * @since 1.0
     */
    private static ByteBuffer arrayToBuffer(byte[] byteArray) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(byteArray.length);
        byteBuffer.put(byteArray);
        byteBuffer.rewind();
        return byteBuffer;
    }

    /**
     * Load a image resource file from its resource ID and return it as a bitmap object
     *
     * @param resourceId The resource ID of the image file
     * @return A bitmap containing the image in the resource file
     * @see Bitmap
     * @since 1.0
     */
    private static Bitmap resourceToBitmap(int resourceId) {
        Resources resources = Crispin.getApplicationContext().getResources();
        Logger.debug(TAG, "Reading texture resource: ID[" + resourceId + "], Name[" +
                resources.getResourceEntryName(resourceId) + "]");

        final BitmapFactory.Options IMAGE_OPTIONS = new BitmapFactory.Options();
        IMAGE_OPTIONS.inScaled = false;

        final Bitmap BITMAP = BitmapFactory.decodeResource(resources, resourceId, IMAGE_OPTIONS);

        return BITMAP;
    }

    /**
     * Load a given buffer of texture data into video memory
     *
     * @param buffer The texture data as a ByteBuffer. If your texture is not in the form of a
     *               ByteBuffer, use the conversion functions.
     * @see #bitmapToBuffer(Bitmap)
     * @see #arrayToBuffer(byte[])
     * @see #resourceToBitmap(int)
     * @since 1.0
     */
    private void loadTexture(ByteBuffer buffer) {
        int[] glTextureId = new int[1];

        glGenTextures(1, glTextureId, 0);

        // If the texture ID generated by OpenGL ES is invalid, log an error and exit the function
        if (glTextureId[0] == GL_INVALID_VALUE) {
            Logger.error(TAG, "Failed to generate texture handle [GL_INVALID_VALUE]");
            return;
        }

        glTextureID = glTextureId[0];

        glBindTexture(GL_TEXTURE_2D, glTextureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, options.minFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, options.magFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, options.textureWrapS);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, options.textureWrapT);

        // If a texture is monochrome set the unpack alignment to one (because we only have one
        // colour channel)
        if (options.monochrome) {
            glPixelStorei(GL_UNPACK_ALIGNMENT, MONOCHROME_PIXEL_UNPACK_ALIGNMENT);
        }

        glTexImage2D(GL_TEXTURE_2D, 0, options.internalFormat, getWidth(), getHeight(),
                0, options.format, options.type, buffer);

        // Reset back to default value (unpack alignment has initial value of 4)
        if (options.monochrome) {
            glPixelStorei(GL_UNPACK_ALIGNMENT, RGBA_PIXEL_UNPACK_ALIGNMENT);
        }

        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);

        // Register the texture with the shader cache
        TextureCache.registerTexture(resourceId, this);
    }

    /**
     * Re-load the texture using its file resource ID. This can be necessary because OpenGL ES
     * controlled memory gets destroyed on an Android activity resume, so textures must be reloaded
     * to be seen again.
     *
     * @since 1.0
     */
    @Override
    public void reload() {
        // Reload the texture to graphics memory
        if (resourceId == NO_RESOURCE_ID) {
            // The byte buffer contains the memory of the texture
            loadTexture(buffer);
        } else {
            // Using the resource ID, reload the texture
            loadTexture(bitmapToBuffer(resourceToBitmap(resourceId)));
        }
    }

    /**
     * Get the texture width in pixels
     *
     * @return The width of the texture in pixels
     * @since 1.0
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Get the texture height in pixels
     *
     * @return The height of the texture in pixels
     * @since 1.0
     */
    @Override
    public int getHeight() {
        return height;
    }
}
