package com.alphagen.studio.pianocontroller.filepath;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Settings {

    public static final String INTERNAL_VERSION = "2.1.0.1.0";
    private static final Logger logger = LogManager.getLogger(Settings.class);
    private static Settings settings;
    private final FilePath filepath;

    public Settings() {
        logger.info("Getting FilePath");
        filepath = FilePathFactory.getFilePath();
        logger.info("Retrieved FilePath");
    }

    public static Settings getInstance() {
        logger.info("Getting Settings");
        if (settings == null) settings = new Settings();
        logger.info("Returning Settings");
        return settings;
    }
}
