package com.alphagen.studio.pianocontroller.ui.controllers;

import com.alphagen.studio.pianocontroller.Main;
import com.alphagen.studio.pianocontroller.save.SaveReader;
import com.alphagen.studio.pianocontroller.save.SaveWriter;
import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfilesystem.file.JFSFile;
import jfilesystem.file.exception.JSFFileNullException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.midi.MidiDevice;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class MainFrameController {
    private static final Logger logger = LogManager.getLogger(MainFrameController.class);

    boolean selectMidiUIOpen;
    private File configFile;
    private MidiDevice midiDevice;

    @FXML
    private Label configFileNameLabel;
    @FXML
    private Button config_file_open;
    @FXML
    private Button config_file_create;
    @FXML
    private Button config_file_save;

    @FXML
    private Label midiDeviceNameLabel;
    @FXML
    private Button selectMidiDeviceButton;

    @FXML
    private Label ui_type_name;
    @FXML
    private Button ui_type_select;

    @FXML
    private Button app_run;

    @FXML
    public void initialize() {
        logger.info("Initialized");
        selectMidiUIOpen = false;

        // test
        configFile = new JFSFile("D:/piano_config_1.piano.txt");
        logger.info("Set Config file: " + configFile.getAbsolutePath());
        // test
    }

    @FXML
    public void openConfigFile() {
        File config = null;
        if (configFile == null) {
            logger.info("Open Config File Chooser");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Piano Config File");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Piano Config", "*.piano.txt"));
            config = fileChooser.showOpenDialog(null);
        }
        config = configFile; // test
        if (config != null) {
            this.configFile = config;
            logger.info("Loaded Config File: {}", config.getName());
            configFileNameLabel.setText(this.configFile.getName());
            try {
                SaveReader saveReader = new SaveReader(new JFSFile(configFile.getAbsolutePath()));
                saveReader.readLetters();
                saveReader.readNum();
                saveReader.readNumPad();
                saveReader.readSpecials();
                saveReader.readMouseClicks();
                saveReader.currentSave();
                System.exit(0);// test
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSFFileNullException e) {
                logger.error("Unable to get File");
            }
        }
    }

    @FXML
    public void saveConfigFile() {
        System.out.println("save");
    }

    @FXML
    public void createConfigFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        // Set initial directory (optional)
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Set extension filters, e.g., TXT files only
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Piano Config", "*.piano.txt")
        );
        Stage stage = new Stage();
        File file = fileChooser.showSaveDialog(stage);
        JFSFile jfsFile = new JFSFile(file.getAbsolutePath());
        try {
            boolean success = jfsFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!jfsFile.exists()) {
            logger.error("Unable to create Piano Config");
            return;
        }
        try {
            SaveWriter saveWriter = new SaveWriter(jfsFile);
            saveWriter.newSave();
        } catch (FileAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void selectMidiDevice() throws IOException {

        if (!selectMidiUIOpen) {
            selectMidiUIOpen = true;
            logger.info("Midi Device Selection Window Open");

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/SelectMidiDevice_V1.fxml"));
            VBox midiSelection = fxmlLoader.load();
            MidiDeviceSelectionController midiDeviceSelectionController = fxmlLoader.getController();
            ControllerManager.setMidiDeviceSelectionController(midiDeviceSelectionController);

            if (midiDevice == null) {
                midiDeviceSelectionController.populateList();
            } else {
                midiDeviceSelectionController.populateList(midiDevice);
            }

            Stage stage = new Stage();

            Scene scene = new Scene(midiSelection);
            scene.setOnKeyPressed(keyEvent -> {
                if (new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
                    quit(stage);
                }
            });

            stage.setTitle("Connect to MidiDevice...");
            stage.setResizable(false);
//            stage.setMaxHeight(400);
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(e -> {
                quit(stage);
            });
        }
    }

    public void quit(Stage stage) {
        logger.info("Midi Device Selection Window Closed");
        selectMidiUIOpen = false;
        stage.close();
    }

    public void setSelectMidiUIOpen(boolean selectMidiUIOpen) {
        this.selectMidiUIOpen = selectMidiUIOpen;
    }

    @FXML
    public void selectUIType() {
        System.out.println("ui");
    }

    @FXML
    public void runApplication() {
        System.out.println("run");
    }

    public void setMidiDevice(MidiDevice midiDevice) {
        logger.info("Set Midi Device: {}", midiDevice.getDeviceInfo().getName());
        this.midiDevice = midiDevice;
        midiDeviceNameLabel.setText(this.midiDevice.getDeviceInfo().getName());
    }

    public File getConfigFile() {
        return configFile;
    }
}
