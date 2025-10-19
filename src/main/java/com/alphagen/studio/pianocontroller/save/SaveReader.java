package com.alphagen.studio.pianocontroller.save;

import com.alphagen.studio.pianocontroller.Launcher;
import jfilesystem.JFileSystem;
import jfilesystem.file.JFSFile;
import jfilesystem.file.exception.JSFFileNullException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class SaveReader {

    private static final Logger logger = LogManager.getLogger(SaveReader.class);
    private final JFSFile jfsFile;

    // TODO: store all properties in a file for editing/save backup/save as
    // HasMap<PianoKey, KeyboardKey>
//    private final HashMap<String, Integer> rawMap;
    private final HashMap<Integer, Integer> keyMap;
    private final Properties properties;

    private final HashMap<Integer, Integer> mouseMap;

    public SaveReader(JFSFile jfsFile) throws IOException, JSFFileNullException {
        if (jfsFile == null || jfsFile.length() == 0) throw new JSFFileNullException();
        this.jfsFile = jfsFile;

//        rawMap = new HashMap<>();
        keyMap = new HashMap<>();
        mouseMap = new HashMap<>();
        properties = new Properties();

        FileInputStream fileInputStream = new FileInputStream(jfsFile); // create a file input stream to read key and value pairs
        properties.load(fileInputStream); // create the key and propertie pairs
        logger.info("SaveReader is ready");
    }

    private String getValue(String key) {
        if (!properties.containsKey(key)) return null;
        String value = properties.get(key).toString();
        if (!(value == null || value.isBlank())) {
            System.out.print("key: " + key);
            System.out.print(", value: " + value);
            System.out.println(" > \"" + value + "\"");
            return value;
        }
        return null;
    }

    public void readAll() throws NoSuchFieldException, IllegalAccessException {
        readLetters();
        readNum();
        readNumPad();
        readSpecials();
        readMouseClicks();
        readMouseScrolls();
        readMouseMovement();
    }

    public void readLetters() {

        logger.info("Reading letters");
        for (char letter = 'A'; letter <= 'Z'; letter++) {

            // Get key letter_value
            String valueString = properties.get("LETTER_" + letter).toString();
            boolean validValue = isValueValid(valueString);

            if (validValue) {
                try {
                    int value = Integer.parseInt(valueString);
                    int key = KeyEvent.class.getField("VK_" + letter).getInt(null); // get the VK_{letter} of letter
                    logger.info("Letter: " + letter + ", Key: " + key + ", Value: " + value);
                    keyMap.put(key, value);
                } catch (IllegalAccessException e) {
                    logger.error(e);
                    logger.error("Unable to access value for: " + letter);
                } catch (NoSuchFieldException e) {
                    logger.error(e);
                    logger.error("Field does not exist for: " + letter);
                }
            }

        }
    }

    private boolean isValueValid(String valueString) {
        return !(valueString == null) && !valueString.trim().isBlank() && !valueString.isEmpty() && valueString.matches("\\d+");
    }

    public void readNum() {
        for (int num = 0; num < 10; num++) {

            // Get key letter_value
            String valueString = properties.get("NUM_" + num).toString();
            boolean validValue = isValueValid(valueString);

            if (validValue) {
                try {
                    int value = Integer.parseInt(valueString);
                    int key = KeyEvent.class.getField("VK_" + num).getInt(null); // get the VK_{number} of number
                    logger.info("Num: " + num + ", Key: " + key + ", Value: " + value);
                    keyMap.put(key, value);
                } catch (IllegalAccessException e) {
                    logger.error(e);
                    logger.error("Unable to access value for: " + num);
                } catch (NoSuchFieldException e) {
                    logger.error(e);
                    logger.error("Field does not exist for: " + num);
                }
            }
        }
    }

    public void readNumPad() {
        for (int numPadNum = 0; numPadNum < 10; numPadNum++) {

            // Get key letter_value
            String valueString = properties.get("NUMPAD_" + numPadNum).toString();
            boolean validValue = isValueValid(valueString);

            if (validValue) {
                try {
                    int value = Integer.parseInt(valueString);
                    int key = KeyEvent.class.getField("VK_NUMPAD" + numPadNum).getInt(null); // get the VK_{number} of number
                    logger.info("NumPadNum: " + numPadNum + ", Key: " + key + ", Value: " + value);
                    keyMap.put(key, value);
                } catch (IllegalAccessException e) {
                    logger.error(e);
                    logger.error("Unable to access value for: " + numPadNum);
                } catch (NoSuchFieldException e) {
                    logger.error(e);
                    logger.error("Field does not exist for: " + numPadNum);
                }
            }
        }
    }

    public void readSpecials() {

        String[] specials = new String[]{"SPACE", "CONTROL", "SHIFT", "TAB", "ALT"};

        for (String keyVal : specials) {


            try {


                // Get key letter_value
                String valueString = properties.get("SPECIAL_" + keyVal).toString();
                if (valueString == null) continue;
                boolean validValue = isValueValid(valueString);
                if (validValue) {

                    int value = Integer.parseInt(valueString);
                    int key = KeyEvent.class.getField("VK_" + keyVal).getInt(null); // get the VK_{number} of number
                    logger.info("Modifier: " + keyVal + ", Key: " + key + ", Value: " + value);
                    keyMap.put(key, value);
                }
            } catch (IllegalAccessException e) {
                logger.error("Unable to access value for: " + keyVal);
                logger.error(e);
            } catch (NoSuchFieldException e) {
                logger.error("Field does not exist for: " + keyVal);
                logger.error(e);
            } catch (NullPointerException e) {
                logger.error("Field is null");
                logger.error(e);
            }
        }
    }

    public void readMouseClicks() {
        String[] clicks = new String[]{"LEFT", "MIDDLE", "RIGHT"};

        for (int i = 0; i < clicks.length; i++) {

            // Get key letter_value
            String valueString = properties.get("MOUSE_CLICK_" + clicks[i]).toString();
            boolean validValue = isValueValid(valueString);

            if (validValue) {
                try {
                    int value = Integer.parseInt(valueString);
                    int key = MouseEvent.class.getField("BUTTON" + (i + 1)).getInt(null); // get the VK_{number} of number
                    logger.info("Mouse Button: " + clicks[i] + ", Key: " + key + ", Value: " + value);
                    mouseMap.put(key, value);
                } catch (IllegalAccessException e) {
                    logger.error("Unable to access value for: " + clicks[i]);
                    logger.error(e);
                } catch (NoSuchFieldException e) {
                    logger.error("Field does not exist for: " + clicks[i]);
                    logger.error(e);
                }
            }
        }

    }

    public void readMouseScrolls() {
        String[] scrolls = new String[]{"MIDDLE_UP", "MIDDLE_DOWN"};

        for (String key : scrolls) {
            String value = getValue("MOUSE_SCROLL_" + key);
            logger.debug("Number: " + value);
        }


    }

    public void readMouseMovement() {
        String[] movements = new String[]{"LEFT", "RIGHT", "UP", "DOWN", "SENSITIVITY"};

        for (String key : movements) {
            String value = getValue("MOUSE_MOVEMENT_" + key);
            logger.debug("Number: " + value);
        }


    }

//    public HashMap<String, Integer> getKeyMap() throws KeyMapEmptyException {
//        logger.info("KeyMap size: " + rawMap.size());
//        if (rawMap.size() == 0) throw new KeyMapEmptyException();
//        return rawMap;
//    }

    /**
     * This method takes in the properties that are not null (hopefully) and puts it in a
     * file in root project folder. whenever, the file is edited, save backup/as, the properties
     * will be written in this file.
     */
    public void currentSave() {
        try {
            JFileSystem jFileSystem = Launcher.getjFileSystem();
            FileOutputStream fileOutputStream = new FileOutputStream(jFileSystem.getBasePath() + "/currentFile.piano.txt");
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            logger.error(e);
            logger.error("Unable to get JFSFile's Location");
        }
    }

    public HashMap<Integer, Integer> getKeyMap() {
        return keyMap;
    }

    public HashMap<Integer, Integer> getMouseMap() {
        return mouseMap;
    }
}
