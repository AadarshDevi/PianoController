package com.alphagen.studio.pianocontroller.ui.managers;

import com.alphagen.studio.pianocontroller.ui.controllers.MainFrameController;
import com.alphagen.studio.pianocontroller.ui.controllers.MidiDeviceSelectionController;

public class ControllerManager {

    private static MainFrameController mainFrameController;
    private static ControllerManager controllerManager;
    private static MidiDeviceSelectionController midiDeviceSelectionController;

    public static MainFrameController getMainFrameController() {
        return mainFrameController;
    }

    public static void setMainFrameController(MainFrameController mainFrameController) {
        ControllerManager.mainFrameController = mainFrameController;
    }

    public static void setMidiDeviceSelectionController(MidiDeviceSelectionController midiDeviceSelectionController) {
        ControllerManager.midiDeviceSelectionController = midiDeviceSelectionController;
    }

    public static MidiDeviceSelectionController getMidiDeviceSelectionController() {
        return midiDeviceSelectionController;
    }

    //    public static ControllerManager getInstance() {
//        if (controllerManager == null) controllerManager = new ControllerManager();
//        return controllerManager;
//    }
}
