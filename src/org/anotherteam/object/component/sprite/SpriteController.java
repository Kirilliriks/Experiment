package org.anotherteam.object.component.sprite;

import lombok.NonNull;
import lombok.val;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.sprite.animation.AnimationData;
import org.anotherteam.object.component.sprite.animation.AnimationTimer;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class SpriteController extends Component {

    private final int drawPriority;

    private SpriteAtlas spriteAtlas;
    private Sprite sprite;
    private AnimationData animation;
    private AnimationTimer animationTimer;

    private int frameX, frameY;
    private boolean flipX;

    /**
     * Draw object from center of sprite or edge
     */
    private final boolean center;

    public SpriteController(int drawPriority) {
        this.drawPriority = drawPriority;

        this.sprite = null;
        this.animation = null;
        this.animationTimer = null;

        this.center = true;
    }

    public void setSpriteAtlas(SpriteAtlas spriteAtlas) {
        this.spriteAtlas = spriteAtlas;
    }

    public void update(float delta) {
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
        sprite = spriteAtlas.getSprite(frameX, frameY);
    }

    public void draw(@NotNull Vector2i position, @NonNull RenderBatch batch) {
        if (sprite == null) return;

        val centerOffset = center ? (int)(sprite.getWidth() / 2f) : 0;
        batch.draw(sprite,
                position.x - centerOffset, position.y, flipX);
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
        if (this.animation != null && this.animation == newAnimation) return;
        if (animationTimer != null) animationTimer.cancel();

        this.animation = newAnimation;
        frameX = animation.getStartFrame();
        frameY = animation.getFramePosY();
        sprite = spriteAtlas.getSprite(animation.getStartFrame(), animation.getFramePosY());

        animationTimer = new AnimationTimer(animation.getAnimSpeed(), (animation.getEndFrame() - animation.getStartFrame() + 1));
    }

    @NotNull
    public Sprite getSprite() {
        return sprite;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }
}
