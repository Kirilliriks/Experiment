package org.anotherteam.game.object;

import lombok.Getter;
import lombok.Setter;
import org.anotherteam.game.Game;
import org.anotherteam.game.object.component.Component;
import org.anotherteam.game.level.room.Room;
import org.anotherteam.game.object.component.type.transform.Transform;
import org.anotherteam.logger.GameLogger;
import org.anotherteam.game.object.component.type.collider.Collider;
import org.anotherteam.game.object.component.type.sprite.SpriteComponent;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.*;

@Getter
@Setter
public final class GameObject {

    private final List<Component> components = new ArrayList<>();
    private final Transform transform = addComponent(new Transform());
    private final Collider collider = addComponent(new Collider());
    private Room room = null;
    private String name;

    /**
     * All GameObjects have Transform and Collider component (index 0) {Default size 32 x 32}
     */

    public GameObject(int x, int y) {
        this("unnamed", x, y);
    }

    public GameObject(String name, int x, int y) {
        this.name = name;
        transform.setPosition(x, y);
    }

    public Vector2i getPosition() {
        return transform.getPosition();
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void prepare() {
        for (final Component component : components) {
            component.setDependencies();
            component.init();
        }
    }

    public boolean hasComponent(Class<? extends Component> componentClass) {
        for (final Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) return true;
        }
        return false;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (final Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                return componentClass.cast(component);
            }
        }
        return null;
    }

    public <T extends Component> int getComponentIndex(Class<T> componentClass) {
        for (int i = 0 ; i < components.size(); i++) {
            final Component component = components.get(i);
            if (componentClass.isAssignableFrom(component.getClass())) {
                return i;
            }
        }
        return -1;
    }

    public <T extends Component> T addComponent(T component) {

        final int index = getComponentIndex(component.getClass());
        if (index != -1) {
            components.set(index, component);
        } else {
            components.add(component);
        }

        component.setOwnerObject(this);
        component.setDependencies(); // TODO уже вызывается в Prepare, может можно отказаться?*
        return component;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        if (componentClass.isAssignableFrom(Collider.class)) {
            GameLogger.log("Collider is static component");
            return;
        }

        if (componentClass.isAssignableFrom(Collider.InteractAABB.class)) {
            GameLogger.log("InteractAABB is static component");
            return;
        }

        final Component component = getComponent(componentClass);
        if (component == null) return;

        components.remove(component);
    }

    @NotNull
    public List<Component> getComponents() {
        return Collections.unmodifiableList(components);
    }

    public void update(float delta) {
        for (final Component component : components) {
            component.update(delta);
        }
    }

    public void draw(@NotNull RenderBatch renderBatch, boolean height) {
        final SpriteComponent spriteComponent = getComponent(SpriteComponent.class);
        if (spriteComponent != null) {
            spriteComponent.draw(renderBatch, height);
        }
    }

    public void debugDraw(RenderBatch renderBatch, boolean inEditor) {
        if (!Game.DEBUG_MODE) return;

        final Vector2i pos = transform.getPosition();
        final float x = inEditor ? pos.x : GameScreen.toWindowPosX(pos.x);
        final float y = inEditor ? pos.y : GameScreen.toWindowPosY(pos.y);
        renderBatch.drawText("Pos: " + pos.x + " : " + pos.y, x, y, false, true, true);

        getCollider().debugRender(inEditor, renderBatch.debugBatch);
    }

    //TODO make another method, maybe uze Z?
    public int getRenderPriority() {
        final SpriteComponent controller = getComponent(SpriteComponent.class);
        if (controller == null) return -1;

        return controller.getRenderPriority();
    }

    public GameObject copy() {
        final String json = SerializeUtil.GSON.toJson(this);
        return SerializeUtil.GSON.fromJson(json, GameObject.class);
    }
}
