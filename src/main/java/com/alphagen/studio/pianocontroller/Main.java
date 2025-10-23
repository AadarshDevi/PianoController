package com.alphagen.studio.pianocontroller;

import com.alphagen.studio.pianocontroller.ui.controllers.MainFrameController;
import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import com.alphagen.studio.pianocontroller.ui.managers.SceneManager;
import com.alphagen.studio.pianocontroller.util.WindowUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is the JavaFX app that will set up all the variables and values that will need
 * to be accessed immediately.
 */
public class Main extends Application {

    /**
     * the logger for the class
     */
    private static final Logger logger = LogManager.getLogger(Main.class);

    private static final boolean testCSS = true;

    /**
     * This is the method that will set up the main ui and its controller along
     * with some other window related stuff.
     *
     * @param stage the main window to be used
     * @throws Exception thrown if the main fxml is not found
     */
    @Override
    public void start(Stage stage) throws Exception {

        // set up scene manager if window will change a lot of scenes
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setStage(stage);

        // loads the main ui and the controller
        FXMLLoader fxmlLoader;
        if (testCSS) fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/App_V4.fxml"));
        else fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/App_V3.fxml"));
        BorderPane mainframe = fxmlLoader.load();
        MainFrameController mainFrameController = fxmlLoader.getController();

        // makes the main controller accessible to all the controllers
        ControllerManager.setMainFrameController(mainFrameController);

        // sets up the scene with app quit command
        Scene scene = new Scene(mainframe);
        if (testCSS) scene.getStylesheets().add(Main.class.getResource("css/stylesheet.css").toExternalForm());
        else scene.getStylesheets().add(Main.class.getResource("css/app_v1_stylesheet.css").toExternalForm());
        scene.setOnKeyPressed(keyEvent -> {
            if (new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
                WindowUtil.quit();
            }
        });

        // setting up stage for use
        stage.setScene(scene);
        stage.setTitle("Piano Controller Dev Build 2.1.0.1.0");
        WindowUtil.setCustomWindow(stage, true, mainFrameController.getRoot());

        stage.show();
    }

    public Pane getRoot() {
        return null;
    }
}
