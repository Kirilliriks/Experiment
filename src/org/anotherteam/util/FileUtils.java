package org.anotherteam.util;

import lombok.val;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public final class FileUtils {
    public static String loadAsString(String fileName) {
        val builder = new StringBuilder();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
