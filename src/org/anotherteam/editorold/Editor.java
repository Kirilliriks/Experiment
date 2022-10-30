package org.anotherteam.editorold;

import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.editorold.gui.EditorLog;
import org.anotherteam.editorold.gui.Widget;
import org.anotherteam.editorold.gui.menu.text.TextMenu;
import org.anotherteam.editorold.gui.menu.text.TextButton;
import org.anotherteam.editorold.gui.window.DialogWindow;
import org.anotherteam.editorold.gui.window.SaveLevelDialog;
import org.anotherteam.editorold.level.editor.LevelEditor;
import org.anotherteam.editorold.level.room.RoomEditor;
import org.anotherteam.editorold.render.EditorBatch;
import org.anotherteam.logger.GameLogger;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.framebuffer.FrameBuffer;
import org.anotherteam.render.text.Font;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

public final class Editor extends Widget {

    public static final int DEFAULT_OFFSET_SIZE = 20;
    public static final int DEFAULT_BORDER_SIZE = 10;
    public static final Font EDITOR_FONT = new Font("font/f1.ttf", 8);

    private static Editor EDITOR;

    private final EditorCameraController editorCameraController;
    private final EditorBatch editorBatch;
    private final FrameBuffer editorFrame;

    //GUI
    private final EditorLog editorLog;
    private final EditorMenu editorMenu;
    private static DialogWindow DIALOG_WINDOW;
    //
    public static boolean INPUT_HANDLING;

    public Editor() {
        super("Another Editor",
                10, GameScreen.window.getHeight() / 2.0f,
                GameScreen.window.getWidth() - 10, GameScreen.window.getHeight() / 2 - 40, null);
        EDITOR = this;

        Game.DEBUG_MODE = true;
        Game.STATE_MANAGER.setState(GameState.ON_EDITOR);
        switchGameView(true);

        EDITOR_FONT.setScale(2.0f);

        editorCameraController = new EditorCameraController();
        editorBatch = new EditorBatch(AssetData.DEFAULT_SHADER, GameScreen.WINDOW_CAMERA);
        editorFrame = new FrameBuffer(GameScreen.window.getWidth(), GameScreen.window.getHeight());

        // GUI
        editorLog = new EditorLog(0,  -height , 200, 200, this);
        editorLog.setVisible(false);
        editorMenu = new EditorMenu(0, height - TextMenu.DEFAULT_BUTTON_MENU_HEIGHT, this);
        editorMenu.setWidth(width);

        final var switchStateButton = new TextButton("Play/Stop", 0, 10, this);
        switchStateButton.setPosX(width / 2.0f - switchStateButton.getWidth() - DEFAULT_BORDER_SIZE / 2.0f);
        switchStateButton.setOnClick((click) -> switchPlayStopMode());

        final var debugButton = new TextButton("Debug mode", 0, 10, this);
        debugButton.setPosX(width / 2.0f + DEFAULT_BORDER_SIZE / 2.0f);
        debugButton.setOnClick((click) -> {
            Game.DEBUG_MODE = !Game.DEBUG_MODE;
            GameLogger.sendMessage("Debug mode: " + Game.DEBUG_MODE);
        });

        DIALOG_WINDOW = null;
    }

    public void init() {
        final String levelName = Game.LEVEL_MANAGER.getCurrentLevel().getName();

        LevelEditor.editor().loadLevel(levelName);
        LevelEditor.editor().updateButtons(levelName);
        RoomEditor.editor().updateButtons();
    }

    public void switchPlayStopMode() {
        if (Game.STATE_MANAGER.getState() == GameState.ON_EDITOR) {
            Game.DEBUG_MODE = false;
            Game.STATE_MANAGER.setState(GameState.ON_LEVEL);

            switchGameView(false);
            LevelEditor.editor().storeLevel();
        } else {
            Game.DEBUG_MODE = true;
            Game.STATE_MANAGER.setState(GameState.ON_EDITOR);

            switchGameView(true);
            LevelEditor.editor().restoreLevel();
        }

        GameLogger.sendMessage("Current state: " + Game.STATE_MANAGER.getState());
    }

