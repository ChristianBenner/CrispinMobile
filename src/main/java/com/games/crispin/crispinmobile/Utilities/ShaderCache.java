package com.games.crispin.crispinmobile.Utilities;

import android.util.Pair;

import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Cache shader programs so that they are only loaded once no matter how many times the shader file
 * resource is referenced. This is used to significantly reduce loading times of scenes, reduce
 * memory and reduce video memory. The class consists of static only functions.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class ShaderCache
{
    // Tag used for logging
    private static final String TAG = "ShaderCache";

    // The array of shader programs
    private static Map<Integer, Shader> shaderArray = new HashMap<>();

    // Pairing function
    private static int generateUniqueId(int a, int b)
    {
        return (((a + b) * (a + b + 1)) / 2) + b;
    }

    public static boolean existsInCache(int vertexShaderResourceId,
                                  int fragmentShaderResourceId)
    {
        return shaderArray.containsKey(generateUniqueId(vertexShaderResourceId,
                fragmentShaderResourceId));
    }

    public static Shader getShader(int vertexShaderResourceId,
                                   int fragmentShaderResourceId)
    {
        return shaderArray.get(generateUniqueId(vertexShaderResourceId,
                fragmentShaderResourceId));
    }

    /**
     * Register a shader program in the cache
     *
     * @since 1.0
     */
    public static void registerShader(int vertexShaderResource,
                                      int fragmentShaderResource,
                                      Shader shader)
    {
        shaderArray.put(generateUniqueId(vertexShaderResource, fragmentShaderResource), shader);
    }

    /**
     * Remove all of the shader programs from the cache
     *
     * @since 1.0
     */
    public static void removeAll()
    {
        // Iterate through the shader array, destroying all of the shaders. This will also remove
        // shader from OpenGL controlled memory.
        for (Map.Entry<Integer,Shader> shader : shaderArray.entrySet())
        {
            shader.getValue().destroy();
        }

        shaderArray.clear();
    }

    /**
     * Re-init all of the shader programs in the cache. Because OpenGL controlled memory is cleared
     * when an application is stopped/paused, this function provides an easy way to re-initialise
     * the shader programs.
     *
     * @since 1.0
     */
    public static void reinitialiseAll()
    {
        // Iterate through the shader array, reconstructing all of them
        for (Map.Entry<Integer,Shader> shader : shaderArray.entrySet())
        {
            // Attempt to reconstruct
            try
            {
                shader.getValue().reconstruct();
            }
            catch(Exception e)
            {
                Logger.error(TAG, "Failed to re-initialise shader");
                e.printStackTrace();
            }
        }
    }
}
