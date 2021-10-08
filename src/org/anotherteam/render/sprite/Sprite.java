package org.anotherteam.render.sprite;


import org.anotherteam.math.Vector2i;
import org.anotherteam.render.model.SpriteModel;
import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.NotNull;

public final class Sprite {

    private final Vector2i position;

    private final SpriteModel model;
    private final Texture texture;

    public Sprite(String texturePath) {
        position = new Vector2i(0, 0);
        model = new SpriteModel();
        texture = new Texture(texturePath);
    }

    public void setPosition(int x, int y) {
        position.set(x, y);
    }

    @NotNull
    public Vector2i getPosition() {
        return position;
    }

    public void draw() {
        texture.bind(0);
        model.render();
    }

    public void destroy() {
        texture.destroy();
        model.destroy();
    }
}
