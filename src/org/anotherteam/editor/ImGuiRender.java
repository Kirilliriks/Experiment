package org.anotherteam.editor;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public final class ImGuiRender {

    public static final String BACKEND_PLATFORM = "imgui_java_impl_glfw";

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

        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init(glslVersion);
    }

    public void imgui(float dt) {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        editor.update(dt);

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    public void destroy() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }
}
