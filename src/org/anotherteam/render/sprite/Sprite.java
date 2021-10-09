package org.anotherteam.render.sprite;

import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class Sprite {

    private final Vector2i position;

    private final Texture texture;

    public Sprite(String texturePath) {
        position = new Vector2i(0, 0);
        texture = new Texture(texturePath);
    }

    public void setPosition(int x, int y) {
        position.set(x, y);
    }

    @NotNull
    public Vector2i getPosition() {
        return position;
    }

    @NotNull
    public Texture getTexture() {
        return texture;
    }

    public void destroy() {
        texture.destroy();
    }
}
