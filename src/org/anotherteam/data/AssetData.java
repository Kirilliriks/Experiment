package org.anotherteam.data;

import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.logger.GameLogger;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.render.text.Font;
import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class AssetData {
        public static final String ASSETS_PATH = "assets/";

        public static final String LEVELS_PATH = ASSETS_PATH + "levels/";
        public static final String ATLASES_PATH = ASSETS_PATH + "atlases/";
        public static final String ROOM_PATH = ATLASES_PATH + "room/";
        public static final String ENTITY_PATH = ATLASES_PATH + "entity/";
        public static final String ROOM_OBJECTS_PATH = ATLASES_PATH + "object/";

        public static final Map<String, SpriteAtlas> spriteAtlases = new HashMap<>();
        public static final Map<String, Texture> textures = new HashMap<>();

        // Editor textures
        public static final Texture EDITOR_TEXTURE = getTexture("editorTexture.png");
        public static final Texture EDITOR_HIGHLITER_TEXTURE = getTexture("editor_highliter_texture.png");
        public static final SpriteAtlas EDITOR_HIGHLITER_ATLAS = loadSpriteAtlas("editor_highliter_texture.png", Tile.SIZE.x,  Tile.SIZE.y);
        public static final SpriteAtlas EDITOR_NULL_ICON_ATLAS = loadSpriteAtlas("nullIcon.png", Sprite.SIZE.x,  Sprite.SIZE.y);
        //

        public static final Shader DEFAULT_SHADER = new Shader("shader/defaultVertexShader.glsl", "shader/defaultFragmentShader.glsl");
        public static final Shader DEBUG_SHADER = new Shader("shader/debugVS.glsl", "shader/debugFS.glsl");

        public static final Font DEBUG_FONT = new Font("font/font.ttf", 16);

        @NotNull
        public static Texture getTexture(String textureName) {
                if (textures.containsKey(textureName)) {
                        GameLogger.log("Loaded " + textureName);
                        return textures.get(textureName);
                }

                final var texture = Texture.create(textureName);
                texture.setName(textureName);
                textures.put(textureName, texture);

                GameLogger.log("Created " + textureName);

                return texture;
        }

        @NotNull
        private static SpriteAtlas loadSpriteAtlas(String atlasName, int frameWidth, int frameHeight) {
                final var texture = getTexture(atlasName);
                final var atlas = SpriteAtlas.create(texture, frameWidth, frameHeight, (texture.getHeight() / 2) / frameHeight);
                spriteAtlases.put(atlasName, atlas);
                return atlas;
        }

        @NotNull
        public static SpriteAtlas getOrLoadSpriteAtlas(String atlasName, int frameWidth, int frameHeight) {
                if (!spriteAtlases.containsKey(atlasName)) return loadSpriteAtlas(atlasName, frameWidth, frameHeight);
                //throw new LifeException("Don't found atlas " + atlasName);
                return spriteAtlases.get(atlasName);
        }

        @NotNull
        public static SpriteAtlas getOrLoadRoomAtlas(String atlasName) {
                if (!spriteAtlases.containsKey(atlasName)) return loadSpriteAtlas(atlasName, Tile.SIZE.x, Tile.SIZE.y);
                //throw new LifeException("Don't found atlas " + atlasName);
                return spriteAtlases.get(atlasName);
        }
}
