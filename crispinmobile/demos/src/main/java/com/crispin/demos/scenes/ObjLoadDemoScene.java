package com.crispin.demos.scenes;

import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.Geometry.Point3D;
import com.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.crispin.demos.R;

public class ObjLoadDemoScene extends Scene
{
    // Monkey model
    private Model monkey;

    // Monkey model rotation
    float rotation;

    // Camera for 3D model rendering
    private Camera3D modelCamera;

    public ObjLoadDemoScene() {
        // Load the monkey model. Note that we are using the ThreadedOBJLoader here. This means that
        // the application can continue whilst loading the model. This is especially useful for
        // loading large models in some situations. If you do not wish to use a threaded loader,
        // call OBJModelLoader.readObjFile(resourceId) instead.
        ThreadedOBJLoader.loadModel(R.raw.monkey, loadListener -> {
            this.monkey = loadListener;
            this.monkey.setColour(1.0f, 0.5f, 0.31f);
            this.monkey.setScale(0.7f);
        });

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera3D();
        modelCamera.setPosition(new Point3D(0.0f, 0.0f, 3.0f));
    }

    @Override
    public void update(float deltaTime) {
        rotation += 0.5f * deltaTime;
        monkey.setRotation(rotation, 0.0f, 1.0f, 0.0f);
    }

    @Override
    public void render() {
        if(monkey != null) {
            monkey.render(modelCamera);
        }
    }

    @Override
    public void touch(int type, Point2D position) {

    }
}
