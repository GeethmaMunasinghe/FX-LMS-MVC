package com.pcl.lms.controller;

import com.pcl.lms.DB.Database;
import com.pcl.lms.model.Modules;
import com.pcl.lms.model.Programme;
import com.pcl.lms.model.Teacher;
import com.pcl.lms.tm.ModulesTM;
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
import java.util.ArrayList;

public class ProgramManagementFormController {

    public TextField txtProgramId;
    public TextField txtProgramName;
    public TextField txtCost;
    public ComboBox <String> cbxTeacher;
    public TextField txtModules;
    public TableView<ModulesTM> tblModule;
    public TableColumn<ModulesTM,Integer> colModuleId;
    public TableColumn<ModulesTM,String> colModuleName;
    public TableColumn<ModulesTM,Button> colModuleRemove;
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

    static ArrayList<Modules> modList=new ArrayList<>();
    public void initialize(){
        colModuleId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colModuleName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colModuleRemove.setCellValueFactory(new PropertyValueFactory<>("btn"));
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
    public void addModulesOnAction(ActionEvent actionEvent) {
        if (txtModules.getText().equals(null)){
            return;
        }
        modList.add(new Modules(getModuleId(),txtModules.getText()));

        setModuleTableData();
        txtModules.clear();
    }

    private void setModuleTableData() {
        ObservableList<ModulesTM> list=FXCollections.observableArrayList();

        for (Modules modules:modList){
            Button btn=new Button("Delete");
            list.add(new ModulesTM(
                    modules.getId(),
                    modules.getName(),
                    btn
            ));
            //Delete function
            btn.setOnAction(event->{
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure?",ButtonType.YES,ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult()==ButtonType.YES){
                    modList.remove(modules);
                    setModuleTableData();
                }
            });
        }
        tblModule.setItems(list);
    }

    private int getModuleId() {
        boolean listEmpty=modList.isEmpty();
        if (listEmpty){
            return 1;
        }
        Modules lastModule=modList.get(modList.size()-1);
        int lastId=lastModule.getId();
        lastId++;
        return lastId;
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
