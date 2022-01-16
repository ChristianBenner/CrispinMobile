package com.crispin.demos.scenes;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.Geometry.Point3D;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Entities.Light;
import com.crispin.crispinmobile.Rendering.Models.Cube;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Shaders.LightingTextureShader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.crispin.crispinmobile.Rendering.Utilities.Material;
import com.crispin.crispinmobile.Utilities.LoadListener;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.crispin.demos.R;

import java.util.HashSet;

public class MaterialDemo extends Scene {
    private Model lightBulb;
    private Model torus;
    private HashSet<Light> lightGroup;
    private Light light;
    private Camera3D camera3D;
    private float lightXCount;
    private float lightZCount;

    public MaterialDemo() {
        Crispin.setBackgroundColour(Colour.BLACK);

        Material obsidian = new Material();
        obsidian.ambientStrength = new Colour(0.05375f, 0.05f, 0.06625f);
        obsidian.diffuseStrength = new Colour(0.18275f, 0.17f, 0.22525f);
        obsidian.specularStrength = new Colour(0.296648f, 0.296648f, 0.296648f);
        obsidian.shininess = 0.088f;
        obsidian.setIgnoreTexelData(true);

        ThreadedOBJLoader.loadModel(R.raw.torus_uv, model -> {
            this.torus = model;
            this.torus.setMaterial(obsidian);
        });

        // Load the light bulb model (used to show light position)
        Material lightBulbMaterial = new Material(R.drawable.lightbulb_texture);
        lightBulbMaterial.setSpecularMap(TextureCache.loadTexture(R.drawable.lightbulb_specular));
        ThreadedOBJLoader.loadModel(R.raw.lightbulb_flipped_normals, loadListener -> {
            this.lightBulb = loadListener;
            this.lightBulb.setMaterial(lightBulbMaterial);
            this.lightBulb.setScale(0.3f);
            this.lightBulb.setPosition(0.0f, 1.0f, 0.0f);
        });

        lightGroup = new HashSet<>();

        light = new Light();
        lightGroup.add(light);

        camera3D = new Camera3D();
        camera3D.setPosition(new Point3D(0.0f, 1.0f, 3.0f));
    }

    @Override
    public void update(float deltaTime) {
        lightXCount += 0.03f * deltaTime;
        lightZCount += 0.03f * deltaTime;
        float lightX = (float)Math.sin(lightXCount);
        float lightZ = (float)Math.cos(lightZCount);
        lightBulb.setPosition(lightX, 1.0f, lightZ);
        light.setPosition(lightBulb.getPosition());
    }

    @Override
    public void render() {
        if(lightBulb != null) {
            lightBulb.render(camera3D, lightGroup);
        }

        if(torus != null) {
            torus.render(camera3D, lightGroup);
        }
    }

    @Override
    public void touch(int type, Point2D position) {

    }
}
