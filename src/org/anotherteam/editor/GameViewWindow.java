package org.anotherteam.editor;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.anotherteam.render.frame.RenderFrame;
import org.anotherteam.screen.GameScreen;

public final class GameViewWindow {

    public static void imgui(RenderFrame frame) {
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);

        final float U0 = windowPos.x / GameScreen.RENDER_WIDTH;
        final float V0 = windowPos.y / GameScreen.RENDER_HEIGHT;
        final float U1 = (windowPos.x + windowSize.x) / GameScreen.RENDER_WIDTH;
        final float V1 = (windowPos.y + windowSize.y) / GameScreen.RENDER_HEIGHT;

        ImGui.image(frame.texture.getId(), windowSize.x, windowSize.y, U0, V1, U1, V0);

        ImGui.end();
    }

    private static ImVec2 getLargestSizeForViewport() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        //windowSize.x -= ImGui.getScrollX();
        //windowSize.y -= ImGui.getScrollY();

//        float aspectWidth = windowSize.x;
//        float aspectHeight = aspectWidth / (16.0f / 9.0f);
//        if (aspectHeight > windowSize.y) {
//            // We must switch to pillarbox mode
//            aspectHeight = windowSize.y;
//            aspectWidth = aspectHeight * (16.0f / 9.0f);
//        }

        return new ImVec2(windowSize.x,  windowSize.y);
    }

    private static ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        //windowSize.x -= ImGui.getScrollX();
        //windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(),
                viewportY + ImGui.getCursorPosY());
    }
}