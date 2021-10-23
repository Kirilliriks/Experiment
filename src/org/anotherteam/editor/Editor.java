package org.anotherteam.editor;

import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.Input;
import org.anotherteam.data.AssetsData;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.Log;
import org.anotherteam.editor.gui.Widget;
import org.anotherteam.editor.gui.menu.ButtonMenu;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.framebuffer.FrameBuffer;
import org.anotherteam.render.text.Font;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public final class Editor extends Widget {
    public static final int DBORDER_SIZE = 10;
    public static final Font editorFont = new Font("font/f1.ttf", 8);

    private static Editor editor;

    private final Game game;
    private final GameScreen gameScreen;
    private final EditorBatch editorBatch;
    private final FrameBuffer editorFrame;

    //GUI
    private static Log log;
    private final EditorMenu editorMenu;
    private final Button switchModeButton;
    //

    public Editor(@NotNull Game game, @NotNull GameScreen gameScreen) {
        super("Another Editor",
                10, GameScreen.window.getHeight() / 2.0f,
                GameScreen.window.getWidth() - 10, GameScreen.window.getHeight() / 2 - 40, null);
        editor = this;
        editorFont.setScale(2.0f);
        this.game = game;
        this.gameScreen = gameScreen;
        this.editorBatch = new EditorBatch(AssetsData.DEFAULT_SHADER, gameScreen.windowCamera);
        this.editorFrame = new FrameBuffer(GameScreen.window.getWidth(), GameScreen.window.getHeight());

        // GUI
        log = new Log(0,  -height , 200, 200, this);
        log.setVisible(false);
        this.editorMenu = new EditorMenu(0, height - ButtonMenu.DEFAULT_GUI_HEIGHT, this);
        editorMenu.setWidth(width);
        this.switchModeButton = new Button("Switch mode", width / 2.0f, 10, this);
        switchModeButton.setOnClick(() -> {
            if (game.getGameState() == GameState.ON_EDITOR) {
                game.setGameState(GameState.ON_LEVEL);
            } else game.setGameState(GameState.ON_EDITOR);
            Editor.sendLogMessage("Current mode: " + game.getGameState());
        });
    }

    @NotNull
    public static Editor getInstance() {
        return editor;
    }

    public static void sendLogMessage(String text) {
        log.addMessage(text);
    }

    @NotNull
    public static Log getLog() {
        return log;
    }

    public int getRightBorder() {
        return 20;
    }

    public int getUpBorder() {
        return 60;
    }

    public int getDownBorder() {
        return 60;
    }

    @Override
    public void update(float dt) {
        updateElements(dt);
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
