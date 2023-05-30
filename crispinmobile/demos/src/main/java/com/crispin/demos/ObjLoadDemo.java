package com.crispin.demos;

import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.ThreadedOBJLoader;

public class ObjLoadDemo extends Scene {
    // UI Camera
    private Camera2D camera2D;
    // Back Button
    private Button backButton;
    // Monkey model rotation
    private float rotation;
    // Monkey model
    private Model monkey;
    // Camera for 3D model rendering
    private final Camera modelCamera;
    // Light the scene
    private LightGroup lights;

    public ObjLoadDemo() {
        camera2D = new Camera2D();
        backButton = Util.createBackButton(DemoMasterScene::new);

        lights = new LightGroup();
        lights.add(new DirectionalLight(0f, 0f, -1f));

        // Load the monkey model. Note that we are using the ThreadedOBJLoader here. This means that
        // the application can continue whilst loading the model. This is especially useful for
        // loading large models in some situations. If you do not wish to use a threaded loader,
        // call OBJModelLoader.readObjFile(resourceId) instead.
        ThreadedOBJLoader.loadModel(R.raw.monkey, mesh -> {
            this.monkey = new Model(mesh);
            this.monkey.setColour(1.0f, 0.5f, 0.31f);
            this.monkey.setScale(0.5f);
        });

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera();
        modelCamera.setPosition(new Vec3(0.0f, 0.0f, 3.0f));
    }

    @Override
    public void update(float deltaTime) {
        rotation += 0.5f * deltaTime;
        monkey.setRotation(rotation, 0.0f, 1.0f, 0.0f);
    }

    @Override
    public void render() {
        if (monkey != null) {
            monkey.render(modelCamera, lights);
        }

        backButton.draw(camera2D);
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {

    }
}
