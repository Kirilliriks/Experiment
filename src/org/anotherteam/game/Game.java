package org.anotherteam.game;

import lombok.val;
import org.anotherteam.render.RenderBatch;
import org.anotherteam.render.framebuffer.Framebuffer;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.screen.Screen;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.window.Window;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import static org.lwjgl.glfw.GLFW.*;

public final class Game {

    private final Window window;
    private final Camera gameCamera;
    private final Camera bufferCamera;

    private final Sprite testSprite;

    private final Framebuffer framebuffer;
    private final RenderBatch renderBatch;

    public Game(@NotNull Window window) {
        this.window = window;
        gameCamera = new Camera(0, 0, window.getWidth(), window.getHeight());
        bufferCamera = new Camera(0, 0, Screen.WIDTH, Screen.HEIGHT);
        testSprite = new Sprite("../assets/testTestRoom.png");
        testSprite.setPosition(0, 0);
        val shader = new Shader("../assets/shader/testVertexShader.glsl", "../assets/shader/testFragmentShader.glsl");
        renderBatch = new RenderBatch(shader, gameCamera);
        framebuffer = new Framebuffer(Screen.WIDTH, Screen.HEIGHT);
        init();
    }

    public void init() { }

    public void update(float dt) {

        val speed = 100;
        if (Input.isKeyDown(GLFW_KEY_W)) {
            gameCamera.addPosition(0, speed * dt);
        }

        if (Input.isKeyDown(GLFW_KEY_S)) {
            gameCamera.addPosition(0, speed * -dt);
        }

        if (Input.isKeyDown(GLFW_KEY_D)) {
            gameCamera.addPosition(speed * dt, 0);
        }

        if (Input.isKeyDown(GLFW_KEY_A)) {
            gameCamera.addPosition(speed * -dt, 0);
        }
    }

    public void render(float dt) {
        framebuffer.begin();
        renderBatch.begin();
        renderBatch.setCamera(gameCamera);
        renderBatch.clear();
        renderBatch.drawTexture(testSprite.getPosition(), testSprite.getTexture());
        renderBatch.end();
        framebuffer.end();

        renderBatch.begin();
        renderBatch.setCamera(bufferCamera);
        renderBatch.drawTexture(new Vector2i(0, 0), framebuffer.getTexture());
        renderBatch.end();
    }


    public void destroy() { }
}
