package com.crispin.demos.scenes;

import android.hardware.lights.Light;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Entities.SpotLight;
import com.crispin.crispinmobile.Rendering.Models.Cube;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Shaders.LightingShader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.Rendering.Utilities.Material;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.crispin.demos.R;

public class SpotLightDemo extends Scene {
    private Model torus;
    private Camera camera3D;
    private LightGroup lightGroup;
    private LightGroup lightBulbGroup;
    private Model lightBulb;
    private float lightXCount;
    private float lightZCount;
    private SpotLight spotLight;
    private PointLight lightBulbLight;

    public SpotLightDemo() {
        Crispin.setBackgroundColour(Colour.ORANGE);

        lightXCount = 0.0f;
        lightZCount = (float)Math.PI / 2.0f;

        Material wood = new Material();
        wood.setIgnoreTexelData(true);
        ThreadedOBJLoader.loadModel(R.raw.torus_uv, loadListener -> {
            this.torus = loadListener;
            this.torus.setMaterial(wood);
        });

        camera3D = new Camera();
        camera3D.setPosition(new Vec3(0.0f, 1.0f, 5.0f));

        lightGroup = new LightGroup();

        spotLight = new SpotLight();
        spotLight.setPosition(new Vec3(-1.0f, 2.0f, 0.0f));
        spotLight.setDirection(new Vec3(0.0f, -1.0f, 0.0f));
        spotLight.size = (float)Math.cos(Math.toRadians(12.5));
        spotLight.outerSize = (float)Math.cos(Math.toRadians(15.5));
        lightGroup.addLight(spotLight);

        lightBulbGroup = new LightGroup();
        lightBulbLight = new PointLight();
        lightBulbGroup.addLight(lightBulbLight);

        // Load the light bulb model (used to show light position)
        Material lightBulbMaterial = new Material(R.drawable.lightbulb_texture);
        lightBulbMaterial.setSpecularMap(TextureCache.loadTexture(R.drawable.lightbulb_specular));
        ThreadedOBJLoader.loadModel(R.raw.lightbulb_flipped_normals, loadListener -> {
            this.lightBulb = loadListener;
            this.lightBulb.setMaterial(lightBulbMaterial);
            this.lightBulb.setScale(0.3f);
        });
    }

    @Override
    public void update(float deltaTime) {
        lightXCount += 0.03f * deltaTime;
        lightZCount += 0.03f * deltaTime;
        float lightX = (float)Math.sin(lightXCount) * 1.1f;
        float lightZ = (float)Math.sin(lightZCount) * 1.1f;

        spotLight.setPosition(lightX, 1.0f, lightZ);
        lightBulb.setPosition(spotLight.getPosition());
        lightBulbLight.setPosition(spotLight.getPosition());
    }

    @Override
    public void render() {
        if (lightBulb != null) {
            lightBulb.render(camera3D, lightBulbGroup);
        }

        torus.render(camera3D, lightGroup);
    }

    @Override
    public void touch(int type, Vec2 position) {

    }
}
