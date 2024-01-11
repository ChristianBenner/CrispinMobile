package com.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES30.GL_COLOR_ATTACHMENT0;
import static android.opengl.GLES30.GL_FRAMEBUFFER;
import static android.opengl.GLES30.GL_NEAREST;
import static android.opengl.GLES30.GL_REPEAT;
import static android.opengl.GLES30.GL_RGBA;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES30.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES30.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES30.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES30.GL_UNSIGNED_BYTE;
import static android.opengl.GLES30.GL_VIEWPORT;
import static android.opengl.GLES30.glBindFramebuffer;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glFramebufferTexture2D;
import static android.opengl.GLES30.glGenFramebuffers;
import static android.opengl.GLES30.glGenTextures;
import static android.opengl.GLES30.glGetIntegerv;
import static android.opengl.GLES30.glTexImage2D;
import static android.opengl.GLES30.glTexParameteri;
import static android.opengl.GLES30.glViewport;

import android.opengl.GLES30;

import com.crispin.crispinmobile.Rendering.Data.GlTexture;
import com.crispin.crispinmobile.Rendering.Data.Texture;

public class TextureRenderer {
    public interface DrawCallInterface {
        void draw();
    }

    private int width;
    private int height;
    private int[] framebuffer;
    private int[] fbTexture;
    private GlTexture glTexture;

    public TextureRenderer(int width, int height) {
        this.width = width;
        this.height = height;
        framebuffer = new int[1];
        fbTexture = new int[1];
        genGlObjects();
    }

    public Texture getTexture() {
        return glTexture;
    }

    // Clears by default each call
    public Texture draw(DrawCallInterface drawCallInterface) {
        return draw(true, drawCallInterface);
    }

    public void clear() {
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer[0]);
        glBindTexture(GL_TEXTURE_2D, fbTexture[0]);
//        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_ALPHA_BITS);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public Texture draw(boolean clear, DrawCallInterface drawCallInterface) {
        int[] originalViewportDimensions = new int[4];
        glGetIntegerv(GL_VIEWPORT, originalViewportDimensions, 0);

        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer[0]);
        glBindTexture(GL_TEXTURE_2D, fbTexture[0]);

        glViewport(0, 0, width, height);

        if(clear) {
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_ALPHA_BITS);
        }

        drawCallInterface.draw();

        // Unbind and set viewport back to original size
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(originalViewportDimensions[0], originalViewportDimensions[1],
                originalViewportDimensions[2], originalViewportDimensions[3]);
        return glTexture;
    }

    private void genGlObjects() {
        glGenFramebuffers(1, framebuffer, 0);
        glGenTextures(1, fbTexture, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer[0]);
        glBindTexture(GL_TEXTURE_2D, fbTexture[0]);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, fbTexture[0], 0);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glTexture = new GlTexture(fbTexture[0], width, height);
    }
}
