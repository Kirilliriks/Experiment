package org.anotherteam.util;

public final class Color {

    public int r, g, b, a;

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static void toColor(Color color, int value) {
        color.r = ((value & 0xff000000) >>> 24);
        color.g = ((value & 0x00ff0000) >>> 16);
        color.b = ((value & 0x0000ff00) >>> 8);
        color.a = ((value & 0x000000ff));
    }

    public static int fromRGBA(int r, int g, int b, int a) {
        return  (r << 24) +
                (g << 16) +
                (b << 8) +
                (a << 0);
    }
}
