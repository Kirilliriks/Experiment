package org.anotherteam.object;

import lombok.val;
import org.anotherteam.level.Level;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.sprite.SpriteController;
import org.anotherteam.render.batch.RenderBatch;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Map;

public abstract class GameObject {

    protected Level level;

    protected final Map<Class<? extends Component>, Component> components;
    protected final Vector2i position;

    public GameObject(@NotNull Vector2i position, @NotNull Level level){
        this.level = level;
        this.position = position;
        components = new HashMap<>();
    }

    @NotNull
    public Vector2i getPosition() {
        return position;
    }

    public Level getLevel() {
        return level;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        if (!components.containsKey(componentClass)) return null;
        return componentClass.cast(components.get(componentClass));
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        components.remove(componentClass);
    }

    public <T extends Component> void addComponent(T component) {
        this.components.put(component.getClass(), component);
        component.setOwnerObject(this);
        setComponentsRequirements();
    }

    private void setComponentsRequirements() {
        for (val component : components.values()) {
            component.setDependencies();
        }
    }

    public void update(float delta) {
        for (val component : components.values()) {
            component.update(delta);
        }
    }

    public void render(@NotNull RenderBatch renderBatch) {
        val spriteComponent = getComponent(SpriteController.class);
        if (spriteComponent == null) return;
        spriteComponent.draw(position, renderBatch);
    }

    public void onAnimationEnd() { }

    //TODO make another method
    public int getRenderPriority() {
        if (getComponent(SpriteController.class) == null) return -1;
        return getComponent(SpriteController.class).getRenderPriority();
    }
}
