package org.anotherteam.editor.gui.window;

import org.anotherteam.Input;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.object.objectedit.GameObjectEditor;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;

public final class ComponentSelectWindow extends DialogWindow {

    private final SwitchMenu selector;

    public ComponentSelectWindow(int width, int height, GameObject gameObject, GameObjectEditor gameObjectEditor) {
        super(width, height);

        selector = new SwitchMenu(0, 0,
                width, height,
                SwitchMenu.Type.DOUBLE, this);

        for (final var clazz : Component.components) {
            selector.addButton(clazz.getSimpleName(),
                    ()-> {
                        final Component component = Component.create(clazz);
                        gameObject.addComponent(component);
                        Editor.closeWindow();
                        gameObjectEditor.fillComponentSelector();
                    });
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (Input.isKeyPressed(Input.KEY_ESCAPE)) {
            Editor.closeWindow();
        }
    }
}
