package com.crispin.crispinmobile.Utilities.MeshLoading;

import com.crispin.crispinmobile.Rendering.Utilities.Mesh;
import com.crispin.crispinmobile.Utilities.LoadListener;


/**
 * A class that is currently under development. It is used to load OBJ models on another thread (in
 * the background) so that they can be added into a scene in real time. This reduces the load time
 * at the beginning of a scene but not all models may be visible so it is recommended to instead
 * present a loading bar whilst the threaded models load. The class uses the OBJModelLoader to load
 * OBJ models and does it on another thread so that it doesn't lag or pause the main loop.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
 * @see OBJModelLoader
 * @since 1.0
 */
public class ThreadedOBJLoader implements Runnable {
    // The resource ID of the model
    private final int resourceId;

    // The render object
    private Mesh mesh;

    // Whether or not the model has been loaded or not
    private boolean complete = false;

    // LoadEvent that gets called when the OBJ has loaded
    private final LoadListener loadListener;

    /**
     * Create an ThreadedOBJLoader obj to load an OBJ model on another thread
     *
     * @param resourceId   The OBJ model file resource ID
     * @param loadListener The load listener that listens to load events
     * @since 1.0
     */
    public ThreadedOBJLoader(int resourceId, LoadListener loadListener) {
        this.resourceId = resourceId;
        this.loadListener = loadListener;
    }

    /**
     * Load a model on another thread without having to create the ThreadedOBJLoader object
     *
     * @param resourceId The OBJ model file resource ID
     * @since 1.0
     */
    public static void loadModel(int resourceId, LoadListener loadListener) {
        ThreadedOBJLoader threadedModelLoader = new ThreadedOBJLoader(resourceId, loadListener);
        threadedModelLoader.run();
    }

    /**
     * Overridden from the thread base class
     *
     * @since 1.0
     */
    @Override
    public void run() {
        mesh = OBJModelLoader.readObjFile(resourceId);
        this.loadListener.onLoad(mesh);
        complete = true;
    }

    /**
     * Check if the model has loaded
     *
     * @return The state of the model loading. True if the model has finished loading, else false
     * @since 1.0
     */
    public boolean isComplete() {
        return this.complete;
    }

    /**
     * Get the render object that is loaded
     *
     * @return The state of the model loading. True if the model has finished loading, else false
     * @since 1.0
     */
    public Mesh getMesh() {
        return mesh;
    }
}
