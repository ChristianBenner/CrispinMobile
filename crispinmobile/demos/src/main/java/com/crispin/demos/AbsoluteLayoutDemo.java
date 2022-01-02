package com.crispin.demos;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.crispin.crispinmobile.Crispin;
import com.crispin.demos.scenes.AbsoluteLayoutDemoScene;
import com.crispin.demos.scenes.LinearLayoutDemoScene;

public class AbsoluteLayoutDemo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request that the application does not have a title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide the action bar at the top of the application
        getSupportActionBar().hide();

        // Add graphical view to frame layout
        Crispin.init(this, () -> new AbsoluteLayoutDemoScene());
    }
}
