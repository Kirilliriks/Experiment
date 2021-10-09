package org.anotherteam.game;

import lombok.val;
import org.anotherteam.render.RenderBatch;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.screen.Screen;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.window.Window;
import org.jetbrains.annotations.NotNull;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public final class Game {

    private final Window window;
    private final Camera camera;

    private final Sprite testSprite;

    private final RenderBatch renderBatch;

    public Game(@NotNull Window window) {
        this.window = window;
        camera = new Camera(0, 0);
        testSprite = new Sprite("../assets/testTestRoom.png");
        testSprite.setPosition(Screen.WIDTH / 2, Screen.HEIGHT / 2);
        val shader = new Shader("../assets/shader/testVertexShader.glsl", "../assets/shader/testFragmentShader.glsl");
        renderBatch = new RenderBatch(shader, camera);
        init();
    }

    public void init() { }

    public void update(float dt) {

        val speed = 100;
        if (Input.isKeyDown(GLFW_KEY_W)) {
            camera.addPosition(0, speed * dt);
        }

        if (Input.isKeyDown(GLFW_KEY_S)) {
            camera.addPosition(0, speed * -dt);
        }

        if (Input.isKeyDown(GLFW_KEY_D)) {
            camera.addPosition(speed * dt, 0);
        }

        if (Input.isKeyDown(GLFW_KEY_A)) {
            camera.addPosition(speed * -dt, 0);
        }
    }

    public void render(float dt) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        renderBatch.begin();
        renderBatch.drawTexture(testSprite.getPosition(), testSprite.getTexture());
        renderBatch.end();
    }

    public void destroy() {

    }
}
