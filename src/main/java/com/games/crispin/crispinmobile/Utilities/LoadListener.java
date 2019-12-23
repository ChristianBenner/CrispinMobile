package com.games.crispin.crispinmobile.Utilities;

import com.games.crispin.crispinmobile.Rendering.Utilities.Model;

import java.util.EventListener;

public interface LoadListener extends EventListener
{
    void onLoad(Model model);
}
