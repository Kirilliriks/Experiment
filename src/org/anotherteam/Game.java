package org.anotherteam;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;

import org.anotherteam.level.Level;
import org.anotherteam.level.TestLevel;
import org.anotherteam.render.RenderBatch;
import org.anotherteam.render.framebuffer.FrameBuffer;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import static org.lwjgl.glfw.GLFW.*;

public final class Game {
    public static boolean DebugMode;

    private GameState gameState;
    private GameScreen gameScreen;

    public Level currentLevel;

    private final Window window;
    private final Camera bufferCamera;

    private final Sprite testSprite;

    private final FrameBuffer framebuffer;
    private final RenderBatch renderBatch;

    public Game(@NotNull Window window) {
        this.window = window;
        this.gameScreen = new GameScreen(window.getWidth(), window.getHeight());

        bufferCamera = new Camera(0, 0, GameScreen.WIDTH, GameScreen.HEIGHT);
        testSprite = new Sprite("../assets/testTestRoom.png");
        testSprite.setPosition(0, 0);
        val shader = new Shader("shader/defaultVertexShader.glsl", "shader/defaultFragmentShader.glsl");
        renderBatch = new RenderBatch(shader, gameScreen.camera);
        framebuffer = new FrameBuffer(GameScreen.WIDTH, GameScreen.HEIGHT);

        this.gameState = GameState.ON_LEVEL;
        this.currentLevel = new TestLevel(this);
        DebugMode = false;
        init();
    }

    public void init() { }

    public void update(float dt) {

        val speed = 100;
        if (Input.isKeyDown(GLFW_KEY_W)) {
            gameScreen.camera.addPosition(0, speed * dt);
        }

        if (Input.isKeyDown(GLFW_KEY_S)) {
            gameScreen.camera.addPosition(0, speed * -dt);
        }

        if (Input.isKeyDown(GLFW_KEY_D)) {
            gameScreen.camera.addPosition(speed * dt, 0);
        }

        if (Input.isKeyDown(GLFW_KEY_A)) {
            gameScreen.camera.addPosition(speed * -dt, 0);
        }

        currentLevel.update(dt);
    }

    public void render(float dt) {
        currentLevel.render();

        framebuffer.begin();
        renderBatch.begin();
        renderBatch.setCamera(gameScreen.camera);
        renderBatch.clear();
        renderBatch.draw(testSprite.getTexture(), testSprite.getPosition());
        renderBatch.end();
        framebuffer.end();

        renderBatch.begin();
        renderBatch.setCamera(bufferCamera);
        renderBatch.draw(framebuffer.getTexture(), new Vector2i(0, 0), false, true);
        renderBatch.end();
    }

    @NotNull
    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void destroy() { }
}
