package org.anotherteam.editor.level;

import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.text.Label;
import org.anotherteam.editor.gui.menu.sprite.SpriteMenu;
import org.anotherteam.editor.gui.menu.text.SwitchButton;
import org.anotherteam.editor.gui.menu.text.TextMenu;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.screen.DraggedTile;
import org.anotherteam.editor.screen.DraggedTiles;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class TileViewer extends GUIElement {

    private final SwitchMenu typeMenu;

    private final DraggedTile highliter;
    private DraggedTiles draggedTiles;

    public TileViewer(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        final var editor = Editor.inst();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);
        inverted = true;

        typeMenu = new SwitchMenu(0, 0, width, TextMenu.Type.HORIZONTAL, this);
        typeMenu.setInvertedY(true);
        typeMenu.setColor(100, 100, 100);
        typeMenu.setStartOffset(Label.DEFAULT_TEXT_OFFSET, 0);
        fillAtlasesButtons();
        typeMenu.setClicked(typeMenu.getButton(0));

        highliter = new DraggedTile(0, 0, AssetData.EDITOR_HIGHLITER_ATLAS);
        draggedTiles = null;
    }

    public void fillAtlasesButtons() {
        final var files = new File(AssetData.ROOM_PATH).listFiles();
        if (files == null) throw new LifeException("Room's atlases not found");

        for (final var file : files) {
            final var btn = typeMenu.addButton(file.getName());
            generateTileMenu(btn, file.getName());
        }
    }

    public void generateTileMenu(@NotNull SwitchButton button, @NotNull String fileName) {
        final var spriteAtlas = AssetData.getOrLoadRoomAtlas(AssetData.ROOM_PATH + fileName);

        final var spriteMenu = new SpriteMenu(0, -typeMenu.getHeight(), width, height - typeMenu.getHeight(), this);
        spriteMenu.setVisible(false);
        spriteMenu.setInvertedY(true);
        for (final var sprite : spriteAtlas.getSprites()) {
            final var yCheck = spriteAtlas.getSizeY() - sprite.getFrameY() - 1;
            if (yCheck < spriteAtlas.getSizeY() / 2) continue;

            final var xTile = sprite.getFrameX();
            final var yTile = sprite.getFrameY();
            final var spriteButton = spriteMenu.addButton(xTile, yCheck - spriteAtlas.getSizeY() / 2, sprite);
            spriteButton.setOnClick((info)-> {
                if (Input.isKeyDown(Input.KEY_SHIFT) && draggedTiles != null) {
                    draggedTiles.fillTiles(xTile, yTile);
                } else {
                    draggedTiles = new DraggedTiles(new DraggedTile(xTile, yTile, spriteAtlas));
                    GameScreen.draggedThing = draggedTiles;
                }
            });
        }
        button.setOnClick((info)-> spriteMenu.setVisible(true));
        button.setAfterClick((info)-> spriteMenu.setVisible(false));
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        draggedTiles = null;

        if (visible) {
            GameScreen.draggedThing = highliter;
        }
    }

    @Override
    public void update(float dt) {
        if (draggedTiles != null) {
            if (Input.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                var x = GameScreen.onMouseTileX();
                var y = GameScreen.onMouseTileY();
                if (x < 0 || y < 0) return;

                draggedTiles.placeTiles(x, y);
            } else if (Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) {
                GameScreen.draggedThing = highliter;
                draggedTiles = null;
            }
            return;
        }

        if (!Input.isAnyButtonDown()) return;

        final var x = GameScreen.onMouseTileX();
        final var y = GameScreen.onMouseTileY();
        if (x < 0 || y < 0) return;

        if (Input.isKeyDown(Input.KEY_SHIFT) && Input.isButtonDown(Input.MOUSE_RIGHT_BUTTON) || Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) {
            Game.LEVEL_MANAGER.getCurrentRoom().removeTile(x, y);
        }
    }
}
