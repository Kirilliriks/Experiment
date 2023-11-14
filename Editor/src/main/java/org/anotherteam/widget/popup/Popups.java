package org.anotherteam.widget.popup;

import org.anotherteam.level.popup.LoadLevelPopup;
import org.anotherteam.level.popup.RenameLevelPopup;

import java.util.ArrayList;
import java.util.List;

public final class Popups {

    public static final List<Popup> popups = new ArrayList<>();
    public static final LoadLevelPopup LOAD_LEVEL = registerPopup(new LoadLevelPopup());
    public static final RenameLevelPopup RENAME_LEVEL = registerPopup(new RenameLevelPopup());

    public static void update() {
        for (final Popup popup : popups) {
            if (popup.isSelected()) {
                if (!popup.isOpened()) {
                    popup.call();
                } else {
                    popup.update();
                }
            }
        }
    }

    public static <T extends Popup> T registerPopup(T popup) {
        popups.add(popup);
        return popup;
    }

    public static void closeAll() {
        for (final Popup popup : popups) {
            popup.setSelected(false);
        }
    }
}
