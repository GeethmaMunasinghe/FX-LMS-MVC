package com.pcl.lms.controller;

import com.pcl.lms.DB.Database;
import com.pcl.lms.DB.DbConnection;
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
import java.sql.*;
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
        try {
            boolean login=loginWithMysql(email,password);
            if (login){
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/pcl/lms/DashboardForm.fxml"));
                Parent load=loader.load();
                DashboardFormController dashboardController=loader.getController();
                dashboardController.setData(email);
                Stage stage=(Stage) context.getScene().getWindow();
                stage.setScene(new Scene(load));

                new Alert(Alert.AlertType.INFORMATION,"Welcome"+email).show();
            }else {
                new Alert(Alert.AlertType.INFORMATION,"Something went wrong"+email).show();

            }

        }catch (ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }


    }

    private boolean loginWithMysql(String email, String password) throws ClassNotFoundException, SQLException {
        Connection connection=DbConnection.getInstance().getConnection();
        String sql="SELECT email,password FROM user WHERE email=?";
        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setString(1,email);
        ResultSet set=ps.executeQuery();
        if (set.next()){
            if (new PasswordManager().check(password,set.getString("password"))){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
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
