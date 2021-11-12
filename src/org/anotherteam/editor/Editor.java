package org.anotherteam.editor;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.editor.gui.Log;
import org.anotherteam.editor.gui.Widget;
import org.anotherteam.editor.gui.menu.text.TextMenu;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.framebuffer.FrameBuffer;
import org.anotherteam.render.text.Font;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public final class Editor extends Widget {
    public static final int DEFAULT_BORDER_SIZE = 10;
    public static final Font editorFont = new Font("font/f1.ttf", 8);

    private static Editor editor;

    private final EditorBatch editorBatch;
    private final FrameBuffer editorFrame;

    //GUI
    private static Log log;
    private final EditorMenu editorMenu;
    //

    public Editor() {
        super("Another Editor",
                10, GameScreen.window.getHeight() / 2.0f,
                GameScreen.window.getWidth() - 10, GameScreen.window.getHeight() / 2 - 40, null);
        GameScreen.RENDER_WIDTH = GameScreen.WIDTH * GameScreen.RENDER_SCALE;
        GameScreen.RENDER_HEIGHT = GameScreen.HEIGHT * GameScreen.RENDER_SCALE;
        GameScreen.POSITION.set((int) (GameScreen.window.getWidth() / 2.0f - (GameScreen.RENDER_WIDTH) / 2.0f), 30);

        editor = this;
        editorFont.setScale(2.0f);
        this.editorBatch = new EditorBatch(AssetData.DEFAULT_SHADER, GameScreen.windowCamera);
        this.editorFrame = new FrameBuffer(GameScreen.window.getWidth(), GameScreen.window.getHeight());

        // GUI
        log = new Log(0,  -height , 200, 200, this);
        log.setVisible(false);
        this.editorMenu = new EditorMenu(0, height - TextMenu.DEFAULT_BUTTON_MENU_HEIGHT, this);
        editorMenu.setWidth(width);
        val switchStateButton = new TextButton("Play/Stop", 0, 10, this);
        switchStateButton.setPosX(width / 2.0f - switchStateButton.getWidth() - DEFAULT_BORDER_SIZE / 2.0f);
        switchStateButton.setOnClick(this::switchPlayStopMode);

        val debugButton = new TextButton("Debug mode", 0, 10, this);
        debugButton.setPosX(width / 2.0f + DEFAULT_BORDER_SIZE / 2.0f);
        debugButton.setOnClick(() -> {
            Game.DebugMode = !Game.DebugMode;
            Editor.sendLogMessage("Debug mode: " + Game.DebugMode);
        });
    }

    public void switchPlayStopMode() {
        if (Game.game.getGameState() == GameState.ON_EDITOR) {
            Game.game.setGameState(GameState.ON_LEVEL);

            GameScreen.RENDER_WIDTH = GameScreen.window.getWidth();
            GameScreen.RENDER_HEIGHT = GameScreen.window.getHeight();
            GameScreen.POSITION.set(0, 0);

            editorMenu.getLevelMenu().getLevelSelector().storeEditableLevel();
        } else {
            Game.game.setGameState(GameState.ON_EDITOR);

            GameScreen.RENDER_WIDTH = GameScreen.WIDTH * GameScreen.RENDER_SCALE;
            GameScreen.RENDER_HEIGHT = GameScreen.HEIGHT * GameScreen.RENDER_SCALE;
            GameScreen.POSITION.set((int) (GameScreen.window.getWidth() / 2.0f - (GameScreen.RENDER_WIDTH) / 2.0f), 30);

            editorMenu.getLevelMenu().getLevelSelector().restoreEditableLevel();
            GameScreen.gameCamera.setPosition(GameScreen.WIDTH / 2.0f, GameScreen.HEIGHT / 2.0f);
        }
        Editor.sendLogMessage("Current state: " + Game.game.getGameState());
    }

    @Override
    public void update(float dt) {
        updateElements(dt);
        if (Input.isKeyPressed(Input.KEY_TILDA)) {
            log.setVisible(!log.isVisible());
        }

        if (Game.game.getGameState() == GameState.ON_LEVEL) {
            if (Input.isKeyPressed(Input.KEY_TILDA)) {
                switchPlayStopMode();
                return;
            }
        }

        float speed = 45;
        float moveX = 0, moveY = 0;

        if (Input.isKeyDown(Input.KEY_W)) {
            moveY += speed * dt;
        }
        if (Input.isKeyDown(Input.KEY_S)) {
            moveY -= speed * dt;
        }
        if (Input.isKeyDown(Input.KEY_A)) {
            moveX -= speed * dt;
        }
        if (Input.isKeyDown(Input.KEY_D)) {
            moveX += speed * dt;
        }

        GameScreen.gameCamera.addPosition(moveX, moveY);
    }

    private void renderGUI() {
        editorFrame.begin();
        editorBatch.begin();
        super.render(editorBatch);
        val text = "GamePos: " + GameScreen.inGameMouseX() + " " + GameScreen.inGameMouseY() + " | WindowPos: " + Input.getMousePos().x + " " + Input.getMousePos().y;
        editorBatch.drawText(editorFont, text,
                width, (int) pos.y + 5, 1.0f, Color.WHITE, true);
        editorBatch.end();
        editorFrame.end();
    }

    public void renderFrame(@NotNull RenderBatch renderBatch) {
        renderGUI();
        renderBatch.draw(editorFrame.getTexture(), 0, 0, GameScreen.window.getWidth(), GameScreen.window.getHeight(), false, true);
    }

    @NotNull
    public EditorMenu getEditorMenu() {
        return editorMenu;
    }

    @NotNull
    public static Editor getInstance() {
        return editor;
    }

    public static void sendLogMessage(String text) {
        log.addMessage(text);
    }

    @NotNull
    public static Log log() {
        return log;
    }

    public static int getRightBorderSize() {
        return 20;
    }

    public static int getUpBorderSize() {
        return 60;
    }

    public static int getDownBorderSize() {
        return 60;
    }

    public static float getDownBorderPos() {
        return editor.getPosY();
    }

    public void destroy() {
        //TODO add save question messages
    }
}
