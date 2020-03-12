package com.games.crispin.crispinmobile.Rendering.Data;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;

public class ParticleBatch
{
    // Interface acts as a lambda to provide programmer ability to add their own functionality for
    // adding and updating particles. Adding particles could involve setting direction, speed or
    // other attributes. Update function is responsible for what happens to the particle through its
    // lifetime
    public interface ParticleFunctionality
    {
        void spawn(Particle particle);
        void update(Particle particle, float deltaTime);
    }

    private Particle particles[];
    private Square graphic;
    private int index;
    private int maxParticles;

    private ParticleFunctionality particleFunctionality;

    public ParticleBatch(int maxParticles,
                         ParticleFunctionality particleFunctionality,
                         int textureResourceId)
    {
        this.particles = new Particle[maxParticles];
        for(int i = 0; i < maxParticles; i++)
        {
            this.particles[i] = new Particle();
        }

        index = 0;
        this.maxParticles = maxParticles;

        this.particleFunctionality = particleFunctionality;

        graphic = new Square();
        graphic.setMaterial(new Material(textureResourceId));
    }

    public ParticleBatch(int maxParticles,
                         ParticleFunctionality particleFunctionality)
    {
        this(maxParticles, particleFunctionality, R.drawable.particle);
    }

    public void addParticle()
    {
        particleFunctionality.spawn(particles[index]);
        index++;
        if(index >= maxParticles)
        {
            index = 0;
        }
    }

    public void update(float deltaTime)
    {
        for(int i = 0; i < maxParticles; i++)
        {
            if(particles[i].currentLifeTime > 0.0f)
            {
                particleFunctionality.update(particles[i], deltaTime);
                particles[i].currentLifeTime -= deltaTime;
            }
        }
    }

    public void render(Camera2D camera)
    {
        for(int i = 0; i < maxParticles; i++)
        {
            if(particles[i].currentLifeTime > 0.0f)
            {
                graphic.setPosition(particles[i].position);
                graphic.setScale(particles[i].size, particles[i].size);
                graphic.setColour(particles[i].colour);
                graphic.setRotationAroundPoint(
                        graphic.getScale().x / 2.0f,
                        graphic.getScale().y / 2.0f,
                        0.0f,
                        particles[i].angle,
                        0.0f,
                        0.0f,
                        -1.0f);

                graphic.render(camera);
            }
        }
    }
}
