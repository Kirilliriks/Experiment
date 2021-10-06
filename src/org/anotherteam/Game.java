package org.anotherteam;

import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.sprite.Sprite;

import static org.lwjgl.opengl.GL11.*;

public final class Game {

    private final Sprite testSprite;

    private final Shader testShader;

    public Game() {
        init();
        testSprite = new Sprite("../assets/testPlayerAtlas.png");
        testShader = new Shader("../assets/shader/testVertexShader.glsl", "../assets/shader/testFragmentShader.glsl");
    }

    public void init() {
        glEnable(GL_TEXTURE_2D);
    }

    public void update() {

    }

    public void draw() {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        testShader.bind();
        testSprite.draw();
    }
}
