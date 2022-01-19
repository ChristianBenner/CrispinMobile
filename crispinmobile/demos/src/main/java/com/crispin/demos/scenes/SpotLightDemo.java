package com.crispin.demos.scenes;

import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.Geometry.Point3D;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.SpotLight;
import com.crispin.crispinmobile.Rendering.Models.Cube;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Shaders.LightingShader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.Rendering.Utilities.Material;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.crispin.demos.R;

public class SpotLightDemo extends Scene {
    private Cube cube;
    private Camera3D camera3D;
    private LightGroup lightGroup;
    private float rotY;
    private Model lightBulb;

    public SpotLightDemo() {
        Material wood = new Material();
        wood.setIgnoreNormalData(false);
        wood.setIgnoreColourData(true);
        wood.setIgnoreTexelData(false);
        wood.setIgnorePositionData(false);

        cube = new Cube(wood);
        cube.useCustomShader(new LightingShader());
        cube.setScale(0.5f);
        cube.setColour(Colour.ORANGE);

        camera3D = new Camera3D();
        camera3D.setPosition(new Point3D(0.0f, 1.5f, 5.0f));

        lightGroup = new LightGroup();

        SpotLight spotLight = new SpotLight();
//        spotLight.setDirection(camera3D.getDirection());
//        spotLight.setPosition(camera3D.getPosition());
//        spotLight.outerSize = 0.25f;
//        spotLight.size = 0.3f;

        // Load the light bulb model (used to show light position)
        Material lightBulbMaterial = new Material(R.drawable.lightbulb_texture);
        lightBulbMaterial.setSpecularMap(TextureCache.loadTexture(R.drawable.lightbulb_specular));
        ThreadedOBJLoader.loadModel(R.raw.lightbulb_flipped_normals, loadListener -> {
            this.lightBulb = loadListener;
            this.lightBulb.setMaterial(lightBulbMaterial);
            this.lightBulb.setScale(0.3f);
            this.lightBulb.setPosition(spotLight.getPosition());
        });

        lightGroup.addLight(spotLight);
        lightGroup.addLight(new DirectionalLight(1.0f, 1.0f, 1.0f));
    }
    @Override
    public void update(float deltaTime) {
        rotY += 1.0f * deltaTime;
        cube.setRotation(rotY, 0.0f, 1.0f, 0.0f);
    }

    @Override
    public void render() {
        if(lightBulb != null) {
            lightBulb.render(camera3D, lightGroup);
        }

        cube.render(camera3D, lightGroup);
    }

    @Override
    public void touch(int type, Point2D position) {

    }
}
