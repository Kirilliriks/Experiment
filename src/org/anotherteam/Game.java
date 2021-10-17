package org.anotherteam;

import org.anotherteam.data.FileLoader;
import org.anotherteam.debug.DebugRender;
import org.anotherteam.editor.Editor;
import org.anotherteam.level.Level;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public final class Game {
    private static Game game;
    public static boolean DebugMode;

    private final Editor editor;

    private final GameScreen gameScreen;
    private final GameRender gameRender;

    private GameState gameState;
    public Level gameLevel;

    // TODO NewGameData
    private final String startLevelName = "StartLevel";

    public Game(@NotNull Window window) {
        game = this;
        this.gameScreen = new GameScreen(window);
        this.gameRender = new GameRender(gameScreen);
        short scale = 5;
        gameRender.setPosition((int) (window.getWidth() / 2.0f - (GameScreen.WIDTH * scale) / 2.0f), 0);
        gameRender.setSize(GameScreen.WIDTH * scale, GameScreen.HEIGHT * scale);

        this.gameState = GameState.ON_EDITOR;

        DebugMode = false;
        DebugRender.start();
        init();

        this.editor = new Editor(this, gameScreen);

        setLevel(FileLoader.loadLevel(startLevelName));
    }

    public void setLevel(@NotNull Level level) {
        this.gameLevel = level;
    }

    @NotNull
    public static Game getInstance() {
        return game;
    }

    public void init() { }

    public void update(float dt) {
        editor.update(dt);
        if (gameState == GameState.ON_EDITOR) return;
        gameLevel.update(dt);
    }


    public void render(float dt) {
        gameLevel.render(GameScreen.windowBatch);
        editor.renderFrame(GameScreen.windowBatch);

        DebugRender.drawLine(new Vector2f(0, 0), new Vector2f(100, 10), Color.RED);
        DebugRender.draw(gameScreen.windowCamera);
    }

    public void setGameState(@NotNull GameState gameState) {
        this.gameState = gameState;
    }

    @NotNull
    public GameState getGameState() {
        return gameState;
    }

    @NotNull
    public GameRender getGameRender() {
        return gameRender;
    }

    public void destroy() { }
}
