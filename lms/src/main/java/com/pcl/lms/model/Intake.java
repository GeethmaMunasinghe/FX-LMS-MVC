package com.pcl.lms.model;

import java.util.Date;

public class Intake {
    private String id;
    private Date date;
    private String name;
    private String program;

    public Intake() {
    }

    public Intake(String id, Date date, String name, String program) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.program = program;
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
}
