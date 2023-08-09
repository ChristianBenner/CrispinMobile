package com.crispin.demos;

import android.opengl.Matrix;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Physics.BoundBox2D;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.Scene;

public class BoundBoxDemo extends Scene {
    private Camera2D camera2D;
    private Camera2D uiCamera;
    private Square square;
    private BoundBox2D boundBox2D;
    private Square transformedBoundBoxGraphic;
    private float rotation;

    public BoundBoxDemo() {
        Crispin.setBackgroundColour(Colour.BLACK);
        camera2D = createCamera(10f);
        uiCamera = new Camera2D();

        square = new Square(Colour.ORANGE);
        square.setPosition(3f, 3f);
        square.setScale(3f, 3f);

        transformedBoundBoxGraphic = new Square(Colour.BLUE);
        transformedBoundBoxGraphic.setAlpha(0.5f);

        boundBox2D = new BoundBox2D(0f, 0f, 1f, 1f);
    }

    @Override
    public void update(float deltaTime) {
        rotation += deltaTime * 0.3f;
        square.setRotationAroundPoint(1.5f, 1.5f, rotation);

        BoundBox2D transformedBoundBox = boundBox2D.transform(square.getModelMatrix());
        transformedBoundBoxGraphic.setPosition(transformedBoundBox.x, transformedBoundBox.y);
        transformedBoundBoxGraphic.setScale(transformedBoundBox.w, transformedBoundBox.h);
    }

    @Override
    public void render() {
        square.render(camera2D);
        transformedBoundBoxGraphic.render(camera2D);
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {
        if(touchType == TouchType.DOWN || touchType == TouchType.MOVE) {
            float ndcX = ((pointer.getPosition().x / Crispin.getSurfaceWidth()) * 2f) - 1f;
            float ndcY = ((pointer.getPosition().y / Crispin.getSurfaceHeight()) * 2f) - 1f;
            float[] ndc = new float[]{ndcX, ndcY, 0f, 1f};
            float[] inverse = new float[16];
            Matrix.invertM(inverse, 0, camera2D.getOrthoMatrix(), 0);

            float[] worldSpace = new float[4];
            Matrix.multiplyMV(worldSpace, 0, inverse, 0, ndc, 0);
            square.setPosition(worldSpace[0] - (square.getScale().w / 2f), worldSpace[1] - (square.getScale().h / 2f));
        }
    }

    // Min visibility of smallest dimension
    public static Camera2D createCamera(float minVisibility) {
        double viewWidth = Crispin.getSurfaceWidth();
        double viewHeight = Crispin.getSurfaceHeight();
        double viewRatio = viewWidth / viewHeight;
        float cameraWidth;
        float cameraHeight;
        if(viewWidth > viewHeight) {
            cameraWidth = (float)(minVisibility * viewRatio);
            cameraHeight = minVisibility;
        } else {
            cameraWidth = minVisibility;
            cameraHeight = (float)(minVisibility / viewRatio);
        }

        return new Camera2D(0f, 0f, cameraWidth, cameraHeight);
    }
}
