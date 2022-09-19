package com.example.bachelors_project;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;


public class addingNewUserController implements Initializable {

    public TextField ID;
    public TextField name;
    public TextField surname;
    public TextField email;
    public ComboBox<String> department;
    public DatePicker start_date;
    public CheckBox is_employed;
    public Button save_button;
    public Button cancel_button;
    private Connection conn = connectToDatabase_users();

    private final ArrayList<String> dept = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");



    private void setDept(){
        dept.add("");
        dept.add("IT");
        dept.add("HR");
        dept.add("Sales");
        dept.add("Billing");
        dept.add("Production");
        dept.add("Marketing");
        dept.add("Legal");
        dept.add("Logistics");
    }



    public Connection connectToDatabase_users() {
        SqliteConnection sqLite = new SqliteConnection();
        sqLite.databaseName = "users.db";
        return sqLite.getConnection();
    }


    public void saveToDB(ActionEvent event) throws Throwable {
        try {
            String sql = "INSERT INTO Users (ID,Name,Surname,Email,Department,[Start date],[Is employed])" +
                    "VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ID.getText());
            ps.setString(2, name.getText());
            ps.setString(3, surname.getText());
            ps.setString(4, email.getText());
            ps.setString(5, department.getValue());
            ps.setString(6, start_date.getValue().format(formatter));
            ps.setString(7, is_employed.isSelected() ? "1" : "0");
            ps.executeUpdate();
            conn.close();
            Platform.exit();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void cancelAndExit(ActionEvent event) {
        Platform.exit();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            createID();
            setDept();
            start_date.setValue(LocalDate.now());
            department.setItems(observableArrayList(dept));
        }
        catch (SQLException SE){
            SE.printStackTrace();
        }

    }

    private void createID() throws SQLException {
        String sql = "SELECT MAX(ID) FROM users";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        String maxID = "";
        if(rs.next()){
            maxID = rs.getString(1);
        }else{
            System.out.println("Something went wrong");
        }
        char[] modifyID = maxID.toCharArray();
        String beginning = "";
        String number = "";
        for (char c : modifyID) {
            if ((int) c > 57) {
                beginning += c;
            } else {
                number += c;
            }
        }
        int intNumber = Integer.parseInt(number);
        if(intNumber < 9){
            number = "00" + String.valueOf(intNumber + 1);
        }
        else if (intNumber < 99){
            number = "0" + String.valueOf(intNumber + 1);
        }
        else if (intNumber < 999){
            number = "0" + String.valueOf(intNumber + 1);
        }
        else if (intNumber < 9999){
            number = "0" + String.valueOf(intNumber + 1);
        }
        else if (intNumber <99999){
            number = "0" + String.valueOf(intNumber + 1);
        }
        ID.setText(beginning + number);
        rs.close();
    }




}
