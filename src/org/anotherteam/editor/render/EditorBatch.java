package org.anotherteam.editor.render;

import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.shader.Shader;
import org.jetbrains.annotations.NotNull;

public final class EditorBatch extends RenderBatch {

    public EditorBatch(@NotNull Shader shader, @NotNull Camera camera) {
        super(shader, camera);
    }
}