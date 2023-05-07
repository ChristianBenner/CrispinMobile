package com.crispin.crispinmobile.Utilities;

/**
 * Created by Christian Benner on 16/12/2017.
 */

public class Sound {
    private final int sound;
    private boolean loaded;

    public Sound(int soundId, boolean loaded) {
        this.sound = soundId;
        this.loaded = loaded;
    }

    public int getSound() {
        return this.sound;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void setLoaded(boolean state) {
        this.loaded = state;
    }
}