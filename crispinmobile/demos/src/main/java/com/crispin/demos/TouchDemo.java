package com.crispin.demos;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Data.TextureResource;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.Scene;

import java.util.HashMap;
import java.util.Random;

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

    class PositionAndColour {
        Vec2 position = new Vec2();
        Colour colour = new Colour();
    }

    private PositionAndColour[] touchLocations;
    int index;

    private Square touchLocationSprite;

    private Button backButton;

    private HashMap<Integer, Colour> pointerColourMap;

    private Random random;

    /**
     * Construct the touch demo scene
     *
     * @since 1.0
     */
    public TouchDemo() {
        Crispin.setBackgroundColour(Colour.DARK_GREY);

        touchLocations = new PositionAndColour[DRAW_CAP];
        for(int i = 0; i < touchLocations.length; i++) {
            touchLocations[i] = new PositionAndColour();
        }

        index = 0;

        camera2D = new Camera2D();

        touchLocationSprite = new Square(new Material(new TextureResource(R.drawable.touch_demo_circle)));
        touchLocationSprite.setScale(TOUCH_SPRITE_SIZE, TOUCH_SPRITE_SIZE);

        backButton = Util.createBackButton(DemoMasterScene::new);

        pointerColourMap = new HashMap<>();
        random = new Random();
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
                touchLocationSprite.setColour(touchLocations[i].colour);
                touchLocationSprite.setPosition(touchLocations[i].position.x - (TOUCH_SPRITE_SIZE / 2f),
                        touchLocations[i].position.y - (TOUCH_SPRITE_SIZE / 2f));
                touchLocationSprite.render(camera2D);
            }
        }

        backButton.draw(camera2D);
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {
        Vec2 position = new Vec2(pointer.getPosition());

        switch (touchType) {
            case DOWN:
                pointerColourMap.put(pointer.getPointerId(), new Colour(random.nextFloat(),
                        random.nextFloat(), random.nextFloat()));
            case MOVE:
                touchLocations[index].colour = pointerColourMap.get(pointer.getPointerId());
                touchLocations[index].position = position;
                index++;
                if(index >= DRAW_CAP) {
                    index = 0;
                }
                break;
            case UP:
                pointerColourMap.remove(pointer.getPointerId());
        }
    }
}
