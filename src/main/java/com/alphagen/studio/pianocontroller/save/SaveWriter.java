package com.alphagen.studio.pianocontroller.save;

import jfilesystem.file.JFSFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;

public class SaveWriter {
    private static final Logger logger = LogManager.getLogger(SaveWriter.class);
    private final boolean middleMouseScrollEnabled = false;
    private final boolean mouseMovementEnabled = false;
    private JFSFile jfsFile;

    public SaveWriter(JFSFile jfsFile) throws FileAlreadyExistsException {
        // TODO: create new file with filename and if filename exists, add a num to filename
        logger.info("Using filename: {}", jfsFile.getName());
        this.jfsFile = new JFSFile(jfsFile.getAbsolutePath());
    }

    public void writeSave() {
    }

    public void newSave() {

        try (PrintWriter printWriter = new PrintWriter(jfsFile)) {

            // Letters: LETTER_A - LETTER_Z
            char letter = 'A';
            for (int i = 0; i < 26; i++) {
                printWriter.println("LETTER_" + letter + "=");
                letter++;
            }
            printWriter.println();

            // Nums: NUM_0 - NUM_9
            for (int i = 0; i < 10; i++) {
                printWriter.println("NUM_" + i + "=");
            }
            printWriter.println();

            // Nums: NumPad_0-NumPad_9
            for (int i = 0; i < 10; i++) {
                printWriter.println("NUMPAD_" + i + "=");
            }
            printWriter.println();


            // Special Keys
            String[] specials = new String[]{"SPACE", "CONTROL", "SHIFT", "TAB", "ALT"};
            for (String key : specials) {
                printWriter.println("SPECIAL_" + key + "=");
            }
            printWriter.println();

            // Arrow Keys
            String[] arrows = new String[]{"UP", "DOWN", "LEFT", "RIGHT"};
            for (String key : arrows) printWriter.println("ARROW_" + key + "=");
            printWriter.println();

            // Mouse Buttons
            printWriter.println("MOUSE_CLICK_LEFT=");
            printWriter.println("MOUSE_CLICK_MIDDLE=");
            printWriter.println("MOUSE_CLICK_RIGHT=");
            printWriter.println();

            // Button Scroll
            if (middleMouseScrollEnabled) {
                printWriter.println("MOUSE_SCROLL_MIDDLE_UP=");
                printWriter.println("MOUSE_SCROLL_MIDDLE_DOWN=");
                printWriter.println();
            }

            // Mouse Movements
            if (mouseMovementEnabled) {
                printWriter.println("MOUSE_MOVEMENT_SENSITIVITY=");
                printWriter.println("MOUSE_MOVEMENT_LEFT=");
                printWriter.println("MOUSE_MOVEMENT_RIGHT=");
                printWriter.println("MOUSE_MOVEMENT_UP=");
                printWriter.println("MOUSE_MOVEMENT_DOWN=");
                printWriter.println();
            }

            logger.info("Printed all files");

        } catch (FileNotFoundException e) {
            logger.error("Unable to find File: {}", jfsFile.getAbsolutePath());
        }
    }
}
