package com.example.bachelors_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class supportSpaceController{

    public Stage stage;
    public Scene scene;

    public Label user_name_and_surname;
    public Button own_requests_button;
    public Button new_request_button;
    public Button new_employee_button;
    public ListView request_list;

    public void newRequestWindow(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("regular request.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void newEmployeeWindow(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("adding new user.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

}
