package com.example.bachelors_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class employeeListController {

    public TextField search_by;
    public Button search_button;
    public ListView employees_list;
    public Button add_new_employee_button;

    public void addNewEmployeeWindow(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("adding new user.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
