package com.games.crispin.crispinmobile.Utilities;

import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Cache textures so that they are only loaded once no matter how many times the texture file
 * resource is referenced. This is used to significantly reduce loading times of scenes, reduce
 * memory and reduce video memory. The class consists of static only functions.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class TextureCache
{
    // Tag used for logging
    private static final String TAG = "TextureCache";

    // Array of textures
    private static Map<Integer, Texture> textures = new HashMap<>();

    public static Texture loadTexture(int resourceId)
    {
        if(containsTexture(resourceId))
        {
            return getTexture(resourceId);
        }

        Texture newTexture = new Texture(resourceId);
        return newTexture;
    }

    public static boolean containsTexture(int resourceId)
    {
        return textures.containsKey(resourceId);
    }

    public static Texture getTexture(int resourceId)
    {
        return textures.get(resourceId);
    }

    /**
     * Register a texture in the cache
     *
     * @since 1.0
     */
    public static void registerTexture(int resourceId, Texture texture)
    {
        textures.put(resourceId, texture);
    }

    /**
     * Remove all of the textures from the cache
     *
     * @since 1.0
     */
    public static void removeAll()
    {
        // Remove the texture from graphics memory
        for (Map.Entry<Integer,Texture> texture : textures.entrySet())
        {
            texture.getValue().destroy();
        }

        textures.clear();
    }

    /**
     * Re-init all of the textures in the cache. Because OpenGL controlled memory is cleared
     * when an application is stopped/paused, this function provides an easy way to re-initialise
     * the textures (without having to construct them again causing render objects with textures to
     * loose their references to those textures)
     *
     * @since 1.0
     */
    public static void reinitialiseAll()
    {
        // Iterate through the textures array, re-initialising all of them
        for (Map.Entry<Integer,Texture> texture : textures.entrySet())
        {
            // Attempt to reload the texture
            try
            {
                texture.getValue().reload();
            }
            catch(Exception e)
            {
                Logger.error(TAG, "Failed to re-initialise texture");
                e.printStackTrace();
            }
        }
    }
}
