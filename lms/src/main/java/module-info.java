module com.pcl.lms.lms {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Allow FXML and JavaFX reflection access
    opens com.pcl.lms to javafx.fxml, javafx.graphics;
    opens com.pcl.lms.controller to javafx.fxml;

    // Export your base package (main entry point)
    exports com.pcl.lms;
}