    private void switchGameView(boolean onEditor) {
        if (onEditor) {
            final int viewWidth = GameScreen.window.getWidth() - getBorderSize() * 2;
            GameScreen.WIDTH = viewWidth / GameScreen.RENDER_SCALE;
            GameScreen.RENDER_WIDTH = viewWidth;
            GameScreen.RENDER_HEIGHT = GameScreen.HEIGHT * GameScreen.RENDER_SCALE;
            GameScreen.POSITION.set((int) (GameScreen.window.getWidth() / 2.0f - (GameScreen.RENDER_WIDTH) / 2.0f), getDownBorderSize() / 2);
        } else {
            GameScreen.WIDTH = 160; // TODO make static constant
            GameScreen.RENDER_WIDTH = GameScreen.window.getWidth();
            GameScreen.RENDER_HEIGHT = GameScreen.window.getHeight();
            GameScreen.POSITION.set(0, 0);
        }

        Game.getGameRender().updateFrames();
    }

    @Override
    public void update(float dt) {
        if (Game.STATE_MANAGER.getState() == GameState.ON_LEVEL) {
            if (Input.isKeyPressed(Input.KEY_ESCAPE)) {
                switchPlayStopMode();
            }
            return;
        } else if (Game.STATE_MANAGER.getState() != GameState.ON_EDITOR) {
            return;
        }

        if (DIALOG_WINDOW != null) {
            DIALOG_WINDOW.update(dt);
            return;
        }

        if (Input.isKeyPressed(Input.KEY_ESCAPE)) {
            final var saveWindow = new SaveLevelDialog(LevelEditor.editor().getLevel().getName());
            saveWindow.setOnAfterClose(() -> Game.STATE_MANAGER.setState(GameState.ON_CLOSE_GAME));
            Editor.callWindow(saveWindow);
            return;
        }

        updateElements(dt);
        if (INPUT_HANDLING) return;

        if (Input.isKeyPressed(Input.KEY_TILDA)) {
            editorLog.setVisible(!editorLog.isVisible());
        }

        editorCameraController.handle(dt);
    }

    private void renderGUI() {
        editorFrame.begin();
        editorBatch.begin();

        super.render(editorBatch);
        final var text = "GamePos: " + GameScreen.inGameMouseX() + " " + GameScreen.inGameMouseY() + " | WindowPos: " + Input.getMousePos().x + " " + Input.getMousePos().y + " | GameWindowPos: " + GameScreen.inGameWindowMouseX() + " " + GameScreen.inGameWindowMouseY();
        editorBatch.drawText(EDITOR_FONT, text,
                width, (int) pos.y + 5, 1.0f, Color.WHITE, true,  false, false);

        if (DIALOG_WINDOW != null) DIALOG_WINDOW.render(editorBatch);

        if (GameScreen.draggedThing != null) {
            if (GameScreen.inGameWindowMouseX() == -1 || GameScreen.inGameWindowMouseY() == -1) {
                final var x = (int) Input.getMouseX();
                final var y = (int) Input.getMouseY();
                GameScreen.draggedThing.draw(x, y, editorBatch);
                GameScreen.draggedThing.debugDraw(x, y, true, editorBatch);
            }
        }

        editorBatch.end();
        editorFrame.end();
    }

    public void renderFrame(@NotNull RenderBatch renderBatch) {
        if (Game.STATE_MANAGER.getState() != GameState.ON_EDITOR) return;

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
    public static Editor inst() { // TODO maybe remove?
        return EDITOR;
    }

    public static void sendLogMessage(String text) {
        if (EDITOR != null) EDITOR.editorLog.addMessage(text);
    }

    @NotNull
    public static EditorLog log() {
        return EDITOR.editorLog;
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
        return EDITOR.getPosY();
    }

    public void destroy() {
        if (Editor.DIALOG_WINDOW != null) closeWindow();

        super.destroy();
    }

    public static void callWindow(@NotNull DialogWindow dialogWindow) {
        if (Editor.DIALOG_WINDOW != null) throw new LifeException("Last DialogWindow should be closed");

        Editor.DIALOG_WINDOW = dialogWindow;
    }

    public static void closeWindow() {
        Editor.DIALOG_WINDOW.destroy();
        Editor.DIALOG_WINDOW = null;
    }
}
