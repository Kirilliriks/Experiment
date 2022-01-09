package org.anotherteam.editor;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.editor.gui.EditorLog;
import org.anotherteam.editor.gui.Widget;
import org.anotherteam.editor.gui.menu.text.TextMenu;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.anotherteam.editor.gui.window.DialogWindow;
import org.anotherteam.editor.gui.window.SaveLevelDialog;
import org.anotherteam.editor.level.editor.LevelEditor;
import org.anotherteam.editor.level.room.RoomEditor;
import org.anotherteam.editor.object.objectedit.GameObjectEditor;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.logger.GameLogger;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.framebuffer.FrameBuffer;
import org.anotherteam.render.text.Font;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

public final class Editor extends Widget {
    public static final int DEFAULT_BORDER_SIZE = 10;
    public static final Font editorFont = new Font("font/f1.ttf", 8);

    private static Editor editor;

    private final EditorCameraController editorCameraController;
    private final EditorBatch editorBatch;
    private final FrameBuffer editorFrame;

    //GUI
    private static EditorLog editorLog;

    private final EditorMenu editorMenu;

    public static LevelEditor levelEditor;
    public static RoomEditor roomEditor;
    public static GameObjectEditor gameObjectEditor;
    //

    private static DialogWindow dialogWindow;
    public static boolean inputHandling;

    public Editor() {
        super("Another Editor",
                10, GameScreen.window.getHeight() / 2.0f,
                GameScreen.window.getWidth() - 10, GameScreen.window.getHeight() / 2 - 40, null);
        Game.DebugMode = true;
        Game.stateManager.setState(GameState.ON_EDITOR);
        switchGameView(true);

        editor = this;
        editorFont.setScale(2.0f);

        editorCameraController = new EditorCameraController();
        editorBatch = new EditorBatch(AssetData.DEFAULT_SHADER, GameScreen.windowCamera);
        editorFrame = new FrameBuffer(GameScreen.window.getWidth(), GameScreen.window.getHeight());

        // GUI
        editorLog = new EditorLog(0,  -height , 200, 200, this);
        editorLog.setVisible(false);
        editorMenu = new EditorMenu(0, height - TextMenu.DEFAULT_BUTTON_MENU_HEIGHT, this);
        editorMenu.setWidth(width);
        val switchStateButton = new TextButton("Play/Stop", 0, 10, this);
        switchStateButton.setPosX(width / 2.0f - switchStateButton.getWidth() - DEFAULT_BORDER_SIZE / 2.0f);
        switchStateButton.setOnClick(this::switchPlayStopMode);

        val debugButton = new TextButton("Debug mode", 0, 10, this);
        debugButton.setPosX(width / 2.0f + DEFAULT_BORDER_SIZE / 2.0f);
        debugButton.setOnClick(() -> {
            Game.DebugMode = !Game.DebugMode;
            GameLogger.sendMessage("Debug mode: " + Game.DebugMode);
        });

        dialogWindow = null;
    }

    public void init(String levelName) {
        levelEditor = editorMenu.getLevelMenu().getLevelEditor();
        roomEditor = editorMenu.getLevelMenu().getRoomEditor();
        gameObjectEditor = editorMenu.getGameObjectMenu().getGameObjectEditor();

        levelEditor.loadLevel(levelName);
        levelEditor.updateButtons(levelName);
        roomEditor.updateButtons();
    }

    public void switchPlayStopMode() {
        if (Game.stateManager.getState() == GameState.ON_EDITOR) {
            Game.DebugMode = false;
            Game.stateManager.setState(GameState.ON_LEVEL);

            switchGameView(false);
            levelEditor.storeLevel();
        } else {
            Game.DebugMode = true;
            Game.stateManager.setState(GameState.ON_EDITOR);

            switchGameView(true);
            levelEditor.restoreLevel();
        }

        GameLogger.sendMessage("Current state: " + Game.stateManager.getState());
    }

    private void switchGameView(boolean onEditor) {
        if (onEditor) {
            final int viewWidth = GameScreen.window.getWidth() - getBorderSize() * 2;
            GameScreen.WIDTH = viewWidth / GameScreen.RENDER_SCALE;
            GameScreen.RENDER_WIDTH = viewWidth;
            GameScreen.RENDER_HEIGHT = GameScreen.HEIGHT * GameScreen.RENDER_SCALE;
            GameScreen.POSITION.set((int) (GameScreen.window.getWidth() / 2.0f - (GameScreen.RENDER_WIDTH) / 2.0f), getDownBorderSize() / 2);

        } else {
            GameScreen.WIDTH = 160; // TODO
            GameScreen.RENDER_WIDTH = GameScreen.window.getWidth();
            GameScreen.RENDER_HEIGHT = GameScreen.window.getHeight();
            GameScreen.POSITION.set(0, 0);
        }
        Game.getGameRender().updateFrames();
    }

    @Override
    public void update(float dt) {
        if (dialogWindow != null) {
            dialogWindow.update(dt);
            return;
        }

        if (Input.isKeyPressed(Input.KEY_ESCAPE)) {
            if (Game.stateManager.getState() == GameState.ON_LEVEL) {
                switchPlayStopMode();
            } else {
                val saveWindow = new SaveLevelDialog(Editor.levelEditor.getLevel().getName());
                saveWindow.setOnAfterClose(() -> Game.stateManager.setState(GameState.ON_CLOSE_GAME));
                Editor.callWindow(saveWindow);
            }
            return;
        }

        updateElements(dt);
        if (inputHandling) return;

        if (Input.isKeyPressed(Input.KEY_TILDA)) {
            editorLog.setVisible(!editorLog.isVisible());
        }

        if (Game.stateManager.getState() == GameState.ON_EDITOR) editorCameraController.handle(dt);
    }

    private void renderGUI() {
        editorFrame.begin();
        editorBatch.begin();
        super.render(editorBatch);
        val text = "GamePos: " + GameScreen.inGameMouseX() + " " + GameScreen.inGameMouseY() + " | WindowPos: " + Input.getMousePos().x + " " + Input.getMousePos().y;
        editorBatch.drawText(editorFont, text,
                width, (int) pos.y + 5, 1.0f, Color.WHITE, true);
        if (dialogWindow != null) dialogWindow.render(editorBatch);
        editorBatch.end();
        editorFrame.end();
    }

    public void renderFrame(@NotNull RenderBatch renderBatch) {
        renderGUI();
        renderBatch.begin(false);
        renderBatch.draw(editorFrame.getTexture(), 0, 0, GameScreen.window.getWidth(), GameScreen.window.getHeight(), false, true);
        renderBatch.end();
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
        editorLog.addMessage(text);
    }

    @NotNull
    public static EditorLog log() {
        return editorLog;
    }

    public static int getBorderSize() {
        return 60;
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
        if (Editor.dialogWindow != null) closeWindow();
        super.destroy();
    }

    public static void callWindow(@NotNull DialogWindow dialogWindow) {
        if (Editor.dialogWindow != null) throw new LifeException("Last DialogWindow should be closed");
        Editor.dialogWindow = dialogWindow;
    }

    public static void closeWindow() {
        Editor.dialogWindow.destroy();
        Editor.dialogWindow = null;
    }
}
