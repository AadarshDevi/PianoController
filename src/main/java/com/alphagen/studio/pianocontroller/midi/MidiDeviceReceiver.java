package com.alphagen.studio.pianocontroller.midi;

import com.alphagen.studio.pianocontroller.ui.controllers.KeyVisualizerController;
import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import com.alphagen.studio.pianocontroller.ui.swing.KeyVisualizer;
import com.alphagen.studio.pianocontroller.util.WindowUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MidiDeviceReceiver implements Receiver, Runnable {
    private static final Logger logger = LogManager.getLogger(MidiDeviceReceiver.class);
    //    public final LinkedBlockingQueue<Integer> blockingQueue;
    private final String[] keys = new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private final HashMap<Integer, Integer> keyMap;
    //    private final HashMap<Integer, Integer> mouseMap;
    private final KeyVisualizerController keyVisualizerController;
    //    public JFrame jFrame;
    private KeyVisualizer visualizer;
    private Robot robot;

    public MidiDeviceReceiver(String name) {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            logger.error(e);
            WindowUtil.quit();
        }

        visualizer = new KeyVisualizer();

        keyMap = ControllerManager.getMainFrameController().getSaveReader().getKeyMap();
//        mouseMap = new HashMap<>();
//        blockingQueue = new LinkedBlockingQueue<>();
        keyVisualizerController = ControllerManager.getKeyVisualizerController();
    }

    @Override public void run() {
    }

    @Override public void send(MidiMessage message, long timeStamp) {
        ShortMessage sm = (ShortMessage) message;
        int cmd = sm.getCommand();
        int key = sm.getData1();
        int velocity = sm.getData2();
        if (cmd == ShortMessage.NOTE_ON && velocity > 0 && keyMap.containsKey(key)) {
            int value = keyMap.get(key);
            robot.keyPress(value);
            logger.info("key: {}, value: {}", key, value);

            SwingUtilities.invokeLater(() -> {
                visualizer.getPianoKeyNumLabel().setText("PK#: " + key);
                visualizer.getPianoKeyNameLabel().setText("KN: " + keys[key % 12]);
                visualizer.getKeyboardKeyNumLabel().setText("KK#: " + value);
            });

//            try {
//                blockingQueue.put(key);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            keyVisualizerController.setKey(key, keyMap.get(key));
            logger.info("keyPressed");
        } else if (keyMap.containsKey(key)) {
            logger.info("released key: {}", key);
            robot.keyRelease(keyMap.get(key));
        }
    }

//    public LinkedBlockingQueue<Integer> getPianoKeyList() {
//        return blockingQueue;
//    }

    @Override public void close() {

    }

    public JFrame getKeyViewer() {
        return visualizer.getRoot();
    }
}
