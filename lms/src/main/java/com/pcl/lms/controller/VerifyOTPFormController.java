package com.pcl.lms.controller;

import com.pcl.lms.env.StaticResource;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class VerifyOTPFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;
    public Label lblUserEmail;
    public TextField txtOTP;
    private int OTP;
    private String email;

    public void initialize(){
        setLabelData();
    }

    private void setLabelData() {
        lblCompany.setText(StaticResource.getCompany());
        lblVersion.setText(StaticResource.getVersion());
        lblUserEmail.setText(email);
    }

    public void navigateEmailVerificationFormOnAction(ActionEvent actionEvent) throws IOException {
        setUI("EmailVerificationForm");
    }
    public void setUserData(int verificationCode, String email) {
        this.email=email;
        this.OTP=verificationCode;

    }

    public void navigatePasswordFormOnAction(ActionEvent actionEvent) throws IOException {
        if (OTP==Integer.parseInt(txtOTP.getText())){
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/com/pcl/lms/ResetPasswordForm.fxml"));
            Parent load=fxmlLoader.load();
            ResetPasswordFormController controller=fxmlLoader.getController();
            controller.SetUserData(email);
            Stage stage=(Stage) context.getScene().getWindow();
            stage.setScene(new Scene(load));
        }else {
            new Alert(Alert.AlertType.ERROR,"Invalid OTP").show();
        }

    }
    private void setUI(String location) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(new Scene((FXMLLoader.load(getClass().getResource("/com/pcl/lms/"+location+".fxml")))));
    }


}
