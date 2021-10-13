package org.anotherteam.editor;

import org.anotherteam.Game;
import org.anotherteam.data.AssetsData;
import org.anotherteam.editor.gui.Widget;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.render.text.Font;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public final class Editor extends Widget {
    public final static Font editorFont = new Font("font/font.ttf", 16);

    private final Game game;
    private final EditorBatch editorBatch;

    public Editor(@NotNull Game game) {
        super("Editor", new Vector2f(0, 0), 100, 100);
        this.game = game;
        this.editorBatch = new EditorBatch(AssetsData.DEFAULT_SHADER, game.getGameScreen().windowCamera);
    }

    public void render() {
        super.render(editorBatch);
    }
}
