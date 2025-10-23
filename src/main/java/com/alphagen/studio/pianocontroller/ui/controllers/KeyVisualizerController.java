package com.alphagen.studio.pianocontroller.ui.controllers;

import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import com.alphagen.studio.pianocontroller.util.WindowUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class KeyVisualizerController {

    private static final Logger logger = LogManager.getLogger(KeyVisualizerController.class);

    private final String[] keys = new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    @FXML private Label pianoKeyNum;
    @FXML private Label pianoKeyName;
    @FXML private Label keyboardKeyNum;
    @FXML private HBox mainPage;
    @FXML private HBox titleBar;
    @FXML private BorderPane basePane;
    @FXML private StackPane appLogoButton;
    @FXML private Button closeButton;
//    private MidiDeviceReceiver midiDeviceReceiver;
//    private LinkedBlockingQueue<Integer> lbq;

    @FXML public void initialize() {
//        midiDeviceReceiver = ControllerManager.getMidiDeviceReceiver();
//        lbq = midiDeviceReceiver.getPianoKeyList();

        HashMap<Integer, Integer> keymap = ControllerManager.getMainFrameController().getSaveReader().getKeyMap();

//        Thread thread = new Thread(new Runnable() {
//            @Override public void run() {
//                while (!Thread.currentThread().isInterrupted()) {
//                    Integer pk = lbq.peek();
//                    if (pk != null) {
//                        int kk = keymap.get(pk);
//                        logger.info("pk: {}, kk: {}", pk, kk);
//                        pianoKeyName.setText(keys[pk % 12]);
//                        pianoKeyNum.setText(Integer.toString(pk));
//                        keyboardKeyNum.setText(Integer.toString(kk));
//                    }
//                }
//            }
//        });
//        thread.setDaemon(true);
//        thread.start();

    }

    @FXML public void close() {
        ControllerManager.getMainFrameController().stopApplication();
        WindowUtil.close(basePane);
    }

    public void setKey(int pianoKey, int keyboardKey) {
        Platform.runLater(() -> {
            try {
//                Integer pk = lbq.peek();
//                if (pk != null) {
                logger.info("pk: {}, kk: {}", pianoKey, keyboardKey);
                pianoKeyName.setText(keys[pianoKey % 12]);
                pianoKeyNum.setText(Integer.toString(pianoKey));
                keyboardKeyNum.setText(Integer.toString(keyboardKey));
//                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Pane getRoot() {
        return titleBar;
    }
}
