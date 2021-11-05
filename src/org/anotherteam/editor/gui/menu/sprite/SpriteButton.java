package org.anotherteam.editor.gui.menu.sprite;

import org.anotherteam.Input;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public class SpriteButton extends Button {

    public static final int PREVIEW_SCALE = 5;

    private final Sprite sprite;

    public SpriteButton(@NotNull Sprite sprite, float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        this.sprite = sprite;
        width = SpriteMenu.ICON_SIZE;
        height = SpriteMenu.ICON_SIZE;
        setColor(Color.BLACK);
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
        if (!isMouseOnWidget()) return;
        if (!Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) return;
        setClicked(true);
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
        editorBatch.draw(sprite, getPosX(), getPosY(), SpriteMenu.ICON_SIZE, SpriteMenu.ICON_SIZE);
    }

    public boolean tryDrawPreview(@NotNull EditorBatch editorBatch) {
        if (!isMouseOnWidget()) return false;
        editorBatch.draw(sprite, getPosX() + SpriteMenu.ICON_SIZE, getPosY() - SpriteMenu.ICON_SIZE * PREVIEW_SCALE,
                SpriteMenu.ICON_SIZE * PREVIEW_SCALE, SpriteMenu.ICON_SIZE * PREVIEW_SCALE);
        return true;
    }
}
