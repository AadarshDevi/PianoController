package com.alphagen.studio.pianocontroller.ui.controllers;

import com.alphagen.studio.pianocontroller.Main;
import com.alphagen.studio.pianocontroller.midi.MidiDeviceChecker;
import com.alphagen.studio.pianocontroller.midi.MidiDeviceReceiver;
import com.alphagen.studio.pianocontroller.save.SaveReader;
import com.alphagen.studio.pianocontroller.save.SaveWriter;
import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfilesystem.file.JFSFile;
import jfilesystem.file.exception.JSFFileNullException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;

public class MainFrameController {
    private static final Logger logger = LogManager.getLogger(MainFrameController.class);

    private static Thread receiverThread = null;
    boolean selectMidiUIOpen;
    boolean isRunning = false;
    private File configFile;
    private MidiDevice midiDevice;
    private SaveReader saveReader;
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
    private Button runAppButton;
    @FXML
    private HBox configButtonContainer;

    public static Thread getReceiverThread() {
        return receiverThread;
    }

    @FXML
    public void initialize() {
        logger.info("Initialized");
        selectMidiUIOpen = false;
        runAppButton.setTextFill(Paint.valueOf("#ffffff"));
        isRunning();

        // test
//        configFile = new JFSFile("D:/piano_config_1.piano.txt");
//        logger.info("Set Config file: " + configFile.getAbsolutePath());
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
            validConfigFile();
        }
//        config = configFile; // test
        if (config != null) {
            validConfigFile();
            this.configFile = config;
            logger.info("Loaded Config File: {}", config.getName());
            configFileNameLabel.setText(this.configFile.getName());
            try {
                saveReader = new SaveReader(new JFSFile(configFile.getAbsolutePath()));
                saveReader.readLetters();
                saveReader.readNum();
                saveReader.readNumPad();
                saveReader.readSpecials();
                saveReader.readMouseClicks();
                saveReader.currentSave();
//                System.exit(0);// test
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSFFileNullException e) {
                logger.error("Unable to get File");
            }
        }
        validConfigFile();
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Piano Config", "*.piano.txt"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
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


        logger.info("run");

        validConfigFile();

        if (saveReader == null) {
            logger.error("Piano Config Reader is Null");
            validConfigFile();
            return;
        }

        HashMap<Integer, Integer> keyMap = saveReader.getKeyMap();
        HashMap<Integer, Integer> mouseMap = saveReader.getMouseMap();

        if (keyMap == null || mouseMap == null || keyMap.isEmpty()) {
            logger.error("Data is unparsed or empty");
            configButtonContainer.setBorder(new Border(new BorderStroke(
                    Color.RED,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths.DEFAULT
            )));
            return;
        } else {
            configButtonContainer.setBorder(null);
        }

        if (midiDevice == null) {
            logger.error("There is no Midi Device connected");
            midiDeviceNameLabel.setTextFill(Color.WHITE);
            midiDeviceNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#9c0000"), new CornerRadii(0), Insets.EMPTY)));
            return;
        } else {
            midiDeviceNameLabel.setTextFill(Color.BLACK); // default text color
            midiDeviceNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#f4f4f4"), new CornerRadii(0), Insets.EMPTY)));
        }

        if (!MidiDeviceChecker.checkDevice(midiDevice)) {
            logger.error("Unable to connect to device");
            return;
        }

        isRunning();

        MidiDeviceReceiver mdr = new MidiDeviceReceiver(midiDevice.getDeviceInfo().getName());

        try {
            Transmitter transmitter = midiDevice.getTransmitter();
            transmitter.setReceiver(mdr);
        } catch (MidiUnavailableException e) {
            logger.error("Unable to get Transmitter for MidiDevice: " + midiDevice.getDeviceInfo().getName());
        }

        receiverThread = new Thread(mdr);
        receiverThread.start();

        try {
            midiDevice.open();
        } catch (MidiUnavailableException e) {
            logger.error("Unable to Open Midi Device");
        }
    }

    public File getConfigFile() {
        return configFile;
    }

    public void isRunning() {
        if (isRunning) {
            runAppButton.setText("⏹");
            runAppButton.setPrefWidth(30);
            runAppButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#bf3434"), new CornerRadii(5), Insets.EMPTY)));
            isRunning = false;
        } else {
            if (midiDevice != null) midiDevice.close();
            runAppButton.setText("▶");
            runAppButton.setPrefWidth(30);
            runAppButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#42bf34"), new CornerRadii(5), Insets.EMPTY)));
            isRunning = true;
        }
    }

    @FXML
    public void openSettings() {
    }

    @FXML
    public void resetData() {
        midiDevice = null;
        saveReader = null;
        configFile = null;
        isRunning = false;

        configFileNameLabel.setTextFill(Color.BLACK); // default text color
        configFileNameLabel.setText("Config File");
        configFileNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#f4f4f4"), new CornerRadii(0), Insets.EMPTY)));

        midiDeviceNameLabel.setTextFill(Color.BLACK); // default text color
        midiDeviceNameLabel.setText("Midi Device");
        midiDeviceNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#f4f4f4"), new CornerRadii(0), Insets.EMPTY)));

        isRunning();
    }

    public void validConfigFile() {
        if (saveReader == null) {
            logger.error("ConfigReader is null");
            configFileNameLabel.setTextFill(Color.WHITE);
            configFileNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#9c0000"), new CornerRadii(0), Insets.EMPTY)));
            return;
        } else {
            configFileNameLabel.setTextFill(Color.BLACK); // default text color
            configFileNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#f4f4f4"), new CornerRadii(0), Insets.EMPTY)));
        }
    }

    public MidiDevice getMidiDevice() {
        return midiDevice;
    }

    public void setMidiDevice(MidiDevice midiDevice) {
        logger.info("Set Midi Device: {}", midiDevice.getDeviceInfo().getName());
        this.midiDevice = midiDevice;
        midiDeviceNameLabel.setText(this.midiDevice.getDeviceInfo().getName());
    }
}
