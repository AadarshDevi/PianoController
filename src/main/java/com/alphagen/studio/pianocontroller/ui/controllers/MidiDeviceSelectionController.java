package com.alphagen.studio.pianocontroller.ui.controllers;

import com.alphagen.studio.pianocontroller.Main;
import com.alphagen.studio.pianocontroller.ui.controllers.modules.MidiDeviceModuleController;
import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import java.io.IOException;

public class MidiDeviceSelectionController {
    private static final Logger logger = LogManager.getLogger(MidiDeviceSelectionController.class);
    @FXML
    private VBox rootPane;
    @FXML
    private ListView<BorderPane> midiDeviceListView;
    @FXML
    private Label currentMidiDeviceNameLabel;
    @FXML
    private Button useSelectedMidiDeviceButton;
    private MidiDevice midiDevice;
    private boolean testingConnection = false;

    public void populateList() {
        logger.info("Selecting Midi Device");
        ObservableList<MidiDevice.Info> observableList = FXCollections.observableArrayList(MidiSystem.getMidiDeviceInfo());

        for (MidiDevice.Info mdi : observableList) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/MidiDeviceModule_V1.fxml"));
            BorderPane module = null;
            try {
                module = fxmlLoader.load();
                module.setPrefWidth(midiDeviceListView.getPrefWidth() - 20);
            } catch (IOException e) {
                logger.error("Unable to load Midi Module FXML");
            }
            MidiDeviceModuleController moduleController = fxmlLoader.getController();
            if (module != null) {
                module.getProperties().put("controller", moduleController);
            }
            try {
                moduleController.setDevice(mdi);
            } catch (MidiUnavailableException e) {
                logger.error("MidiDevice is Unavailable");
            }
            midiDeviceListView.getItems().add(module);
        }

        logger.info("Added all MidiDevice");
    }

    public void setMidiDevice(MidiDevice midiDevice) {
        this.midiDevice = midiDevice;
    }

    public void setCurrentMidiDeviceName(String midiName) {
    }

    public void unique() {
        ObservableList<BorderPane> observableList = midiDeviceListView.getItems();
        for (BorderPane module : observableList) {
            MidiDeviceModuleController mdc = (MidiDeviceModuleController) module.getProperties().get("controller");
            MidiDevice checkDevice = mdc.getMidiDevice();
            if (!checkDevice.equals(midiDevice)) {
                mdc.reset();
            } else {
                currentMidiDeviceNameLabel.setText(checkDevice.getDeviceInfo().getName());
            }
        }
    }

    @FXML
    public void useSelectedMidiDevice() {
        if (midiDevice != null) {
            ControllerManager.getMainFrameController().setMidiDevice(midiDevice);
            testingConnection = false;
            close();
        }
    }

    public void close() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        ControllerManager.getMainFrameController().setSelectMidiUIOpen(false);
        logger.info("Closing Midi Selection Window");
        stage.close();
    }

    public void populateList(MidiDevice midiDevice) {
        populateList();
        ObservableList<BorderPane> observableList = midiDeviceListView.getItems();
        for (BorderPane module : observableList) {
            MidiDeviceModuleController mdc = (MidiDeviceModuleController) module.getProperties().get("controller");
            MidiDevice checkDevice = mdc.getMidiDevice();
            if (!checkDevice.equals(midiDevice)) {
                mdc.reset();
            } else {
                mdc.active();
                this.midiDevice = midiDevice;
                logger.info("Midi Module Activated");
            }
        }
        logger.info("Set MidiDevice Selection");
    }


    public boolean isTestingConnection() {
        return testingConnection;
    }

    public void setTestCode(MidiMessage message) {
        currentMidiDeviceNameLabel.setText(message.toString());
    }
}
