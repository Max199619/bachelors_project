package com.example.bachelors_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class equipmentListController {

    public Stage stage;
    public Scene scene;

    public TextField search_by;
    public ComboBox equipment_type;
    public ListView equipment_list_view;
    public Button search_button;
    public Button add_new_equipment;

    public void addEquipmentWindow(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("new IT equipment.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
