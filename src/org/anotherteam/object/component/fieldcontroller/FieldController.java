package org.anotherteam.object.component.fieldcontroller;

public final class FieldController<T> {

    private final UpdateField<T> onSaveValue;

    private String fieldName;
    private T value;

    public FieldController(String fieldName, T value, UpdateField<T> onSaveValue) {
        this.fieldName = fieldName;
        this.value = value;
        this.onSaveValue = onSaveValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        onSaveValue.save(value);
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class<?> getValueClass() {
        return value.getClass();
    }

    public interface UpdateField<T> {
        void save(T newValue);
    }
}
