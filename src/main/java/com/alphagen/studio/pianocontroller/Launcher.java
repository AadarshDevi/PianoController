package com.alphagen.studio.pianocontroller;

import com.alphagen.studio.pianocontroller.filepath.Settings;
import javafx.application.Application;
import jfilesystem.JFileSystem;
import jfilesystem.file.exception.JFSFileNotCreatedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Launcher {

    private static final Logger logger = LogManager.getLogger(Launcher.class);
    private static JFileSystem jFileSystem;

    public static void main(String[] args) {

        try {
            String homeDir = System.getProperty("user.home");
            String basePath = homeDir + "/AppData/Local/alphagnfss/PianoController/0.1.0";
            logger.info(basePath);
            jFileSystem = new JFileSystem(basePath);
        } catch (JFSFileNotCreatedException e) {
            logger.error("Unable to create Base Folder");
            Main.quit();
        }

        logger.info("Launching Settings...");
        Settings.getInstance();

        logger.info("Launching App...");
        Application.launch(Main.class, args);
    }

    public static JFileSystem getjFileSystem() {
        return jFileSystem;
    }
}
