package com.alphagen.studio.pianocontroller.filepath;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FilePathFactory {
    private static final Logger logger = LogManager.getLogger(FilePathFactory.class);
    private static FilePath filePath;

    public static FilePath getFilePath() {
        if (filePath == null) filePath = new FilePath();
        logger.info("Created FilePath");
        return filePath;
    }

    public static FilePath getFilePath(String basePath) {
        if (filePath == null) filePath = new FilePath(basePath);
        logger.info("Created FilePath with Given BasePath");
        return filePath;
    }
}
