package org.anotherteam.object.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.anotherteam.data.deserialization.room.gameobject.ComponentDeserializer;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.fieldcontroller.FieldController;
import org.anotherteam.object.component.type.player.PlayerController;
import org.anotherteam.object.component.type.sprite.SpriteController;
import org.anotherteam.object.component.type.state.StateController;
import org.anotherteam.object.component.type.transform.Transform;
import org.anotherteam.util.FileUtils;
import org.anotherteam.util.SerializeUtil;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Component {

    public static final List<Class<? extends Component>> components = new ArrayList<>();

    static {
        components.add(SpriteController.class);
        components.add(StateController.class);
        components.add(Transform.class);
        components.add(PlayerController.class);
    }

    protected GameObject ownerObject;
    protected boolean serializable;

    public Component() {
        ownerObject = null;
        serializable = false;
    }

    public void init() { }

    public void update(float dt) { }

    public void setOwnerObject(@NotNull GameObject ownerObject) {
        this.ownerObject = ownerObject;
    }

    @NotNull
    public GameObject getOwnerObject() {
        return ownerObject;
    }

    public void setDependencies() { }

    public boolean isSerializable() {
        return serializable;
    }

    public List<FieldController> getFields() {
        return null;
    }

    @Nullable
    protected <T extends Component> T getDependsComponent(Class<T> clazz) {
        return ownerObject.getComponent(clazz);
    }

    @Nullable
    public JsonElement serialize(JsonObject result) {
        return null;
    }

    public static <T extends Component> T create(@NotNull Class<T> componentClass) {
        try {
            return componentClass.cast(componentClass
                    .getDeclaredConstructor()
                    .newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            throw new LifeException("Unknown Component class " + componentClass.getSimpleName());
        }
    }
}
