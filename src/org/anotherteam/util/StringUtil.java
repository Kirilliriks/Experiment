package org.anotherteam.util;

public final class StringUtil {

    public static boolean isNumeric(String str) {
        for (final char ch : str.toCharArray()) {
            if (!CharUtil.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }
}
