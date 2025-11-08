package com.pcl.lms.controller;

import com.pcl.lms.DB.Database;
import com.pcl.lms.env.StaticResource;
import com.pcl.lms.model.User;
import com.pcl.lms.util.security.PasswordManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class LoginFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;
    public TextField txtEmail;
    public PasswordField txtPassword;

    public void initialize(){
        setStaticData();
    }

    private void setStaticData() {
        lblCompany.setText(StaticResource.getCompany());
        lblVersion.setText(StaticResource.getVersion());
    }


    public void navigateForgotPwOnAction(ActionEvent actionEvent) throws IOException {
        setUI("EmailVerificationForm");
    }

    public void navigateDashboardOnAction(ActionEvent actionEvent) throws IOException {
        String email=txtEmail.getText();
        String password=txtPassword.getText();
        Optional<User>selectUser= Database.userTable.stream().filter(e->e.getEmail().equals(email)).findFirst();

        if (selectUser.isPresent()){
            if (new PasswordManager().check(password,selectUser.get().getPassword())){
                new Alert(Alert.AlertType.INFORMATION,"Welcome...").show();
                setUI("DashboardForm");
            }else {
                new Alert(Alert.AlertType.ERROR,"Incorrect Password !!!").show();
            }
        }else {
            new Alert(Alert.AlertType.ERROR,"User not found...").show();
        }

    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("WelcomeScreenForm");
    }
    private void setUI(String location) throws IOException {
        URL resource=getClass().getResource("/com/pcl/lms/"+location+".fxml");
        Parent load= FXMLLoader.load(resource);
        Scene scene=new Scene(load);
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(scene);
    }
}
