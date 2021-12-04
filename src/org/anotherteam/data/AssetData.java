package org.anotherteam.data;

import lombok.val;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class AssetData {
        public static final String ASSETS_PATH = "../assets/";
        public static final String ROOM_ATLASES_PATH = "atlases/room/";
        public static final String ENTITY_ATLASES_PATH = "atlases/entity/";

        public static final Map<String, SpriteAtlas> spriteAtlases = new HashMap<>();
        public static final Map<String, Texture> textures = new HashMap<>();

        // Editor textures
        public static final Texture EDITOR_TEXTURE = getTexture("editorTexture.png");
        public static final Texture EDITOR_HIGHLITER_TEXTURE = getTexture("editor_highliter_texture.png");
        public static final SpriteAtlas EDITOR_HIGHLITER_ATLAS = loadSpriteAtlas("editor_highliter_texture.png", Tile.SIZE.x,  Tile.SIZE.y);
        public static final SpriteAtlas EDITOR_NULL_ICON_ATLAS = loadSpriteAtlas("nullIcon.png", Sprite.SIZE.x,  Sprite.SIZE.y);
        //

        public static final SpriteAtlas TEST_PLAYER_ATLAS = loadSpriteAtlas(ENTITY_ATLASES_PATH + "testPlayerAtlas.png", Sprite.SIZE.x,  Sprite.SIZE.y);

        public static final Shader DEFAULT_SHADER = new Shader("shader/defaultVertexShader.glsl", "shader/defaultFragmentShader.glsl");
        public static final Shader DEBUG_SHADER = new Shader("shader/debugVS.glsl", "shader/debugFS.glsl");

        @NotNull
        private static Texture getTexture(String textureName) {
                if (textures.containsKey(textureName)) return textures.get(textureName);

                val path = ASSETS_PATH + textureName;
                val texture = Texture.create(path);
                texture.setName(textureName);
                textures.put(textureName, texture);
                return texture;
        }

        @NotNull
        private static SpriteAtlas loadSpriteAtlas(String atlasName, int frameWidth, int frameHeight) {
                val texture = getTexture(atlasName);
                val atlas = SpriteAtlas.create(getTexture(atlasName), frameWidth, frameHeight, (texture.getHeight() / 2) / frameHeight);
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
        public static SpriteAtlas getOrLoadSpriteAtlas(String atlasName) {
                if (!spriteAtlases.containsKey(atlasName)) return loadSpriteAtlas(atlasName, Tile.SIZE.x, Tile.SIZE.y);
                //throw new LifeException("Don't found atlas " + atlasName);
                return spriteAtlases.get(atlasName);
        }
}
