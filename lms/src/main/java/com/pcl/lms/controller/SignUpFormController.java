package com.pcl.lms.controller;

import com.pcl.lms.DB.Database;
import com.pcl.lms.DB.DbConnection;
import com.pcl.lms.env.StaticResource;
import com.pcl.lms.model.User;
import com.pcl.lms.util.security.PasswordManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;
    public TextField txtFullName;
    public TextField txtAge;
    public TextField txtEmail;
    public PasswordField txtPassword;
    public void initialize(){
        setStaticData();
    }

    private void setStaticData() {
        lblCompany.setText(StaticResource.getCompany());
        lblVersion.setText(StaticResource.getVersion());
    }

    public void alreadyHaveAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUI("LoginForm");
    }

    public void signupOnAction(ActionEvent actionEvent) throws IOException {
        String email=txtEmail.getText();
        String fullname=txtFullName.getText();
        int age= Integer.parseInt(txtAge.getText());
        String password=new PasswordManager().encode(txtPassword.getText());

        User user=new User(fullname,email,age,password);
        try{
            signup(user);
            System.out.println(user.toString());

            new Alert(Alert.AlertType.INFORMATION,"Account Created").show();
            setUI("LoginForm");
        }catch (ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
    }
    private boolean signup(User user) throws ClassNotFoundException, SQLException {
        Connection connection=DbConnection.getInstance().getConnection();
        String sql="INSERT INTO user VALUES(?,?,?,?)"; //Blind parameters

        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setString(1,user.getEmail());
        ps.setString(2,user.getFullName());
        ps.setInt(3,user.getAge());
        ps.setString(4,user.getPassword());

        return ps.executeUpdate()>0; //affected rows count
    }
    private void setUI(String location) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(new Scene((FXMLLoader.load(getClass().getResource("/com/pcl/lms/"+location+".fxml")))));
    }
}
