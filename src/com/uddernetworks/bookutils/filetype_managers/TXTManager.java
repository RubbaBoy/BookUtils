package com.uddernetworks.bookutils.filetype_managers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TXTManager {

    private final String name;
    private final ArrayList<String> contents;

    public TXTManager(String name, ArrayList<String> contents) {
        this.name = name;
        this.contents = contents;
    }

    public boolean writeTXT() {
        try {
            new File("plugins", "SavedBooks").mkdirs();
            Files.write(Paths.get("plugins" + File.separator + "SavedBooks" + File.separator + name + ".txt"), contents, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
