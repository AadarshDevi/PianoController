package com.alphagen.studio.pianocontroller.ui.managers;

import javafx.stage.Stage;

public class SceneManager {

    private static SceneManager sceneManager;
    private Stage stage;

    public static SceneManager getInstance() {
        if (sceneManager == null) sceneManager = new SceneManager();
        return sceneManager;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
