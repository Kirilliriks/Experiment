package org.anotherteam.editor;

import org.anotherteam.Game;
import org.anotherteam.data.AssetsData;
import org.anotherteam.editor.gui.Widget;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.framebuffer.FrameBuffer;
import org.anotherteam.render.text.Font;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public final class Editor extends Widget {
    public final static Font editorFont = new Font("font/font.ttf", 16);

    private final Game game;
    private final GameScreen gameScreen;
    private final EditorBatch editorBatch;
    private final FrameBuffer editorFrame;

    public Editor(@NotNull Game game) {
        super("Editor", new Vector2f(200, 200), 200, 100);
        this.game = game;
        this.gameScreen = game.getGameScreen();
        this.editorBatch = new EditorBatch(AssetsData.DEFAULT_SHADER, gameScreen.windowCamera);
        this.editorFrame = new FrameBuffer(game.getGameScreen().window.getWidth(), gameScreen.window.getHeight());
    }

    public void render(@NotNull RenderBatch renderBatch) {
        editorFrame.begin();
        editorBatch.begin();
        super.render(editorBatch);
        editorBatch.end();
        editorFrame.end();

        renderBatch.draw(editorFrame.getTexture(), 0, 0, gameScreen.window.getWidth(), gameScreen.window.getHeight(), false, true);
    }
}
