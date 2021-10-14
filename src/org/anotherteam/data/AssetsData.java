package org.anotherteam.data;


import lombok.val;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.render.texture.Texture;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public final class AssetsData {
        public static final Map<String, SpriteAtlas> spriteAtlases = new HashMap<>();
        public static final Map<String, Texture> textures = new HashMap<>();

        public static final Texture EDITOR_TEXTURE = getTexture("editorTexture.png");
        public static final SpriteAtlas TEST_ROOM_ATLAS = loadSpriteAtlas("testTestRoom.png", Tile.SIZE.x,  Tile.SIZE.y, 4);

        public static final Shader DEFAULT_SHADER = new Shader("shader/defaultVertexShader.glsl", "shader/defaultFragmentShader.glsl");

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
        public static SpriteAtlas loadSpriteAtlas(String atlasName, int width, int height, int heightOffset) {
                val atlas = SpriteAtlas.create(getTexture(atlasName), width, height, heightOffset);
                spriteAtlases.put(atlasName, atlas);
                return atlas;
        }

        @NotNull
        public static SpriteAtlas getSpriteAtlas(String atlasName) {
                if (!spriteAtlases.containsKey(atlasName)) throw new LifeException("Don't found atlas " + atlasName);
                return spriteAtlases.get(atlasName);
        }
}
