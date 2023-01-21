package org.anotherteam.editor.render;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.anotherteam.editor.Editor;

public final class ImGuiRender {

    public static final String BACKEND_PLATFORM = "imgui_java_impl_glfw";

    public static final boolean cyrillic = false;

    private final Editor editor;
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public ImGuiRender(long windowPtr, String glslVersion, Editor editor) {
        this.editor = editor;

        ImGui.createContext();
        final ImGuiIO io = imgui.ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.addBackendFlags(ImGuiBackendFlags.HasMouseCursors);
        io.setBackendPlatformName(BACKEND_PLATFORM);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        if (cyrillic) {
            io.getFonts().addFontFromFileTTF("C:\\Windows\\Fonts\\Arial.ttf", 20, io.getFonts().getGlyphRangesCyrillic());
        }


        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init(glslVersion);
    }

    public void render(float dt) {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        editor.update(dt);

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    public void destroy() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }
}
