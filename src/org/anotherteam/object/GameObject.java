package org.anotherteam.object;

import lombok.val;
import org.anotherteam.level.room.Room;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.collider.Collider;
import org.anotherteam.object.component.sprite.SpriteController;
import org.anotherteam.render.batch.RenderBatch;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GameObject {

    protected Room room;

    protected final Map<Class<? extends Component>, Component> components;
    protected final Vector2i position;

    public GameObject(int x, int y) {
        this.room = null;
        this.position = new Vector2i(x, y);
        components = new HashMap<>();
    }

    @NotNull
    public Vector2i getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        position.set(x, y);
    }

    @NotNull
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        if (!components.containsKey(componentClass)) return null;
        return componentClass.cast(components.get(componentClass));
    }

    @NotNull
    public Map<Class<? extends Component>, Component> getComponents() {
        return Collections.unmodifiableMap(components);
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        components.remove(componentClass);
    }

    public <T extends Component> void addComponent(T component) {
        if (components.containsKey(component.getClass())) {
            components.get(component.getClass()).initBy(component);
            return;
        }
        components.put(component.getClass(), component);
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
        val collider = getComponent(Collider.class);
        if (collider == null) return;
        collider.debugRender(renderBatch);
    }

    public void onAnimationEnd() { }

    //TODO make another method
    public int getRenderPriority() {
        if (getComponent(SpriteController.class) == null) return -1;
        return getComponent(SpriteController.class).getRenderPriority();
    }
}
