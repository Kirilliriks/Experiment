package org.anotherteam.oldeditor.gui.menu.sprite;

import org.anotherteam.oldeditor.gui.GUIElement;
import org.anotherteam.oldeditor.render.EditorBatch;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.texture.Texture;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpriteMenu extends GUIElement {

    public static final int ICON_SIZE = 32;

    private final List<DrawableButton> buttons;

    private int offsetIcon;
    private int sizeX, sizeY;

    protected DrawableButton lastClicked;

    public SpriteMenu(float x, float y, int width, int height, GUIElement ownerElement) {
        super(x, y, width, height, ownerElement);
        buttons = new ArrayList<>();
        offsetIcon = 0;
        sizeX = (width + offsetIcon) / ICON_SIZE;
        sizeY = (height + offsetIcon) / ICON_SIZE;
        lastClicked = null;
    }

    @Nullable
    public DrawableButton getLastClicked() {
        return lastClicked;
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

    @NotNull
    public TextureButton addButton(@NotNull Texture texture) {
        final var index = buttons.size();
        if (index >= sizeX * sizeY) throw new LifeException("Need create pages mechanic");

        int x = index % sizeX;
        int y = (index - x) / sizeY;
        return addButton(x, y, texture);
    }

    @NotNull
    public TextureButton addButton(int x, int y, @NotNull Texture texture) {
        if (x * y >= sizeX * sizeY) throw new LifeException("Need create pages mechanic");

        final var textureButton = new TextureButton(texture, 0, 0, this);

        textureButton.setPos(x * ICON_SIZE                + (x + 1) * offsetIcon,
                height - (y + 1) * ICON_SIZE - (y + 1) * offsetIcon);
        buttons.add(textureButton);
        return textureButton;
    }

    public void removeLastButton() {
        final DrawableButton last = getLastClicked();
        if (last == null) return;

        removeButton(last);
    }

    public void removeButton(DrawableButton button) {
        buttons.remove(button);
        removeChild(button);

        if (lastClicked == button) lastClicked = null;
    }

    public void setClicked(DrawableButton button, boolean left) {
        if (button == null) return;

        if (lastClicked != null && button != lastClicked) {
            lastClicked.setClicked(false);
        }

        button.setClicked(true, left);
        lastClicked = button;
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        super.render(editorBatch);
        for (final var bnt : buttons) {

            if (bnt.isClicked()) {
                bnt.drawHighlight(editorBatch);
            }

            if (!bnt.isMouseOnWidget()) continue;

            if (!bnt.isClicked()) {
                bnt.drawHighlight(editorBatch);
            }
            bnt.drawPreview(editorBatch);
        }
    }

    @Override
    public void clearChild() {
        super.clearChild();
        buttons.clear();
    }
}
