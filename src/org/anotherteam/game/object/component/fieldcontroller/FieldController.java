package org.anotherteam.game.object.component.fieldcontroller;

import lombok.Getter;

@Getter
public final class FieldController {

    private final UpdateField onSaveValue;
    private final String fieldName;
    private Object value;

    public FieldController(String fieldName, Object value, UpdateField onSaveValue) {
        this.fieldName = fieldName;
        this.value = value;
        this.onSaveValue = onSaveValue;
    }

    public void setValue(Object value) {
        this.value = value;
        onSaveValue.save(value);
    }

    public Class<?> getValueClass() {
        return value.getClass();
    }

    public interface UpdateField {
        void save(Object newValue);
    }
}
