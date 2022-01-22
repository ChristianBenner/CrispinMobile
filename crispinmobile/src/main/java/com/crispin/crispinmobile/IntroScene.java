package com.crispin.crispinmobile;

import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.crispin.crispinmobile.Rendering.Utilities.TextureOptions;
import com.crispin.crispinmobile.UserInterface.Image;
import com.crispin.crispinmobile.UserInterface.Plane;
import com.crispin.crispinmobile.Utilities.Audio;
import com.crispin.crispinmobile.Utilities.Scene;

import glm_.vec2.Vec2;
import glm_.vec3.Vec3;
import glm_.vec4.Vec4;

public class IntroScene extends Scene
{
    private enum TRANSITION_STATE
    {
        NONE,
        FADE_IN,
        FADE_OUT,
        COMPLETED
    }

    // UI Camera
    private Camera2D uiCamera;

    // Background UI colour
    private Plane background;

    // Image UI for the white logo
    private Image whiteLogoImage;

    // Image UI for the red logo
    private Image redLogoImage;

    private Vec4 backgroundColour = Colour.BLACK;

    private TRANSITION_STATE backgroundTransitionState;
    private TRANSITION_STATE whiteLogoImageTransitionState;
    private TRANSITION_STATE redLogoImageTransitionState;

    final float TRANSITION_RATE = 0.015f;

    private Scene.Constructor nextSceneConstructor;

   // private FadeT

    public IntroScene(Scene.Constructor nextSceneConstructor)
    {
        Crispin.setBackgroundColour(Colour.BLACK);

        uiCamera = new Camera2D();

        background = new Plane(new Vec2(0.0f, 0.0f), new Vec2(Crispin.getSurfaceWidth(),
                Crispin.getSurfaceHeight()));
        background.setColour(Colour.BLACK);

        whiteLogoImage = createLogoImage(R.drawable.crispin_logo_red);
        whiteLogoImage.setAlpha(0.0f);

        redLogoImage = createLogoImage(R.drawable.crispin_logo_white);
        redLogoImage.setAlpha(0.0f);

        backgroundTransitionState = TRANSITION_STATE.FADE_IN;
        whiteLogoImageTransitionState = TRANSITION_STATE.FADE_IN;
        redLogoImageTransitionState = TRANSITION_STATE.NONE;

        this.nextSceneConstructor = nextSceneConstructor;

        Audio.getInstance().initSoundChannel(1);
        Audio.getInstance().playSound(R.raw.intro);
    }

    private Image createLogoImage(int resourceId)
    {
        // The texture is quite high quality so resizing it may cause sharp pixel look using default
        // min and mag filter. Set to linear filter.
        TextureOptions textureOptions = new TextureOptions();
        textureOptions.minFilter = TextureOptions.LINEAR_FILTER;
        textureOptions.magFilter = TextureOptions.LINEAR_FILTER;

        Texture texture = new Texture(resourceId, textureOptions);
        float widthToHeightRatio = (float)texture.getWidth() / (float)texture.getHeight();
        int width = Crispin.getSurfaceWidth();
        int height = (int)(width / widthToHeightRatio);
        float y = (Crispin.getSurfaceHeight() / 2.0f) - (height / 2.0f);
        Image image = new Image(texture, width, height);
        image.setPosition(0.0f, y);
        return image;
    }

    public void modifyBackgroundColour(float amount)
    {
        backgroundColour.plusAssign(amount);
        background.setColour(backgroundColour);
    }

    @Override
    public void update(float deltaTime)
    {
        // Handle background transition
        if(backgroundTransitionState == TRANSITION_STATE.FADE_IN)
        {
            // Over time, change the background colour to white
            modifyBackgroundColour(TRANSITION_RATE * deltaTime);

            // Check if its time to fade back out to white
            if(backgroundColour.r() >= 1.0f)
            {
                backgroundTransitionState = TRANSITION_STATE.FADE_OUT;
            }
        }
        else if(backgroundTransitionState == TRANSITION_STATE.FADE_OUT)
        {
            // Over time, change the background colour to black
            modifyBackgroundColour(-TRANSITION_RATE * deltaTime);

            if(backgroundColour.r() <= 0.0f)
            {
                backgroundTransitionState = TRANSITION_STATE.COMPLETED;
            }
        }

        // Handle white logo image transition
        if(whiteLogoImageTransitionState == TRANSITION_STATE.FADE_IN)
        {
            whiteLogoImage.setAlpha(whiteLogoImage.getOpacity() + (TRANSITION_RATE * deltaTime));

            if(whiteLogoImage.getOpacity() >= 1.0f)
            {
                whiteLogoImageTransitionState = TRANSITION_STATE.FADE_OUT;
                redLogoImageTransitionState = TRANSITION_STATE.FADE_IN;
            }
        }
        else if(whiteLogoImageTransitionState == TRANSITION_STATE.FADE_OUT)
        {
            whiteLogoImage.setAlpha(whiteLogoImage.getOpacity() + (-TRANSITION_RATE * deltaTime));

            if(whiteLogoImage.getOpacity() <= 0.0f)
            {
                whiteLogoImageTransitionState = TRANSITION_STATE.COMPLETED;
            }
        }

        // Handle red logo image transition
        if(redLogoImageTransitionState == TRANSITION_STATE.FADE_IN)
        {
            redLogoImage.setAlpha(redLogoImage.getOpacity() + (TRANSITION_RATE * deltaTime));

            if(redLogoImage.getOpacity() >= 1.0f)
            {
                redLogoImageTransitionState = TRANSITION_STATE.FADE_OUT;
            }
        }
        else if(redLogoImageTransitionState == TRANSITION_STATE.FADE_OUT)
        {
            redLogoImage.setAlpha(redLogoImage.getOpacity() + (-TRANSITION_RATE * deltaTime));

            if(redLogoImage.getOpacity() <= 0.0f)
            {
                redLogoImageTransitionState = TRANSITION_STATE.COMPLETED;
            }
        }
        else if(redLogoImageTransitionState == TRANSITION_STATE.COMPLETED)
        {
            if(nextSceneConstructor != null)
            {
                Crispin.setScene(nextSceneConstructor);
            }
        }
    }

    @Override
    public void render()
    {
        background.draw(uiCamera);
        whiteLogoImage.draw(uiCamera);
        redLogoImage.draw(uiCamera);
    }

    @Override
    public void touch(int type, Vec2 position)
    {

    }
}
