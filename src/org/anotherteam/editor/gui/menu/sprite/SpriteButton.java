package org.anotherteam.editor.gui.menu.sprite;

import org.anotherteam.Input;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.render.sprite.Sprite;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class SpriteButton extends Button {

    private final Sprite sprite;

    public SpriteButton(@NotNull Sprite sprite, float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        this.sprite = sprite;
        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    @Override
    public void setClicked(boolean clicked) {
        super.setClicked(clicked);
        if (!clicked) return;
        if (onClick != null) onClick.run();
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

        setClicked(false);
        if (!mouseOnWidget()) return;
        if (!Input.isButtonDown(Input.MOUSE_LEFT_BUTTON)) return;
        setClicked(true);
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
        editorBatch.draw(sprite, getPosX(), getPosY());
    }
}
