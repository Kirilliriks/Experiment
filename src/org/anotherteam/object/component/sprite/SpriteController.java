package org.anotherteam.object.component.sprite;

import lombok.NonNull;
import lombok.val;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.sprite.animation.AnimationData;
import org.anotherteam.object.component.sprite.animation.AnimationTimer;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class SpriteController extends Component {

    private int drawPriority;

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
    private final boolean center;

    public SpriteController() {
        drawPriority = 0;

        textureSprite = null;
        heightSprite = null;
        animation = null;
        animationTimer = null;

        center = true;
    }

    public void setDrawPriority(int drawPriority) {
        this.drawPriority = drawPriority;
    }

    public void setSpriteAtlas(SpriteAtlas spriteAtlas) {
        this.spriteAtlas = spriteAtlas;
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

        if (frameX >= animation.getEndFrame()) {
            ownerObject.onAnimationEnd();
            animationTimer.cancel();
            if (!animation.isCancelOnEnd()) {
                repeatAnimation(); // Again call animation
            } else stopAnimation();
            return;
        }
        frameX++;
    }

    public void draw(@NotNull Vector2i position, @NonNull RenderBatch batch, boolean height) {
        if (textureSprite == null || (height && heightSprite == null)) throw new LifeException("Try draw null texture");

        val centerOffset = center ? (int)(textureSprite.getWidth() / 2f) : 0;
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

    public void setAnimation(@NonNull AnimationData newAnimation) {
        if (animation != null && animation == newAnimation) return;
        if (animationTimer != null) animationTimer.cancel();

        animation = newAnimation;
        frameX = animation.getStartFrame();
        frameY = animation.getFramePosY();
        textureSprite = spriteAtlas.getTextureSprite(frameX, frameY);
        heightSprite = spriteAtlas.getHeightSprite(frameX, frameY);

        animationTimer = new AnimationTimer(animation.getAnimSpeed(), (animation.getEndFrame() - frameX + 1));
    }

    @NotNull
    public Sprite getTextureSprite() {
        return textureSprite;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }
}
