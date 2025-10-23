package com.alphagen.studio.pianocontroller.ui.controllers;

import com.alphagen.studio.pianocontroller.Main;
import com.alphagen.studio.pianocontroller.midi.MidiDeviceReceiver;
import com.alphagen.studio.pianocontroller.save.SaveReader;
import com.alphagen.studio.pianocontroller.save.SaveWriter;
import com.alphagen.studio.pianocontroller.ui.controllers.modules.MidiDeviceModuleController;
import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import com.alphagen.studio.pianocontroller.util.MidiDeviceUtil;
import com.alphagen.studio.pianocontroller.util.WindowUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfilesystem.file.JFSFile;
import jfilesystem.file.exception.JSFFileNullException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;

/**
 * This is the controller of the App. everything will go through this controller. So
 * this controller is in a manager and accessible to all other classes to modify data.
 */
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
    // Root Pane
    @FXML private HBox root;
    @FXML private StackPane appLogoButton;

    // Title Bar Window
    @FXML private Button minimizeAppButton;
    @FXML private Button quitAppButton;

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
    @FXML private Button testButton; // testing action
    @FXML private Button openSettingsButton; // opens settings
    @FXML private Button resetDataButton; // reset data in app

    @FXML private Button getKeyValueButton; // reset data in app
    @FXML private TextField pianoKeyNumTextField; // reset data in app
    @FXML private Label keyboardKeyLabel; // reset data in app

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

        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItemAppLogoQuit = new MenuItem("Quit");
        menuItemAppLogoQuit.setOnAction(event -> quitApp());

        MenuItem menuItemAppLogoMinimize = new MenuItem("Minimize");
        menuItemAppLogoMinimize.setOnAction(event -> minimizeApp());
        contextMenu.getItems().addAll(menuItemAppLogoMinimize, new SeparatorMenuItem(), menuItemAppLogoQuit);

        appLogoButton.setOnContextMenuRequested(event -> {
            double screenX = appLogoButton.localToScreen(0, 0).getX() + 4;
            double screenY = appLogoButton.localToScreen(0, 0).getY() + 22;
            contextMenu.show(appLogoButton, screenX, screenY);
        });

        pianoKeyNumTextField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case KeyCode.ENTER -> {
                    getKeyValue();
                    pianoKeyNumTextField.selectAll();
                }
                case KeyCode.ESCAPE -> {
                    pianoKeyNumTextField.getScene().getRoot().requestFocus();
                }
            }
        });

        // TODO: Add tooltips to remaining buttons
        runAppButton.setTooltip(new Tooltip("Run"));
        stopAppButton.setTooltip(new Tooltip("Stop"));
        openSettingsButton.setTooltip(new Tooltip("Settings"));
        resetDataButton.setTooltip(new Tooltip("Reset"));
        testButton.setTooltip(new Tooltip("Test"));
        isRunning();

        // This code makes the undecorated app draggable and being able to move freely
    }

    /**
     * Shows a File chooser if the config file is null. gets the file, opens and
     * reads the data used to map piano keys to keyboard keys
     */
    @FXML public void openConfigFile() {
        logger.info("Open Config File Chooser");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Piano Config File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Piano Config", "*.piano.txt"));
        File config = fileChooser.showOpenDialog(null);

        setValidConfigLabel(config);
        validConfigFile();
    }

    private void setValidConfigLabel(File config) {

        if (config != null) {
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
    }

    /**
     * Note: this might need some though and rework
     * save the current config file as a new file somewhere else.
     * or overwrite existing file and use it instead
     */
    @FXML public void saveConfigFile() {
        System.out.println("save");
        // todo
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
        File file = fileChooser.showSaveDialog(null);
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

            FXMLLoader selectMidiDeviceFXMLLoader = new FXMLLoader(Main.class.getResource("fxml/SelectMidiDevice_V1.fxml"));
            VBox midiSelection = selectMidiDeviceFXMLLoader.load(); // throws IOException
//            FXMLLoader selectMidiDeviceFXMLLoader = new FXMLLoader(Main.class.getResource("fxml/SelectMidiDevice_V3.fxml"));
//            BorderPane midiSelection = selectMidiDeviceFXMLLoader.load(); // throws IOException
            MidiDeviceSelectionController midiDeviceSelectionController = selectMidiDeviceFXMLLoader.getController();
            ControllerManager.setMidiDeviceSelectionController(midiDeviceSelectionController);

            logger.info("Placing Midi Device");
            ObservableList<MidiDevice.Info> observableList = FXCollections.observableArrayList(MidiSystem.getMidiDeviceInfo());

            if (MidiDeviceUtil.enableStreams) {
                FXCollections
                        .observableArrayList(MidiSystem.getMidiDeviceInfo())
                        .stream()
                        .filter(mdi -> {
                            try {
                                if (!MidiDeviceUtil.checkDevice(MidiSystem.getMidiDevice(mdi))) return false;
                            } catch (MidiUnavailableException e) {
                                throw new RuntimeException(e);
                            }
                            return true;
                        }).forEach(mdi -> {
                            FXMLLoader mdm = new FXMLLoader(Main.class.getResource("fxml/MidiDeviceModule_V1.fxml"));
                        });
            } else {
                for (MidiDevice.Info mdi : observableList) {
                    try {
                        if (!MidiDeviceUtil.checkDevice(MidiSystem.getMidiDevice(mdi))) continue;
                    } catch (MidiUnavailableException e) {
                        throw new RuntimeException(e);
                    }

                    FXMLLoader midiDeviceModule = new FXMLLoader(Main.class.getResource("fxml/MidiDeviceModule_V1.fxml"));
//                FXMLLoader midiDeviceModule = new FXMLLoader(Main.class.getResource("fxml/MidiDeviceModule_V2.fxml"));
                    BorderPane module = null;
                    try {
                        module = midiDeviceModule.load();
                        module.setPrefWidth(midiDeviceSelectionController.getListView().getPrefWidth() - 18);
//                    module.setMaxWidth(midiDeviceSelectionController.getListView().getPrefWidth() - 60);
                    } catch (IOException e) {
                        logger.error("Unable to load Midi Module FXML");
                    }
                    MidiDeviceModuleController moduleController = midiDeviceModule.getController();
                    if (module != null) {
                        module.getProperties().put("controller", moduleController);
                    }
                    try {
                        moduleController.setDevice(mdi);
                    } catch (MidiUnavailableException e) {
                        logger.error("MidiDevice is Unavailable");
                    }
                    midiDeviceSelectionController.getListView().getItems().add(module);
                }

                logger.info("Added all MidiDevice");

                if (midiDevice != null) {
                    ObservableList<BorderPane> moduleList = midiDeviceSelectionController.getListView().getItems();
                    for (BorderPane module : moduleList) {
                        MidiDeviceModuleController mdc = (MidiDeviceModuleController) module.getProperties().get("controller");
                        MidiDevice checkDevice = mdc.getMidiDevice();
                        if (!checkDevice.equals(midiDevice)) {
                            mdc.reset();
                        } else {
                            mdc.active();
//                        this.midiDevice = midiDevice;
                            midiDeviceSelectionController.setMidiDevice(midiDevice);
                            logger.info("Midi Module Activated");
                        }
                    }
                    logger.info("Set MidiDevice Selection");
                }
            }

            Stage stage = new Stage();

            Scene scene = new Scene(midiSelection);
            scene.setOnKeyPressed(keyEvent -> {
                if (new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
                    quit(stage);
                }
            });

            stage.setTitle("Connect to MidiDevice...");
            stage.setScene(scene);
//            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            String filepath = "/com/alphagen/studio/pianocontroller/images/app_logo_2.png";
            InputStream inputStream = MainFrameController.class.getResourceAsStream(filepath);
            stage.getIcons().add(new Image(inputStream));
            stage.setOnCloseRequest(event -> {
                WindowUtil.close(stage);
            });
            stage.show();
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
            midiDeviceNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#9c0000"), new CornerRadii(0), Insets.EMPTY)));
            return;
        } else {
            midiDeviceNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#f4f4f400"), new CornerRadii(0), Insets.EMPTY)));
        }

        if (!MidiDeviceUtil.checkDevice(midiDevice)) {
            logger.error("Unable to connect to device");
            return;
        }

        isRunning();

        MidiDeviceReceiver mdr = new MidiDeviceReceiver(midiDevice.getDeviceInfo().getName());
        ControllerManager.setMidiDeviceReceiver(mdr);

        try {
            Transmitter transmitter = midiDevice.getTransmitter();
            transmitter.setReceiver(mdr);
        } catch (MidiUnavailableException e) {
            logger.error("Unable to get Transmitter for MidiDevice: {}", midiDevice.getDeviceInfo().getName());
        }

        receiverThread = new Thread(mdr);
        receiverThread.start();

        try {
            midiDevice.open();
        } catch (MidiUnavailableException e) {
            logger.error("Unable to Open Midi Device");
        }

        /*
         * Writing code to run KeyVisualizer in separate thread
         */
//        FXMLLoader kvl = new FXMLLoader(Main.class.getResource("fxml/KeyVisualizer_V1.fxml"));
//        try {
//            Stage stage = new Stage();
//            BorderPane kv = kvl.load();
//            KeyVisualizerController kvc = kvl.getController();
//            WindowUtil.setCustomWindow(stage, false, kvc.getRoot());
//            ControllerManager.setKeyVisualizerController(kvc);
//            stage.setScene(new Scene(kv));
//            stage.show();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }

    /**
     * checks if the app is running. Used to hide/show the process buttons
     * run/stop.
     */
    public void isRunning() {
        if (isRunning) {
            isRunning = false;
            runAppButton.setDisable(true);
            stopAppButton.setDisable(false);
            testButton.setDisable(true);
            resetDataButton.setDisable(true);
        } else {
            isRunning = true;
            runAppButton.setDisable(false);
            stopAppButton.setDisable(true);
            testButton.setDisable(false);
            resetDataButton.setDisable(false);
        }

        if (saveReader == null) {
            getKeyValueButton.setDisable(true);
            pianoKeyNumTextField.setDisable(true);
        } else {
            getKeyValueButton.setDisable(false);
            pianoKeyNumTextField.setDisable(false);
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

        configFileNameLabel.setText("Config File");
        configFileNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#f4f4f400"), new CornerRadii(0), Insets.EMPTY)));

        midiDeviceNameLabel.setText("Midi Device");
        midiDeviceNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#f4f4f400"), new CornerRadii(0), Insets.EMPTY)));

        isRunning();
    }

    /**
     * checks if the config file selected does not exist or is not valid or is empty
     */
    public void validConfigFile() {
        if (saveReader == null) {
            logger.error("ConfigReader is null");
            configFileNameLabel.setText("Invalid Config File");
            configFileNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#9c0000"), new CornerRadii(0), Insets.EMPTY)));
        } else {
            configFileNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#f4f4f400"), new CornerRadii(0), Insets.EMPTY)));
        }
    }

    public void validMidiDevice() {
        if (saveReader == null) {
            logger.error("Midi Device selected is null");
            configFileNameLabel.setText("Invalid Midi Device");
            midiDeviceNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#9c0000"), new CornerRadii(0), Insets.EMPTY)));
        } else {
            midiDeviceNameLabel.setText(this.midiDevice.getDeviceInfo().getName());
            midiDeviceNameLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#f4f4f400"), new CornerRadii(0), Insets.EMPTY)));
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
        validMidiDevice();
    }

    /**
     * Used to stop getting data from the midi device. Does not
     * quit or close the app.
     */
    public void stopApplication() {
        ControllerManager.getMidiDeviceReceiver().getKeyViewer().dispose();
        midiDevice.close();
        isRunning = false;
        isRunning();
    }

    @FXML public void testAction() {
        configFile = new JFSFile("D:/piano_config_1.piano.txt");
        try {
            saveReader = new SaveReader((JFSFile) configFile);
            saveReader.readAll();
            setValidConfigLabel(configFile);
            validConfigFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSFFileNullException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            midiDevice = MidiSystem.getMidiDevice(MidiSystem.getMidiDeviceInfo()[5]);
            logger.debug(midiDevice.getDeviceInfo().getName());
            validMidiDevice();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
        runApplication();
    }

    @FXML private void getKeyValue() {
        try {
            int pianoKey = Integer.parseInt(pianoKeyNumTextField.getText());
            int keyboardKey = saveReader.getKeyMap().get(pianoKey);
            logger.info(keyboardKey);
            keyboardKeyLabel.setText(Integer.toString(keyboardKey));
        } catch (NumberFormatException e) {
            logger.info(-1);
            keyboardKeyLabel.setText(Integer.toString(-1));
        } catch (NullPointerException e) {
            logger.info("NL");
            keyboardKeyLabel.setText("NL");
        }
    }

    public SaveReader getSaveReader() {
        return saveReader;
    }

    public void minimizeApp() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setIconified(true);
    }

    public void quitApp() {
        WindowUtil.quit();
    }

    public Pane getRoot() {
        return root;
    }
}
