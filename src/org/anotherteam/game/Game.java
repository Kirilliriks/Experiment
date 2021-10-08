package org.anotherteam.game;

import org.anotherteam.math.Matrix4f;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.sprite.Sprite;

import static org.lwjgl.opengl.GL11.*;

public final class Game {

    private final Sprite testSprite;
    private final Shader testShader;

    public int frames;
    public static long time;

    private float temp;

    public Game() {
        testSprite = new Sprite("../assets/testPlayerAtlas.png");
        testShader = new Shader("../assets/shader/testVertexShader.glsl", "../assets/shader/testFragmentShader.glsl");
        init();
        temp = 0;
    }

    public void init() {
        time = System.currentTimeMillis();
    }

    public void update() {
        temp += 0.0005f;
        testSprite.setPosition((int) temp, 0);
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
        testShader.setUniform("model", Matrix4f.transform(testSprite.getPosition()));
        testSprite.draw();
        testShader.unbind();
    }

    public void destroy() {
        testSprite.destroy();
        testShader.destroy();
    }
}
