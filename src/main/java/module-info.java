module com.alphagen.studio.pianocontroller {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    requires JFileSystem;
    requires javafx.base;


    exports com.alphagen.studio.pianocontroller;
    exports com.alphagen.studio.pianocontroller.ui.managers to javafx.fxml;
    exports com.alphagen.studio.pianocontroller.ui.controllers to javafx.fxml;
    exports com.alphagen.studio.pianocontroller.util to javafx.fxml;


    opens com.alphagen.studio.pianocontroller to javafx.fxml;
    opens com.alphagen.studio.pianocontroller.ui.controllers to javafx.fxml;
    opens com.alphagen.studio.pianocontroller.ui.controllers.modules to javafx.fxml;
    opens com.alphagen.studio.pianocontroller.ui.managers to javafx.fxml;
    opens com.alphagen.studio.pianocontroller.util to javafx.fxml;


}