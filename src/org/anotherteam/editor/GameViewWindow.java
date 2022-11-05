package org.anotherteam.editor;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import org.anotherteam.Input;
import org.anotherteam.render.frame.RenderFrame;
import org.anotherteam.screen.GameScreen;

public final class GameViewWindow {

    public static final ImVec2 POS = new ImVec2();
    public static final ImVec2 WINDOW_SIZE = new ImVec2();

    public static void imgui(RenderFrame frame) {
        ImGui.pushStyleColor(ImGuiCol.WindowBg,100, 100, 100, 255);
        ImGui.pushAllowKeyboardFocus(false);
        ImGui.begin("Game Viewport",
                ImGuiWindowFlags.NoScrollbar |
                ImGuiWindowFlags.NoScrollWithMouse);

        if (ImGui.isWindowFocused() && ImGui.isWindowHovered()) {
            final ImGuiIO io = ImGui.getIO();
            io.setWantCaptureKeyboard(false);
            io.setWantCaptureMouse(false);
            io.setWantTextInput(false);
            io.setWantSetMousePos(false);
        }

        WINDOW_SIZE.set(getLargestSizeForViewport());
        final ImVec2 windowPos = getCenteredPositionForViewport(WINDOW_SIZE);

        ImGui.setCursorPos(windowPos.x, windowPos.y);

        POS.set(ImGui.getCursorScreenPos());
        POS.x -= ImGui.getScrollX();
        POS.y = GameScreen.RENDER_HEIGHT - (POS.y - ImGui.getScrollY());

        final float U0 = POS.x / GameScreen.RENDER_WIDTH;
        final float V0 = POS.y / GameScreen.RENDER_HEIGHT;
        final float U1 = (POS.x + WINDOW_SIZE.x) / GameScreen.RENDER_WIDTH;
        final float V1 = (POS.y - WINDOW_SIZE.y) / GameScreen.RENDER_HEIGHT;

        ImGui.image(frame.texture.getId(), WINDOW_SIZE.x, WINDOW_SIZE.y, U0, V0, U1, V1);

        ImGui.popAllowKeyboardFocus();
        ImGui.popStyleColor();
        ImGui.end();
    }

    private static ImVec2 getLargestSizeForViewport() {
        final ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / (16.0f / 9.0f);
        if (aspectHeight > windowSize.y) {
            // We must switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * (16.0f / 9.0f);
        }

        return new ImVec2(aspectWidth,  aspectHeight);
    }

    private static ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        final ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        final float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        final float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(),
                viewportY + ImGui.getCursorPosY());
    }

    public static boolean isMouseOnWindow() {
        if (Input.getMouseX() < POS.x || Input.getMouseX() > POS.x + WINDOW_SIZE.x) return false;
        if (Input.getMouseY() > POS.y || Input.getMouseY() < POS.y - WINDOW_SIZE.y) return false;
        return true;
    }

}