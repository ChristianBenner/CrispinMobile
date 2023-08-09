package com.crispin.demos;

import android.util.Pair;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Models.ModelProperties;
import com.crispin.crispinmobile.Rendering.Shaders.LightingShader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Rendering.Utilities.RenderBatch;
import com.crispin.crispinmobile.MeshLoading.MeshData;
import com.crispin.crispinmobile.MeshLoading.OBJModelLoader;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RenderBatchDemo extends Scene {
    private Camera2D camera2D;
    private Button backButton;
    private RenderBatch renderBatch;
    private ArrayList<Pair<Float, ModelProperties>> modelProperties;
    private Camera camera;

    private PointLight pointLight;

    public RenderBatchDemo() {
        Crispin.setBackgroundColour(Colour.BLACK);

        camera2D = new Camera2D();
        backButton = Util.createBackButton(DemoMasterScene::new);

        camera = new Camera();
        camera.setPosition(0.0f, 0.0f, 16.0f);
        camera.setFar(20.0f);

        //LightingBatchShader shader = new LightingBatchShader();
        LightingShader shader = new LightingShader();
       // LightingTextureShader shader = new LightingTextureShader();
        LightGroup lightGroup = new LightGroup();
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setDirection(1.0f, -1.0f, 0.0f);
      //  lightGroup.addLight(directionalLight);

        pointLight = new PointLight();
        lightGroup.add(pointLight);

        renderBatch = new RenderBatch();
        renderBatch.setCamera(camera);

        HashMap<String, MeshData> meshes = OBJModelLoader.read(R.raw.torus_uv);
        renderBatch.setMesh(meshes.get("torus").mesh);
        renderBatch.setShader(shader);
        renderBatch.setLightGroup(lightGroup);
     //   renderBatch.setRenderObject(OBJModelLoader.readObjFile(R.raw.torus_uv));


        List<Material> materials = createMaterialList();
        modelProperties = new ArrayList<>();
        Random random = new Random();

        Material torusWoodMaterial = new Material(R.drawable.tiledwood16);
        torusWoodMaterial.setSpecularMap(TextureCache.loadTexture(R.drawable.tiledwood16_specular));
        torusWoodMaterial.setUvMultiplier(16.0f, 4.0f);

        final int nCol = 7;
        final int nRow = 15;
        final int zM = 20;
        for(int z = 0; z < zM; z++) {
            for (int col = 0; col < nCol; col++) {
                for (int row = 0; row < nRow; row++) {
                    // Get random material
                    Material m = materials.get(random.nextInt(materials.size()));
                    ModelProperties mp = new ModelProperties(m);
                    mp.setScale(0.4f);
                    mp.setPosition((col * 1.0f) - ((nCol - 1) / 2.0f), (row * 1.0f) - ((nRow - 1) / 2.0f), (z * 3.0f) - ((zM - 1) / 2.0f));
                    modelProperties.add(new Pair<>(0.5f + random.nextFloat() * (3.0f - 0.5f), mp));
                    renderBatch.add(mp);
                }
            }
        }
    }

    float counter = 0.0f;

    @Override
    public void update(float deltaTime) {
        for(int i = 0; i < modelProperties.size(); i++) {
            Pair<Float, ModelProperties> pair = modelProperties.get(i);
            pair.second.setRotation((pair.first * deltaTime) + pair.second.getRotation().angle, 1.0f, 1.0f, 1.0f);
        }

        counter += 0.005f * deltaTime;
        float multiplier = ((float)Math.sin(counter) + 1.0f) / 2.0f; // 0.0 - 1.0 ranging over time
        camera.setPosition(0.0f, 0.0f, 60.0f * (0.0f + (multiplier * (1.0f - 0.0f))));
        pointLight.setPosition(camera.getPosition());
    }

    @Override
    public void render() {
        renderBatch.render();
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

        Material jade = new Material();
        jade.ambientStrength = new Colour(0.135f, 0.2225f, 0.1575f);
        jade.diffuseStrength = new Colour(0.54f, 0.89f, 0.63f);
        jade.specularStrength = new Colour(0.316228f, 0.316228f, 0.316227f);
        jade.shininess = 3.2f;

        Material obsidian = new Material();
        obsidian.ambientStrength = new Colour(0.05375f, 0.05f, 0.06625f);
        obsidian.diffuseStrength = new Colour(0.18275f, 0.17f, 0.22525f);
        obsidian.specularStrength = new Colour(0.296648f, 0.296648f, 0.296648f);
        obsidian.shininess = 2.816f;

        Material pearl = new Material();
        pearl.ambientStrength = new Colour(0.25f, 0.20725f, 0.20725f);
        pearl.diffuseStrength = new Colour(1.0f, 0.829f, 0.829f);
        pearl.specularStrength = new Colour(0.296648f, 0.296648f, 0.296648f);
        pearl.shininess = 2.816f;

        Material ruby = new Material();
        ruby.ambientStrength = new Colour(0.1745f, 0.01175f, 0.01175f);
        ruby.diffuseStrength = new Colour(0.61424f, 0.04136f, 0.04136f);
        ruby.specularStrength = new Colour(0.727811f, 0.626959f, 0.0626959f);
        ruby.shininess = 19.2f;

        List<Material> materials = new ArrayList<>();
        materials.add(emerald);
        materials.add(jade);
        materials.add(obsidian);
        materials.add(pearl);
        materials.add(ruby);
        return materials;
    }
}
