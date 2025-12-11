package com.pcl.lms.controller;

import com.pcl.lms.DB.Database;
import com.pcl.lms.DB.DbConnection;
import com.pcl.lms.model.Student;
import com.pcl.lms.tm.StudentTM;
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
    String searchText="";
    String userEmail;

    public void initialize(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDoB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setStudentId();
        setTableData(searchText);

        tblStudent.getSelectionModel().selectedItemProperty().addListener(
                (observable,oldValue,newValue)->{
                    if (newValue!=null){
                        setData((StudentTM)newValue);
                    }
                }
        );
        txtSearch.textProperty().addListener((observable,oldValue,newValue)->{
            this.searchText=newValue;
            setTableData(newValue);
        });
    }

    private void setData(StudentTM newValue) {
        txtStudentID.setText(newValue.getId());
        txtStudentName.setText(newValue.getName());
        txtAddress.setText(newValue.getAddress());

        dteDOB.setValue(LocalDate.parse(newValue.getDob()));
        btnSave.setText("Update");

    }

    private void setTableData(String newValue) {
        ObservableList<StudentTM> studentTM= FXCollections.observableArrayList();
        for (Student st:Database.studentTable){
            if (st.getStudentName().contains(newValue)){
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
                        setTableData(searchText);
                        setStudentId();
                    }
                });
                studentTM.add(tm);
            }

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
        try {
            if (btnSave.getText().equals("Save")){
                boolean isSaved=saveStudent(student,userEmail);
                if (isSaved){
                    setStudentId();
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION,"Student saved...").show();
                    setTableData(searchText);
                }
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
                    setTableData(searchText);
                    btnSave.setText("Save");
                }
            }
        }catch (SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }



    }

    private boolean saveStudent(Student student, String userEmail) throws SQLException, ClassNotFoundException {
        Connection connection=DbConnection.getInstance().getConnection();
        PreparedStatement ps=connection.prepareStatement("INSERT INTO student VALUES (?,?,?,?,?)");
        ps.setString(1,student.getStudentId());
        ps.setString(2,student.getStudentName());
        ps.setString(3,student.getStudentAddress());
        ps.setObject(4,student.getDob());
        ps.setString(5,userEmail);
        return ps.executeUpdate()>0;
    }

    private void setStudentId() {
        try {
            String lastStudentId=getLastStudent();
                //id generate
            if (lastStudentId!=null){
                String[] splittedId=lastStudentId.split("-");
                String lastCharAsString=splittedId[1];
                int lastDigit=Integer.parseInt(lastCharAsString);
                lastDigit++;
                String generatedId="S-"+lastDigit;
                txtStudentID.setText(generatedId);
            }else {
                txtStudentID.setText("S-1");
            }

        }catch (SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private String getLastStudent() throws SQLException, ClassNotFoundException {
        Connection connection=DbConnection.getInstance().getConnection();
        PreparedStatement statement =connection.prepareStatement(
                "SELECT id FROM student ORDER BY CAST(SUBSTRING(id,3)AS UNSIGNED)DESC LIMIT 1");
        ResultSet set=statement.executeQuery();
        if (set.next()){
            return set.getString(1);
        }
        return null;
    }

    private void clearFields(){
        txtStudentName.clear();
        txtAddress.clear();
        dteDOB.setValue(null);
        setTableData(searchText);
    }

    public void newStudentOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }
    private void setUI(String location) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(new Scene((FXMLLoader.load(getClass().getResource("/com/pcl/lms/"+location+".fxml")))));
    }
    public void setUserEmail(String userEmail){
        this.userEmail=userEmail;
    }
}
