package com.crispin.demos;

import com.crispin.crispinmobile.Audio.AudioEngineWIP;
import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Data.TextureResource;
import com.crispin.crispinmobile.Rendering.Models.Square;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.UserInterface.Text;
import com.crispin.crispinmobile.UserInterface.TouchType;
import com.crispin.crispinmobile.Utilities.FontCache;
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
public class AudioDemo extends Scene {
    private Camera2D uiCamera;
    private Text title;
    private AudioEngineWIP audioEngineWIP;

    public AudioDemo() {
        Crispin.setBackgroundColour(Colour.DARK_GREY);
        uiCamera = new Camera2D();
        title = new Text(FontCache.getFont(R.raw.aileron_bold, 76), "Audio Demo", false, true, uiCamera.getRight());
        title.setPosition(0f, Crispin.getSurfaceHeight() - title.getHeight() - 10f);
        title.setColour(Colour.WHITE);
        audioEngineWIP = new AudioEngineWIP();
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
        title.draw(uiCamera);
    }

    @Override
    public void touch(TouchType touchType, Pointer pointer) {
        if(touchType == TouchType.UP) {
            System.out.println("PLAY SOUND");
            audioEngineWIP.playSound();
        }
    }
}
