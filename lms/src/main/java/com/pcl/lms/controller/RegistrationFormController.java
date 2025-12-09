package com.pcl.lms.controller;

import com.pcl.lms.DB.Database;
import com.pcl.lms.model.Programme;
import com.pcl.lms.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationFormController {

    public AnchorPane context;
    public TextField txtId;
    public Button btnSave;
    public ComboBox<String> cmbProgram;
    public RadioButton rbtnPaid;
    public ToggleGroup rbtnPayment;
    public ComboBox<String> cmbStudent;
    public RadioButton rbtnUnpaid;
    public TextField txtSearch;
    String searchText="";

    public void initialize(){
        setStudentId();
        setStudentData(searchText);
        txtSearch.textProperty().addListener((observable,oldValue,newValue)->{
            if (newValue!=null){
                this.searchText=newValue;
                setStudentData(searchText);
                cmbStudent.show();
            }
        });
        cmbStudent.valueProperty().addListener((observable,oldValue,newValue)->{
            setStudentId();
        });
        setProgramData();
    }

    private void setStudentId() {
        if (cmbStudent.getValue()==null){
            txtId.setText("Select student");
        }else {
            String studentComboValue=cmbStudent.getValue();
            String[] splittedComboValue=studentComboValue.split("-");
            txtId.setText(splittedComboValue[0]+"-"+splittedComboValue[1]);
        }
    }

    private void setStudentData(String searchText) {
        ObservableList<String> studentObList=FXCollections.observableArrayList();
        studentObList.clear();
        if (!Database.studentTable.isEmpty()){
            for (Student st:Database.studentTable){
                if (st.getStudentName().toLowerCase().contains(searchText.toLowerCase())){
                    studentObList.add(st.getStudentId()+"-"+st.getStudentName());
                }
            }
            cmbStudent.setItems(studentObList);
        }
    }

    private void setProgramData() {
        ObservableList<String> programObList= FXCollections.observableArrayList();
        programObList.clear();
        if (!Database.programmeTable.isEmpty()){
            for (Programme programme:Database.programmeTable){
                programObList.add(programme.getProgrammeId()+"-"+programme.getProgrammeName());
            }
            cmbProgram.setItems(programObList);
        }else {
            cmbProgram.setValue("Programs not found!");
        }
    }

    public void newRegistrationOnAction(ActionEvent actionEvent) {
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
