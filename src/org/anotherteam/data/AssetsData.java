package org.anotherteam.data;


import lombok.val;
import org.anotherteam.render.texture.Pixmap;
import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class AssetsData {
        public static final Pixmap TEST_TEXTURE = new Pixmap("../assets/testTestRoom.png");

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
