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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
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

    /**
     * This is the logger for this class
     */
    private static final Logger logger = LogManager.getLogger(MainFrameController.class);

    /**
     * This is a thread that will run MidiDeviceReceiver to get Midi input
     *
     * @see MidiDeviceReceiver
     */
    private static Thread receiverThread = null;

    /**
     * This is used to check if the Midi Device Selection Window is open
     */
    boolean selectMidiUIOpen;

    /**
     * This is used to know if the app is reading midi data
     */
    boolean isRunning = false;

    /**
     * this is a file that will contain the Config File used to map piano keys
     * to keyboard keys
     */
    private File configFile;

    /**
     * This is the midi device that will connect and send data to the app.
     */
    private MidiDevice midiDevice;

    /**
     * This is the Config File Reader of "configFile"
     */
    private SaveReader saveReader;

    // UI Labels

    // config buttons
    @FXML private Label configFileNameLabel; // File
    @FXML private Button openConfigButton; // open config
    @FXML private Button createConfigButton; // create config
    @FXML private Button saveConfigButton; // save config

    // midi device selection
    @FXML private Label midiDeviceNameLabel; // midi device
    @FXML private Button selectMidiDeviceButton;

    // ui selection
    @FXML private Label uiTypeLabel; // ui type
    @FXML private Button uiTypeButton; // ui type

    // app processes
    @FXML private Button runAppButton; // run app
    @FXML private Button stopAppButton; // stop app (not quit/close app)

    /**
     * gets the thread used to get piano input
     */
    public static Thread getReceiverThread() {
        return receiverThread;
    }

    /**
     * The first method called when the Controller is created.
     * sets up the process buttons (run/stop)
     */
    @FXML public void initialize() {
        logger.info("Initialized");
        selectMidiUIOpen = false;

        runAppButton.setTextFill(Paint.valueOf("#ffffff"));
        runAppButton.setText("▶");
        runAppButton.setPrefWidth(30);
        runAppButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#42bf34"), new CornerRadii(5), Insets.EMPTY)));

        stopAppButton.setTextFill(Paint.valueOf("#ffffff"));
        stopAppButton.setText("⏹");
        stopAppButton.setPrefWidth(30);
        stopAppButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#bf3434"), new CornerRadii(5), Insets.EMPTY)));

        isRunning();
    }

    /**
     * Shows a File chooser if the config file is null. gets the file, opens and
     * reads the data used to map piano keys to keyboard keys
     */
    @FXML public void openConfigFile() {
        File config = null;
        if (configFile == null) {
            logger.info("Open Config File Chooser");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Piano Config File");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Piano Config", "*.piano.txt"));
            config = fileChooser.showOpenDialog(null);
            validConfigFile();
        }

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

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSFFileNullException e) {
                logger.error("Unable to get File");
            }
        }
        validConfigFile();
    }

    /**
     * Note: this might need some though and rework
     * save the current config file as a new file somewhere else.
     * or overwrite existing file and use it instead
     */
    @FXML public void saveConfigFile() {
        System.out.println("save");
    }

    /**
     * creates a new config file using the numbers given by the
     * user in the ui
     */
    @FXML public void createConfigFile() {
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

    /**
     * This opens the ui that lists the midi devices connected to the Laptop/PC
     *
     * @throws IOException unable to get fxml for Device Selection
     * @see MidiDeviceSelectionController
     */
    @FXML public void selectMidiDevice() throws IOException {

        if (!selectMidiUIOpen) {
            selectMidiUIOpen = true;
            logger.info("Midi Device Selection Window Open");

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/SelectMidiDevice_V1.fxml"));
            VBox midiSelection = fxmlLoader.load(); // throws IOException
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

    /**
     * Quits the app.
     *
     * @param stage the correct window to close
     */
    public void quit(Stage stage) {
        logger.info("Midi Device Selection Window Closed");
        selectMidiUIOpen = false;
        stage.close();
    }

    /**
     * Setter for selectMidiUIOpen. sets that the selection window is either closed or opened.
     *
     * @param selectMidiUIOpen is the selection window for the midi device open?
     */
    public void setSelectMidiUIOpen(boolean selectMidiUIOpen) {
        this.selectMidiUIOpen = selectMidiUIOpen;
    }

    @FXML public void selectUIType() {
        System.out.println("ui");
    }

    /**
     * This method is really important. This method checks if the values are not null
     * and empty and valid before running the app to get data from the midi device.
     */
    @FXML public void runApplication() {


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
            return;
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

    /**
     * checks if the app is running. Used to hide/show the process buttons
     * run/stop.
     */
    public void isRunning() {
        if (isRunning) {
            isRunning = false;
            runAppButton.setVisible(false);
            runAppButton.setManaged(false);
            stopAppButton.setVisible(true);
            stopAppButton.setManaged(true);
        } else {
            isRunning = true;
            stopAppButton.setVisible(false);
            stopAppButton.setManaged(false);
            runAppButton.setVisible(true);
            runAppButton.setManaged(true);
        }
    }

    /**
     * Opens the settings ui
     */
    @FXML public void openSettings() {
    }

    /**
     * resets all the data in the MainUI, the config file, file reader, selected
     * midi device and ui type.
     */
    @FXML public void resetData() {
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

    /**
     * checks if the config file selected does not exist or is not valid or is empty
     */
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

    /**
     * gets the midi device used to get input
     *
     * @return midi device that gives the app input data
     */
    public MidiDevice getMidiDevice() {
        return midiDevice;
    }

    /**
     * sets the midi device to be used to get data
     *
     * @param midiDevice Midi Device to connect to
     */
    public void setMidiDevice(MidiDevice midiDevice) {
        logger.info("Set Midi Device: {}", midiDevice.getDeviceInfo().getName());
        this.midiDevice = midiDevice;
        midiDeviceNameLabel.setText(this.midiDevice.getDeviceInfo().getName());
    }

    /**
     * Used to stop getting data from the midi device. Does not
     * quit or close the app.
     */
    public void stopApplication() {
        midiDevice.close();
        isRunning = false;
        isRunning();
    }

}
