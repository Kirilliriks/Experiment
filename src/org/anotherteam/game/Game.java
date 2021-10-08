package org.anotherteam.game;

import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.sprite.Sprite;

import static org.lwjgl.opengl.GL11.*;

public final class Game {

    private final Sprite testSprite;
    private final Shader testShader;

    public int frames;
    public static long time;

    public Game() {
        testSprite = new Sprite("../assets/testPlayerAtlas.png");
        testShader = new Shader("../assets/shader/testVertexShader.glsl", "../assets/shader/testFragmentShader.glsl");
        init();
    }

    public void init() {
        time = System.currentTimeMillis();
    }

    public void update() { }

    public void render() {
        frames++;
        if (System.currentTimeMillis() > time + 1000) {
            System.out.println("Frames: " + frames);
            time = System.currentTimeMillis();
            frames = 0;
        }

        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        testShader.bind();
        testShader.setUniform("sampler", 0);
        testSprite.draw();
        testShader.unbind();
    }
}
