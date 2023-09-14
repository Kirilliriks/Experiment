package org.anotherteam.game.object.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.component.fieldcontroller.FieldController;
import org.anotherteam.game.object.component.type.player.PlayerController;
import org.anotherteam.game.object.component.type.sprite.SpriteController;
import org.anotherteam.game.object.component.type.state.StateController;
import org.anotherteam.game.object.component.type.transform.Transform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
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
}
