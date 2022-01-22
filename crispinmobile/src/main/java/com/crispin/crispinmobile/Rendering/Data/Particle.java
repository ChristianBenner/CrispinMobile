package com.crispin.crispinmobile.Rendering.Data;

import glm_.vec2.Vec2;
import glm_.vec4.Vec4;

public class Particle
{
    public float maxLifeTime;
    public float currentLifeTime;
    public Vec2 position;
    public float size;
    public Vec2 velocity;
    public Vec4 colour;
    public float angle;

    public Particle(Vec2 startPosition,
                    Vec2 startVelocity,
                    float size,
                    float life,
                    Vec4 colour,
                    float angle)
    {
        this.position = startPosition;
        this.velocity = startVelocity;
        this.size = size;
        this.maxLifeTime = life;
        this.currentLifeTime = life;
        this.colour = colour;
        this.angle = angle;
    }

    public Particle()
    {
        this.position = new Vec2();
        this.velocity = new Vec2();
        this.size = 0.0f;
        this.maxLifeTime = 0.0f;
        this.currentLifeTime = 0.0f;
        this.colour = new Vec4();
        this.angle = 0.0f;
    }

    public void setAttributes(float posX,
                              float posY,
                              float velocityX,
                              float velocityY,
                              float size,
                              float life,
                              float colourR,
                              float colourG,
                              float colourB,
                              float colourA,
                              float angle)
    {
        this.position.x = posX;
        this.position.y = posY;
        this.velocity.x = velocityX;
        this.velocity.y = velocityY;
        this.size = size;
        this.maxLifeTime = life;
        this.currentLifeTime = life;
        this.colour.x = colourR;
        this.colour.y = colourG;
        this.colour.z = colourB;
        this.colour.w = colourA;
        this.angle = angle;
    }
}
