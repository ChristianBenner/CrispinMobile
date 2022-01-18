package com.crispin.crispinmobile.Rendering.Utilities;

import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Entities.SpotLight;

import java.util.ArrayList;

public class LightGroup {
    private DirectionalLight directionalLight;
    private ArrayList<PointLight> pointLights;
    private ArrayList<SpotLight> spotLights;

    public LightGroup() {
        pointLights = new ArrayList<>();
        spotLights = new ArrayList<>();
    }

    public void setDirectionalLight(DirectionalLight directionalLight) {
        this.directionalLight = directionalLight;
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public void removeDirectionalLight() {
        directionalLight = null;
    }

    public void addPointLight(PointLight pointLight) {
        this.pointLights.add(pointLight);
    }

    public void removePointLight(PointLight pointLight) {
        this.pointLights.remove(pointLight);
    }

    public void clearPointLights() {
        this.pointLights.clear();
    }

    public final ArrayList<PointLight> getPointLights() {
        return pointLights;
    }

    public void addSpotLight(SpotLight spotLight) {
        this.spotLights.add(spotLight);
    }

    public void removeSpotLight(SpotLight spotLight) {
        this.spotLights.remove(spotLight);
    }

    public void clearSpotLights() {
        this.spotLights.clear();
    }

    public final ArrayList<SpotLight> getSpotLights() {
        return spotLights;
    }
}
