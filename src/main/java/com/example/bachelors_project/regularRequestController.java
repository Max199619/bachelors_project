package com.example.bachelors_project;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

public class regularRequestController implements Initializable {

    public Label issue_number;
    public TextField customer;
    public TextField customer_number;
    public TextField requester;
    public TextField assignee;
    public TextField elapsed_time;
    public ComboBox<String> priority;
    public TextField request_title;
    public ComboBox<String> requester_department;
    public ComboBox<String> assignee_group;
    public ComboBox<String> status;
    public Button save_button;
    public Button cancel_button;
    public Button resolve_button;
    private ArrayList<String> priorities = new ArrayList<>();
    private ArrayList<String> req_dept = new ArrayList<>();
    private ArrayList<String> groups = new ArrayList<>();
    private ArrayList<String> statuses = new ArrayList<>();

    private Connection conn_us = connectToDatabase_users();
    private Connection conn_eq = connectToDatabase_equipment();
    private Connection conn_rq = connectToDatabase_tasks();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setPriorities();
            setGroups();
            setReq_dept();
            setStatuses();

            createID();
            priority.setItems(observableArrayList(priorities));
            requester_department.setItems(observableArrayList(req_dept));
            assignee_group.setItems(observableArrayList(groups));
            status.setItems(observableArrayList(statuses));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Connection connectToDatabase_users() {
        SqliteConnection sqLite = new SqliteConnection();
        sqLite.databaseName = "users.db";
        return sqLite.getConnection();
    }

    public Connection connectToDatabase_equipment() {
        SqliteConnection sqLite = new SqliteConnection();
        sqLite.databaseName = "Equipment.db";
        return sqLite.getConnection();
    }

    public Connection connectToDatabase_tasks() {
        SqliteConnection sqLite = new SqliteConnection();
        sqLite.databaseName = "Tasks.db";
        return sqLite.getConnection();
    }

    private void setPriorities() {
        priorities.add("P1");
        priorities.add("P2");
        priorities.add("P3");
        priorities.add("P4");
        priorities.add("P5");
    }

    private void setReq_dept() {
        req_dept.add("");
        req_dept.add("IT");
        req_dept.add("HR");
        req_dept.add("Sales");
        req_dept.add("Billing");
        req_dept.add("Production");
        req_dept.add("Marketing");
        req_dept.add("Legal");
        req_dept.add("Logistics");
    }

    private void setGroups() {
        groups.add("IT - SL1");
        groups.add("IT - SL2");
        groups.add("IT - Administrators");
        groups.add("IT - Devs");
    }

    private void setStatuses() {
        statuses.add("Created");
        statuses.add("Awaiting User");
        statuses.add("Awaiting Support");
        statuses.add("Pending");
        statuses.add("Resolved");
    }

    private void setElapsedTime(LocalDateTime createdOn) throws SQLException {
        Duration elapsed = Duration.between(createdOn, LocalDateTime.now());
        if (elapsed != null) {
            elapsed_time.setText(String.format("%d d %d h %02d min", elapsed.toDaysPart(), elapsed.toHoursPart(), elapsed.toMinutesPart()));
        }

    }

    public void resolve(ActionEvent event) {
        status.setValue("Resolved");
    }

