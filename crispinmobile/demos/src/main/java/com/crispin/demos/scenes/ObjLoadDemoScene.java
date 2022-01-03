package com.crispin.demos.scenes;

import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.Geometry.Point3D;
import com.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.crispin.crispinmobile.Rendering.Utilities.Model;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.Utilities.LoadListener;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.crispin.demos.R;

public class ObjLoadDemoScene extends Scene
{
    private Model monkey;

    // Render object model matrix
    private ModelMatrix modelMatrix;

    // Camera for 3D model rendering
    private Camera3D modelCamera;

    public ObjLoadDemoScene() {
        ThreadedOBJLoader.loadModel(R.raw.monkey, loadListener -> this.monkey = loadListener);

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera3D();
        modelCamera.setPosition(new Point3D(0.0f, 0.0f, 5.0f));

        // Create the model matrix for transforming the model
        modelMatrix = new ModelMatrix();
    }

    @Override
    public void update(float deltaTime) {
        // Update the model matrix to the most recent touch rotation values
        modelMatrix.rotate(1.0f, 0.0f, 1.0f, 0.0f);
    }

    @Override
    public void render() {
        if(monkey != null) {
            monkey.render(modelCamera, modelMatrix);
        }
    }

    @Override
    public void touch(int type, Point2D position) {

    }
}
