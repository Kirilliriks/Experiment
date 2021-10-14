package org.anotherteam.data;


import lombok.val;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.render.texture.Pixmap;
import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class AssetsData {
        public static final Shader DEFAULT_SHADER = new Shader("shader/defaultVertexShader.glsl", "shader/defaultFragmentShader.glsl");

        public static final Texture EDITOR_TEXTURE = new Texture("../assets/editorTexture.png");
        public static final SpriteAtlas TEST_ROOM_TEXTURE = new SpriteAtlas("../assets/testTestRoom.png", Tile.SIZE.x,  Tile.SIZE.y);



        public static final Map<String, Texture> textures = new HashMap<>();

        @NotNull
        public static Texture getTexture(String textureName) {
                if (textures.containsKey(textureName)) return textures.get(textureName);
                val path = "../assets/" + textureName;
                val texture = new Texture(path);
                textures.put(textureName, texture);
                return texture;
        }
}
