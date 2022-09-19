package com.example.bachelors_project;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

public class newITEquipmentController implements Initializable {

    public TextField new_equipment_ID;
    public TextField new_equipment_make;
    public ComboBox<String> new_equipment_type;
    public TextField new_equipment_model;
    public TextField new_equipment_invoice_number;
    public DatePicker delivered_on_date;
    public Button save_button;
    public Button cancel_button;
    private final ArrayList<String> types = new ArrayList<>();
    private Connection conn = connectToDatabase_equipment();

    public void setTypes(){
        types.add("");
        types.add("Laptop");
        types.add("PC");
        types.add("Monitor");
        types.add("Printer");
        types.add("Docking Station");
        types.add("Server");
        types.add("Switch");
        types.add("Batteries");
        types.add("Handheld");
        types.add("Cellphone");
        types.add("Deskphone");
        types.add("Router");
        types.add("Other");
    }


    public void saveToDB(ActionEvent event) throws SQLException {
        try {
            String sql = "INSERT INTO Equipment (ID, Make, Type, Model, [Invoice number], [Delivered on date]) " +
                    "VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, new_equipment_ID.getText());
            ps.setString(2, new_equipment_make.getText());
            ps.setString(3, new_equipment_type.getValue());
            ps.setString(4, new_equipment_model.getText());
            ps.setString(5, new_equipment_invoice_number.getText());
            ps.setString(6, outputStartDate());
            ps.executeUpdate();
            Platform.exit();
            conn.close();
        }
        catch (SQLException SQE){
            SQE.printStackTrace();
        }
    }

    public Connection connectToDatabase_equipment() {
        SqliteConnection sqLite = new SqliteConnection();
        sqLite.databaseName = "Equipment.db";
        return sqLite.getConnection();
    }

    public void cancelAndExit(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setDatePicker(){
        delivered_on_date.setValue(LocalDate.now());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDatePicker();
        setTypes();
        new_equipment_type.setItems(observableArrayList(types));
    }

    private String outputStartDate(){
        LocalDate deliveredOn = delivered_on_date.getValue();
        return deliveredOn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


}
