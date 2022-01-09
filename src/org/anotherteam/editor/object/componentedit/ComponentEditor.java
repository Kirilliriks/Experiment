package org.anotherteam.editor.object.componentedit;

import lombok.val;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.SwitchButton;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.fieldcontroller.FieldController;

public final class ComponentEditor extends GUIElement {

    private final SwitchMenu fieldMenu;

    private Component component;

    public ComponentEditor(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);

        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);

        fieldMenu = new SwitchMenu(Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE,
                width - Editor.DEFAULT_BORDER_SIZE * 2, height - Editor.DEFAULT_BORDER_SIZE * 3,
                SwitchMenu.Type.DOUBLE, this);
        fieldMenu.setInvertedY(true);

        inverted = true;
        component = null;
    }

    public void setComponent(Component component) {
        this.component = component;
        for (final FieldController<?> field : component.getFields()) {
            if (field.getValueClass() == Boolean.class) {
                final SwitchButton button = fieldMenu.addButton(field.getFieldName());

            }
        }
    }
}
