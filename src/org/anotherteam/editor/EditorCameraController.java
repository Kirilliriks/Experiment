package org.anotherteam.editor;

import org.anotherteam.Input;
import org.anotherteam.screen.GameScreen;
import org.joml.Vector2f;

public final class EditorCameraController {

    private static final float DRAG_SCALE = 0.2f;

    private final Vector2f lastPos = new Vector2f(0, 0);

    public void handle(float dt) {
        float speed = 75.0f;
        float moveX = 0, moveY = 0;

        if (Input.isKeyDown(Input.KEY_SHIFT)) {
            speed = 125.0f;
        }

        if (Input.isKeyDown(Input.KEY_W)) {
            moveY += speed * dt;
        }
        if (Input.isKeyDown(Input.KEY_S)) {
            moveY -= speed * dt;
        }
        if (Input.isKeyDown(Input.KEY_A)) {
            moveX -= speed * dt;
        }
        if (Input.isKeyDown(Input.KEY_D)) {
            moveX += speed * dt;
        }

        if (GameScreen.draggedThing != null ||
           (GameScreen.inGameWindowMouseX() == -1 || GameScreen.inGameMouseY() == -1)) {
            lastPos.set(0, 0);
            return;
        }
        if (Input.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (lastPos.equals(0, 0)) {
                lastPos.set(Input.getMousePos());
                return;
            }

            final float diffX = lastPos.x - Input.getMouseX();
            final float diffY = lastPos.y - Input.getMouseY();
            moveX += diffX * DRAG_SCALE;
            moveY += diffY * DRAG_SCALE;
            lastPos.set(Input.getMousePos());
        } else lastPos.set(0, 0);

        GameScreen.gameCamera.addPosition(moveX, moveY);
    }
}
