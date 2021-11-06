package org.anotherteam.render.screen;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class Camera {
    private static final Vector3f temp = new Vector3f();
    private static final Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);

    private final Vector3f position;
    private final Matrix4f projection;
    private final Matrix4f view;

    private int projectionWidth;
    private int projectionHeight;

    public Camera() {
        this(0, 0, 0,0);
    }

    public Camera(int x, int y, int width, int height) {
        position = new Vector3f(x, y, 0.0f);
        view = new Matrix4f();
        projection = new Matrix4f();
        setProjection(width, height);
    }

    public void setPosition(float x, float y){
        position.set(x, y, 0.0f);
    }

    public void addPosition(float x, float y){
        position.add(x, y, 0.0f);
    }

    public void setProjection(int width, int height) {
        projectionWidth = width;
        projectionHeight = height;
        projection.setOrtho2D(
                -width / 2.0f, width / 2.0f,
                -height / 2.0f, height / 2.0f);
    }

    @NotNull
    public Matrix4f getProjection() {
        return projection;
    }

    @NotNull
    public Vector3f getPosition() {
        return position;
    }

    @NotNull
    public Matrix4f getViewMatrix() {
        return view.setLookAt(position, temp.set(position.x, position.y, -1.0f), up);
    }

    public int translateX(int x) {
        return (int) (x - position.x + projectionWidth / 2.0f);
    }

    public int translateY(int y) {
        return (int) (y - position.y + projectionHeight / 2.0f);
    }
}
