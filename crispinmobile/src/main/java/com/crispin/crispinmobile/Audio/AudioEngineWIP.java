package com.crispin.crispinmobile.Audio;

import android.content.res.AssetManager;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Native.CrispinNativeInterface;
import com.crispin.crispinmobile.Utilities.Logger;

import java.io.IOException;

public class AudioEngineWIP {
    public AudioEngineWIP() {
        CrispinNativeInterface.initAudioEngine();
    }

    public void playSound() {
        AssetManager assetManager = Crispin.getActivity().getAssets();
        boolean state = CrispinNativeInterface.loadAudio(assetManager, "pistol_shot_asset_test.wav");
        if(state == false) {
            Logger.error(getClass().getName(), "FAILED TO LOAD AUDIO");
        }
    }
}
