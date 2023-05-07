package com.crispin.crispinmobile.Rendering.Data;

import com.crispin.crispinmobile.Geometry.Vec2;

public class Particle {
    public float maxLifeTime;
    public float currentLifeTime;
    public Vec2 position;
    public float size;
    public Vec2 velocity;
    public Colour colour;
    public float angle;

    public Particle(Vec2 startPosition, Vec2 startVelocity, float size, float life, Colour colour,
                    float angle) {
        this.position = startPosition;
        this.velocity = startVelocity;
        this.size = size;
        this.maxLifeTime = life;
        this.currentLifeTime = life;
        this.colour = colour;
        this.angle = angle;
    }

    public Particle() {
        this.position = new Vec2();
        this.velocity = new Vec2();
        this.size = 0.0f;
        this.maxLifeTime = 0.0f;
        this.currentLifeTime = 0.0f;
        this.colour = new Colour();
        this.angle = 0.0f;
    }

    public void setAttributes(float posX, float posY, float velocityX, float velocityY, float size,
                              float life, float colourR, float colourG, float colourB,
                              float colourA, float angle) {
        this.position.x = posX;
        this.position.y = posY;
        this.velocity.x = velocityX;
        this.velocity.y = velocityY;
        this.size = size;
        this.maxLifeTime = life;
        this.currentLifeTime = life;
        this.colour.red = colourR;
        this.colour.green = colourG;
        this.colour.blue = colourB;
        this.colour.alpha = colourA;
        this.angle = angle;
    }
}
