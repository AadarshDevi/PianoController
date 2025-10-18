package com.alphagen.studio.pianocontroller.midi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MidiDeviceReceiver implements Receiver, Runnable {
    private static final Logger logger = LogManager.getLogger(MidiDeviceReceiver.class);

    public MidiDeviceReceiver(String name) {
    }

    @Override
    public void run() {

    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        ShortMessage sm = (ShortMessage) message;
        int cmd = sm.getCommand();
        int key = sm.getData1();
        int velocity = sm.getData2();
        if (velocity > 0) {
            System.out.println(key + ", " + cmd + ", " + velocity);
        }
    }

    @Override
    public void close() {

    }
}
