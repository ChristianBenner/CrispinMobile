package com.crispin.crispinmobile.Rendering.Utilities;

import com.crispin.crispinmobile.Rendering.Data.ParticleBatch;

import java.util.ArrayList;

public class ParticleEngine
{
    private ArrayList<ParticleBatch> particleBatches;

    public ParticleEngine()
    {
        particleBatches = new ArrayList<>();
    }

    public void addParticleBatch(ParticleBatch particleBatch)
    {
        particleBatches.add(particleBatch);
    }

    public boolean removeParticleBatch(ParticleBatch particleBatch)
    {
        return particleBatches.remove(particleBatch);
    }

    // Update each particle batch
    public void update(float deltaTime)
    {
        for(ParticleBatch particleBatch : particleBatches)
        {
            particleBatch.update(deltaTime);
        }
    }

    public void render(Camera2D camera)
    {
        for(ParticleBatch particleBatch : particleBatches)
        {
            particleBatch.render(camera);
        }
    }
}
