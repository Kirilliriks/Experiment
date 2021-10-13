package org.anotherteam;

import org.anotherteam.editor.Editor;
import org.anotherteam.level.Level;
import org.anotherteam.level.TestLevel;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public final class Game {
    public static boolean DebugMode;

    private final Editor editor;

    private final GameScreen gameScreen;
    private final GameRender gameRender;

    private GameState gameState;
    public Level currentLevel;

    public Game(@NotNull Window window) {
        this.gameScreen = new GameScreen(window);
        this.gameRender = new GameRender(gameScreen);
        short scale = 6;
        gameRender.setPosition((int) (window.getWidth() / 2.0f - (GameScreen.WIDTH * scale) / 2.0f), 0);
        gameRender.setSize(GameScreen.WIDTH * scale, GameScreen.HEIGHT * scale);

        this.gameState = GameState.ON_EDITOR;
        this.currentLevel = new TestLevel(this);
        DebugMode = false;
        init();

        this.editor = new Editor(this, gameScreen);
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
    }

    public void setGameState(@NotNull GameState gameState) {
        this.gameState = gameState;
    }

    @NotNull
    public GameState getGameState() {
        return gameState;
    }

    @NotNull
    public GameScreen getGameScreen() {
        return gameScreen;
    }

    @NotNull
    public GameRender getGameRender() {
        return gameRender;
    }

    public void destroy() { }
}
