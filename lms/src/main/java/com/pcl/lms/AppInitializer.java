package com.pcl.lms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppInitializer extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/pcl/lms/WelcomeScreenForm.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("WELCOME TO THE LMS");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
