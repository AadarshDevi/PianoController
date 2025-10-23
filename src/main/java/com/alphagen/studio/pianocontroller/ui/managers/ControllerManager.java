package com.alphagen.studio.pianocontroller.ui.managers;

import com.alphagen.studio.pianocontroller.midi.MidiDeviceReceiver;
import com.alphagen.studio.pianocontroller.ui.controllers.KeyVisualizerController;
import com.alphagen.studio.pianocontroller.ui.controllers.MainFrameController;
import com.alphagen.studio.pianocontroller.ui.controllers.MidiDeviceSelectionController;

public class ControllerManager {

    private static MainFrameController mainFrameController = null;
    private static ControllerManager controllerManager = null;
    private static MidiDeviceSelectionController midiDeviceSelectionController = null;
    private static KeyVisualizerController keyVisualizerController = null;
    private static MidiDeviceReceiver midiDeviceReceiver;

    public static MainFrameController getMainFrameController() {
        return mainFrameController;
    }

    public static void setMainFrameController(MainFrameController mainFrameController) {
        if (mainFrameController != null)
            ControllerManager.mainFrameController = mainFrameController;
    }

    public static MidiDeviceSelectionController getMidiDeviceSelectionController() {
        return midiDeviceSelectionController;
    }

    public static void setMidiDeviceSelectionController(MidiDeviceSelectionController midiDeviceSelectionController) {
        if (mainFrameController != null)
            ControllerManager.midiDeviceSelectionController = midiDeviceSelectionController;
    }

    public static KeyVisualizerController getKeyVisualizerController() {
        return keyVisualizerController;
    }

    public static void setKeyVisualizerController(KeyVisualizerController keyVisualizerController) {
        if (mainFrameController != null)
            ControllerManager.keyVisualizerController = keyVisualizerController;
    }

    public static void setMidiDeviceReceiver(MidiDeviceReceiver midiDeviceReceiver) {
        ControllerManager.midiDeviceReceiver = midiDeviceReceiver;
    }

    public static MidiDeviceReceiver getMidiDeviceReceiver() {
        return midiDeviceReceiver;
    }
}
