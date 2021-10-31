package org.anotherteam.editor.gui.menu.sprite;

import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.sprite.Sprite;
import org.jetbrains.annotations.NotNull;

public class SpriteButton extends GUIElement {

    private final Sprite sprite;

    public SpriteButton(@NotNull Sprite sprite, float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        this.sprite = sprite;
        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    public void render(@NotNull RenderBatch renderBatch) {
        renderBatch.draw(sprite, getPosX(), getPosY());
    }
}
