package org.anotherteam.object.component.type.sprite;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.anotherteam.data.AssetData;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.fieldcontroller.FieldController;
import org.anotherteam.object.component.type.sprite.animation.AnimationData;
import org.anotherteam.object.component.type.sprite.animation.AnimationTimer;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public final class SpriteController extends Component {

    private int drawPriority;

    // Info for texture
    private String atlasPath;
    private int frameWidth, frameHeight;

    private SpriteAtlas spriteAtlas;
    private Sprite textureSprite;
    private Sprite heightSprite;
    private AnimationData animation;
    private AnimationTimer animationTimer;

    private int frameX, frameY;
    private boolean flipX;

    /**
     * Draw object from center of sprite or edge
     */
    private boolean center;

    public SpriteController() {
        drawPriority = 0;

        atlasPath = null;
        spriteAtlas = null;
        textureSprite = null;
        heightSprite = null;
        animation = null;
        animationTimer = null;

        center = true;
    }

    @Override
    public List<FieldController> getFields() {
        final List<FieldController> list = new ArrayList<>();
        list.add(new FieldController("Select sprite", textureSprite, (value) -> textureSprite = (Sprite) value));
        return list;
    }

    public void setDrawPriority(int drawPriority) {
        this.drawPriority = drawPriority;
    }

    public void setSpriteAtlas(String atlasPath, int frameWidth, int frameHeight) {
        this.atlasPath = atlasPath;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        spriteAtlas = AssetData.getOrLoadSpriteAtlas(atlasPath, frameWidth, frameHeight);
        frameX = 0;
        frameY = 0;
        textureSprite = spriteAtlas.getTextureSprite(frameX, frameY);
        heightSprite = spriteAtlas.getHeightSprite(frameX, frameY);
    }

    public void update(float delta) {
        textureSprite = spriteAtlas.getTextureSprite(frameX, frameY);
        heightSprite = spriteAtlas.getHeightSprite(frameX, frameY);

        if (animationTimer == null ||
            animationTimer.isFinish() ||
            !animationTimer.tick(delta)) return;

        if (frameX >= animation.endFrame()) {
            animationTimer.cancel();
            if (!animation.cancelOnEnd()) {
                repeatAnimation(); // Again call animation
            } else stopAnimation();
            return;
        }
        frameX++;
    }

    public void draw(@NotNull Vector2i position, @NotNull RenderBatch batch, boolean height) {
        if (textureSprite == null || (height && heightSprite == null)) throw new LifeException("Try draw null texture");

        final var centerOffset = center ? (int)(textureSprite.getWidth() / 2f) : 0;
        if (height) {
            batch.draw(heightSprite,
                    position.x - centerOffset, position.y, flipX);
        } else {
            batch.draw(textureSprite,
                    position.x - centerOffset, position.y, flipX);
        }
    }

    public int getRenderPriority() {
        return drawPriority;
    }

    public void stopAnimation() {
        if (animationTimer != null) animationTimer.cancel();
        animation = null;
        animationTimer = null;
    }

    private void repeatAnimation() {
        AnimationData againAnimation = animation;
        animation = null;
        setAnimation(againAnimation);
    }

    public void setAnimation(@NotNull AnimationData newAnimation) {
        if (animation != null && animation == newAnimation) return;
        if (animationTimer != null) animationTimer.cancel();

        animation = newAnimation;
        frameX = animation.startFrame();
        frameY = animation.getFramePosY();
        textureSprite = spriteAtlas.getTextureSprite(frameX, frameY);
        heightSprite = spriteAtlas.getHeightSprite(frameX, frameY);

        animationTimer = new AnimationTimer(animation.getAnimSpeed(), (animation.endFrame() - frameX + 1));
    }

    @NotNull
    public Sprite getTextureSprite() {
        return textureSprite;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    @Override
    public @NotNull JsonElement serialize(JsonObject result) {
        result.add("atlasPath", new JsonPrimitive(atlasPath));
        result.add("width", new JsonPrimitive(frameWidth));
        result.add("height", new JsonPrimitive(frameHeight));
        return result;
    }

    public static SpriteController deserialize(JsonObject object) {
        final var atlasPath = object.get("atlasPath").getAsString();
        final var width = object.get("width").getAsInt();
        final var height = object.get("height").getAsInt();

        final var spriteController = new SpriteController();
        spriteController.setSpriteAtlas(atlasPath, width, height);
        return spriteController;
    }
}
