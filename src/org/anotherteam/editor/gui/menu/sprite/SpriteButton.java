package org.anotherteam.editor.gui.menu.sprite;

import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.render.sprite.Sprite;
import org.jetbrains.annotations.NotNull;

public class SpriteButton extends Button {

    private final Sprite sprite;

    public SpriteButton(@NotNull Sprite sprite, float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        this.sprite = sprite;
        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    @Override
    public void update(float dt) { }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
        editorBatch.draw(sprite, getPosX(), getPosY());
    }
}
