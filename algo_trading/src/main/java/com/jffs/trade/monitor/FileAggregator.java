package com.jffs.trade.monitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileAggregator {
    private final File file;

    public FileAggregator(File file) {
        this.file = file;
    }

    public void write(String message) throws IOException {
        try(var fileWriter = new FileWriter(file, true)) {
            fileWriter.write(message);
        }
    }

    public File getFile() {
        return file;
    }
}
