package org.anotherteam.render.model;

public final class SpriteModel extends Model {
    public SpriteModel() {
        super();
        vertices = new float[] {
                -0.5f, 0.5f,
                0.5f, 0.5f,
                0.5f, -0.5f,
                -0.5f, -0.5f,
        };

        uv = new float[] {
                0, 0,
                1, 0,
                1, 1,
                0, 1
        };

        indices = new int[] {
                0, 1, 2,
                2, 3, 0
        };
        generate();
    }
}
