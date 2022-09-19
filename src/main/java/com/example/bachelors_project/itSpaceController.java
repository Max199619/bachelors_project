package com.example.bachelors_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;

public class itSpaceController {

    public Label support_agent;
    public ListView<String> list_of_requests;
    public Button current_requests_button;
    public Button own_requests_button;
    public Button assigned_to_me_button;
    public Button add_equipment_button;
    public Button add_user_button;
    public Button new_request_button;
    public Button manage_users_button;
    public Button manage_equipment_button;

    ArrayList<String> requests = new ArrayList<>();

    private Connection conn = connectToDatabase_tasks();

    public Connection connectToDatabase_tasks() {
        SqliteConnection sqLite = new SqliteConnection();
        sqLite.databaseName = "Tasks.db";
        return sqLite.getConnection();
    }



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

    public void newRegularRequestWindow(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("regular request.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        regularRequestController regReq = fxmlLoader.getController();
        regReq.disableElapsedTime();
        regReq.disableResolving();
        regReq.setRequester(support_agent.getText());
        regReq.setDepartment(support_agent.getText());
        regReq.setStatus();
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
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("equipment list.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void current_requests_window(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("request_list.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        requestListController reqList = fxmlLoader.getController();
        reqList.sort_by_not_resolved();
        stage.show();

    }

    public void my_requests_window(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("request_list.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        requestListController reqList = fxmlLoader.getController();
        reqList.sort_by_creator(support_agent.getText());
        stage.show();
    }

    public void assigned_to_me_window(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("request_list.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        requestListController reqList = fxmlLoader.getController();
        reqList.sort_by_assignee(support_agent.getText());
        stage.show();
    }

    public void setUserNameAndSurname(String username) {
        support_agent.setText(username.split("_")[0] + " " + username.split("_")[1]);
    }

    public void fillRequestList(String user){
        try {
            String[] names = user.split("_");
            String requester = (names[0]) + " " + (names[1]);
            String sql = "SELECT ID, Title FROM Task WHERE (Requester = ? AND Status != 'Resolved')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, requester);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                requests.add(rs.getString(1) + "----" + rs.getString(2));
            }
            list_of_requests.setItems(observableArrayList(requests));
            rs.close();
            conn.close();
        } catch (NullPointerException | SQLException NPE) {
            NPE.printStackTrace();
        }
    }

    public void listChooser(MouseEvent mouseEvent) throws IOException, SQLException {
        String currentItemSelected;
        String reqID;
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                currentItemSelected = list_of_requests.getSelectionModel().getSelectedItem();
                String[] sepEqSelected = currentItemSelected.split("----");
                reqID = sepEqSelected[0];
                FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("regular request.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                regularRequestController regReq = fxmlLoader.getController();
                regReq.setID(reqID);
                stage.show();
            }
        }
    }
}


