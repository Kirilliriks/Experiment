package org.anotherteam.editor.object.componentedit;

import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.FieldMenu;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.gui.text.input.InputPart;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.fieldcontroller.FieldController;

public final class ComponentEditor extends GUIElement {

    private final FieldMenu fieldMenu;

    private Component component;

    public ComponentEditor(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);

        final var editor = Editor.inst();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);

        fieldMenu = new FieldMenu(Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE,
                width - Editor.DEFAULT_BORDER_SIZE * 2, height - Editor.DEFAULT_BORDER_SIZE * 3,
                SwitchMenu.Type.DOUBLE, this);
        fieldMenu.setInvertedY(true);

        inverted = true;
        component = null;
    }

    public void setComponent(Component component) {
        fieldMenu.clearChild();
        if (component == null || component.getFields() == null) return;

        this.component = component;

        for (final FieldController field : component.getFields()) {
            final InputPart button = fieldMenu.addButton(field.getFieldName());
            button.setField(field);
        }
    }
}
