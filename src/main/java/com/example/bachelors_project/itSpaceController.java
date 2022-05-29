package com.example.bachelors_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class itSpaceController {

    public Label support_agent;
    public ListView list_of_requests;
    public Button current_requests_button;
    public Button own_requests_button;
    public Button assigned_to_me_button;
    public Button add_equipment_button;
    public Button add_user_button;
    public Button new_request_button;
    public Button manage_users_button;
    public Button manage_equipment_button;

    public void addEquipmentWindow(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("new IT equipment.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void addNewUserWindow(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("adding new user.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void newRegularRequestWindow(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("regular request.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void manageUsersWindow(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("employee list.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void manageITEquipmentWindow(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("new IT equipment.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

}
