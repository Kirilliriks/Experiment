package org.anotherteam.level;

import imgui.ImGui;
import org.anotherteam.Editor;
import org.anotherteam.dragged.DraggedHighliter;
import org.anotherteam.input.Input;
import org.anotherteam.game.data.AssetData;
import org.anotherteam.dragged.DraggedTile;
import org.anotherteam.dragged.DraggedTiles;
import org.anotherteam.util.EditorInput;
import org.anotherteam.widget.Widget;
import org.anotherteam.game.level.room.Room;
import org.anotherteam.game.level.room.tile.Tile;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.render.texture.Texture;
import org.anotherteam.screen.DraggedObject;
import org.anotherteam.screen.Screen;
import org.anotherteam.util.exception.LifeException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class TileViewer extends Widget {

    public static final DraggedHighliter DRAGGED_HIGHLITER = new DraggedHighliter();
    public static final int SCALE_MULTIPLAYER = 2;

    private final List<SpriteAtlas> atlases;
    private final List<Sprite> sprites;

    private SpriteAtlas selectedAtlas;
    private int highlightedSprite;

    public TileViewer(int x, int y, int width, int height) {
        super(x, y, width, height);

        atlases = new ArrayList<>();
        sprites = new ArrayList<>();
        selectedAtlas = null;
        highlightedSprite = -1;

        loadAtlases();
    }

    @Override
    public void update() {
        super.update();

        ImGui.begin("Tile Viewer");

        if (isClicked()) {
            Editor.getInstance().setMode(Editor.Mode.TILE);
        }

        ImGui.text("Select atlas");
        if (ImGui.beginListBox("##select_atlas")) {

            for (final SpriteAtlas spriteAtlas : atlases) {
                if (!ImGui.selectable(spriteAtlas.getName())) {
                    continue;
                }

                selectAtlas(spriteAtlas);
            }

            ImGui.endListBox();
        }

        if (selectedAtlas != null) {
            updatePicker();
        }

        ImGui.end();

        if (Editor.getInstance().getMode() != Editor.Mode.TILE) {
            return;
        }

        if (Screen.getDraggedObject() == null) {
            Screen.setDraggedObject(DRAGGED_HIGHLITER);
        }

        final int tileX = Screen.mouseOnTileX();
        final int tileY = Screen.mouseOnTileY();
        if (tileX < 0 || tileY < 0) return;

        final DraggedObject draggedObject = getDraggedTile();
        final Room room = Editor.getInstance().getGame().getLevelManager().getRoom();
        if (draggedObject == null && EditorInput.isButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
            room.removeTile(tileX, tileY);
        }

        if (EditorInput.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) {
            if (draggedObject == null && EditorInput.isKeyDown(Input.KEY_SHIFT)) {
                final Tile tile = room.getTile(tileX, tileY);
                if (tile == null) {
                    return;
                }

                Screen.setDraggedObject(new DraggedTile(tile));
                return;
            }

            if (draggedObject instanceof DraggedTile draggedTile) {
                draggedTile.placeTile(room, tileX, tileY);
            } else if (draggedObject instanceof DraggedTiles draggedTiles) {
                draggedTiles.placeTiles(room, tileX, tileY);
            }
        }
    }

    private void updatePicker() {
        final Texture texture = selectedAtlas.getTexture();

        final int maxSizeX = selectedAtlas.getSizeX();

        boolean highlighted = false;
        for (int y = selectedAtlas.getSizeY() / 2 - 1; y >= 0; y--) {
            for (int x = 0; x < maxSizeX; x++) {
                final int index = x + y * maxSizeX;

                final Sprite sprite = sprites.get(index);

                final var u0 = sprite.getU0();
                final var v0 = sprite.getV0();
                final var u1 = sprite.getU1();
                final var v1 = sprite.getV1();

                ImGui.getStyle().setItemSpacing(0, 0);
                if (highlightedSprite == index) {
                    ImGui.image(texture.getId(), sprite.getWidth() * SCALE_MULTIPLAYER, sprite.getHeight() * SCALE_MULTIPLAYER, u0, v0, u1, v1, 128, 128, 0, 255);
                } else {
                    ImGui.image(texture.getId(), sprite.getWidth() * SCALE_MULTIPLAYER, sprite.getHeight() * SCALE_MULTIPLAYER, u0, v0, u1, v1);
                }

                if (ImGui.isItemHovered()) {
                    if (ImGui.isItemClicked()) {
                        if (Input.isKeyDown(Input.KEY_SHIFT) && getDraggedTile() instanceof DraggedTile dragged) {
                            Screen.setDraggedObject(
                                    new DraggedTiles(dragged.getFrameX(), dragged.getFrameY(), x, y, selectedAtlas)
                            );
                        } else {
                            Screen.setDraggedObject(new DraggedTile(x, y, selectedAtlas));
                        }
                    }

                    highlighted = true;
                    highlightedSprite = index;

                    ImGui.beginTooltip();
                    ImGui.image(texture.getId(), sprite.getWidth() * SCALE_MULTIPLAYER * 2, sprite.getHeight() * SCALE_MULTIPLAYER * 2, u0, v0, u1, v1);
                    ImGui.endTooltip();
                }

                if (x + 1 != maxSizeX) {
                    ImGui.sameLine();
                }
            }
        }

        if (!highlighted) {
            highlightedSprite = -1;
        }
    }

    private void selectAtlas(SpriteAtlas atlas) {
        selectedAtlas = atlas;

        sprites.clear();
        for (final Sprite sprite : selectedAtlas.getSprites()) {
            final var yCheck = selectedAtlas.getSizeY() - sprite.getFrameY() - 1;
            if (yCheck < selectedAtlas.getSizeY() / 2) continue;

            sprites.add(sprite);
        }
    }

    private void loadAtlases() {
        final var files = new File(AssetData.ROOM_PATH).listFiles();
        if (files == null) {
            throw new LifeException("Room's atlases not found");
        }

        for (final var file : files) {
            final SpriteAtlas spriteAtlas = AssetData.getOrLoadRoomAtlas(AssetData.ROOM_PATH + file.getName());

            atlases.add(spriteAtlas);
        }
    }

    private DraggedObject getDraggedTile() {
        final DraggedObject draggedObject = Screen.getDraggedObject();
        if (draggedObject instanceof DraggedHighliter) {
            return null;
        }

        return draggedObject;
    }
}
