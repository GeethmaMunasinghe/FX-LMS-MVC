package com.pcl.lms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;

    public void alreadyHaveAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUI("LoginForm");
    }

    public void signupOnAction(ActionEvent actionEvent) throws IOException {
        setUI("LoginForm");
    }
    private void setUI(String location) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(new Scene((FXMLLoader.load(getClass().getResource("/com/pcl/lms/"+location+".fxml")))));
    }
}
