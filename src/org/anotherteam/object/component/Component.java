package org.anotherteam.object.component;

import org.anotherteam.object.GameObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Component {

    protected GameObject ownerObject;
    protected boolean serializable = false;

    public Component() {
        ownerObject = null;
    }

    public void instanceBy(Component component) { }

    public void setOwnerObject(@NotNull GameObject ownerObject) {
        this.ownerObject = ownerObject;
    }

    @NotNull
    public GameObject getOwnerObject() {
        return ownerObject;
    }

    public void setDependencies() { }

    public void init() { }

    public void update(float dt) { }

    public boolean isSerializable() {
        return serializable;
    }

    @Nullable
    protected <T extends Component> T getDependsComponent(Class<T> clazz) {
        return ownerObject.getComponent(clazz);
    }
}
