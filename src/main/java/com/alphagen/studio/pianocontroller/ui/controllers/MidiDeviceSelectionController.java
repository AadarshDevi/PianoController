package com.alphagen.studio.pianocontroller.ui.controllers;

import com.alphagen.studio.pianocontroller.ui.controllers.modules.MidiDeviceModuleController;
import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import com.alphagen.studio.pianocontroller.util.WindowUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;

public class MidiDeviceSelectionController {
    private static final Logger logger = LogManager.getLogger(MidiDeviceSelectionController.class);
    @FXML
//    private HBox root;
    private VBox rootPane;
    @FXML
    private ListView<BorderPane> midiDeviceListView;
    @FXML
    private Label currentMidiDeviceNameLabel;
    @FXML
    private Button useSelectedMidiDeviceButton;
    private MidiDevice midiDevice;
    private boolean testingConnection = false;
//    @FXML private Button quitAppButton;


    public void setMidiDevice(MidiDevice midiDevice) {
        this.midiDevice = midiDevice;
    }

    public void setCurrentMidiDeviceName(String midiName) {
    }

    public void unique() {
//        ObservableList<BorderPane> observableList = midiDeviceListView.getItems();
//        for (BorderPane module : observableList) {
//            MidiDeviceModuleController mdc = (MidiDeviceModuleController) module.getProperties().get("controller");
//            MidiDevice checkDevice = mdc.getMidiDevice();
//            if (!checkDevice.equals(midiDevice)) {
//                mdc.reset();
//            } else {
//                currentMidiDeviceNameLabel.setText(checkDevice.getDeviceInfo().getName());
//            }
//        }
        midiDeviceListView
                .getItems()
                .stream()
                .map(module -> (MidiDeviceModuleController) module.getProperties().get("controller"))
                .forEach(mmc -> {
                    MidiDevice md = mmc.getMidiDevice();
                    if (!md.equals(midiDevice)) mmc.reset();
                    else currentMidiDeviceNameLabel.setText(md.getDeviceInfo().getName());
                });
    }

    @FXML
    public void useSelectedMidiDevice() {
        if (midiDevice != null) {
            ControllerManager.getMainFrameController().setMidiDevice(midiDevice);
            testingConnection = false;
//            logger.info(root.getWidth());
            WindowUtil.close(rootPane);
        }
    }

    @FXML
    public void close() {
        WindowUtil.close(rootPane);
    }

    public boolean isTestingConnection() {
        return testingConnection;
    }

    public ListView<BorderPane> getListView() {
        return midiDeviceListView;
    }

    public MidiDevice setMidiDevice() {
        return midiDevice;
    }

    //    public HBox getRoot() {
//        return root;
//    }
    public VBox getRoot() {
        return rootPane;
    }

    public void setTestCode(MidiMessage message) {
        currentMidiDeviceNameLabel.setText(message.toString());
    }
}
