package com.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES20.GL_COLOR_ATTACHMENT0;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_RGB;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glFramebufferTexture2D;
import static android.opengl.GLES20.glGenFramebuffers;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES30.GL_TEXTURE_2D_ARRAY;
import static android.opengl.GLES30.glTexImage3D;

import android.hardware.lights.Light;
import android.opengl.GLES30;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Models.ShadowModel;

// todo: investigate making this capable of rendering entire sets of models or instance renderers
public class ShadowMap {
    private int[] framebuffer;
    private int[] fbTexture;
    private static final int SHADOW_MAP_SIZE = 512;
    private static final int MAX_SHADOW_MAPS = 10;

    public ShadowMap() {
        // Create the frame buffers and texture array for the shadow maps
        framebuffer = new int[MAX_SHADOW_MAPS];
        fbTexture = new int[1];
        glGenFramebuffers(MAX_SHADOW_MAPS, framebuffer, 0);
        glGenTextures(1, fbTexture, 0);

        for(int i = 0; i < MAX_SHADOW_MAPS; i++) {
            glBindFramebuffer(GL_FRAMEBUFFER, framebuffer[i]);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D_ARRAY, fbTexture[0], 0);
            glBindTexture(GL_TEXTURE_2D_ARRAY, fbTexture[0]);
            glTexImage3D(GL_TEXTURE_2D_ARRAY, i, GL_RGB, SHADOW_MAP_SIZE, SHADOW_MAP_SIZE, MAX_SHADOW_MAPS, 0, GL_RGB, GL_UNSIGNED_BYTE, null);
            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glBindTexture(GL_TEXTURE_2D_ARRAY, 0);
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getShadowMap() {
        return fbTexture[0];
    }

    public void start(int n) {
        if(n >= MAX_SHADOW_MAPS) {
            return;
        }

        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer[n]);
        glViewport(0, 0, SHADOW_MAP_SIZE, SHADOW_MAP_SIZE);
        GLES30.glFramebufferTextureLayer(GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, fbTexture[0], 0, n);
        GLES30.glClearColor(1f, 1f, 1f, 1f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
    }

    public void end() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        // todo: set back to original?
        glViewport(0, 0, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());
    }
}
