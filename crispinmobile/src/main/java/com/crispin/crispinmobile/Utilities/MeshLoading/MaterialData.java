package com.crispin.crispinmobile.Utilities.MeshLoading;

import com.crispin.crispinmobile.Rendering.Data.Colour;

public class MaterialData {
    public String name;
    public Colour ambientColour; // Ka
    public float specularExponent; // Ks
    public Colour diffuseColour; // Kd
    public Colour specularColour; // Ns
    public String ambientTextureMap; // map_Ka
    public String diffuseTextureMap; // map_Kd
    public String specularColourTextureMap; // map_Ks
    public String specularHighlightComponent; // map_Ns
    public String normalTextureMap; // norm

    public MaterialData() {
        this.name = "";
        this.ambientColour = new Colour(Colour.WHITE);
        this.specularExponent = 0f;
        this.diffuseColour = new Colour(Colour.WHITE);
        this.specularColour = new Colour(Colour.WHITE);
        this.ambientTextureMap = "";
        this.diffuseTextureMap = "";
        this.specularColourTextureMap = "";
        this.specularHighlightComponent = "";
        this.normalTextureMap = "";
    }
}