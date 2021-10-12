package org.anotherteam.render.frame;

import org.anotherteam.render.GameRender;
import org.anotherteam.render.batch.RenderBatch;
import org.jetbrains.annotations.NotNull;

public final class TextureFrame extends AbstractFrame {

    public TextureFrame(@NotNull GameRender gameRender, @NotNull RenderBatch renderBatch) {
        super(gameRender, renderBatch);
    }
}
