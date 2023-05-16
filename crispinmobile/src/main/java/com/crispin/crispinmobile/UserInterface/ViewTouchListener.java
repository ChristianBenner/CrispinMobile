package com.crispin.crispinmobile.UserInterface;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Utilities.UIHandler;

import java.util.HashMap;
import java.util.Queue;

public class ViewTouchListener implements View.OnTouchListener {
    private HashMap<Integer, Pointer> pointers;
    private GLSurfaceView glSurfaceView; // save so we can queue touch events to run on gl thread

    public ViewTouchListener(GLSurfaceView glSurfaceView) {
        pointers = new HashMap<>();
        this.glSurfaceView = glSurfaceView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                Vec2 downPositionCorrected = toSceneCoordinates(event.getX(pointerIndex), event.getY(pointerIndex));
                Pointer newPointer = new Pointer(downPositionCorrected, pointerId);
                pointers.put(pointerId, newPointer);

                glSurfaceView.queueEvent(() -> {
                    // Now we want to check if the pointer should be consumed by any UI components
                    boolean consumedByUI = UIHandler.consume(newPointer);

                    // If there is currently a scene and the UI did not consume the pointer, pass
                    // event to scene
                    if (!consumedByUI && Crispin.getCurrentScene() != null) {
                        Crispin.getCurrentScene().touch(TouchType.DOWN, newPointer);
                    }
                });
                break;
            // ACTION_MOVE is called for any pointer movement, update positions of all pointers
            case MotionEvent.ACTION_MOVE:
                for (int pi = 0; pi < event.getPointerCount(); pi++) {
                    int movePointerId = event.getPointerId(pi);
                    Vec2 movePositionCorrected = toSceneCoordinates(event.getX(pi), event.getY(pi));
                    Pointer movePointer = pointers.get(movePointerId);
                    glSurfaceView.queueEvent(() -> {
                        movePointer.move(movePositionCorrected);

                        // Only pass to scene if not consumed by UI
                        if (!movePointer.isConsumedByUI() && Crispin.getCurrentScene() != null) {
                            Crispin.getCurrentScene().touch(TouchType.MOVE, movePointer);
                        }
                    });
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                Vec2 upPositionCorrected = toSceneCoordinates(event.getX(pointerIndex), event.getY(pointerIndex));
                Pointer upPointer = pointers.get(pointerId);
                glSurfaceView.queueEvent(() -> {
                    boolean consumed = upPointer.isConsumedByUI();
                    upPointer.release(upPositionCorrected);

                    // Only pass to scene if not consumed by UI
                    if (!consumed && Crispin.getCurrentScene() != null) {
                        Crispin.getCurrentScene().touch(TouchType.UP, upPointer);
                    }
                });
                pointers.remove(pointerIndex);
                break;
        }
        return true;
    }

    private Vec2 toSceneCoordinates(float x, float y) {
        return new Vec2(x, Crispin.getSurfaceHeight() - y);
    }
}
