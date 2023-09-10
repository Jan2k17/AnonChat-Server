package de.jan2k17.AnonChat.Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Functions {
    public static void writeToFile(String text) {
        try {
            String path = System.getProperty("user.dir");
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(String.valueOf(path) + "/Chat-" + ChatServer.port + ".log"), true));
            bw.write(text);
            bw.newLine();
            bw.close();
        } catch (Exception exception) {}
    }
}
