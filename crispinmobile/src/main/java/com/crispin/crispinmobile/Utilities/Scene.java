package com.crispin.crispinmobile.Utilities;

import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.TouchType;

/**
 * Scene base class. This provides functions that are controlled by the SceneManager object in
 * different ways.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see SceneManager
 * @since 1.0
 */
public abstract class Scene {
    /**
     * The update function is called as frequently as possible. It provides a deltaTime value which
     * can be used in logic calculations. No render calls should be made from the update function.
     *
     * @param deltaTime Delta time is used in logic calculations. Lower deltaTime means the function
     *                  is being updated at a faster rate. Higher deltaTime means the function is
     *                  being updated at a slower rate. A delta time of 1.0 means the function is
     *                  being updated at 60hz.
     * @since 1.0
     */
    public abstract void update(float deltaTime);

    /**
     * The render function is called as frequently as possible. It is where all of the render calls
     * should be made. Logic based operations should be kept to a minimal in the render function.
     *
     * @since 1.0
     */
    public abstract void render();

    // Single pointer motion event
    public abstract void touch(TouchType touchType, Pointer pointer);

    /**
     * This is the initialisation function that is to be assigned by the deriving class. This is
     * commonly the deriving class constructor. The initialisation of a scene is controlled by the
     * SceneManager. It will only be called when starting a scene.
     *
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public interface Constructor {
        Scene init();
    }
}
