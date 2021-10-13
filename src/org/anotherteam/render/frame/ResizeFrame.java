package org.anotherteam.render.frame;

import org.anotherteam.render.batch.RenderBatch;
import org.jetbrains.annotations.NotNull;

/**
 * Resize game frame to player window size
 */
public class ResizeFrame extends AbstractFrame {

    public ResizeFrame(@NotNull RenderBatch renderBatch, int width, int height) {
        super(renderBatch, width, height);
    }
}
