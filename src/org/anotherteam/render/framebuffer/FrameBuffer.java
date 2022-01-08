package org.anotherteam.render.framebuffer;
import static org.lwjgl.opengl.GL42.*;

import org.anotherteam.render.texture.Texture;
import org.anotherteam.util.exception.RenderException;
import org.jetbrains.annotations.NotNull;

public final class FrameBuffer {

    private final int fboId;
    private Texture texture;

    public FrameBuffer(int width, int height) {
        fboId = glGenFramebuffers();
        changeSize(width, height);
    }

    public void changeSize(int width, int height) {
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);

        if (texture != null) texture.destroy();
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
        glViewport(0, 0, texture.getWidth(), texture.getHeight());
        glEnable(GL_BLEND);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA, GL_ONE);
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
    }

    public void end() {
        glDisable(GL_BLEND);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}
