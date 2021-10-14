package org.anotherteam.editor;

import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.Input;
import org.anotherteam.data.AssetsData;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.Log;
import org.anotherteam.editor.gui.Widget;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.framebuffer.FrameBuffer;
import org.anotherteam.render.text.Font;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public final class Editor extends Widget {
    public final static Font editorFont = new Font("font/f1.ttf", 8);

    private final Game game;
    private final GameScreen gameScreen;
    private final EditorBatch editorBatch;
    private final FrameBuffer editorFrame;

    //GUI
    private static Log log;

    private final Button switchModeButton;

    public Editor(@NotNull Game game, @NotNull GameScreen gameScreen) {
        super("Editor GGG",
                10, GameScreen.window.getHeight() / 2.0f,
                GameScreen.window.getWidth() - 10, GameScreen.window.getHeight() / 2 - 40);
        editorFont.setScale(2.0f);
        this.game = game;
        this.gameScreen = gameScreen;
        this.editorBatch = new EditorBatch(AssetsData.DEFAULT_SHADER, gameScreen.windowCamera);
        this.editorFrame = new FrameBuffer(GameScreen.window.getWidth(), GameScreen.window.getHeight());
        log = new Log(0,  0 , 200, 200);
        log.setVisible(false);
        addElement(log);
        this.switchModeButton = new Button("Switch mode", width / 2.0f, 15, 10);
        switchModeButton.setRunnable(() -> {
            if (game.getGameState() == GameState.ON_EDITOR) {
                game.setGameState(GameState.ON_LEVEL);
            } else game.setGameState(GameState.ON_EDITOR);
            Editor.sendLogMessage("Current mode: " + game.getGameState());
        });
        addElement(switchModeButton);
    }

    public static void sendLogMessage(String text) {
        log.addMessage(text);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (Input.isKeyPressed(Input.KEY_TILDA)) {
            log.setVisible(!log.isVisible());
        }
    }

    private void renderGUI() {
        editorFrame.begin();
        editorBatch.begin();
        super.render(editorBatch);
        editorBatch.drawText(editorFont, "Pos: " + Input.getMousePos().x + " " + Input.getMousePos().y,
                width, (int) pos.y + 5, 1.0f, new Color(255, 255, 255, 255), true);
        editorBatch.end();
        editorFrame.end();
    }

    public void renderFrame(@NotNull RenderBatch renderBatch) {
        renderGUI();
        renderBatch.draw(editorFrame.getTexture(), 0, 0, GameScreen.window.getWidth(), GameScreen.window.getHeight(), false, true);
    }
}
