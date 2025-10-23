package com.alphagen.studio.pianocontroller.util;

import com.alphagen.studio.pianocontroller.ui.controllers.MainFrameController;
import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

public class WindowUtil {

    private static final Logger logger = LogManager.getLogger(WindowUtil.class);

    public static void quit() {
        if (MainFrameController.getReceiverThread() != null) {
            if (ControllerManager.getMainFrameController().getMidiDevice() != null) {
                ControllerManager.getMainFrameController().getMidiDevice().close();
            }
            MainFrameController.getReceiverThread().interrupt();
        }
        ControllerManager.getMidiDeviceReceiver().getKeyViewer().dispose();
        logger.info("Closing Piano Controller...");
        LogManager.shutdown();
        System.out.println("Quit Piano Controller.");
        Platform.exit();
    }

    public static void setCustomWindow(Stage stage, boolean quit, Pane root) {

        final Delta dragDelta = new Delta(); // contains x and y
        root.setOnMousePressed(event -> { // moving starts when mouse is pressed on the root
            dragDelta.x = event.getSceneX(); // gets initial position x
            dragDelta.y = event.getSceneY(); // gets initial position y
        });
        root.setOnMouseDragged(event -> { // drag scene
            Stage rootStage = (Stage) root.getScene().getWindow();
            rootStage.setX(event.getScreenX() - dragDelta.x); // change global x with delta x
            rootStage.setY(event.getScreenY() - dragDelta.y); // change global y with delta y
        });

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        String filepath = "/com/alphagen/studio/pianocontroller/images/app_logo_2.png";
        InputStream inputStream = WindowUtil.class.getResourceAsStream(filepath);
        if (inputStream == null) {
            logger.error("Unable to get App Logo.");
            quit();
        }
        stage.getIcons().add(new Image(inputStream));
        stage.setOnCloseRequest(event -> {
            if (quit) quit();
            else close(stage);
        });
    }

    public static void close(Node rootPane) {
        close((Stage) rootPane.getScene().getWindow());
    }

    public static void close(Stage stage) {
        ControllerManager.getMainFrameController().setSelectMidiUIOpen(false);
        logger.info("Closing Midi Selection Window");
        stage.close();
    }

    public static void closeSwing() {
    }
}

class Delta {
    double x, y;
}