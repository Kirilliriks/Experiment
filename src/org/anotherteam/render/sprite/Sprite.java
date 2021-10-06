package org.anotherteam.render.sprite;


import org.anotherteam.render.texture.Texture;
import org.anotherteam.render.model.Model;

public final class Sprite {

    private final Model model;
    private Texture texture;

    public Sprite(String texturePath) {
        float[] vertices = new float[] {
                -0.5f, 0.5f,
                0.5f, 0.5f,
                0.5f, -0.5f,
                -0.5f, -0.5f,
        };

        float[] uv = new float[] {
                0, 0,
                1, 0,
                1, 1,
                0, 1
        };

        int[] indices = new int[] {
                0, 1, 2,
                2, 3, 0
        };
        model = new Model(vertices, uv, indices);
        texture = new Texture(texturePath);
    }

    public void draw() {
        texture.bind();
        model.render();
    }
}
