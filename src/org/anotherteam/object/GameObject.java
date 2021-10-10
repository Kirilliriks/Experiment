package org.anotherteam.object;

import org.anotherteam.level.Level;
import org.anotherteam.object.sprite.SpriteComponent;
import org.anotherteam.physic.Collider;
import org.anotherteam.render.RenderBatch;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public abstract class GameObject {

    protected Level level;

    protected final SpriteComponent sprite;
    protected final Collider collider;
    protected final Vector2i position;

    protected int drawPriority;

    // Object size multiplier
    protected float scale;

    public GameObject(@NotNull Vector2i position, @NotNull Level level){
        this.scale = 1;
        this.level = level;
        this.position = position;
        this.drawPriority = 0;

        this.sprite = new SpriteComponent(this);
        this.collider = new Collider(position, this);
    }

    public int getDrawPriority() {
        return drawPriority;
    }

    @NotNull
    public Vector2i getPosition() {
        return position;
    }

    @NotNull
    public Collider getCollider() {
        return collider;
    }

    @NotNull
    public SpriteComponent getSprite() {
        return sprite;
    }

    public float getScale() {
        return scale;
    }

    public void update(float delta) {
        sprite.update(delta);
    }

    public void drawTexture(@NotNull RenderBatch renderBatch) {
        sprite.draw(position, renderBatch);
    }

    public void onAnimationEnd() { }
}
