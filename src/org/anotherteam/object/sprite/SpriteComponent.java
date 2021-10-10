package org.anotherteam.object.sprite;

import lombok.NonNull;
import lombok.val;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.sprite.animation.AnimationData;
import org.anotherteam.object.sprite.animation.AnimationTimer;
import org.anotherteam.render.RenderBatch;
import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class SpriteComponent {

    private final GameObject gameObject;

    private final Vector2i defaultUV;
    private final Vector2i UV;

    private Texture texture;
    private final Vector2i frameSize;
    private final Vector2i textureSize;

    private final Vector2i offSet;

    private AnimationData animation;
    private AnimationTimer animationTimer;

    /**
     * Draw object from center of sprite or edge
     */
    private boolean center;

    private boolean flip;

    public SpriteComponent(@NonNull GameObject gameObject) {
        this.gameObject = gameObject;
        this.defaultUV = new Vector2i(0, 0);
        this.UV = new Vector2i(0, 0);
        this.textureSize = new Vector2i(0, 0);
        this.frameSize = new Vector2i(0, 0);
        this.offSet = new Vector2i(0, 0);

        this.animation = null;
        this.animationTimer = null;

        this.center = true;
        this.flip = false;
    }

    public void loadSprite(String texturePath) {
        this.texture = new Texture(texturePath);
        setFrameSize(texture.getHeight(), texture.getWidth());
    }

    public void update(float delta) {
        if (animationTimer == null ||
            animationTimer.isFinish() ||
            !animationTimer.tick(delta)) return;

        if (UV.x >= animation.getEndFrame()) {
            gameObject.onAnimationEnd();
            animationTimer.cancel();
            if (!animation.isCancelOnEnd()) {
                repeatAnimation(); // Again call animation
            } else stopAnimation();
            return;
        }
        setFrame(UV.x + 1, UV.y);
    }

    public void draw(@NotNull Vector2i position, @NonNull RenderBatch batch) {
        if (texture == null) return;

        val centerOffset = center ? (int)(textureSize.x / 2f) : 0;
        batch.draw(texture,
                position.x + offSet.x - centerOffset, position.y + offSet.y,
                textureSize.x, textureSize.y,
                UV.x, UV.y,
                frameSize.x, frameSize.y,
                flip, false);
    }

    public void setFrame(int u, int v) {
        UV.set(u, v);
    }

    public void setTextureSize(int x, int y) {
        val scale = gameObject.getScale();
        textureSize.set((int)(x * scale), (int)(y * scale));
    }

    public void setCenter(boolean center) {
        this.center = center;
    }
    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    /**
     * Set frame size and rendering size
     */
    public void setFrameSize(int x, int y) {
        frameSize.set(x, y);
        setTextureSize(x, y);
    }

    public void stopAnimation() {
        if (animationTimer != null) animationTimer.cancel();
        animation = null;
        animationTimer = null;
        setFrame(defaultUV.x, defaultUV.y);
    }

    private void repeatAnimation() {
        AnimationData againAnimation = animation;
        animation = null;
        setAnimation(againAnimation);
    }

    public void setAnimation(@NonNull AnimationData newAnimation) {
        if (this.animation != null && this.animation == newAnimation) return;
        if (animationTimer != null) animationTimer.cancel();

        this.animation = newAnimation;
        setFrame(animation.getStartFrame(), animation.getYTexture());

        animationTimer = new AnimationTimer(animation.getAnimSpeed(), (animation.getEndFrame() - animation.getStartFrame() + 1));
    }
}
