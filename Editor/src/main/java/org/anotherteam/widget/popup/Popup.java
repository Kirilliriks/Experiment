package org.anotherteam.widget.popup;

import imgui.ImGui;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class Popup {

    protected final String id;
    protected boolean selected = false;
    protected boolean opened = false;

    public void call() {
        opened = true;

        ImGui.openPopup("####" + id);
    }

    public abstract void update();

    protected void close() {
        ImGui.closeCurrentPopup();

        opened = false;
        selected = false;
    }
}
