package com.crispin.crispinmobile.MeshLoading;

import android.content.res.Resources;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;
import com.crispin.crispinmobile.Utilities.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Read material template library files (.mtl)
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
 * @since 1.0
 */
public class MaterialTemplateLibraryLoader {
    // Tag used in logging output
    private static final String TAG = "MaterialTemplateLibraryLoader";

    // The number to divide nanoseconds by to get milliseconds
    private static final int NANOSECONDS_TO_MILLISECONDS_DIVIDE = 1000000;

    private static final int TYPE_INDEX = 0;
    private static final String TYPE_NEW_MATERIAL = "newmtl";
    private static final String TYPE_AMBIENT_COLOUR = "Ka";
    private static final String TYPE_SPECULAR_COLOUR = "Ks";
    private static final String TYPE_DIFFUSE_COLOUR = "Kd";
    private static final String TYPE_SPECULAR_EXPONENT = "Ns";
    private static final String TYPE_AMBIENT_TEXTURE_MAP = "map_Ka";
    private static final String TYPE_DIFFUSE_TEXTURE_MAP = "map_Kd";
    private static final String TYPE_SPECULAR_COLOUR_TEXTURE_MAP = "map_Ks";
    private static final String TYPE_SPECULAR_HIGHLIGHT_COMPONENT = "map_Ns";
    private static final String TYPE_NORMAL_TEXTURE_MAP = "norm";

    /**
     * Read an material template library resource
     *
     * @param resourceId The OBJ model file resource ID
     * @return A RenderObject built from the model data. The model can be rendered to a scene
     * @see Mesh
     * @since 1.0
     */
    public static MaterialTemplateLibrary read(int resourceId) {
        // Attempt to open and read an OBJ file
        try {
            // Measure how long it takes to load load and read the model file
            long start = System.nanoTime();

            Resources resources = Crispin.getApplicationContext().getResources();
            InputStream inputStream;
            inputStream = resources.openRawResource(resourceId);
            inputStream.reset();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            MaterialTemplateLibrary materialTemplateLibrary = process(reader);

            // End of time measurement
            long end = System.nanoTime();

            // Log time taken to load the OBJ model
            Logger.debug(TAG, "Model Loaded in: " + ((end - start) /
                    NANOSECONDS_TO_MILLISECONDS_DIVIDE) + "ms");

            return materialTemplateLibrary;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Colour parseColour(String[] split, int startIndex) {
        if(split.length - startIndex < 3) {
            Logger.error(TAG, "Expected 3 colour components for ambient colour");
        }

        Colour colour = new Colour();
        colour.red = Float.parseFloat(split[startIndex]);
        colour.green = Float.parseFloat(split[startIndex + 1]);
        colour.blue = Float.parseFloat(split[startIndex + 2]);
        return colour;
    }

    private static MaterialTemplateLibrary process(BufferedReader reader) throws IOException {
        MaterialTemplateLibrary materialTemplateLibrary = new MaterialTemplateLibrary();

        MaterialData temp = null;
        while (reader.ready()) {
            String[] split = reader.readLine().split(" ");
            switch (split[TYPE_INDEX]) {
                case TYPE_NEW_MATERIAL:
                    temp = new MaterialData();
                    temp.name = split[1];
                    materialTemplateLibrary.put(temp.name, temp);
                    break;
                case TYPE_AMBIENT_COLOUR:
                    temp.ambientColour = parseColour(split, 1);
                    break;
                case TYPE_SPECULAR_COLOUR:
                    temp.specularColour = parseColour(split, 1);
                    break;
                case TYPE_DIFFUSE_COLOUR:
                    temp.diffuseColour = parseColour(split, 1);
                    break;
                case TYPE_SPECULAR_EXPONENT:
                    temp.specularExponent = Float.parseFloat(split[1]);
                    break;
                case TYPE_AMBIENT_TEXTURE_MAP:
                    temp.ambientTextureMap = split[1];
                    break;
                case TYPE_DIFFUSE_TEXTURE_MAP:
                    temp.diffuseTextureMap = split[1];
                    break;
                case TYPE_SPECULAR_COLOUR_TEXTURE_MAP:
                    temp.specularColourTextureMap = split[1];
                    break;
                case TYPE_SPECULAR_HIGHLIGHT_COMPONENT:
                    temp.specularHighlightComponent = split[1];
                    break;
                case TYPE_NORMAL_TEXTURE_MAP:
                    temp.normalTextureMap = split[1];
                    break;
            }
        }

        return materialTemplateLibrary;
    }

    private static MaterialTemplateLibrary process(byte[] reader) throws IOException {
        return null;
    }

    private static float[] getFloats(String[] split, int index) {
        if (split.length - index <= 0) {
            return new float[]{};
        }

        float[] floats = new float[split.length - index];
        for (int i = index, n = 0; i < split.length; i++, n++) {
            floats[n] = Float.parseFloat(split[i]);
        }
        return floats;
    }
}
