package com.games.crispin.crispinmobile.Rendering.Data;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Vector2D;

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
}
