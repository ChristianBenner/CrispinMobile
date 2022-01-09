package com.crispin.demos.scenes;

import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.Geometry.Point3D;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Shaders.NormalShader;
import com.crispin.crispinmobile.Rendering.Shaders.NormalShaderNew;
import com.crispin.crispinmobile.Rendering.Shaders.UniformColourShader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Utilities.Material;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.crispin.demos.R;

public class ObjLoadDemoScene extends Scene
{
    private Model torus;
    private Model lightBulb;
    private Material lightBulbMaterial;

    // Render object model matrix
    private ModelMatrix lightModelMatrix;

    // Camera for 3D model rendering
    private Camera3D modelCamera;

    private float lightX = 0.0f;
    private float lightY = 4.0f;
    private float lightZ = 7.0f;

    private NormalShaderNew normalShader;

    public ObjLoadDemoScene() {
        normalShader = new NormalShaderNew();
        normalShader.enableIt();
        normalShader.setLightColour(1.0f, 1.0f, 1.0f);
        normalShader.setViewPosition(0.0f, 0.0f, 3.0f);
        normalShader.disableIt();

        ThreadedOBJLoader.loadModel(R.raw.torus, loadListener -> {
            this.torus = loadListener;
            this.torus.setColour(1.0f, 0.5f, 0.31f);
            this.torus.useCustomShader(normalShader);
            this.torus.setRotation(20.0f, 1.0f, 0.0f, 0.0f);
            this.torus.translate(0.0f, 0.0f, 0.0f);
        });

        lightBulbMaterial = new Material(R.drawable.lightbulbtex);
        ThreadedOBJLoader.loadModel(R.raw.lightbulbuv, loadListener -> {
            this.lightBulb = loadListener;
            this.lightBulb.useCustomShader(new UniformColourShader());
            this.lightBulb.setMaterial(lightBulbMaterial);
            this.lightBulb.setColour(Colour.YELLOW);
        });

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera3D();
        modelCamera.setPosition(new Point3D(0.0f, 0.0f, 3.0f));

        // Model matrix for rotating the light
        lightModelMatrix = new ModelMatrix();
        lightModelMatrix.scale(0.3f);
        lightModelMatrix.translate(lightX, lightY, lightZ);
    }

    float totalRot = 0.0f;
    boolean circleRot = true;
    float rotateSpeed = 1.0f;
    boolean moveToZCenter = false;
    float moveToCenterZRemain = lightZ;
    float moveMultiplier = 0.03f;
    boolean moveDown = false;

    float torusRot = 20.0f;

    @Override
    public void update(float deltaTime) {
        // Rotate around the center once
        if(circleRot) {
            float cycleRot = rotateSpeed * deltaTime;
            totalRot += cycleRot;
            lightModelMatrix.rotateAroundPoint(-lightX, -lightY, -lightZ, cycleRot, 0.0f, 1.0f, 0.0f);

            if(totalRot >= 360.0f) {
                circleRot = false;
                totalRot = 360.0f;
                moveToCenterZRemain = lightZ;
                moveToZCenter = true;
            }
        }

        if(moveToZCenter) {
            float cycleMoveToCenterMultiplier = moveMultiplier * deltaTime;
            float cycleMoveZ = -moveToCenterZRemain * cycleMoveToCenterMultiplier;
            if(moveToCenterZRemain <= 0.3f) {
                cycleMoveZ = -moveToCenterZRemain;
                moveToCenterZRemain = 0.0f;
                moveToZCenter = false;
                moveDown = true;
            } else {
                moveToCenterZRemain += cycleMoveZ;
            }
            lightModelMatrix.translate(0.0f, 0.0f, cycleMoveZ);
        }

        if(moveDown) {
            lightModelMatrix.translate(0.0f, -0.1f, 0.0f);
            torusRot += 0.7f;
            torus.setRotation(torusRot, 1.0f, 0.0f, 0.0f);
        }

        Point3D lightPos = lightModelMatrix.getPosition();

        normalShader.enableIt();
        normalShader.setLightPosition(lightPos.x, lightPos.y, lightPos.z);
        normalShader.disableIt();
    }

    @Override
    public void render() {
        if(torus != null) {
            lightBulb.render(modelCamera, lightModelMatrix);
        }

        if(torus != null) {
            torus.render(modelCamera);
        }
    }

    @Override
    public void touch(int type, Point2D position) {

    }
}
