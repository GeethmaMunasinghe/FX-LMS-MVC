package com.pcl.lms.controller;

import com.pcl.lms.model.Modules;
import com.pcl.lms.tm.ModulesTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class ModulePopUpController {

    public AnchorPane context;
    public ListView<String> lstModule;
    ObservableList<String> moduleObList= FXCollections.observableArrayList();

    public void initialize(){
        setModuleList();
    }

    private void setModuleList() {
        for (ModulesTM tempMod:ProgramManagementFormController.list){
            moduleObList.add(tempMod.getId()+" : "+tempMod.getName());
        }
        lstModule.setItems(moduleObList);
    }
}
