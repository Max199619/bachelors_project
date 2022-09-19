package com.example.bachelors_project;

import javafx.collections.FXCollections;
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

public class supportSpaceController{

    public Stage stage;
    public Scene scene;

    public Label user_name_and_surname;
    public Button own_requests_button;
    public Button new_request_button;
    public ListView<String> request_list;
    public ArrayList<String> requests = new ArrayList<>();
    private Connection conn = connectToDatabase_tasks();



    public void newRequestWindow(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("regular request.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setScene(scene);
        regularRequestController regReq = fxmlLoader.getController();
        regReq.disableResolving();
        regReq.disableElapsedTime();
        regReq.setRequester(user_name_and_surname.getText());
        regReq.setDepartment(user_name_and_surname.getText());
        regReq.setStatus();
        stage.show();
    }
    public void my_requests(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("request_list.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setScene(scene);
        requestListController reqList = fxmlLoader.getController();
        reqList.sort_by_creator(user_name_and_surname.getText());
        stage.show();
    }

    public Connection connectToDatabase_tasks() {
        SqliteConnection sqLite = new SqliteConnection();
        sqLite.databaseName = "Tasks.db";
        return sqLite.getConnection();
    }

    public void setUser_name_and_surname(String username) throws SQLException {
        user_name_and_surname.setText(username.split("_")[0] + " " + username.split("_")[1]);
    }



    public void fillRequestList(String username) throws SQLException {
        try {
            String[] names = username.split("_");
            String requester = (names[0]) + " " + (names[1]);

            String sql = "SELECT ID, Title FROM Task WHERE Requester = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, requester);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                requests.add(rs.getString(1) + "----" + rs.getString(2));
            }
            request_list.setItems(FXCollections.observableArrayList(requests));
            rs.close();
        } catch (NullPointerException NPE) {
            NPE.printStackTrace();
        }
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void listChooser(MouseEvent mouseEvent) throws IOException, SQLException {
        String currentItemSelected;
        String reqID = "";
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                currentItemSelected = request_list.getSelectionModel().getSelectedItem();
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
