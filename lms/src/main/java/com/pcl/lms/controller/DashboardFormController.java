package com.pcl.lms.controller;

import com.pcl.lms.env.StaticResource;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;
    public Label lblDate;
    public Label lblTime;
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
}
