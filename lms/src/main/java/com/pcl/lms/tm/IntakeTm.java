package com.pcl.lms.tm;

import javafx.scene.control.Button;

import java.util.Date;

public class IntakeTm {
    private String id;
    private Date date;
    private String name;
    private String program;
    private Button btn;

    public IntakeTm() {
    }

    public IntakeTm(String id, Date date, String name, String program, Button btn) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.program = program;
        this.btn = btn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
