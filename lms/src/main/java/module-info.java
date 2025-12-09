module com.pcl.lms.lms {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jbcrypt;
    requires jakarta.mail;
    requires jakarta.activation;
    requires java.sql;
    requires mysql.connector.j;

    // Allow FXML and JavaFX reflection access
    opens com.pcl.lms to javafx.fxml, javafx.graphics;
    opens com.pcl.lms.controller to javafx.fxml;
    opens com.pcl.lms.tm to javafx.base;

    // Export your base package (main entry point)
    exports com.pcl.lms;
}
