package com.alphagen.studio.pianocontroller.ui.controllers.modules;

import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

public class MidiDeviceModuleController {
    private static final Logger logger = LogManager.getLogger(MidiDeviceModuleController.class);
    @FXML
    private StackPane connected_icon;
    @FXML
    private Label midiDeviceNameLabel;
    @FXML
    private Button midiDeviceConnector;
    private MidiDevice midiDevice;

    @FXML
    public void connectMidiDevice() throws MidiUnavailableException {
        if (midiDevice == null) {
            logger.error("MidiDevice is Null");
            return;
        }
        midiDevice.open();
        logger.info("MidiDevice Opened: {}", midiDevice.getDeviceInfo().getName());
        if (midiDevice.isOpen()) {

            try {

                Transmitter transmitter = midiDevice.getTransmitter();

                connected_icon.setVisible(true);
                midiDeviceConnector.setText("Connectable");
                ControllerManager.getMidiDeviceSelectionController().setMidiDevice(midiDevice);

            } catch (MidiUnavailableException e) {
                logger.error("Unable to get Transmitter");
                logger.error(e);
            }

            midiDevice.close();
            logger.info("MidiDevice Closed: {}", midiDevice.getDeviceInfo().getName());
        } else {
            connected_icon.setVisible(false);
        }
        ControllerManager.getMidiDeviceSelectionController().unique();
    }

    public MidiDevice getMidiDevice() {
        logger.info("Getting MidiDevice: {}", midiDevice.getDeviceInfo().getName());
        return midiDevice;
    }

    public void setDevice(MidiDevice.Info midiDeviceInfo) throws MidiUnavailableException {
        this.midiDevice = MidiSystem.getMidiDevice(midiDeviceInfo);
        logger.info("Setting MidiDevice: {}", midiDevice.getDeviceInfo().getName());
        midiDeviceNameLabel.setText(midiDeviceInfo.getName());
    }

    public void reset() {
        connected_icon.setVisible(false);
        midiDeviceConnector.setText("Connect");
//        logger.info("MidiDevice Module Reset");
    }

    public void active() {
        connected_icon.setVisible(true);
        midiDeviceConnector.setText("Connectable");
        midiDevice.close();
        logger.info("MidiDevice Connectable: {}", midiDevice.getDeviceInfo().getName());
        ControllerManager.getMidiDeviceSelectionController().setCurrentMidiDeviceName(midiDevice.getDeviceInfo().getName());
    }
}
