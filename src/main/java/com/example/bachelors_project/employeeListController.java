package com.example.bachelors_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

public class employeeListController implements Initializable {


    public Stage stage;
    public Scene scene;

    public TextField search_by;
    public Button search_button;
    public ListView<String> employees_list;
    public Button add_new_employee_button;
    public ArrayList<String> employee_list = new ArrayList<>();
    private Connection conn = connectToDatabase_users();




    public void addNewEmployeeWindow(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("adding new user.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            createListOfEmployees();
            employees_list.setItems(observableArrayList(employee_list));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void search(ActionEvent event) throws SQLException {
        try {
            String sql = "SELECT * FROM Users WHERE ? IN (ID, Name, Surname, Email, Department)";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setString(1, search_by.getText());
            ResultSet rs = ps.executeQuery();
            ArrayList<String> newUserList = new ArrayList<>();
            while (rs.next()) {
                newUserList.add(rs.getString(1) + "----" + rs.getString(2) + "----" + rs.getString(3)
                        + "----" + rs.getString(4));
            }
            employees_list.setItems(observableArrayList(newUserList));
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createListOfEmployees() throws SQLException {
        try {
            String sql = "SELECT * FROM users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                employee_list.add(rs.getString(1) + "----" + rs.getString(2) + "----" + rs.getString(3)
                        + "----" + rs.getString(4));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection connectToDatabase_users() {
        SqliteConnection conn = new SqliteConnection();
        conn.databaseName = "users.db";
        return conn.getConnection();
    }


    public void listChooser(MouseEvent mouseEvent) throws IOException, SQLException {
        String currentItemSelected;
        String userID="";
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                System.out.println("item selected - doubleclick");
                currentItemSelected = employees_list.getSelectionModel().getSelectedItem();
                String[] sepUserSelected = currentItemSelected.split("----");
                userID = sepUserSelected[0];
                FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("user info.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                userInfoController userInfo = fxmlLoader.getController();
                userInfo.setID(userID);
                stage.setScene(scene);
                stage.show();
            }
        }
    }




}