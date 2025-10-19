package com.alphagen.studio.pianocontroller.midi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class MidiDeviceReceiver implements Receiver, Runnable {
    private static final Logger logger = LogManager.getLogger(MidiDeviceReceiver.class);
    private Robot robot;
    private HashMap<Integer, Integer> keyMap;
    private HashMap<Integer, Integer> mouseMap;

    public MidiDeviceReceiver(String name) {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            logger.error(e);
        }
        keyMap = new HashMap<>();
        mouseMap = new HashMap<>();
    }

    @Override public void run() {
    }

    @Override public void send(MidiMessage message, long timeStamp) {
        ShortMessage sm = (ShortMessage) message;
        int cmd = sm.getCommand();
        int key = sm.getData1();
        int velocity = sm.getData2();
        if (cmd == ShortMessage.NOTE_ON && velocity > 0) {
            System.out.println("pressed: " + key);
            if (keyMap.containsKey(key)) {
                System.out.println("key: " + key + ", value: " + keyMap.get(key));
                robot.keyPress(keyMap.get(key));
            } else {
                System.out.println("value of key not found: " + key);
            }
        } else if (cmd == ShortMessage.NOTE_OFF) {
            robot.delay(50);
            System.out.println("released: " + key);
            if (keyMap.containsKey(key)) robot.keyRelease(keyMap.get(key));
        }
    }

    @Override public void close() {

    }
}
