package com.alphagen.studio.pianocontroller.midi;

import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

public class MidiDeviceChecker {
    private static final Logger logger = LogManager.getLogger(MidiDeviceChecker.class);

    public static boolean checkDevice(MidiDevice midiDevice) {
        if (midiDevice == null) {
            logger.error("MidiDevice is Null");
            return false;
        }
        try {
            midiDevice.open();
        } catch (MidiUnavailableException e) {
            logger.error("MidiDevice is connected to something else");
            return false;
        }
        logger.info("MidiDevice Opened: {}", midiDevice.getDeviceInfo().getName());
        if (midiDevice.isOpen()) {
            try {

                midiDevice.getTransmitter();
                if (ControllerManager.getMidiDeviceSelectionController() != null)
                    ControllerManager.getMidiDeviceSelectionController().setMidiDevice(midiDevice);

                midiDevice.close();
                logger.info("Connectable MidiDevice Closed: {}", midiDevice.getDeviceInfo().getName());
                if (ControllerManager.getMidiDeviceSelectionController() != null)
                    ControllerManager.getMidiDeviceSelectionController().unique();
                return true;

            } catch (MidiUnavailableException e) {
                logger.error("Unable to get Transmitter");
                logger.error(e);

                midiDevice.close();
                logger.info("Unconnectable MidiDevice Closed: {}", midiDevice.getDeviceInfo().getName());

                ControllerManager.getMidiDeviceSelectionController().unique();
                return false;
            }

        } else {
            ControllerManager.getMidiDeviceSelectionController().unique();
            return false;
        }
    }
}
