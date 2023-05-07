package com.crispin.crispinmobile.Rendering.Utilities;

import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Entities.SpotLight;

import java.util.ArrayList;

/**
 * LightGroups contain multiple of each different light types. The purpose is to group all lights
 * together to be used against multiple render targets. When rendering a RenderObject for example, a
 * light group containing multiple lights can be passed in as a single managable object.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
 * @since 1.0
 */
public class LightGroup {
    // Directional light used to shine light from all positions towards one given direction
    private DirectionalLight directionalLight;

    // List of point lights as multiple can be used for one render target
    private final ArrayList<PointLight> pointLights;

    // Light of spot lights as multiple can be used for one render target
    private final ArrayList<SpotLight> spotLights;

    /**
     * Create a light group object to store multiple different types of lights. Useful to store
     * entire scenes of lights to be easily provided to render targets.
     *
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public LightGroup() {
        pointLights = new ArrayList<>();
        spotLights = new ArrayList<>();
    }

    /**
     * Sets the groups directional light
     *
     * @param directionalLight A directional light
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void addLight(DirectionalLight directionalLight) {
        this.directionalLight = directionalLight;
    }

    /**
     * Retrieves the groups directional light
     *
     * @return Groups directional light
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    /**
     * Removes the groups directional light
     *
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void removeDirectionalLight() {
        directionalLight = null;
    }

    /**
     * Adds a point light to the group
     *
     * @param pointLight A point light to add to the group
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void addLight(PointLight pointLight) {
        this.pointLights.add(pointLight);
    }

    /**
     * Remove a point light from the group
     *
     * @param pointLight A point light to remove from the group
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void removePointLight(PointLight pointLight) {
        this.pointLights.remove(pointLight);
    }

    /**
     * Removes all the groups point lights
     *
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void clearPointLights() {
        this.pointLights.clear();
    }

    /**
     * Retrieves all point lights
     *
     * @return ArrayList of PointLights
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public final ArrayList<PointLight> getPointLights() {
        return pointLights;
    }

    /**
     * Adds a spot light to the group
     *
     * @param spotLight A spot light to add to the group
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void addLight(SpotLight spotLight) {
        this.spotLights.add(spotLight);
    }

    /**
     * Remove a spot light from the group
     *
     * @param spotLight A spot light to remove from the group
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void removeSpotLight(SpotLight spotLight) {
        this.spotLights.remove(spotLight);
    }

    /**
     * Removes all the groups spot lights
     *
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void clearSpotLights() {
        this.spotLights.clear();
    }

    /**
     * Retrieves all point lights
     *
     * @return ArrayList of SpotLights
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public final ArrayList<SpotLight> getSpotLights() {
        return spotLights;
    }
}
