package com.crispin.crispinmobile.Utilities;

import com.crispin.crispinmobile.Rendering.Utilities.Mesh;

import java.util.EventListener;

public interface LoadListener extends EventListener {
    void onLoad(Mesh mesh);
}
