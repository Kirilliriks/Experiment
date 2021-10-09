package org.anotherteam.render.framebuffer;
import org.anotherteam.render.texture.Texture;
import org.anotherteam.util.exception.RenderException;
import org.jetbrains.annotations.NotNull;

import static org.lwjgl.opengl.GL42.*;

public final class Framebuffer {

    private final int fboId;
    private final Texture texture;

    public Framebuffer(int width, int height) {
        fboId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);

        texture = new Texture(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.getId(), 0);

        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new RenderException("Framebuffer is not complete");

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @NotNull
    public Texture getTexture() {
        return texture;
    }

    public void begin() {
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
    }

    public void end() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}
