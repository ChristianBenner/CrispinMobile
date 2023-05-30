package com.crispin.demos;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Entities.SpotLight;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.crispinmobile.Utilities.ThreadedOBJLoader;

public class SpotLightDemo extends Scene {
    private Camera2D camera2D;
    private Button backButton;
    private Model torus;
    private Camera camera3D;
    private LightGroup lightGroup;
    private LightGroup lightBulbGroup;
    private Model torch;
    private float lightXCount;
    private float lightZCount;
    private SpotLight spotLight;
    private PointLight lightBulbLight;

    public SpotLightDemo() {
        Crispin.setBackgroundColour(Colour.BLACK);

        camera2D = new Camera2D();
        backButton = Util.createBackButton(DemoMasterScene::new);

        lightXCount = 0.0f;
        lightZCount = (float)Math.PI / 2.0f;

        Material wood = new Material(R.drawable.tiledwood16);
        wood.setSpecularMap(TextureCache.loadTexture(R.drawable.tiledwood16_specular));
        wood.setUvMultiplier(16.0f, 4.0f);
        ThreadedOBJLoader.loadModel(R.raw.torus_uv, mesh -> {
            this.torus = new Model(mesh);
            this.torus.setMaterial(wood);
        });

        camera3D = new Camera();
        camera3D.setPosition(new Vec3(0.0f, 1.0f, 5.0f));

        lightGroup = new LightGroup();
        spotLight = new SpotLight();
        spotLight.setPosition(new Vec3(-1.0f, 2.0f, 0.0f));
        spotLight.setDirection(new Vec3(0.0f, -1.0f, 0.0f));
        spotLight.size = (float)Math.cos(Math.toRadians(10.5));
        spotLight.outerSize = (float)Math.cos(Math.toRadians(11.5));
        lightGroup.add(spotLight);

        DirectionalLight directionalLight = new DirectionalLight(1.0f, -1.0f, -0.4f);
        directionalLight.ambientStrength = 0.1f;
        directionalLight.diffuseStrength = 0.4f;
        lightGroup.add(directionalLight);

        lightBulbGroup = new LightGroup();
        lightBulbLight = new PointLight();
        lightBulbGroup.add(lightBulbLight);

        ThreadedOBJLoader.loadModel(R.raw.torch, mesh -> {
            this.torch = new Model(mesh);
            this.torch.setScale(0.1f);
            this.torch.setRotation(90.0f, 1.0f, 0.0f, 0.0f);
        });
    }

    @Override
    public void update(float deltaTime) {
        lightXCount += 0.03f * deltaTime;
        lightZCount += 0.03f * deltaTime;
        float lightX = (float)Math.sin(lightXCount);
        float lightZ = (float)Math.sin(lightZCount);

        spotLight.setPosition(lightX, 1.0f, lightZ);
        torch.setPosition(spotLight.getPosition());
        lightBulbLight.setPosition(Geometry.plus(spotLight.getPosition(), new Vec3(0.0f, -0.5f, 0.3f)));

        camera3D.setPosition(lightX, 0.8f, lightZ);
        camera3D.setPitch(-90.0f);
    }

    @Override
    public void render() {
        if (torch != null) {
            torch.render(camera3D, lightBulbGroup);
        }

        torus.render(camera3D, lightGroup);

        backButton.draw(camera2D);
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {

    }
}
