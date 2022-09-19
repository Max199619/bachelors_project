package com.example.bachelors_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;

public class requestListController{

    public Label IDAndTitle;
    public TextField search_by;
    public Button search_button;
    public ListView<String> request_list;
    public Button add_new_request;

    public ArrayList<String> requestArray = new ArrayList<>();

    private Connection conn = connectToDatabase_tasks();


    public void createRequestWindow(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("regular request.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
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

    public void search(ActionEvent event) throws SQLException {
        try {
            String sql = "SELECT ID, Title FROM Task WHERE ? IN (ID, Title, Requester)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, search_by.getText());
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }

    }

    public Connection connectToDatabase_tasks() {
        SqliteConnection sqLite = new SqliteConnection();
        sqLite.databaseName = "Tasks.db";
        return sqLite.getConnection();
    }


    public void sort_by_assignee(String assignee) throws SQLException, NullPointerException {
        try {
            String sql = "SELECT ID, Title FROM Task WHERE Assignee = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, assignee);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                requestArray.add(rs.getString(1) + "----" + rs.getString(2));
            }
            request_list.setItems(observableArrayList(requestArray));
            rs.close();
        } catch (SQLException | NullPointerException SE) {
            SE.printStackTrace();
        }

    }


    public void sort_by_creator(String requester) throws SQLException, NullPointerException{
        try {
            String sql = "SELECT ID, Title FROM Task WHERE Requester = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, requester);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                requestArray.add(rs.getString(1) + "----" + rs.getString(2));
            }
            request_list.setItems(observableArrayList(requestArray));
            rs.close();
        } catch (SQLException | NullPointerException SE) {
            SE.printStackTrace();
        }

    }

    public void sort_by_not_resolved() throws SQLException {
        try {
            String sql = "SELECT ID, Title FROM Task WHERE Status != 'Resolved' ";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                requestArray.add(rs.getString(1) + "----" + rs.getString(2));
            }
            request_list.setItems(observableArrayList(requestArray));
            rs.close();
        } catch (SQLException | NullPointerException SE) {
            SE.printStackTrace();
        }

    }

}


