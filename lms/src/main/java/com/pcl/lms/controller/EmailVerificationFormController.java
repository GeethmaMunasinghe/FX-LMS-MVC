package com.pcl.lms.controller;

import com.pcl.lms.env.StaticResource;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class EmailVerificationFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;

    public void initialize(){
        setStaticData();
    }

    private void setStaticData() {
        lblCompany.setText(StaticResource.getCompany());
        lblVersion.setText(StaticResource.getVersion());
    }

    public void navigateVerifyCodeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("VerifyOTPForm");
    }
    private void setUI(String location) throws IOException {
        /*URL resource=getClass().getResource("/com/pcl/lms/"+location+".fxml");
        Parent load= FXMLLoader.load(resource);
        Scene scene=new Scene(load);
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(scene);*/
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(new Scene((FXMLLoader.load(getClass().getResource("/com/pcl/lms/"+location+".fxml")))));
    }
}
