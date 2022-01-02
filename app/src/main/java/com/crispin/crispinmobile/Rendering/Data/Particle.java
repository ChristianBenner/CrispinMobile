package com.crispin.crispinmobile.Rendering.Data;

import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.Geometry.Vector2D;

public class Particle
{
    public float maxLifeTime;
    public float currentLifeTime;
    public Point2D position;
    public float size;
    public Vector2D velocity;
    public Colour colour;
    public float angle;

    public Particle(Point2D startPosition,
                    Vector2D startVelocity,
                    float size,
                    float life,
                    Colour colour,
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
        this.position = new Point2D();
        this.velocity = new Vector2D();
        this.size = 0.0f;
        this.maxLifeTime = 0.0f;
        this.currentLifeTime = 0.0f;
        this.colour = new Colour();
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
        this.colour.setRed(colourR);
        this.colour.setGreen(colourG);
        this.colour.setBlue(colourB);
        this.colour.setAlpha(colourA);
        this.angle = angle;
    }
}
