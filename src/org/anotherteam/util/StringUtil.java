package org.anotherteam.util;

import lombok.experimental.UtilityClass;

@UtilityClass
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
