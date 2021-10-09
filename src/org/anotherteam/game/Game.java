package org.anotherteam.game;

import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.screen.Screen;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.sprite.Sprite;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public final class Game {

    private final Camera camera;

    private final Sprite testSprite;
    private final Shader testShader;

    public int frames;
    public static long time;

    private float temp;

    public Game() {
        camera = new Camera(0, 0);
        testSprite = new Sprite("../assets/testRoom.png");
        testSprite.setPosition(Screen.WIDTH / 2, Screen.HEIGHT / 2);
        testShader = new Shader("../assets/shader/testVertexShader.glsl", "../assets/shader/testFragmentShader.glsl");
        init();
        temp = 0;
    }

    public void init() {
        time = System.currentTimeMillis();
    }

    public void update() {
        if (Input.isKeyDown(GLFW_KEY_W)) {
            camera.addPosition(0, 1);
        }

        if (Input.isKeyDown(GLFW_KEY_S)) {
            camera.addPosition(0, -1);
        }

        if (Input.isKeyDown(GLFW_KEY_D)) {
            camera.addPosition(1, 0);
        }

        if (Input.isKeyDown(GLFW_KEY_A)) {
            camera.addPosition(-1, 0);
        }
    }

    public void render() {
        frames++;
        if (System.currentTimeMillis() > time + 1000) {
            System.out.println("Frames: " + frames);
            time = System.currentTimeMillis();
            frames = 0;
        }

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        testShader.bind();
        testShader.setUniform("sampler", 0);
        testShader.setUniform("projection", camera.getProjection());
        testShader.setUniform("view", camera.getViewMatrix());
        testSprite.draw();
        testShader.unbind();
    }

    public void destroy() {
        testSprite.destroy();
        testShader.destroy();
    }
}
