package org.anotherteam;

import org.anotherteam.debug.DebugRender;
import org.anotherteam.editor.Editor;
import org.anotherteam.level.Level;
import org.anotherteam.level.room.Room;
import org.anotherteam.manager.LevelManager;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public final class Game {
    public static Game game;
    public static boolean DebugMode;

    private final Editor editor;

    public final LevelManager levelManager;

    public final GameScreen gameScreen;
    public final GameRender gameRender;

    @NotNull
    private GameState gameState;

    // TODO NewGameData
    private final String startLevelName = "StartLevel";

    public Game(@NotNull Window window) {
        game = this;
        levelManager = new LevelManager();
        gameScreen = new GameScreen(window);
        GameScreen.POSITION.set((int) (window.getWidth() / 2.0f - (GameScreen.WIDTH * GameScreen.RENDER_SCALE) / 2.0f), 0);
        gameRender = new GameRender(gameScreen);

        this.gameState = GameState.ON_EDITOR;

        DebugMode = false;
        DebugRender.global = new DebugRender(gameScreen.windowCamera);
        init();

        editor = new Editor(gameScreen);

        levelManager.loadLevel(startLevelName);

        editor.getEditorMenu().getLevelMenu().getLevelSelector().fillButtons();
    }

    public void init() { }

    public void update(float dt) {
        editor.update(dt);
        if (gameState == GameState.ON_EDITOR) return;
        levelManager.updateLevel(dt);
    }


    public void render(float dt) {
        levelManager.renderLevel(GameScreen.windowBatch);
        editor.renderFrame(GameScreen.windowBatch);

        if (gameState != GameState.ON_EDITOR && !DebugMode) return;
        DebugRender.global.draw();
    }

    public void setGameState(@NotNull GameState gameState) {
        this.gameState = gameState;
    }

    @NotNull
    public GameState getGameState() {
        return this.gameState;
    }

    @NotNull
    public Level getCurrenLevel() {
        return levelManager.getCurrentLevel();
    }

    @NotNull
    public Room getCurrentRoom() {
        return getCurrenLevel().getCurrentRoom();
    }

    public void destroy() { }
}
