package org.anotherteam.render.screen;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class Camera {
    private static final Vector3f temp = new Vector3f();
    private static final Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);

    private final Vector3f position;
    private final Matrix4f projection;
    private final Matrix4f view;

    public Camera(int x, int y, int width, int height) {
        position = new Vector3f(x, y, 0.0f);
        projection = new Matrix4f();
        projection.setOrtho2D(
                0.0f, width,
                0.0f, height);
        view = new Matrix4f();
    }

    public void setPosition(float x, float y){
        position.set(x, y, 0.0f);
    }

    public void addPosition(float x, float y){
        position.add(x, y, 0.0f);
    }

    public void setProjection(int width, int height) {
        projection.setOrtho2D(
                0.0f, width,
                0.0f, height);
    }

    @NotNull
    public Matrix4f getProjection() {
        return projection;
    }

    @NotNull
    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getMul(){
        val matr = new Matrix4f();
        matr.identity();
        return projection.mul(view, matr);
    }

    @NotNull
    public Matrix4f getViewMatrix() {
        return view.setLookAt(position, temp.set(position.x, position.y, -1.0f), up);
    }
}
