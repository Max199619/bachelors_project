package com.example.bachelors_project;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

public class userInfoController implements Initializable {

    public TextField ID;
    public Label name_and_surname;
    public TextField name;
    public TextField surname;
    public DatePicker start_date;
    public ComboBox<String> department;
    public CheckBox is_employed;
    public Button save_button;
    public Button cancel_button;
    private ArrayList<String> userDept = new ArrayList<>();
    private ArrayList<String> users_hardware = new ArrayList<>();
    private String userEquipment = "";
    private ArrayList<String> equipmentList = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Connection conn = connectToDatabase_users();


    public void saveToDB(ActionEvent event) throws SQLException {
        System.out.println("save clicked");
        System.out.println(department.getAccessibleText());
        System.out.println(department.getPromptText());
        System.out.println(department.getValue());

        String sql = "UPDATE Users SET Name = ?, Surname = ?,Department = ?, [Start date] = ?, [Is employed] = ? WHERE ID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name.getText());
        ps.setString(2, surname.getText());
        ps.setString(3, department.getValue());
        ps.setString(4, outputStartDate());
        ps.setInt(5, stillEmployedSave());
        ps.setString(6, ID.getText());
        ps.executeUpdate();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        conn.close();
        System.out.println("Updated");
    }

    public void cancelAndExit(ActionEvent event) throws SQLException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        conn.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            String sql = "SELECT * FROM Users WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ID.getText());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                name.setText(rs.getString(2));
                surname.setText(rs.getString(3));
                department.setValue(rs.getString(5));
                setStartDate(rs.getString(6));
                stillEmployedRead(rs.getInt(8));
            }
            setUserDept();
            department.setItems(observableArrayList(userDept));
            setName_and_surname(name.getText(), surname.getText());
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public Connection connectToDatabase_users() {
        SqliteConnection sqLite = new SqliteConnection();
        sqLite.databaseName = "users.db";
        return sqLite.getConnection();
    }

    private void setUserDept() {
        userDept.add("");
        userDept.add("IT");
        userDept.add("HR");
        userDept.add("Sales");
        userDept.add("Billing");
        userDept.add("Production");
        userDept.add("Marketing");
        userDept.add("Legal");
        userDept.add("Logistics");
    }

    private int stillEmployedSave() {
        int employed = 0;
        if (is_employed.isSelected()) {
            employed = 1;
        }
        return employed;
    }

    private void stillEmployedRead(int employed) {
        if (employed == 1) {
            is_employed.setSelected(true);
        } else {
            is_employed.setSelected(false);
        }
    }

    private void setStartDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        start_date.setValue(LocalDate.parse(date, formatter));
    }

    private String outputStartDate() {
        LocalDate startDate = start_date.getValue();
        return startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void setID(TextField ID) {
        this.ID = ID;
    }

    public void setName_and_surname(String name, String surname) {
        String personalDataLabel = name + surname;
        name_and_surname.setText(personalDataLabel);
    }

    public void setID(String userID) throws SQLException {
        Connection conn = connectToDatabase_users();
        try {
            ID.setText(userID);
            String sql = "SELECT * FROM Users WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ID.getText());
            ResultSet rs = ps.executeQuery();
            name.setText(rs.getString(2));
            surname.setText(rs.getString(3));
            department.setValue(rs.getString(5));
            setStartDate(rs.getString(6));
            if (rs.getInt(8) == 1) {
                is_employed.setSelected(true);
            }
            rs.close();
        } catch (SQLException | NullPointerException throwables) {
            throwables.printStackTrace();
        }
        name_and_surname.setText(name.getText() + " " + surname.getText());
        conn.close();
    }
}

