package com.crispin.crispinmobile.Utilities;

import com.crispin.crispinmobile.Rendering.Utilities.Model;

import java.util.EventListener;

public interface LoadListener extends EventListener
{
    void onLoad(Model model);
}