package com.alphagen.studio.pianocontroller;

import com.alphagen.studio.pianocontroller.filepath.Settings;
import com.alphagen.studio.pianocontroller.save.SaveReader;
import javafx.application.Application;
import jfilesystem.JFileSystem;
import jfilesystem.file.JFSFile;
import jfilesystem.file.exception.JFSFileNotCreatedException;
import jfilesystem.file.exception.JSFFileNullException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Launcher {

    private static final Logger logger = LogManager.getLogger(Launcher.class);
    private static JFileSystem jFileSystem;

    public static void main(String[] args) {


//        try {
//            logger.info("Key: " + KeyEvent.class.getField("VK_A").getInt(null));
//            logger.info("Key: " + KeyEvent.VK_A);
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }


        try {
            String homeDir = System.getProperty("user.home");
            String basePath = homeDir + "/AppData/Local/alphagnfss/PianoController/0.1.0";
            logger.info(basePath);
            jFileSystem = new JFileSystem(basePath);
        } catch (JFSFileNotCreatedException e) {
            logger.error("Unable to create Base Folder");
        }

//        JFSFile jfsFile = new JFSFile(jFileSystem.getBasePath() + "/blossom.piano.txt");
//        try {
//            SaveWriter saveWriter = new SaveWriter(jfsFile);
//            saveWriter.newSave();
//        } catch (FileAlreadyExistsException e) {
//            logger.error("File already exists");
//        }

        try {
            SaveReader saveReader = new SaveReader(new JFSFile(jFileSystem.getBasePath() + "/blossom_1.piano.txt"));
            saveReader.readLetters();
//            saveReader.readNum();
//            saveReader.readNumPad();
//            saveReader.readSpecials();
//            saveReader.readMouseClicks();
//            saveReader.readMouseScrolls();
//            saveReader.readMouseMovement();
//            HashMap<String, Integer> hashMap = saveReader.getKeyMap();
        } catch (IOException e) {
            logger.error("Unable to get File: {}", new JFSFile(jFileSystem.getBasePath() + "/blossom_1.piano.txt").getAbsolutePath());
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
        } catch (JSFFileNullException e) {
            logger.error("JFSFile was found to be null");
        }
//        catch (KeyMapEmptyException e) {
//            logger.error("KeyMap has no piano key id values");
//        }

        logger.info("Launching Settings...");
        Settings settings = Settings.getInstance();

        logger.info("Launching App...");
        Application.launch(Main.class, args);
    }

//    public static JFileSystem getjFileSystem() {
//        return jFileSystem;
//    }
}
