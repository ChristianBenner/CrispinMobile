package com.crispin.demos.Scenes;

import android.view.MotionEvent;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Data.Texture;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Border;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Font;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.TouchEvent;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.demos.R;

/**
 * A demonstration scene designed to show the touch capabilities of the engine
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @since 1.0
 */
public class TouchDemo extends Scene {
    private final int DRAW_CAP = 100;
    private final float TOUCH_SPRITE_SIZE = 32f;

    // 2-dimensional camera
    private final Camera2D camera2D;

    private Vec2[] touchLocations;
    int index;

    private Square touchLocationSprite;

    private Button homeButton;

    /**
     * Construct the touch demo scene
     *
     * @since 1.0
     */
    public TouchDemo() {
        Crispin.setBackgroundColour(Colour.DARK_GREY);

        touchLocations = new Vec2[DRAW_CAP];
        index = 0;

        camera2D = new Camera2D();

        touchLocationSprite = new Square(new Material(new Texture(R.drawable.touch_demo_circle)));
        touchLocationSprite.setScale(TOUCH_SPRITE_SIZE, TOUCH_SPRITE_SIZE);

        Font homeButtonFont = new Font(com.crispin.demos.R.raw.aileron_regular, 36);
        homeButton = new Button(homeButtonFont, "Back");
        homeButton.setPosition(Crispin.getSurfaceWidth() - 10 - 200, 10);
        homeButton.setSize(200, 200);
        homeButton.setBorder(new Border(Colour.BLACK));
        homeButton.setColour(Colour.LIGHT_GREY);
        homeButton.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                Crispin.setScene(DemoMasterScene::new);
            }
        });
    }

    /**
     * Update function that overrides the Scene base class
     *
     * @see Scene
     * @since 1.0
     */
    @Override
    public void update(float deltaTime) {

    }

    /**
     * Render function that overrides the Scene base class
     *
     * @see Scene
     * @since 1.0
     */
    @Override
    public void render() {
        int startIndex = index == 0 ? DRAW_CAP - 1 : index - 1;
        int endIndex = startIndex + 1 == DRAW_CAP ? 0 : startIndex + 1;

        for(int i = startIndex; i != endIndex; i--) {
            if(i < 0) {
                i = DRAW_CAP - 1;

                if(i == endIndex) {
                    break;
                }
            }

            if(touchLocations[i] != null) {
                touchLocationSprite.setPosition(touchLocations[i].x - (TOUCH_SPRITE_SIZE / 2f),
                        touchLocations[i].y - (TOUCH_SPRITE_SIZE / 2f));
                touchLocationSprite.render(camera2D);
            }
        }

        homeButton.draw(camera2D);
    }

    @Override
    public void touch(int eventType, Pointer pointer) {
        Vec2 position = new Vec2(pointer.getPosition());

        switch (eventType) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                touchLocations[index] = position;
                index++;
                if(index >= DRAW_CAP) {
                    index = 0;
                }
                break;
        }
    }
}
