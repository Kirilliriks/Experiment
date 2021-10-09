package org.anotherteam.render.model;

public final class SpriteModel extends Model {
    public SpriteModel() {
        super();
        vertices = new float[] {
                0, 16,
                16, 16,
                16, 0,
                0, 0,
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
