package com.pcl.lms.controller;

import com.pcl.lms.DB.Database;
import com.pcl.lms.model.Programme;
import com.pcl.lms.model.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ProgramManagementFormController {

    public TextField txtProgramId;
    public TextField txtProgramName;
    public TextField txtCost;
    public ComboBox <String> cbxTeacher;
    public TextField txtModules;
    public TableView tblModule;
    public TableColumn colModuleId;
    public TableColumn colModuleName;
    public TableColumn colModuleRemove;
    public Button btnSave;
    public TableView tblProgram;
    public TableColumn colProgramId;
    public TableColumn colProgramName;
    public TableColumn colTeacher;
    public TableColumn colModuleList;
    public TableColumn colCost;
    public TableColumn colOption;
    public TextField txtSearch;
    public AnchorPane context;

    public void initialize(){
        setProgrammeId();
        setTeacher();
    }

    private void setTeacher() {
        ObservableList<String> list= FXCollections.observableArrayList();
        for (Teacher t: Database.teacherTable){
            list.add(t.getId().trim()+"-"+t.getName().trim());
        }
        cbxTeacher.setItems(list);
    }

    private void setProgrammeId() {

        if (!Database.programmeTable.isEmpty()){
            //generate id
            Programme programme =Database.programmeTable.get(Database.programmeTable.size()-1);
            String programmeId=programme.getProgrammeId();
            String[] splittedId=programmeId.split("-");
            String splittedLastCharacterAsString=splittedId[1];
            int lastDigit=Integer.parseInt(splittedLastCharacterAsString);
            lastDigit++;
            String generatedId="P-"+lastDigit;
            txtProgramId.setText(generatedId);
        }else {
            txtProgramId.setText("P-1");
        }
    }

    public void newProgramOnAction(ActionEvent actionEvent) {
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void saveOnAction(ActionEvent actionEvent) {

    }
    private void setUI(String location) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(new Scene((FXMLLoader.load(getClass().getResource("/com/pcl/lms/"+location+".fxml")))));
    }
}
