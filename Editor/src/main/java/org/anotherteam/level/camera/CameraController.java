package org.anotherteam.level.camera;

import org.anotherteam.Editor;
import org.anotherteam.game.Game;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.input.Input;
import org.anotherteam.screen.Screen;
import org.anotherteam.util.EditorInput;
import org.anotherteam.util.ImGuiUtils;
import org.joml.Vector2f;
import org.joml.Vector2i;

public final class CameraController {

    private final Vector2f lastPos = new Vector2f(0, 0);
    private final Vector2f impulse = new Vector2f(0, 0);

    public void center() {
        final Game game = Editor.getInstance().getGame();
        if (game == null) {
            return;
        }

        final GameObject player = game.getLevelManager().getRoom().getPlayer();
        final Vector2i position = player.getPosition();
        Screen.camera.setPosition(position.x, position.y + (player.getCollider().getMax().y / 1.5f));
    }

    public void handle(float dt) {
        if (Screen.inGameWindowMouseX() == -1 || Screen.inGameMouseY() == -1) {
            lastPos.set(0, 0);
            return;
        }

        if (EditorInput.isKeyDown(Input.KEY_SPACE)) {
            Editor.saveLevel();
            Editor.switchPlayMode();
            return;
        }

        final float mouseWheelVelocity = Input.getMouseWheelVelocity();
        if (mouseWheelVelocity != 0.0F && !ImGuiUtils.imGuiWantMouse()) {
            if (mouseWheelVelocity < 0) {
                Screen.width += Screen.getWindow().getAspect() * 2;
                Screen.height += Screen.getWindow().getRatio() * 2;
            } else {
                Screen.width -= Screen.getWindow().getAspect() * 2;
                Screen.height -= Screen.getWindow().getRatio() * 2;
            }

            if (Screen.width < Screen.DEFAULT_WIDTH) {
                Screen.width = Screen.DEFAULT_WIDTH;
            }
            if (Screen.height < Screen.DEFAULT_HEIGHT) {
                Screen.height = Screen.DEFAULT_HEIGHT;
            }

            Editor.getInstance().getGame().getRender().updateFrames(Screen.width, Screen.height);
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
            impulse.x += Screen.width * (diffX / (float) Screen.getWindow().getWidth());
            impulse.y += Screen.height * (diffY / (float) Screen.getWindow().getHeight());
            lastPos.set(Input.getMousePos());
        } else {
            lastPos.set(0, 0);
        }

        Screen.camera.addPosition(impulse.x, impulse.y);
        impulse.zero();
    }
}
