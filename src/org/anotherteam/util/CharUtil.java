package org.anotherteam.util;

import lombok.experimental.UtilityClass;
import org.lwjgl.glfw.GLFW;

@UtilityClass
public final class CharUtil {

    public static boolean isPrintable(int key) {
        return isLetterOrDigit(key) || isSpecialSymbol(key);
    }

    public static boolean isSpecialSymbol(int key) {
        return (key >= ' ' && key <= '/') || (key >= ':' && key <= '@') || (key >= '[' && key < 'a') || (key >= '{' && key <= '~');
    }

    public static boolean isLetterOrDigit(int key) {
        return isLetter(key) || isDigit(key);
    }

    public static boolean isLetter(int key) {
        return (key >= 'A' && key <= 'Z') || (key >= 'a' && key <= 'z');
    }

    public static boolean isDigit(int key) {
        return key >= '0' && key <= '9';
    }

     //TODO maybe find correct solve?
     public static int transformKeyCode(int key, final int mods) {
        if (CharUtil.isLetter(key)) {
            if (mods != GLFW.GLFW_MOD_SHIFT && mods != GLFW.GLFW_MOD_CAPS_LOCK) {
                key += 32;
            }
        } else if (CharUtil.isDigit(key)) {
            if (mods == GLFW.GLFW_MOD_SHIFT || mods == GLFW.GLFW_MOD_CAPS_LOCK) {
                if (key >= '1' && key <= '5') {
                    if (key == '2')
                        key = '@';
                    else
                        key -= 16;
                } else if (key == '6') {
                    key = '^';
                } else if (key == '7') {
                    key = '&';
                } else if (key == '8') {
                    key = '*';
                } else if (key == '9') {
                    key = '(';
                } else if (key == '0') {
                    key = ')';
                }
            }
        } else if (mods == GLFW.GLFW_MOD_SHIFT || mods == GLFW.GLFW_MOD_CAPS_LOCK) {
                if (key == '-') {
                    key = '_';
                } else if (key == '=') {
                    key = '+';
                } else if (key == '[') {
                    key = '{';
                } else if (key == ']') {
                    key = '}';
                } else if (key == '\\') {
                    key = '|';
                } else if (key == ';') {
                    key = ':';
                } else if (key == '\'') {
                    key = '"';
                } else if (key == ',') {
                    key = '<';
                } else if (key == '.') {
                    key = '>';
                } else if (key == '/') {
                    key = '?';
                } else if (key == '`') {
                    key = '~';
                }
        }
        return key;
    }

    public static char toAnotherCase(int key) {
        if (Character.isUpperCase(key)) {
            return (char) Character.toLowerCase(key);
        } else {
            return (char) Character.toUpperCase(key);
        }
    }
}
