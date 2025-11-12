package com.pcl.lms.controller;

import com.pcl.lms.DB.Database;
import com.pcl.lms.model.Student;
import com.pcl.lms.tm.StudentTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class StudentManagementFormController {

    public AnchorPane context;
    public TextField txtStudentID;
    public TextField txtStudentName;
    public TextField txtAddress;
    public DatePicker dteDOB;
    public TextField txtSearch;
    public Button btnSave;
    public TableView<StudentTM> tblStudent;
    public TableColumn<StudentTM,String> colID;
    public TableColumn<StudentTM,String> colName;
    public TableColumn<StudentTM,String> colAddress;
    public TableColumn<StudentTM,Date> colDoB;
    public TableColumn<StudentTM,Button> colOption;

    public void initialize(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDoB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setStudentId();
        setTableData();

        tblStudent.getSelectionModel().selectedItemProperty().addListener(
                (observable,oldValue,newValue)->{
                    if (newValue!=null){
                        setData((StudentTM)newValue);
                    }
                }
        );
    }

    private void setData(StudentTM newValue) {
        txtStudentID.setText(newValue.getId());
        txtStudentName.setText(newValue.getName());
        txtAddress.setText(newValue.getAddress());

        dteDOB.setValue(LocalDate.parse(newValue.getDob()));
        btnSave.setText("Update");

    }

    private void setTableData() {
        ObservableList<StudentTM> studentTM= FXCollections.observableArrayList();
        for (Student st:Database.studentTable){
            Button btn=new Button("Delete");
            StudentTM tm=new StudentTM(
                    st.getStudentId(),
                    st.getStudentName(),
                    st.getStudentAddress(),
                    new SimpleDateFormat("yyyy-MM-dd").format(st.getDob()),
                    btn
            );
            btn.setOnAction(event->{
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete ",
                        ButtonType.YES,ButtonType.NO);
                alert.showAndWait();

                if (alert.getResult()==ButtonType.YES){
                    Database.studentTable.remove(st);
                    new Alert(Alert.AlertType.INFORMATION,"Deleted Successfully...").show();
                    setTableData();
                    setStudentId();
                }
            });
            studentTM.add(tm);
        }
        tblStudent.setItems(studentTM);
    }

    public void saveOnAction(ActionEvent actionEvent) {
        Student student=new Student(
                txtStudentID.getText(),
                txtStudentName.getText(),
                txtAddress.getText(),
                Date.from(dteDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
        if (btnSave.getText().equals("Save")){
            Database.studentTable.add(student);
            System.out.println(student.toString());
            setStudentId();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION,"Student saved...").show();
            setTableData();
        }else {
            Optional<Student> selectedStudent=Database.studentTable.stream().filter(e->e.getStudentId().
                    equals(txtStudentID.getText())).findFirst();
            if (selectedStudent.isPresent()){
                selectedStudent.get().setStudentName(txtStudentName.getText());
                selectedStudent.get().setStudentAddress(txtAddress.getText());
                selectedStudent.get().setDob(Date.from(dteDOB.getValue().atStartOfDay(ZoneId.systemDefault()).
                        toInstant()));
                new Alert(Alert.AlertType.INFORMATION,"Student updated").show();
                setStudentId();
                clearFields();
                setTableData();
                btnSave.setText("Save");
            }
        }


    }
    private void setStudentId() {
        if (!Database.studentTable.isEmpty()){
            //id generate
            Student lastStudent=Database.studentTable.get(Database.studentTable.size()-1);
            String lastStudentId=lastStudent.getStudentId();
            String[] splitData=lastStudentId.split("-");
            String lastCharacter=splitData[1];
            int lastDigit=Integer.parseInt(lastCharacter);
            lastDigit++;
            String generatedId="s-"+lastDigit;
            txtStudentID.setText(generatedId);
        }else {
            txtStudentID.setText("s-1");
        }
    }
    private void clearFields(){
        txtStudentName.clear();
        txtAddress.clear();
        dteDOB.setValue(null);
    }

}
