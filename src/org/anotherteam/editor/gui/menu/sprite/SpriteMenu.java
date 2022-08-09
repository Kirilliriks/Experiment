package org.anotherteam.editor.gui.menu.sprite;

import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpriteMenu extends GUIElement {

    public static final int ICON_SIZE = 32;

    private final List<SpriteButton> buttons;

    private int offsetIcon;
    private int sizeX, sizeY;

    public SpriteMenu(float x, float y, int width, int height, GUIElement ownerElement) {
        super(x, y, width, height, ownerElement);
        buttons = new ArrayList<>();
        offsetIcon = 0;
        sizeX = (width + offsetIcon) / ICON_SIZE;
        sizeY = (height + offsetIcon) / ICON_SIZE;
    }

    public void setOffsetIcon(int offsetIcon) {
        this.offsetIcon = offsetIcon;
        sizeX = (width + offsetIcon) / ICON_SIZE;
        sizeY = (height + offsetIcon) / ICON_SIZE;
    }

    @NotNull
    public SpriteButton addButton(@NotNull Sprite sprite) {
        final var index = buttons.size();
        if (index >= sizeX * sizeY) throw new LifeException("Need create pages mechanic");

        int x = index % sizeX;
        int y = (index - x) / sizeY;
        return addButton(x, y, sprite);
    }

    @NotNull
    public SpriteButton addButton(int x, int y, @NotNull Sprite sprite) {
        if (x * y >= sizeX * sizeY) throw new LifeException("Need create pages mechanic");

        final var spriteButton = new SpriteButton(sprite, 0, 0, this);

        spriteButton.setPos(x * ICON_SIZE                + (x + 1) * offsetIcon,
                            height - (y + 1) * ICON_SIZE - (y + 1) * offsetIcon);
        buttons.add(spriteButton);
        return spriteButton;
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        super.render(editorBatch);
        for (final var bnt : buttons) {
            if (bnt.tryDrawPreview(editorBatch))
                break;
        }
    }

    @Override
    public void clearChild() {
        super.clearChild();
        buttons.clear();
    }
}
