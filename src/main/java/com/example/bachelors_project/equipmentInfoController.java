package com.example.bachelors_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class equipmentInfoController{

    public Label equipment_ID;
    public TextField edit_ID;
    public TextField make;
    public TextField type;
    public TextField model;
    public TextField quantity;
    public ComboBox<String> responsible_department;
    public ComboBox<String> responsible_user;
    public Button quantity_add;
    public Button quantity_take;
    public Button save_button;
    public Button cancel_button;

    ObservableList<String> departments = FXCollections.observableArrayList(
            "IT",
            "HR",
            "Finance",
            "Billing",
            "Production",
            "Marketing",
            "Sales",
            "Legal",
            "Administration"
    );


}


