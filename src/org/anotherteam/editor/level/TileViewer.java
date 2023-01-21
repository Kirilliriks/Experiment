package org.anotherteam.editor.level;

import imgui.ImGui;
import org.anotherteam.data.AssetData;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.render.texture.Texture;
import org.anotherteam.util.exception.LifeException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class TileViewer {

    public static final int TILE_SCALE = 2;

    private final List<SpriteAtlas> atlases;
    private final List<Sprite> sprites;

    private SpriteAtlas selectedAtlas;
    private int highlightedSprite;

    public TileViewer() {
        atlases = new ArrayList<>();
        sprites = new ArrayList<>();
        selectedAtlas = null;
        highlightedSprite = -1;

        loadAtlases();
    }

    public void update() {
        ImGui.begin("Tile Viewer");

        if (ImGui.beginListBox("Select pack")) {
            for (final SpriteAtlas spriteAtlas : atlases) {
                if (ImGui.selectable(spriteAtlas.getName())) {
                    selectAtlas(spriteAtlas);
                }
            }
            ImGui.endListBox();
        }

        if (selectedAtlas != null) {
            generatePicker();
        }

        ImGui.getStyle().setItemSpacing(0.5f, 0.5f);

        ImGui.end();
    }

    private void generatePicker() {
        final Texture texture = selectedAtlas.getTexture();

        final int maxSizeX = selectedAtlas.getSizeX();

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
                    ImGui.image(texture.getId(), sprite.getWidth() * TILE_SCALE, sprite.getHeight() * TILE_SCALE, u0, v0, u1, v1, 128, 128, 0, 255);
                } else {
                    ImGui.image(texture.getId(), sprite.getWidth() * TILE_SCALE, sprite.getHeight() * TILE_SCALE, u0, v0, u1, v1);
                }

                if (ImGui.isItemHovered()) {
                    highlightedSprite = index;
                    ImGui.beginTooltip();
                    ImGui.image(texture.getId(), sprite.getWidth() * TILE_SCALE * 2, sprite.getHeight() * TILE_SCALE * 2, u0, v0, u1, v1);
                    ImGui.endTooltip();
                }

                if (x + 1 != maxSizeX) {
                    ImGui.sameLine();
                }
            }
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
        if (files == null) throw new LifeException("Room's atlases not found");

        for (final var file : files) {
            final SpriteAtlas spriteAtlas = AssetData.getOrLoadRoomAtlas(AssetData.ROOM_PATH + file.getName());

            atlases.add(spriteAtlas);
        }
    }
}
