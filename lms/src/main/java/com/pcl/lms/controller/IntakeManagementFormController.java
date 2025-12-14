package com.pcl.lms.controller;

import com.pcl.lms.DB.Database;
import com.pcl.lms.DB.DbConnection;
import com.pcl.lms.model.Intake;
import com.pcl.lms.model.Programme;
import com.pcl.lms.tm.IntakeTm;
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
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class IntakeManagementFormController {

    public AnchorPane context;
    public TextField txtId;
    public Button btnSave;
    public DatePicker dteStart;
    public TextField txtName;
    public ComboBox<String> cbxProgram;
    public TextField txtSearch;
    public TableView<IntakeTm> tblIntake;
    public TableColumn<IntakeTm,String> colID;
    public TableColumn<IntakeTm,String> colName;
    public TableColumn<IntakeTm,Date> colDate;
    public TableColumn<IntakeTm,String> colProgram;
    public TableColumn<IntakeTm,Button> colOption;
    private String searchText="";

    public void initialize(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("program"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setIntakeID();
        setProgramsData();
        loadTableData(searchText);

        tblIntake.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->{
            if (newValue!=null){
                setDataToForm((IntakeTm)newValue);
            }
        });
    }

    private void setDataToForm(IntakeTm tm) {
        txtId.setText(tm.getId());
        txtName.setText(tm.getName());
        dteStart.setValue(tm.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        cbxProgram.setValue(tm.getProgram());
        btnSave.setText("Update");
    }

    private void loadTableData(String searchText) {
        ObservableList<IntakeTm> intakeObList=FXCollections.observableArrayList();
        intakeObList.clear();
        for (Intake intake:Database.intakeTable){
            if (intake.getName().contains(searchText)){
                Button btn=new Button("Delete");
                intakeObList.add(new IntakeTm(
                      intake.getId(),
                        intake.getDate(),
                        intake.getName(),
                        intake.getProgram(),
                        btn
                ));
                btn.setOnAction((event)->{
                    Alert delAlert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure?",ButtonType.YES,ButtonType.NO);
                    delAlert.showAndWait();
                    if (delAlert.getResult()==ButtonType.YES){
                        Database.intakeTable.remove(intake);
                        loadTableData(searchText);
                        setIntakeID();
                    }
                });
            }
        }
        tblIntake.setItems(intakeObList);
    }

    private void setProgramsData() {
        try {
            ObservableList<String> programsObList= fetchPrograms();
            cbxProgram.setItems(programsObList);
        }catch (SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private ObservableList<String> fetchPrograms() throws SQLException, ClassNotFoundException {
        ObservableList<String> programsObList=FXCollections.observableArrayList();
        Connection connection=DbConnection.getInstance().getConnection();
        PreparedStatement ps=connection.prepareStatement("SELECT * FROM program");
        ResultSet set=ps.executeQuery();
        while (set.next()){
            //here data sets according to the program table
            programsObList.add(set.getString(1)+"-"+set.getString(2));
        }
        return programsObList;
    }

    private void setIntakeID() {
        try {
            String lastIntakeId=fetchLastIntakeId();
            if (lastIntakeId!=null){
                String[] split=lastIntakeId.split("-");
                int lastDigit=Integer.parseInt(split[1]); //1:-second element
                lastDigit++;
                txtId.setText("I-"+lastDigit);
                return;
            }
            txtId.setText("I-1");
        }catch (SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private String fetchLastIntakeId() throws SQLException, ClassNotFoundException {
       Connection connection=DbConnection.getInstance().getConnection();
       PreparedStatement ps=connection.prepareStatement(
               "SELECT id FROM intake ORDER BY CAST(SUBSTRING(id,3)AS UNSIGNED)DESC LIMIT 1");
       ResultSet set=ps.executeQuery();
       if (set.next()){
           return set.getString(1);
       }
       return null;
    }

    public void newIntakeOnAction(ActionEvent actionEvent) {
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void saveOnAction(ActionEvent actionEvent) {
        Intake intake=new Intake(
                txtId.getText(),
                Date.from(dteStart.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                txtName.getText(),
                cbxProgram.getValue()
        );
        try {
            if (btnSave.getText().equals("Save")){
                //save
                boolean isSaved=saveIntake(intake);
                new Alert(Alert.AlertType.INFORMATION,"Saved").show();
                setIntakeID();
                setProgramsData();
                clearFields();
                loadTableData(searchText);
                txtSearch.textProperty().addListener((observable,oldValue,newValue)->{
                    this.searchText=newValue;
                    loadTableData(searchText);
                });
            }else {
                Optional<Intake> selectedIntake =Database.intakeTable.stream().filter(e->e.getId()
                        .equals(txtId.getText())).findFirst();
                if (selectedIntake.isPresent()){
                    selectedIntake.get().setName(txtName.getText());
                    selectedIntake.get().setDate(Date.from(dteStart.getValue()
                            .atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    selectedIntake.get().setProgram(cbxProgram.getValue());
                    new Alert(Alert.AlertType.INFORMATION,"Update"+selectedIntake.get().getId()).show();
                    clearFields();
                    loadTableData(searchText);
                    setIntakeID();
                    btnSave.setText("Save");
                }
            }
        }catch (SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private boolean saveIntake(Intake intake) throws SQLException, ClassNotFoundException {
        Connection connection=DbConnection.getInstance().getConnection();
        PreparedStatement ps=connection.prepareStatement("INSERT INTO intake VALUES(?,?,?,?)");
        ps.setString(1,intake.getId());
        ps.setString(2,intake.getName());
        ps.setObject(3,intake.getDate());
        ps.setString(4,splitId(intake.getProgram()));
        return ps.executeUpdate()>0;
    }

    private String splitId(String value) {
        String[] split=value.split("-");
        return split[0].trim()+"-"+split[1].trim();
    }

    private void clearFields() {
        txtName.clear();
        dteStart.setValue(null);
    }

    private void setUI(String location) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(new Scene((FXMLLoader.load(getClass().getResource("/com/pcl/lms/"+location+".fxml")))));
    }
}
