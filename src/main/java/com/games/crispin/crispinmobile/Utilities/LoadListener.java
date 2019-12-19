package com.games.crispin.crispinmobile.Utilities;

import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

import java.util.EventListener;

public interface LoadListener extends EventListener
{
    void onLoad(RenderObject renderObject);
}
