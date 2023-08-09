package com.crispin.demos;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Font;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.TextRewrite;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.Scene;

/**
 * A demonstration scene designed to show the usage of text in the Crispin engine
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @since 1.0
 */
public class TextRewriteDemo extends Scene {
    // Size to load the font as
    private static final int FONT_SIZE = 256;

    // Back button
    private Button backButton;

    // Standard text with a maximum length
    private final TextRewrite singleChar;

    // 2-dimensional camera
    private final Camera2D camera2D;

    /**
     * Construct the text demo scene
     *
     * @since 1.0
     */
    public TextRewriteDemo() {
        Crispin.setBackgroundColour(Colour.DARK_GREY);

        camera2D = new Camera2D();
        backButton = Util.createBackButton(DemoMasterScene::new);

        final Font AILERON_REGULAR = new Font(R.raw.aileron_regular, FONT_SIZE);
        singleChar = new TextRewrite(AILERON_REGULAR, "g");
        singleChar.setColour(Colour.BLACK);
        singleChar.setPosition(0.0f, Crispin.getSurfaceHeight() - singleChar.getHeight());
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
        singleChar.draw(camera2D);
        backButton.draw(camera2D);
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {

    }
}
