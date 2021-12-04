package org.anotherteam.object;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.level.room.Room;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.collider.Collider;
import org.anotherteam.object.component.sprite.SpriteController;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GameObject {

    protected Room room;

    protected final Map<Class<? extends Component>, Component> components;
    protected final Vector2i position;

    /**
     * All GameObjects have a Collider component {Default size 32 x 32}
     */
    protected final Collider collider;

    public GameObject(int x, int y) {
        this.room = null;
        this.position = new Vector2i(x, y);
        components = new HashMap<>();
        collider = new Collider();
        addComponent(collider);
    }

    public static <T extends GameObject> T create(int x, int y, @NotNull Class<T> gameObjectClass) {
        try {
            return gameObjectClass.cast(gameObjectClass
                    .getDeclaredConstructor(int.class, int.class)
                    .newInstance(x, y));
        } catch (Exception e) {
            e.printStackTrace();
            throw new LifeException("Unknown GameObject class " + gameObjectClass.getSimpleName());
        }
    }

    @NotNull
    public Vector2i getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        position.set(x, y);
    }

    @NotNull
    public Collider getCollider() {
        return collider;
    }

    @NotNull
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
        prepare();
    }

    public void prepare() {
        for (val component : components.values()) {
            component.setDependencies();
            component.init();
        }
    }

    public boolean hasComponent(Class<? extends Component> componentClass) {
        return components.containsKey(componentClass);
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
            components.get(component.getClass()).instanceBy(component);
            return;
        }
        components.put(component.getClass(), component);
        component.setOwnerObject(this);
    }

    public void update(float delta) {
        for (val component : components.values()) {
            component.update(delta);
        }
    }

    public void render(@NotNull RenderBatch renderBatch) {
        render(renderBatch, false);
    }

    public void render(@NotNull RenderBatch renderBatch, boolean height) {
        val spriteComponent = getComponent(SpriteController.class);
        if (spriteComponent != null) {
            spriteComponent.draw(position, renderBatch, height);
        }

        if (Game.stateManager.getState() != GameState.ON_EDITOR && !Game.DebugMode) return;
        collider.debugRender(renderBatch.debugRender);
    }

    public void onAnimationEnd() { }

    //TODO make another method
    public int getRenderPriority() {
        if (getComponent(SpriteController.class) == null) return -1;
        return getComponent(SpriteController.class).getRenderPriority();
    }
}
