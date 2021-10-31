package org.anotherteam.data;


import lombok.val;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.render.texture.Texture;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class AssetData {
        public static final Map<String, SpriteAtlas> spriteAtlases = new HashMap<>();
        public static final Map<String, Texture> textures = new HashMap<>();

        public static final Texture EDITOR_TEXTURE = getTexture("editorTexture.png");
        public static final SpriteAtlas TEST_ROOM_ATLAS = loadRoomAtlas("testTestRoom.png", Tile.SIZE.x,  Tile.SIZE.y, 4);
        public static final SpriteAtlas TEST_PLAYER_ATLAS = loadSpriteAtlas("testPlayerAtlas.png", 32,  32);
        public static final SpriteAtlas EDITOR_NULL_ICON_ATLAS = loadSpriteAtlas("nullIcon.png", 32,  32);

        public static final Shader DEFAULT_SHADER = new Shader("shader/defaultVertexShader.glsl", "shader/defaultFragmentShader.glsl");
        public static final Shader DEBUG_SHADER = new Shader("shader/debugVS.glsl", "shader/debugFS.glsl");

        @NotNull
        public static Texture getTexture(String textureName) {
                if (textures.containsKey(textureName)) return textures.get(textureName);
                val path = "../assets/" + textureName;
                val texture = Texture.create(path);
                texture.setName(textureName);
                textures.put(textureName, texture);
                return texture;
        }

        @NotNull
        public static SpriteAtlas loadSpriteAtlas(String atlasName, int frameWidth, int frameHeight) {
                return loadRoomAtlas(atlasName, frameWidth, frameHeight, -1);
        }

        @NotNull
        public static SpriteAtlas loadRoomAtlas(String atlasName, int frameWidth, int frameHeight, int heightOffset) {
                val atlas = SpriteAtlas.create(getTexture(atlasName), frameWidth, frameHeight, heightOffset);
                spriteAtlases.put(atlasName, atlas);
                return atlas;
        }

        @NotNull
        public static SpriteAtlas getSpriteAtlas(String atlasName) {
                if (!spriteAtlases.containsKey(atlasName)) throw new LifeException("Don't found atlas " + atlasName);
                return spriteAtlases.get(atlasName);
        }
}
