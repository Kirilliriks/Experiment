package org.anotherteam.object;

import org.anotherteam.Game;
import org.anotherteam.level.room.Room;
import org.anotherteam.logger.GameLogger;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.type.collider.Collider;
import org.anotherteam.object.component.type.sprite.SpriteController;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.SerializeUtil;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.*;

public class GameObject {

    protected Room room;

    protected final List<Component> components;
    protected final Vector2i position;
    protected String name;

    /**
     * All GameObjects have a Collider component (index 0) {Default size 32 x 32}
     */

    public GameObject(int x, int y) {
        this(x, y, "unnamed");
    }

    public GameObject(int x, int y, String name) {
        this.name = name;
        room = null;
        position = new Vector2i(x, y);
        components = new ArrayList<>();
        addComponent(new Collider());
    }

    public static <T extends GameObject> T create(@NotNull Class<T> gameObjectClass) {
        return create(0, 0, "unnamed", gameObjectClass);
    }

    public static <T extends GameObject> T create(int x, int y, String name, @NotNull Class<T> gameObjectClass) {
        try {
            final var gameObject = gameObjectClass.cast(gameObjectClass
                    .getDeclaredConstructor(int.class, int.class)
                    .newInstance(x, y));
            gameObject.setName(name);
            return gameObject;
        } catch (Exception exception) {
            exception.printStackTrace();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public Collider getCollider() {
        return (Collider) components.get(0);
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
            if (componentClass.isAssignableFrom(component.getClass())) return componentClass.cast(component);
        }
        return null;
    }

    public <T extends Component> int getComponentIndex(Class<T> componentClass) {
        for (int i = 0 ; i < components.size(); i++) {
            final Component component = components.get(i);
            if (componentClass.isAssignableFrom(component.getClass())) return i;
        }
        return -1;
    }

    public <T extends Component> void addComponent(T component) {

        final int index = getComponentIndex(component.getClass());
        if (index != -1) {
            components.set(index, component);
        } else {
            components.add(component);
        }

        component.setOwnerObject(this);
        component.setDependencies(); // TODO уже вызывается в Prepare, может можно отказаться?*
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
        final SpriteController spriteComponent = getComponent(SpriteController.class);
        if (spriteComponent != null) {
            spriteComponent.draw(position, renderBatch, height);
        }
    }

    public void debugDraw(RenderBatch renderBatch, boolean inEditor) {
        if (!Game.DEBUG_MODE) return;

        final Vector2i pos = getPosition();
        final float x = inEditor ? pos.x : GameScreen.toWindowPosX(pos.x);
        final float y = inEditor ? pos.y : GameScreen.toWindowPosY(pos.y);
        renderBatch.drawText("Pos: " + pos.x + " : " + pos.y, x, y, false, true, true);

        getCollider().debugRender(inEditor, renderBatch.debugBatch);
    }

    //TODO make another method, maybe uze Z?
    public int getRenderPriority() {
        final SpriteController controller = getComponent(SpriteController.class);
        if (controller == null) return -1;

        return controller.getRenderPriority();
    }

    public GameObject copy() {
        final String json = SerializeUtil.GSON.toJson(this);
        return SerializeUtil.GSON.fromJson(json, GameObject.class);
    }
}
