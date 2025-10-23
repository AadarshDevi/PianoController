package com.alphagen.studio.pianocontroller.ui.controllers.modules;

import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import com.alphagen.studio.pianocontroller.util.MidiDeviceUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

public class MidiDeviceModuleController {
    private static final Logger logger = LogManager.getLogger(MidiDeviceModuleController.class);
    @FXML
    private StackPane connected_icon;
    @FXML
    private Label midiDeviceNameLabel;
    @FXML
    private Button midiDeviceConnector;

//    @FXML private SVGPath connectMidi;
//    @FXML private SVGPath unconnectableMidi;
//    @FXML private SVGPath connectableMidi;


    //    private ButtonState buttonState;
    private MidiDevice midiDevice;

    @FXML public void initialize() {
//        connectMidi.setVisible(true);
//        connectableMidi.setVisible(false);
//        unconnectableMidi.setVisible(false);

//        buttonState = ButtonState.CONNECT;
//        midiDeviceConnector.setBackground(new Background(new BackgroundFill(Theme.MIDI_CONNECTOR_HOVER, null, null)));
//        midiDeviceConnector.setOnMouseEntered(event -> {
//            if (buttonState == ButtonState.CONNECT) {
//                midiDeviceConnector.setBackground(new Background(new BackgroundFill(Theme.MIDI_CONNECTOR_HOVER, null, null)));
//                connectMidi.setVisible(false);
//                connectableMidi.setVisible(true);
//                unconnectableMidi.setVisible(false);
//            } else if (buttonState == ButtonState.UNCONNECTABLE) {
//                midiDeviceConnector.setBackground(new Background(new BackgroundFill(Theme.MIDI_CONNECTOR_ERROR, null, null)));
//                buttonState = ButtonState.UNCONNECTABLE;
//                connectMidi.setVisible(false);
//                connectableMidi.setVisible(false);
//                unconnectableMidi.setVisible(true);
//            }
//        });
//        midiDeviceConnector.setOnMouseExited(event -> {
//            if (buttonState == ButtonState.CONNECT) {
//                midiDeviceConnector.setBackground(new Background(new BackgroundFill(Theme.MIDI_CONNECTOR_DEFAULT, null, null)));
//                connectMidi.setVisible(true);
//                connectableMidi.setVisible(false);
//                unconnectableMidi.setVisible(false);
//            } else if (buttonState == ButtonState.UNCONNECTABLE) {
//                midiDeviceConnector.setBackground(new Background(new BackgroundFill(Theme.MIDI_CONNECTOR_ERROR, null, null)));
//                buttonState = ButtonState.UNCONNECTABLE;
//                connectMidi.setVisible(false);
//                connectableMidi.setVisible(false);
//                unconnectableMidi.setVisible(true);
//            }
//        });

    }

    @FXML
    public void connectMidiDevice() {
        boolean connectable = MidiDeviceUtil.checkDevice(midiDevice);
//        if (connectable) {
//            connectMidi.setVisible(false);
//            connectableMidi.setVisible(true);
//            unconnectableMidi.setVisible(false);
//            buttonState = ButtonState.CONNECTABLE;
//        } else {
//            connectMidi.setVisible(false);
//            connectableMidi.setVisible(false);
//            unconnectableMidi.setVisible(true);
//            buttonState = ButtonState.UNCONNECTABLE;
//        }
        connected_icon.setVisible(connectable);
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
//        buttonState = ButtonState.CONNECT;
//
//        connectMidi.setVisible(true);
//        connectableMidi.setVisible(false);
//        unconnectableMidi.setVisible(false);

        logger.info("MidiDevice Module Reset");
    }

    public void active() {
        connected_icon.setVisible(true);
        midiDeviceConnector.setText("Connectable");
        midiDevice.close();
        logger.info("MidiDevice Connectable: {}", midiDevice.getDeviceInfo().getName());
        ControllerManager.getMidiDeviceSelectionController().setCurrentMidiDeviceName(midiDevice.getDeviceInfo().getName());
    }

//    enum ButtonState {
//        CONNECT, CONNECTABLE, UNCONNECTABLE
//    }


}
