package org.anotherteam.render.screen;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class Camera {

    private final Vector3f position;
    private final Matrix4f projection;
    private final Matrix4f view;

    private final Vector3f temp = new Vector3f();
    private final Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);

    public Camera(int x, int y) {
        position = new Vector3f(x, y, 0.0f);
        projection = new Matrix4f();
        projection.setOrtho2D(
                0.0f, Screen.WIDTH,
                0.0f, Screen.HEIGHT);
        view = new Matrix4f();
    }

    public void setPosition(int x, int y){
        position.set(x, y, 0.0f);
    }

    public void addPosition(int x, int y){
        position.add(x, y, 0.0f);
    }

    @NotNull
    public Matrix4f getProjection() {
        return projection;
    }

    @NotNull
    public Matrix4f getViewMatrix() {
        return view.setLookAt(position, temp.set(position.x, position.y, -1.0f), up);
    }
}
