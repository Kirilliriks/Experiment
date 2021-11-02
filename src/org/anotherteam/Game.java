package org.anotherteam;

import org.anotherteam.data.FileLoader;
import org.anotherteam.debug.DebugRender;
import org.anotherteam.editor.Editor;
import org.anotherteam.level.Level;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public final class Game {
    public static Game game;
    public static boolean DebugMode;

    private final Editor editor;

    private final GameScreen gameScreen;
    private final GameRender gameRender;

    @NotNull
    private GameState gameState;
    @NotNull
    public Level currentLevel;

    // TODO NewGameData
    private final String startLevelName = "StartLevel";

    public Game(@NotNull Window window) {
        game = this;
        gameScreen = new GameScreen(window);
        GameScreen.POSITION.set((int) (window.getWidth() / 2.0f - (GameScreen.WIDTH * GameScreen.RENDER_SCALE) / 2.0f), 0);
        gameRender = new GameRender(gameScreen);

        this.gameState = GameState.ON_EDITOR;

        DebugMode = false;
        DebugRender.global = new DebugRender(gameScreen.windowCamera);
        init();

        this.editor = new Editor(gameScreen);
        currentLevel = Level.createEmpty();

        setLevel(FileLoader.loadLevel(startLevelName));
    }

    public void setLevel(@NotNull Level level) {
        currentLevel = level;
    }

    public void init() { }

    public void update(float dt) {
        editor.update(dt);
        if (gameState == GameState.ON_EDITOR) return;
        currentLevel.update(dt);
    }


    public void render(float dt) {
        currentLevel.render(GameScreen.windowBatch);
        editor.renderFrame(GameScreen.windowBatch);

        if (gameState != GameState.ON_EDITOR && !DebugMode) return;
        DebugRender.global.draw();
    }

    public static void setGameState(@NotNull GameState gameState) {
        game.gameState = gameState;
    }

    @NotNull
    public static GameState getGameState() {
        return game.gameState;
    }

    @NotNull
    public static GameRender getGameRender() {
        return game.gameRender;
    }

    public void destroy() { }
}
