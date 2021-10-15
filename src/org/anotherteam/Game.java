package org.anotherteam;

import com.google.gson.Gson;
import lombok.val;
import org.anotherteam.data.AssetsData;
import org.anotherteam.data.Loader;
import org.anotherteam.data.level.LevelDeserializer;
import org.anotherteam.data.level.room.RoomDeserializer;
import org.anotherteam.data.level.room.tile.TileDeserializer;
import org.anotherteam.data.level.room.gameobject.ComponentDeserializer;
import org.anotherteam.data.level.room.gameobject.GameObjectDeserializer;
import org.anotherteam.editor.Editor;
import org.anotherteam.level.Level;
import org.anotherteam.level.room.Room;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.type.entity.player.Player;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public final class Game {
    private static Game game;
    public static boolean DebugMode;

    private final Editor editor;

    private final GameScreen gameScreen;
    private final GameRender gameRender;

    private GameState gameState;
    public Level gameLevel;

    public Game(@NotNull Window window) {
        game = this;
        this.gameScreen = new GameScreen(window);
        this.gameRender = new GameRender(gameScreen);
        short scale = 6;
        gameRender.setPosition((int) (window.getWidth() / 2.0f - (GameScreen.WIDTH * scale) / 2.0f), 0);
        gameRender.setSize(GameScreen.WIDTH * scale, GameScreen.HEIGHT * scale);

        this.gameState = GameState.ON_EDITOR;

        // TestLevel
        this.gameLevel = Loader.loadLevel("TestLevel");
        Loader.saveLevel(gameLevel);
        //


        DebugMode = false;
        init();

        this.editor = new Editor(this, gameScreen);
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
