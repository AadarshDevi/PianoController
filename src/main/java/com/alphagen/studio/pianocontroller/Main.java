package com.alphagen.studio.pianocontroller;

import com.alphagen.studio.pianocontroller.ui.controllers.MainFrameController;
import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import com.alphagen.studio.pianocontroller.ui.managers.SceneManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void quit() {
        logger.info("Closing Piano Controller...");
        LogManager.shutdown();
        Platform.exit();
    }

    @Override
    public void start(Stage stage) throws Exception {

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setStage(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/App_V1.fxml"));
        VBox mainframe = fxmlLoader.load();
        MainFrameController mainFrameController = fxmlLoader.getController();

        ControllerManager.setMainFrameController(mainFrameController);

        Scene scene = new Scene(mainframe);
        scene.setOnKeyPressed(keyEvent -> {
            if (new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
                quit();
            }
        });

        stage.setScene(scene);
        stage.setTitle("Piano Controller Dev Build 2.1.0.1.0");
        stage.setResizable(true);
        stage.setMaxHeight(240);
        stage.show();
        stage.setOnCloseRequest(event -> {
            quit();
        });
    }
}
