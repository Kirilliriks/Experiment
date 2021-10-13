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
        gameRender.setPosition(1000, 500);
        gameRender.setSize(window.getWidth() - 1000, window.getHeight() - 500);

        this.gameState = GameState.ON_LEVEL;
        this.currentLevel = new TestLevel(this);
        DebugMode = false;
        init();

        this.editor = new Editor(this);
    }

    public void init() { }

    public void update(float dt) {
        currentLevel.update(dt);
    }


    public void render(float dt) {
        editor.render();
        currentLevel.render();
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
