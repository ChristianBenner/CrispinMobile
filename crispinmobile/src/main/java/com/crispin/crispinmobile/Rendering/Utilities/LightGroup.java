package com.crispin.crispinmobile.Rendering.Utilities;

import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.EmissiveEdge;
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

    // List of emissive edges as multiple can be used for one render target
    private final ArrayList<EmissiveEdge> emissiveEdges;

    // List of spot lights as multiple can be used for one render target
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
        emissiveEdges = new ArrayList<>();
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
    public void add(DirectionalLight directionalLight) {
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
    public void add(PointLight pointLight) {
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
    public void remove(PointLight pointLight) {
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
     * Adds an emissive edge to the group
     *
     * @param emissiveEdge Emssive edge to add to the group
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void add(EmissiveEdge emissiveEdge) {
        this.emissiveEdges.add(emissiveEdge);
    }

    /**
     * Remove an emmisive edge from the group
     *
     * @param emissiveEdge Emssive edge to remove from the group
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void remove(EmissiveEdge emissiveEdge) {
        this.emissiveEdges.remove(emissiveEdge);
    }

    /**
     * Removes all the groups emissive edges
     *
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void clearEmissiveEdges() {
        this.emissiveEdges.clear();
    }

    /**
     * Retrieves all emissive edges
     *
     * @return ArrayList of EmissiveEdges
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public final ArrayList<EmissiveEdge> getEmissiveEdges() {
        return emissiveEdges;
    }

    /**
     * Adds a spot light to the group
     *
     * @param spotLight A spot light to add to the group
     * @author Christian Benner
     * @version %I%, %G%
     * @since 1.0
     */
    public void add(SpotLight spotLight) {
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
    public void remove(SpotLight spotLight) {
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
