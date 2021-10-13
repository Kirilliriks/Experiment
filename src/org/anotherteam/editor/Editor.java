package org.anotherteam.editor;

import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.Input;
import org.anotherteam.data.AssetsData;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.Label;
import org.anotherteam.editor.gui.Widget;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.framebuffer.FrameBuffer;
import org.anotherteam.render.text.Font;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public final class Editor extends Widget {
    public final static Font editorFont = new Font("font/font.ttf", 16);

    private final Game game;
    private final GameScreen gameScreen;
    private final EditorBatch editorBatch;
    private final FrameBuffer editorFrame;

    //GUI
    private static Label logLabel;
    private final Button switchModeButton;

    public Editor(@NotNull Game game, @NotNull GameScreen gameScreen) {
        super("Editor", new Vector2f(10, GameScreen.window.getHeight() / 2.0f), GameScreen.window.getWidth() - 10, GameScreen.window.getHeight() / 2 - 40);
        this.game = game;
        this.gameScreen = gameScreen;
        this.editorBatch = new EditorBatch(AssetsData.DEFAULT_SHADER, gameScreen.windowCamera);
        this.editorFrame = new FrameBuffer(GameScreen.window.getWidth(), GameScreen.window.getHeight());
        this.switchModeButton = new Button("Switch mode", new Vector2f(width / 2.0f, 15), 10);
        switchModeButton.setRunnable(() -> {
            if (game.getGameState() == GameState.ON_EDITOR) {
                game.setGameState(GameState.ON_LEVEL);
                return;
            }
            game.setGameState(GameState.ON_EDITOR);
        });
        addElement(switchModeButton);
        logLabel = new Label("", new Vector2f(0, 15), 10);
        addElement(logLabel);
    }

    public static void setLogText(String text) {
        logLabel.setText(text);
    }

    private void renderGUI() {
        editorFrame.begin();
        editorBatch.begin();
        super.render(editorBatch);
        editorBatch.drawText(editorFont, "Pos: " + Input.getMousePos().x + " " + Input.getMousePos().y,
                15, (int) pos.y + 5, 1.0f, new Color(255, 255, 255, 255));
        editorBatch.end();
        editorFrame.end();
    }

    public void renderFrame(@NotNull RenderBatch renderBatch) {
        renderGUI();
        renderBatch.draw(editorFrame.getTexture(), 0, 0, GameScreen.window.getWidth(), GameScreen.window.getHeight(), false, true);
    }
}
