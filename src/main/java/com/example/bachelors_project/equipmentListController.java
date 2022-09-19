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

public class equipmentListController implements Initializable {

    public Stage stage;
    public Scene scene;

    public TextField search_by;
    public ListView<String> equipment_list_view;
    public Button search_button;
    public Button add_new_equipment;
    public ArrayList<String> equipment = new ArrayList<>();
    private Connection conn = connectToDatabase_equipment();





    public void addEquipmentWindow(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("new IT equipment.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        conn.close();
    }

    public void search(ActionEvent event) throws SQLException {
        try {
            String sql = "SELECT * FROM Equipment WHERE ? IN (ID,Make, Type, Model,[Responsible user])";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setString(1, search_by.getText());

            ResultSet rs = ps.executeQuery();
            ArrayList<String> searchedEntries = new ArrayList<>();
            while (rs.next()) {
                searchedEntries.add(rs.getString(1) + "----" + rs.getString(2) + "----" + rs.getString(3)
                        + "----" + rs.getString(4) + "----" + rs.getString(5));
            }
            equipment_list_view.setItems(observableArrayList(searchedEntries));
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection connectToDatabase_equipment() {
        SqliteConnection sqLite = new SqliteConnection();
        sqLite.databaseName = "Equipment.db";
        return sqLite.getConnection();
    }

    public void initialEquipmentList() throws SQLException {
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM Equipment";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                equipment.add(rs.getString(1) + "----" + rs.getString(2) + "----" + rs.getString(3)
                        + "----" + rs.getString(4) + "----" + rs.getString(5));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initialEquipmentList();
            equipment_list_view.setItems(observableArrayList(equipment));
            initialEquipmentList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void listChooser(MouseEvent mouseEvent) throws IOException, SQLException {
        String currentItemSelected;
        String eqID = "";
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                currentItemSelected = equipment_list_view.getSelectionModel().getSelectedItem();
                String[] sepEqSelected = currentItemSelected.split("----");
                eqID = sepEqSelected[0];
                FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("equipment info.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setUserData(eqID);
                equipmentInfoController eqInfo = fxmlLoader.getController();
                eqInfo.setEdit_ID(eqID);
                stage.show();
            }
        }

    }
}
