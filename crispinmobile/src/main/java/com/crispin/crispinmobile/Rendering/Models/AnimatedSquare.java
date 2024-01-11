package com.crispin.crispinmobile.Rendering.Models;

import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Data.Texture;

import java.util.Random;

public class AnimatedSquare extends Square {
    private final int animationDurationMs;
    private int spriteHeight;
    private int frames = 0;
    private float step = 0.0f;
    private int currentFrame = 0;
    private int animationFrameDurationMs;
    private long lastFrameStart = 0;
    private boolean repeat;
    private boolean reverseFrameCount;

    public AnimatedSquare(Material material, int spriteHeight, int animationDurationMs) {
        super(material);
        this.repeat = true;
        this.reverseFrameCount = false;
        this.material = material;
        this.animationDurationMs = animationDurationMs;

        setSpriteHeight(spriteHeight);
    }

    public AnimatedSquare(Texture texture, int spriteHeight, int animationDurationMs) {
        super(texture);
        this.repeat = true;
        this.reverseFrameCount = false;
        this.animationDurationMs = animationDurationMs;

        setSpriteHeight(spriteHeight);
    }

    public AnimatedSquare(int textureResource, int spriteHeight, int animationDurationMs) {
        super(textureResource);
        this.repeat = true;
        this.reverseFrameCount = false;
        this.animationDurationMs = animationDurationMs;

        setSpriteHeight(spriteHeight);
    }

    public AnimatedSquare(int textureResource, int spriteHeight, int animationDurationMs, boolean randomStartFrame) {
        super(textureResource);
        this.repeat = true;
        this.reverseFrameCount = false;
        this.animationDurationMs = animationDurationMs;

        setSpriteHeight(spriteHeight);

        if(randomStartFrame) {
            Random random = new Random(System.currentTimeMillis());
            currentFrame = random.nextInt(frames);
        }
    }

    // false to repeat the animation (go to frame 0 after the last frame)
    // true to traverse back through the frames back to 0
    public void setReverse(boolean reverse) {
        this.repeat = !reverse;
    }

    // true to repeat the animation (go to frame 0 after the last frame)
    // false to traverse back through the frames back to 0
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int getFrames() {
        return frames;
    }

    public void updateAnimation() {
        if (System.currentTimeMillis() > lastFrameStart + animationFrameDurationMs) {
            if (repeat) {
                currentFrame++;

                if (currentFrame >= frames) {
                    currentFrame = 0;
                }
            } else {
                if (reverseFrameCount) {
                    if (currentFrame == 1) {
                        reverseFrameCount = false;
                        currentFrame = 0;
                    } else {
                        currentFrame--;
                    }
                } else {
                    currentFrame++;
                    if (currentFrame >= frames - 1) {
                        reverseFrameCount = true;
                    }
                }
            }


            material.setUvOffset(0.0f, step * currentFrame);

            lastFrameStart = System.currentTimeMillis();
        }
    }

    public void setAnimationDurationMs(int animationDurationMs) {
        this.animationFrameDurationMs = animationDurationMs / frames;
    }

    public void setSpriteHeight(int height) {
        // Calculate frames from sprite height
        this.spriteHeight = height;

        if (spriteHeight == 0) {
            spriteHeight = 1;
        }

        frames = material.getTexture().getHeight() / spriteHeight;
        step = 1f / frames;

        this.animationFrameDurationMs = animationDurationMs / frames;
        lastFrameStart = System.currentTimeMillis();

        material.setUvMultiplier(1f, step);
    }

    public void setMaterial(Material material, int spriteHeight) {
        this.material = material;
        setSpriteHeight(spriteHeight);
    }

    public void setTexture(Texture texture, int spriteHeight) {
        this.material.setTexture(texture);
        setSpriteHeight(spriteHeight);
    }
}
