package com.pcl.lms.controller;

import com.pcl.lms.env.StaticResource;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;
    public Label lblDate;
    public Label lblTime;
    String userEmail;
    public void initialize(){
        setData();
    }

    private void setData() {
        lblCompany.setText(StaticResource.getCompany());
        lblVersion.setText(StaticResource.getVersion());

        String dateFormat=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        lblDate.setText(dateFormat);

        Timeline clock=new Timeline(new KeyFrame(Duration.seconds(1),e->{
            String timeFormat=new SimpleDateFormat("HH:mm:ss").format(new Date());
            lblTime.setText(timeFormat);
        }));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    public void logoutOnAction(ActionEvent actionEvent) throws IOException {
        setUI("LoginForm");
    }
    public void studentManageOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/pcl/lms/StudentManagementForm.fxml"));
        Parent parent=loader.load();
        StudentManagementFormController controller=loader.getController();
        controller.setUserEmail(userEmail);
        Stage stage=(Stage)context.getScene().getWindow();
        stage.setScene(new Scene(parent));
    }
    public void navigateTeacherFormOnAction(ActionEvent actionEvent) throws IOException {
        setUI("TeacherManagementForm");
    }

    private void setUI(String location) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(new Scene((FXMLLoader.load(getClass().getResource("/com/pcl/lms/"+location+".fxml")))));
    }


    public void navigateProgramFormOnAction(ActionEvent actionEvent) throws IOException {
        setUI("ProgramManagementForm");
    }

    public void navigateIntakeFormOnAction(ActionEvent actionEvent) throws IOException {
        setUI("IntakeManagementForm");
    }

    public void navigateStudentRegOnAction(ActionEvent actionEvent) throws IOException {
        setUI("RegistrationForm");
    }
    public void setData(String userEmail){
        this.userEmail=userEmail;
    }
}
