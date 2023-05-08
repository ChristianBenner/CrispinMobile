package com.crispin.crispinmobile.UserInterface;

import android.view.MotionEvent;
import android.view.View;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Utilities.UIHandler;

import java.util.HashMap;

public class ViewTouchListener implements View.OnTouchListener {
    private HashMap<Integer, Pointer> pointers;

    public ViewTouchListener() {
        pointers = new HashMap<>();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                Vec2 downPositionCorrected = toSceneCoordinates(event.getX(pointerIndex), event.getY(pointerIndex));
                Pointer newPointer = new Pointer(downPositionCorrected);
                pointers.put(pointerId, newPointer);

                // Now we want to check if the pointer should be consumed by any UI components
                UIHandler.consume(newPointer);
                if(Crispin.getCurrentScene() != null) {
                    Crispin.getCurrentScene().touch(TouchType.DOWN, newPointer);
                }
                break;
            // ACTION_MOVE is called for any pointer movement, update positions of all pointers
            case MotionEvent.ACTION_MOVE:
                for(int pi = 0; pi < event.getPointerCount(); pi++) {
                    int movePointerId = event.getPointerId(pi);
                    Vec2 movePositionCorrected = toSceneCoordinates(event.getX(pi), event.getY(pi));
                    pointers.get(movePointerId).move(movePositionCorrected);
                    if(Crispin.getCurrentScene() != null) {
                        Crispin.getCurrentScene().touch(TouchType.MOVE, pointers.get(movePointerId));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                Vec2 upPositionCorrected = toSceneCoordinates(event.getX(pointerIndex), event.getY(pointerIndex));
                pointers.get(pointerId).release(upPositionCorrected);
                if(Crispin.getCurrentScene() != null) {
                    Crispin.getCurrentScene().touch(TouchType.UP, pointers.get(pointerId));
                }
                pointers.remove(pointerIndex);
                break;
        }
        return true;
    }

    private Vec2 toSceneCoordinates(float x, float y) {
        return new Vec2(x, Crispin.getSurfaceHeight() - y);
    }
}
