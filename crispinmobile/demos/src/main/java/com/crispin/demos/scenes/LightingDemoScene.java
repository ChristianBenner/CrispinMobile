package com.crispin.demos.scenes;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.Geometry.Point3D;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Entities.Light;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Shaders.NormalShader;
import com.crispin.crispinmobile.Rendering.Shaders.TextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.UniformColourShader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.crispin.crispinmobile.Rendering.Utilities.Material;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.ThreadedOBJLoader;
import com.crispin.demos.R;

import java.util.HashSet;

public class LightingDemoScene extends Scene
{
    // A model for our torus (donut) shape
    private Model torus;

    // A model for a light bulb
    private Model lightBulb;
    private Material lightBulbMaterial;

    // Render object model matrix
    private ModelMatrix lightModelMatrix;

    // Camera for 3D model rendering
    private Camera3D modelCamera;

    // Light start position
    private final Point3D LIGHT_START_POS = new Point3D(0.0f, 5.0f, 5.0f);

    // Light object containing position and colour data
    private Light light;

    // Group of lights as a HashSet is required to pass to the renderer for lighting. Even if there
    // is only one. This is because internally, the engines support multiple
    private HashSet<Light> lightGroup;

    public LightingDemoScene() {
        Crispin.setBackgroundColour(Colour.BLACK);

        light = new Light(LIGHT_START_POS);
        light.setAmbienceStrength(0.05f);

        lightGroup = new HashSet<>();
        lightGroup.add(light);

        ThreadedOBJLoader.loadModel(R.raw.torus, loadListener -> {
            this.torus = loadListener;
            this.torus.setColour(1.0f, 0.5f, 0.31f);
            this.torus.setRotation(20.0f, 1.0f, 0.0f, 0.0f);
            this.torus.translate(0.0f, 0.0f, 0.0f);
        });

        lightBulbMaterial = new Material(R.drawable.lightbulbtex);
        ThreadedOBJLoader.loadModel(R.raw.lightbulb, loadListener -> {
            this.lightBulb = loadListener;
            this.lightBulb.setMaterial(lightBulbMaterial);
        });

        // Create the model camera and move it forward in-front of the origin
        modelCamera = new Camera3D();
        modelCamera.setPosition(new Point3D(0.0f, 0.0f, 3.0f));

        // Model matrix for rotating the light
        lightModelMatrix = new ModelMatrix();
        lightModelMatrix.scale(0.3f);
        lightModelMatrix.translate(LIGHT_START_POS.x, LIGHT_START_POS.y, LIGHT_START_POS.z);
    }

    @Override
    public void update(float deltaTime) {
        light.setPosition(updateLightPath(deltaTime));
    }

    @Override
    public void render() {
        if(torus != null) {
            lightBulb.render(modelCamera, lightModelMatrix);
        }

        if(torus != null) {
            torus.render(modelCamera, lightGroup);
        }
    }

    @Override
    public void touch(int type, Point2D position) {

    }

    float totalRot = 0.0f;
    boolean circleRot = true;
    float rotateSpeed = 1.0f;
    boolean moveToZCenter = false;
    float moveToCenterZRemain = LIGHT_START_POS.z;
    float moveMultiplier = 0.03f;
    boolean moveDown = false;
    boolean rotateTorus = false;
    float torusRot = 20.0f;
    float moveDownRemain = LIGHT_START_POS.y * 2.0f;

    public Point3D updateLightPath(float deltaTime) {
        // Rotate around the center once
        if(circleRot) {
            float cycleRot = rotateSpeed * deltaTime;
            totalRot += cycleRot;
            lightModelMatrix.rotateAroundPoint(-LIGHT_START_POS.x, -LIGHT_START_POS.y,
                    -LIGHT_START_POS.z, cycleRot, 0.0f, 1.0f, 0.0f);

            if(totalRot >= 360.0f) {
                circleRot = false;
                totalRot = 360.0f;
                moveToCenterZRemain = LIGHT_START_POS.z;
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
                moveDownRemain = LIGHT_START_POS.y * 2.0f;
            } else {
                moveToCenterZRemain += cycleMoveZ;
            }
            lightModelMatrix.translate(0.0f, 0.0f, cycleMoveZ);
        }

        if(moveDown) {
            float cycleMoveDown = -0.1f * deltaTime;

            if(moveDownRemain <= 0.3f) {
                cycleMoveDown = -moveDownRemain;
                moveDownRemain = 0.0f;
                moveDown = false;
                torusRot = 20.0f;
                rotateTorus = true;
            } else {
                moveDownRemain += cycleMoveDown;
            }

            lightModelMatrix.translate(0.0f, cycleMoveDown, 0.0f);
        }

        if(rotateTorus) {
            torusRot += 1.4f * deltaTime;
            if(torusRot >= 200.0f) {
                totalRot = 0.0f;
                rotateTorus = false;
                lightModelMatrix.translate(0.0f, LIGHT_START_POS.y * 2.0f, LIGHT_START_POS.z);
                circleRot = true;
            }
            torus.setRotation(torusRot, 1.0f, 0.0f, 0.0f);
        }

        return lightModelMatrix.getPosition();
    }
}
