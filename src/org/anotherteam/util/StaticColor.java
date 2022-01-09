package org.anotherteam.util;

public class StaticColor extends Color {

    public StaticColor(int r, int g, int b) {
        super(r, g, b);
    }

    @Override
    public void set(Color color) {
        throw new RuntimeException("Trying change immutable static color");
    }

    @Override
    public void set(int r, int g, int b) {
        throw new RuntimeException("Trying change immutable static color");
    }

    @Override
    public void set(int r, int g, int b, int a) {
        throw new RuntimeException("Trying change immutable static color");
    }
}
