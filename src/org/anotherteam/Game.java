package org.anotherteam;

import com.google.gson.Gson;
import lombok.val;
import org.anotherteam.data.AssetsData;
import org.anotherteam.data.TileDeserializer;
import org.anotherteam.editor.Editor;
import org.anotherteam.level.Level;
import org.anotherteam.level.room.Room;
import org.anotherteam.level.room.object.Wall;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.object.type.entity.player.Player;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class Game {
    public static boolean DebugMode;

    private final Editor editor;

    private final GameScreen gameScreen;
    private final GameRender gameRender;

    private GameState gameState;
    public Level gameLevel;

    public Game(@NotNull Window window) {
        this.gameScreen = new GameScreen(window);
        this.gameRender = new GameRender(gameScreen);
        short scale = 6;
        gameRender.setPosition((int) (window.getWidth() / 2.0f - (GameScreen.WIDTH * scale) / 2.0f), 0);
        gameRender.setSize(GameScreen.WIDTH * scale, GameScreen.HEIGHT * scale);

        this.gameState = GameState.ON_EDITOR;

        // TestLevel
        this.gameLevel = new Level(this);
        val testRoom = new Room(new Vector2i(9,4));
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 4; y++) {
                testRoom.addTile(x, y, new Tile(x, y, x, y, AssetsData.TEST_ROOM_ATLAS));
            }
        }
        Gson gson = new Gson().newBuilder().registerTypeAdapter(Tile.class, new TileDeserializer()).create();
        System.out.println("Tile " + gson.toJson(new Tile(0, 0, 0, 0, AssetsData.TEST_ROOM_ATLAS)));

        testRoom.addObject(new Player(new Vector2i(26, 29), testRoom));
        testRoom.addObject(new Wall(new Vector2i(6, 29), testRoom));
        testRoom.addObject(new Wall(new Vector2i(150, 29), testRoom));
        gameLevel.addRoom(testRoom);
        //


        DebugMode = false;
        init();

        this.editor = new Editor(this, gameScreen);
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
