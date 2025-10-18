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

/**
 * This is the JavaFX app that will set up all the variables and values that will need
 * to be accessed immediately.
 */
public class Main extends Application {

    /**
     * the logger for the class
     */
    private static final Logger logger = LogManager.getLogger(Main.class);

    /**
     * This is the main method to stop and quit the app entirely. Stops all
     * threads, closes all midi devices and gracefully stops the app
     * and quit.
     */
    public static void quit() {
        if (MainFrameController.getReceiverThread() != null) {
            ControllerManager.getMainFrameController().getMidiDevice().close();
            MainFrameController.getReceiverThread().interrupt();
        }
        logger.info("Closing Piano Controller...");
        LogManager.shutdown();
        System.out.println("Quit Piano Controller.");
        Platform.exit();
    }

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
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/App_V1.fxml"));
        VBox mainframe = fxmlLoader.load();
        MainFrameController mainFrameController = fxmlLoader.getController();

        // makes the main controller accessible to all the controllers
        ControllerManager.setMainFrameController(mainFrameController);

        // sets up the scene with app quit command
        Scene scene = new Scene(mainframe);
        scene.setOnKeyPressed(keyEvent -> {
            if (new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN).match(keyEvent)) {
                quit();
            }
        });

        // setting up stage for use
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
