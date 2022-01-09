package org.anotherteam.object.component.fieldcontroller;

public final class FieldController {

    private final UpdateField onSaveValue;

    private String fieldName;
    private Object value;

    public FieldController(String fieldName, Object value, UpdateField onSaveValue) {
        this.fieldName = fieldName;
        this.value = value;
        this.onSaveValue = onSaveValue;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
        onSaveValue.save(value);
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class<?> getValueClass() {
        return value.getClass();
    }

    public interface UpdateField {
        void save(Object newValue);
    }
}
