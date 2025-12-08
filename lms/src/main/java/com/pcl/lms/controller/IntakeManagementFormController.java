package com.pcl.lms.controller;

import com.pcl.lms.DB.Database;
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
        ObservableList<String> programsObList= FXCollections.observableArrayList();
        for (Programme temp:Database.programmeTable){
            programsObList.add(temp.getProgrammeId()+"-"+temp.getProgrammeName());
        }
        cbxProgram.setItems(programsObList);
    }

    private void setIntakeID() {
        if (!Database.intakeTable.isEmpty()){
            Intake lastIntake =Database.intakeTable.get(Database.intakeTable.size()-1);
            String id=lastIntake.getId();
            String[] split=id.split("-");
            int lastDigit=Integer.parseInt(split[1]);
            lastDigit++;
            txtId.setText("I-"+lastDigit);
        }
        txtId.setText("I-1");

    }
    public void newIntakeOnAction(ActionEvent actionEvent) {
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void saveOnAction(ActionEvent actionEvent) {
        if (btnSave.getText().equals("Save")){
            //save
            Database.intakeTable.add(new Intake
                    (txtId.getText(),
                            Date.from(dteStart.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            txtName.getText(),
                            cbxProgram.getValue()
            ));
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
