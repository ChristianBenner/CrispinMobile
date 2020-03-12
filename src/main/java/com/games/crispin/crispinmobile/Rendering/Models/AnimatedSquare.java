package com.games.crispin.crispinmobile.Rendering.Models;

import android.opengl.GLES20;

import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;

public class AnimatedSquare extends Square
{
    private int spriteHeight;
    private int frames = 0;
    private float step = 0.0f;
    private int currentFrame = 0;
    private int animationDurationMs;
    private int animationFrameDurationMs;
    private long lastFrameStart = 0;

    public AnimatedSquare(Material material, int spriteHeight, int animationDurationMs)
    {
        super(material);
        this.material = material;
        this.spriteHeight = spriteHeight;

        if(spriteHeight == 0)
        {
            spriteHeight = 1;
        }

        frames = material.getTexture().getHeight() / spriteHeight;
        step = 1f / frames;

        this.animationDurationMs = animationDurationMs;
        this.animationFrameDurationMs = animationDurationMs / frames;
        lastFrameStart = System.currentTimeMillis();

        material.setUvMultiplier(1f, step);
    }

    public void updateAnimation()
    {
        if(System.currentTimeMillis() > lastFrameStart + animationFrameDurationMs)
        {
            currentFrame++;
            if(currentFrame >= frames)
            {
                currentFrame = 0;
            }

            material.setUvOffset(0.0f, step * currentFrame);

            lastFrameStart = System.currentTimeMillis();
        }
    }

    public void setSpriteHeight(int height)
    {
        // Calculate frames from sprite height
        frames = material.getTexture().getHeight() / height;
        this.spriteHeight = height;
    }

    public void setMaterial(Material material, int spriteHeight)
    {
        this.material = material;
        setSpriteHeight(spriteHeight);
    }

    public void setTexture(Texture texture, int spriteHeight)
    {
        this.material.setTexture(texture);
        setSpriteHeight(spriteHeight);
    }
}
