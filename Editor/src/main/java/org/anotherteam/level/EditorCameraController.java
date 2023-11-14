package org.anotherteam.level;

import org.anotherteam.Editor;
import org.anotherteam.input.Input;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.EditorInput;
import org.anotherteam.util.FileUtils;
import org.anotherteam.util.ImGuiUtils;
import org.joml.Vector2f;

public final class EditorCameraController {

    private static final float DRAG_SCALE = 0.2f;

    private final Vector2f lastPos = new Vector2f(0, 0);
    private final Vector2f impulse = new Vector2f(0, 0);

    public void handle(float dt) {
        if (GameScreen.inGameWindowMouseX() == -1 || GameScreen.inGameMouseY() == -1) {
            lastPos.set(0, 0);
            return;
        }

        if (EditorInput.isKeyDown(Input.KEY_SPACE)) {
            FileUtils.saveEditorLevel(Editor.getInstance().getGame().getLevelManager().getCurrent());
            Editor.switchPlayMode();
            return;
        }

        final float mouseWheelVelocity = Input.getMouseWheelVelocity();
        if (mouseWheelVelocity != 0.0F && !ImGuiUtils.imGuiWantMouse()) {
            if (mouseWheelVelocity < 0) {
                GameScreen.WIDTH += GameScreen.getWindow().getAspect() * 2;
                GameScreen.HEIGHT += GameScreen.getWindow().getRatio() * 2;
            } else {
                GameScreen.WIDTH -= GameScreen.getWindow().getAspect() * 2;
                GameScreen.HEIGHT -= GameScreen.getWindow().getRatio() * 2;
            }

            if (GameScreen.WIDTH < GameScreen.DEFAULT_WIDTH) {
                GameScreen.WIDTH = GameScreen.DEFAULT_WIDTH;
            }

            if (GameScreen.HEIGHT < GameScreen.DEFAULT_HEIGHT) {
                GameScreen.HEIGHT = GameScreen.DEFAULT_HEIGHT;
            }

            Editor.getInstance().getGame().getRender().updateFrames(GameScreen.WIDTH, GameScreen.HEIGHT);
        }
        
        float speed = 125.0f;

        if (EditorInput.isKeyDown(Input.KEY_SHIFT)) {
            speed = 225.0f;
        }

        if (EditorInput.isKeyDown(Input.KEY_A)) {
            impulse.x -= speed * dt;
        }
        if (EditorInput.isKeyDown(Input.KEY_D)) {
            impulse.x += speed * dt;
        }
        if (EditorInput.isKeyDown(Input.KEY_W)) {
            impulse.y += speed * dt;
        }
        if (EditorInput.isKeyDown(Input.KEY_S)) {
            impulse.y -= speed * dt;
        }

        if (EditorInput.isButtonDown(Input.MOUSE_MIDDLE_BUTTON)) {
            if (lastPos.equals(0, 0)) {
                lastPos.set(Input.getMousePos());
                return;
            }

            final float diffX = (float) Math.floor(lastPos.x - Input.getMouseX());
            final float diffY = (float) Math.floor(lastPos.y - Input.getMouseY());
            impulse.x += GameScreen.WIDTH * (diffX / (float) GameScreen.getWindow().getWidth());
            impulse.y += GameScreen.HEIGHT * (diffY / (float) GameScreen.getWindow().getHeight());
            lastPos.set(Input.getMousePos());
        } else {
            lastPos.set(0, 0);
        }

        GameScreen.GAME_CAMERA.addPosition(impulse.x, impulse.y);
        impulse.zero();
    }
}
