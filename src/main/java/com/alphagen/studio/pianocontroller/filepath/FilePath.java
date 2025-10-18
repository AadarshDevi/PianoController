package com.alphagen.studio.pianocontroller.filepath;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class FilePath {

    private static final Logger logger = LogManager.getLogger(FilePath.class);
    private String basePath;
    private String logPath;

    public FilePath() {
        String user = System.getProperty("user.name");

        basePath = "C:/Users/" + user + "/AppData/Local/alphagnfss/PianoController/" + Settings.INTERNAL_VERSION;
        try {
            generateFolder(basePath);
        } catch (IOException e) {
            logger.error("Unable to generate BaseFolder");
        }
        logPath = basePath + "/log";
        try {
            generateFolder(logPath);
        } catch (IOException e) {
            logger.error("Unable to generate LogFolder");
        }
        System.setProperty("logPath", logPath);
        logger.info("Set Logs' Location");
    }

    public FilePath(String basePath) {
        this.basePath = basePath;
    }

    public static File generateLogFile() {
        return null;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public boolean generateFolder(String folderPath) throws IOException {
        File directory = new File(folderPath);
        if (directory.exists() && directory.isDirectory()) return true;
        boolean directoryCreated = directory.mkdirs();
        logger.info("Generated Folder: {} > {}", directory.getName(), directoryCreated);
        if (!directoryCreated) throw new IOException();
        return directoryCreated;
    }

    public File generateFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            logger.info("File: {} exists", file.getName());
            return file;
        }
        boolean success = file.createNewFile();
        logger.info("Generated File: {} > {}", file.getName(), success);
        if (!success) throw new IOException();
        return file;
    }
}
