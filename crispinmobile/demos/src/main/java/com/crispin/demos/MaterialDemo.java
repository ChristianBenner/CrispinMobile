package com.crispin.demos;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.MeshLoading.ThreadedOBJLoader;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MaterialDemo extends Scene {
    private static final long MATERIAL_TIME_MS = 4000L;
    private Camera2D camera2D;
    private Button backButton;
    private Model lightBulb;
    private Model lightBulb2;
    private Model torus;
    private final LightGroup lightGroup;
    private final PointLight pointLight;
    private final PointLight pointLight2;
    private final Camera camera3D;
    private float lightXCount;
    private float lightZCount;
    private long materialSetTimeMs;
    private final List<Material> materialList;
    private Iterator<Material> materialIterator;

    public MaterialDemo() {
        Crispin.setBackgroundColour(Colour.BLACK);
        backButton = Util.createBackButton(DemoMasterScene::new);
        camera2D = new Camera2D();
        materialList = createMaterialList();
        materialIterator = materialList.iterator();
        materialSetTimeMs = System.currentTimeMillis();

        ThreadedOBJLoader.loadModel(R.raw.torus_uv, mesh -> {
            this.torus = new Model(mesh);
            this.torus.setMaterial(materialIterator.next());
            this.torus.setRotation(15.0f, 1.0f, 0.0f, 0.0f);
        });

        // Load the light bulb model (used to show light position)
        Material lightBulbMaterial = new Material(R.drawable.lightbulb_texture);
        lightBulbMaterial.setSpecularMap(TextureCache.loadTexture(R.drawable.lightbulb_specular));
        ThreadedOBJLoader.loadModel(R.raw.lightbulb_flipped_normals, mesh -> {
            this.lightBulb = new Model(mesh);
            this.lightBulb.setMaterial(lightBulbMaterial);
            this.lightBulb.setScale(0.2f);
            this.lightBulb.setPosition(0.0f, 1.0f, 0.0f);
        });

        // Load the light bulb model (used to show light position)
        ThreadedOBJLoader.loadModel(R.raw.lightbulb_flipped_normals, mesh -> {
            this.lightBulb2 = new Model(mesh);
            this.lightBulb2.setMaterial(lightBulbMaterial);
            this.lightBulb2.setScale(0.2f);
            this.lightBulb2.setPosition(0.0f, 1.0f, 0.0f);
        });

        lightGroup = new LightGroup();

        pointLight = new PointLight();
        lightGroup.add(pointLight);

        pointLight2 = new PointLight();
        lightGroup.add(pointLight2);

        camera3D = new Camera();
        camera3D.setPosition(new Vec3(0.0f, 1.0f, 5.0f));
    }

    @Override
    public void update(float deltaTime) {
        lightXCount += 0.03f * deltaTime;
        lightZCount += 0.03f * deltaTime;

        float lightX = (float) Math.sin(lightXCount);
        float lightZ = (float) Math.cos(lightZCount);
        lightBulb.setPosition(lightX, 1.0f, lightZ);
        pointLight.setPosition(lightBulb.getPosition());

        float light2X = (float) Math.sin(lightXCount + Math.PI);
        float light2Z = (float) Math.cos(lightZCount + Math.PI);
        lightBulb2.setPosition(light2X, 1.0f, light2Z);
        pointLight2.setPosition(lightBulb2.getPosition());

        if (torus != null) {
            if (System.currentTimeMillis() - materialSetTimeMs > MATERIAL_TIME_MS) {
                materialSetTimeMs = System.currentTimeMillis();

                if (!materialIterator.hasNext()) {
                    materialIterator = materialList.iterator();
                }

                torus.setMaterial(materialIterator.next());
            }
        }
    }

    @Override
    public void render() {
        if (lightBulb != null) {
            lightBulb.render(camera3D, lightGroup);
        }

        if (lightBulb2 != null) {
            lightBulb2.render(camera3D, lightGroup);
        }

        if (torus != null) {
            torus.render(camera3D, lightGroup);
        }

        backButton.draw(camera2D);
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {

    }

    public List<Material> createMaterialList() {
        Material emerald = new Material();
        emerald.ambientStrength = new Colour(0.0215f, 0.1745f, 0.0215f);
        emerald.diffuseStrength = new Colour(0.07568f, 0.61424f, 0.07568f);
        emerald.specularStrength = new Colour(0.633f, 0.727811f, 0.633f);
        emerald.shininess = 19.2f;
        emerald.setIgnoreTexelData(true);

        Material jade = new Material();
        jade.ambientStrength = new Colour(0.135f, 0.2225f, 0.1575f);
        jade.diffuseStrength = new Colour(0.54f, 0.89f, 0.63f);
        jade.specularStrength = new Colour(0.316228f, 0.316228f, 0.316227f);
        jade.shininess = 3.2f;
        jade.setIgnoreTexelData(true);

        Material obsidian = new Material();
        obsidian.ambientStrength = new Colour(0.05375f, 0.05f, 0.06625f);
        obsidian.diffuseStrength = new Colour(0.18275f, 0.17f, 0.22525f);
        obsidian.specularStrength = new Colour(0.296648f, 0.296648f, 0.296648f);
        obsidian.shininess = 2.816f;
        obsidian.setIgnoreTexelData(true);

        Material pearl = new Material();
        pearl.ambientStrength = new Colour(0.25f, 0.20725f, 0.20725f);
        pearl.diffuseStrength = new Colour(1.0f, 0.829f, 0.829f);
        pearl.specularStrength = new Colour(0.296648f, 0.296648f, 0.296648f);
        pearl.shininess = 2.816f;
        pearl.setIgnoreTexelData(true);

        Material ruby = new Material();
        ruby.ambientStrength = new Colour(0.1745f, 0.01175f, 0.01175f);
        ruby.diffuseStrength = new Colour(0.61424f, 0.04136f, 0.04136f);
        ruby.specularStrength = new Colour(0.727811f, 0.626959f, 0.0626959f);
        ruby.shininess = 19.2f;
        ruby.setIgnoreTexelData(true);

        List<Material> materials = new ArrayList<>();
        materials.add(emerald);
        materials.add(jade);
        materials.add(obsidian);
        materials.add(pearl);
        materials.add(ruby);
        return materials;
    }
}