    public void cancelAndExit(ActionEvent event) throws SQLException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        conn_eq.close();
        conn_rq.close();
        conn_us.close();
    }


    public void saveToDB(ActionEvent event) throws SQLException {
        try {
            String sql = "SELECT * FROM Task WHERE ID = ?";
            PreparedStatement ps = conn_rq.prepareStatement(sql);
            ps.setString(1, issue_number.getText());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String sql_update = "UPDATE Task SET Title = ?, Requester = ?, [Assignee group] = ?, Assignee = ?, Priority = ?, Status = ?, [Customer number] = ?, Customer = ? WHERE ID = ?";
                PreparedStatement ps_update = conn_rq.prepareStatement(sql_update);
                ps_update.setString(1, request_title.getText());
                ps_update.setString(2, requester.getText());
                ps_update.setString(3, assignee_group.getValue());
                ps_update.setString(4, assignee.getText());
                ps_update.setString(5, priority.getValue());
                ps_update.setString(6, status.getValue());
                ps_update.setString(7, customer_number.getText());
                ps_update.setString(8, customer.getText());
                ps_update.setString(9, issue_number.getText());
                ps_update.executeUpdate();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
                conn_eq.close();
                conn_rq.close();
                conn_us.close();
            } else {
                String sql_new = "INSERT INTO Task (ID,Title,Requester,[Assignee group],Assignee,Priority,[Time created],Status,[Customer number],Customer)" +
                        "VALUES (?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps_new = conn_rq.prepareStatement(sql_new);
                ps_new.setString(1, issue_number.getText());
                ps_new.setString(2, request_title.getText());
                ps_new.setString(3, requester.getText());
                ps_new.setString(4, assignee_group.getValue());
                ps_new.setString(5, assignee.getText());
                ps_new.setString(6, priority.getValue());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                ps_new.setString(7, (LocalDateTime.now()).format(formatter));
                ps_new.setString(8, status.getValue());
                ps_new.setString(9, customer_number.getText());
                ps_new.setString(10, customer.getText());
                ps_new.executeUpdate();
                rs.close();
                conn_us.close();
                conn_rq.close();
                conn_eq.close();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        } catch (SQLException | NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    public void createID() throws SQLException {
        try {
            String sql = "SELECT MAX(ID) FROM Task";
            Statement stmt = conn_rq.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String maxID = "";
            if (rs.next()) {
                maxID = rs.getString(1);
            } else {
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
            if (intNumber < 9) {
                number = "00" + String.valueOf(intNumber + 1);
            } else if (intNumber < 99) {
                number = "0" + String.valueOf(intNumber + 1);
            } else if (intNumber < 999) {
                number = "0" + String.valueOf(intNumber + 1);
            } else if (intNumber < 9999) {
                number = "0" + String.valueOf(intNumber + 1);
            } else if (intNumber < 99999) {
                number = "0" + String.valueOf(intNumber + 1);
            }
            issue_number.setText(beginning + number);
            rs.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void disableResolving() {
        resolve_button.setDisable(true);
    }

    public void setRequester(String user) {
        requester.setText(user);
    }

    public void disableElapsedTime() {
        elapsed_time.setDisable(true);
    }

    public void setDepartment(String user) throws SQLException {
        String name;
        String surname;
        String[] splitUsername = user.split(" ");
        name = splitUsername[0];
        surname = splitUsername[1];
        String sql = "SELECT Department FROM Users WHERE Name = ? AND Surname = ?";
        PreparedStatement ps = conn_us.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, surname);
        ResultSet rs = ps.executeQuery();
        requester_department.setValue(rs.getString(1));
        rs.close();
    }

    public void setID(String id) throws SQLException {
        try {
            issue_number.setText(id);
            String sql = "SELECT * FROM Task WHERE ID = ?";
            PreparedStatement ps = conn_rq.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            request_title.setText(rs.getString(2));
            requester.setText(rs.getString(3));
            assignee_group.setValue(rs.getString(4));
            assignee.setText(rs.getString(5));
            priority.setValue(rs.getString(6));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(rs.getString(7), formatter);
            setElapsedTime(dateTime);
            status.setValue(rs.getString(8));
            customer_number.setText(rs.getString(10));
            customer.setText(rs.getString(11));
            rs.close();

            String[] requesterData = requester.getText().split(" ");
            String name = requesterData[0];
            String surname = requesterData[1];
            String sql1 = "SELECT Department FROM Users WHERE Name = ? AND Surname = ?";
            PreparedStatement ps_requester = conn_us.prepareStatement(sql1);
            ps_requester.setString(1, name);
            ps_requester.setString(2, surname);
            ResultSet rs_requester = ps_requester.executeQuery();
            requester_department.setValue(rs_requester.getString(1));
            rs.close();

        } catch (SQLException | NullPointerException throwables) {
            throwables.printStackTrace();
        }
    }


    public void setStatus() {
        status.setValue("Created");
    }
}