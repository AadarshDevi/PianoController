package com.alphagen.studio.pianocontroller.midi;

import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class MidiDeviceReceiver implements Receiver, Runnable {
    private static final Logger logger = LogManager.getLogger(MidiDeviceReceiver.class);

    public MidiDeviceReceiver(String name) {
    }

    @Override
    public void run() {

    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (ControllerManager.getMidiDeviceSelectionController().isTestingConnection()) {
            logger.info(message);
        }
    }

    @Override
    public void close() {

    }
}
