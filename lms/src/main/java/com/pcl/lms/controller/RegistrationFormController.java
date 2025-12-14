package com.pcl.lms.controller;

import com.pcl.lms.DB.Database;
import com.pcl.lms.DB.DbConnection;
import com.pcl.lms.model.Enroll;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        try {
            ObservableList<String> studentObList=fetchStudents(searchText);
            cmbStudent.setItems(studentObList);

        }catch (SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private ObservableList<String> fetchStudents(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<String> studentObList=FXCollections.observableArrayList();
        studentObList.clear();
        Connection connection=DbConnection.getInstance().getConnection();
        PreparedStatement ps=connection.prepareStatement("SELECT * FROM student WHERE name LIKE?");
        ps.setString(1,"%"+searchText+"%");
        ResultSet set=ps.executeQuery();
        while (set.next()){
            studentObList.add(set.getString(1)+"-"+set.getString(2));
        }
        return studentObList;
    }

    private void setProgramData() {
        try {
            ObservableList<String> programObList= fetchPrograms();
            if (!programObList.isEmpty()){
                cmbProgram.setItems(programObList);
            }
            else {
                cmbProgram.setValue("Programs not found!");
            }
        }catch (SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private ObservableList<String> fetchPrograms() throws SQLException, ClassNotFoundException {
        ObservableList<String> programObList=FXCollections.observableArrayList();
        Connection connection=DbConnection.getInstance().getConnection();
        PreparedStatement ps=connection.prepareStatement("SELECT * FROM program");
        ResultSet set=ps.executeQuery();
        while (set.next()){
            programObList.add(set.getString(1).trim()+"-"+set.getString(2).trim());
        }
        return programObList;
    }

    public void newRegistrationOnAction(ActionEvent actionEvent) {
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void saveOnAction(ActionEvent actionEvent) {
        try {
            boolean isSaved=saveEnrollment(new Enroll(
                    cmbStudent.getValue(),
                    cmbProgram.getValue(),
                    rbtnPaid.isSelected()
            ));
            if (isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Success").show();
            }
        }catch (SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private boolean saveEnrollment(Enroll enroll) throws SQLException, ClassNotFoundException {
        Connection connection=DbConnection.getInstance().getConnection();
        PreparedStatement ps=connection.prepareStatement("INSERT INTO enroll VALUES (?,?,?)");
        ps.setString(1,spliteID(enroll.getProgram()));
        ps.setString(2,spliteID(enroll.getStudent()));
        ps.setBoolean(3,enroll.isPaid());

        return ps.executeUpdate()>0;
    }

    private String spliteID(String program) {
        String[] split=program.split("-");
        return split[0]+"-"+split[1];
    }

    private void setUI(String location) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(new Scene((FXMLLoader.load(getClass().getResource("/com/pcl/lms/"+location+".fxml")))));
    }
}
