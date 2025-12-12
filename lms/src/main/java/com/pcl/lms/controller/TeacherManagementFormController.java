package com.pcl.lms.controller;

import com.pcl.lms.DB.Database;
import com.pcl.lms.DB.DbConnection;
import com.pcl.lms.model.Teacher;
import com.pcl.lms.tm.TeacherTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TeacherManagementFormController {

    public AnchorPane context;
    public TextField txtTeacherID;
    public TextField txtTeacherName;
    public TextField txtContact;
    public Button btnSave;
    public TextField txtAddress;
    public TextField txtSearch;
    public TableView<TeacherTM> tblTeacher;
    public TableColumn<TeacherTM,String> colID;
    public TableColumn<TeacherTM,String> colName;
    public TableColumn<TeacherTM,String> colContact;
    public TableColumn<TeacherTM,String> colAddress;
    public TableColumn<TeacherTM,Button> colOption;
    String searchText="";
    public void initialize(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setTeacherId();
        setTeacherData(searchText);

        txtSearch.textProperty().addListener((observable,oldValue,newValue)-> {
            if (newValue!=null){
                this.searchText=newValue;
                setTeacherData(searchText);
            }
        });
        tblTeacher.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->{
            if (newValue!=null){
                setData((TeacherTM)newValue);
            }
        });
    }

    private void setData(TeacherTM tm) {
        txtTeacherID.setText(tm.getId());
        txtTeacherName.setText(tm.getName());
        txtContact.setText(tm.getContact());
        txtAddress.setText(tm.getAddress());
        btnSave.setText("Update");
    }

    private void setTeacherData(String searchText) {
        //load the data into the table
        ObservableList<TeacherTM>teacherObList= FXCollections.observableArrayList();
        for (Teacher teacher:Database.teacherTable){
            if (teacher.getId().toLowerCase().contains(searchText.toLowerCase())){
                Button btn=new Button("Delete");
                TeacherTM teacherTM=new TeacherTM(
                        teacher.getId(),
                        teacher.getName(),
                        teacher.getAddress(),
                        teacher.getContact(),
                        btn
                );
                btn.setOnAction((event)->{
                    Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete?",ButtonType.YES,
                            ButtonType.NO);
                    alert.showAndWait();
                    if (alert.getResult()==ButtonType.YES){
                        Database.teacherTable.remove(teacher);
                        setTeacherData(searchText);
                        setTeacherId();
                        new Alert(Alert.AlertType.INFORMATION,"Deleted Successfully").show();
                    }
                });
                teacherObList.add(teacherTM);
            }

        }
        tblTeacher.setItems(teacherObList);
    }

    private void setTeacherId() {
        try {
            String lastTeacher=getLastTeacherId();
            if (lastTeacher!=null){
                String[] splittedTeacherId=lastTeacher.split("-");
                String lastCharacterAsString=splittedTeacherId[1];
                int lastDigit=Integer.parseInt(lastCharacterAsString);
                lastDigit++;
                String generatedId="T-"+lastDigit;
                txtTeacherID.setText(generatedId);
            }else {
                txtTeacherID.setText("T-1");
            }
        }catch (SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private String getLastTeacherId() throws SQLException, ClassNotFoundException {
        Connection connection =DbConnection.getInstance().getConnection();
        PreparedStatement ps =connection.prepareStatement(
                "SELECT id FROM teacher ORDER BY CAST(SUBSTRING(id,3)AS UNSIGNED)DESC LIMIT 1");//s-1 --> 1 is represent in 3
        ResultSet set=ps.executeQuery();
        if (set.next()){
            return set.getString(1);
        }else {
            return null;
        }
    }

    public void saveOnAction(ActionEvent actionEvent) {
        Teacher teacher=new Teacher(
                txtTeacherID.getText(),
                txtTeacherName.getText(),
                txtContact.getText(),
                txtAddress.getText()
        );
        try {
            if (btnSave.getText().equals("Save")){
                //Save functionality
                boolean isSaved=saveTeacher(teacher);
                if (isSaved){
                    setTeacherId();
                    setTeacherData(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION,"Teacher saved...").show();
                }

            }else {
                //Update functionality
                Optional<Teacher> selectedTeacher =Database.teacherTable.stream().filter(e->e.getId().
                        equals(teacher.getId())).findFirst();
                if (!selectedTeacher.isPresent()){
                    new Alert(Alert.AlertType.INFORMATION,"Teacher not found").show();
                    return;
                }
                selectedTeacher.get().setName(teacher.getName());
                selectedTeacher.get().setAddress(teacher.getAddress());
                selectedTeacher.get().setContact(teacher.getContact());
                setTeacherData(searchText);
                setTeacherId();
                clearFields();
                setTeacherId();
                btnSave.setText("Save");
                new Alert(Alert.AlertType.INFORMATION,"Teacher Updated").show();
            }
        }catch (SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private boolean saveTeacher(Teacher teacher) throws SQLException, ClassNotFoundException {
        Connection connection=DbConnection.getInstance().getConnection();
        PreparedStatement ps=connection.prepareStatement("INSERT INTO teacher VALUES (?,?,?,?)");
        ps.setString(1,teacher.getId());
        ps.setString(2,teacher.getName());
        ps.setString(3,teacher.getContact());
        ps.setString(4,teacher.getAddress());

        return ps.executeUpdate()>0;
    }

    private void clearFields() {
        txtTeacherName.clear();
        txtContact.clear();
        txtAddress.clear();
    }

    public void newTeacherOnAction(ActionEvent actionEvent) {
        clearFields();
        btnSave.setText("Save");
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }
    private void setUI(String location) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(new Scene((FXMLLoader.load(getClass().getResource("/com/pcl/lms/"+location+".fxml")))));
    }
}
