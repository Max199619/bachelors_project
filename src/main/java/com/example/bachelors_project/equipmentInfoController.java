package com.example.bachelors_project;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

public class equipmentInfoController implements Initializable {

    public equipmentInfoController() {
    }

        public Label equipment_ID;
        public TextField edit_ID;
        public TextField make;
        public TextField type;
        public TextField model;
        public ComboBox<String> responsible_department;
        public ComboBox<String> responsible_user;
        public Button save_button;
        public Button cancel_button;
        public ArrayList<String> departments = new ArrayList<>();
        public ArrayList<String> users = new ArrayList<>();
        private Connection conn_user = connectToDatabase_users();
        private Connection conn_eq = connectToDatabase_equipment();



    @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            String sql = "SELECT * FROM Equipment WHERE ID = ?";
            PreparedStatement ps;
            try {
                ps = conn_eq.prepareStatement(sql);
                ps.setString(1,edit_ID.getText());
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    make.setText(rs.getString(2));
                    type.setText(rs.getString(3));
                    model.setText(rs.getString(4));
                    responsible_department.setValue(rs.getString(5));
                    responsible_user.setValue(rs.getString(6));
                }
                defineDepartments();
                responsible_department.setItems(observableArrayList(departments));
                defineUsers();
                responsible_user.setItems(observableArrayList(users));
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        public void defineDepartments() {
            departments.add("");
            departments.add("IT");
            departments.add("HR");
            departments.add("Sales");
            departments.add("Billing");
            departments.add("Production");
            departments.add("Marketing");
            departments.add("Legal");
            departments.add("Logistics");
        }

        public void defineUsers() throws SQLException {
            try {
                Statement stmt = conn_user.createStatement();
                String sql = "SELECT * FROM users";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    users.add(rs.getString(1) + "----" + rs.getString(2) + " " + rs.getString(3)
                    + "----" + rs.getString(5) + "----" + rs.getString(4));
                }
                rs.close();
            }
            catch (SQLException e){
                e.printStackTrace();
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

        public void saveToDB(ActionEvent event) throws SQLException {

            try {
                String sql = "UPDATE Equipment " +
                        "SET Make = ?, Type = ?, Model = ?, [Responsible Department] = ?, [Responsible user] = ?" +
                        "WHERE ID = ?";
                PreparedStatement ps = conn_eq.prepareStatement(sql);
                ps.setString(1, make.getText());
                ps.setString(2, type.getText());
                ps.setString(3, model.getText());
                ps.setString(4, responsible_department.getValue());
                ps.setString(5, responsible_user.getValue());
                ps.setString(6, equipment_ID.getText());
                ps.executeUpdate();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
                conn_user.close();
                conn_eq.close();

            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }

        public void cancelAndExit(ActionEvent event) {
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }


    public void setEdit_ID(String newEdit_ID) throws SQLException {
        String userID;
        equipment_ID.setText(newEdit_ID);
        edit_ID.setText(newEdit_ID);

        String sql = "SELECT * FROM Equipment WHERE ID = ?";
        PreparedStatement ps;
        try {
            ps = conn_eq.prepareStatement(sql);
            ps.setString(1,edit_ID.getText());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                make.setText(rs.getString(2));
                type.setText(rs.getString(3));
                model.setText(rs.getString(4));
                responsible_department.setValue(rs.getString(5));
                responsible_user.setValue(rs.getString(6));
            }
            responsible_department.setItems(observableArrayList(departments));
            responsible_user.setItems(observableArrayList(users));

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void generateDocuments(ActionEvent event) throws DocumentException, FileNotFoundException {
        List<String> choices = new ArrayList<>();
        choices.add("Hand-Over");
        choices.add("Lease");
        choices.add("Return");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Hand-Over", choices);
        dialog.setTitle("Create document for " + equipment_ID.getText());
        dialog.setHeaderText("What kind of document would you like to create?");
        dialog.setContentText("Choose an option:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            if (result.get().equals("Hand-Over")){
                createHandOver();
            }
            else if (result.get().equals("Lease")){
                createBorrow();
            }
            else{
                createReturn();
            }
        }
    }

    private void createReturn() throws FileNotFoundException, DocumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Font font_company = new Font(Font.FontFamily.TIMES_ROMAN,14,Font.BOLD);
        Font regular = new Font(Font.FontFamily.TIMES_ROMAN,14);
        Font title = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD);

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Return_" + equipment_ID.getText() + ".pdf"));
        document.open();
        document.addTitle("Return of " + equipment_ID.getText());
        document.addKeywords(equipment_ID.getText() + ",Return");
        document.addAuthor("Test Company IT support");
        document.addCreator("Test Company");


        Paragraph company = new Paragraph(
                "\n " +
                        "Test Company Inc.\n" +
                        "Test Street 75\n" +
                        "04-L50 Test\n\n\n\n\n",font_company);
        company.setAlignment(Element.ALIGN_LEFT);
        document.add(company);

        Paragraph doc_title = new Paragraph("Return of equipment\n\n\n",title);
        doc_title.setAlignment(Element.ALIGN_CENTER);
        document.add(doc_title);
        Paragraph returnNote = new Paragraph(responsible_user.getValue().split("----")[1] + " is returning the below-mentioned equipment" +
                "to the IT department of Test Company Inc. on " + LocalDate.now().format(formatter) + ".\n\n\n\n\n",regular);
        returnNote.setAlignment(Element.ALIGN_CENTER);
        document.add(returnNote);
        Paragraph equipmentInformation = new Paragraph(equipment_ID.getText() + " - " + make.getText() + " " + type.getText() + " " + model.getText() +"\n\n", title);
        equipmentInformation.setAlignment(Element.ALIGN_CENTER);
        document.add(equipmentInformation);

        Chunk glue = new Chunk(new VerticalPositionMark());
        PdfPTable table = new PdfPTable(1);
        Phrase p = new Phrase();
        p.add("__________");
        p.add(glue);
        p.add("__________");
        Phrase r = new Phrase();
        r.add("Company");
        r.add(glue);
        r.add("Returner");
        PdfPCell cellP = new PdfPCell(p);
        PdfPCell cellR = new PdfPCell(r);
        cellP.setBorder(Rectangle.NO_BORDER);
        cellR.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellP);
        table.addCell(cellR);
        document.add(table);
        document.close();
    }
    private void createBorrow() throws DocumentException, FileNotFoundException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Font font_company = new Font(Font.FontFamily.TIMES_ROMAN,14,Font.BOLD);
        Font regular = new Font(Font.FontFamily.TIMES_ROMAN,14);
        Font title = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD);

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Lease_" + equipment_ID.getText() + ".pdf"));
        document.open();
        document.addTitle("Lease of " + equipment_ID.getText());
        document.addKeywords(equipment_ID.getText() + ",Lease");
        document.addAuthor("Test Company IT support");
        document.addCreator("Test Company");


        Paragraph company = new Paragraph(
                "\n " +
                        "Test Company Inc.\n" +
                        "Test Street 75\n" +
                        "04-L50 Test\n\n\n\n\n",font_company);
        company.setAlignment(Element.ALIGN_LEFT);
        document.add(company);

        Paragraph doc_title = new Paragraph("Lease of equipment\n\n\n",title);
        doc_title.setAlignment(Element.ALIGN_CENTER);
        document.add(doc_title);
        Paragraph returnNote = new Paragraph("On " + LocalDate.now().format(formatter) + " " + responsible_user.getValue().split("----")[1] + " is borrowing the below-mentioned equipment" +
                "from the IT department of Test Company Inc" + ".\n\n\n\n\n",regular);
        returnNote.setAlignment(Element.ALIGN_CENTER);
        document.add(returnNote);
        Paragraph equipmentInformation = new Paragraph(equipment_ID.getText() + " - " + make.getText() + " " + type.getText() + " " + model.getText() +"\n\n", title);
        equipmentInformation.setAlignment(Element.ALIGN_CENTER);
        document.add(equipmentInformation);

        Chunk glue = new Chunk(new VerticalPositionMark());
        PdfPTable table = new PdfPTable(1);
        Phrase p = new Phrase();
        p.add("__________");
        p.add(glue);
        p.add("__________");
        Phrase r = new Phrase();
        r.add("Company");
        r.add(glue);
        r.add("Borrower");
        PdfPCell cellP = new PdfPCell(p);
        PdfPCell cellR = new PdfPCell(r);
        cellP.setBorder(Rectangle.NO_BORDER);
        cellR.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellP);
        table.addCell(cellR);
        document.add(table);
        document.close();
    }
    private void createHandOver() throws FileNotFoundException, DocumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Font font_company = new Font(Font.FontFamily.TIMES_ROMAN,14,Font.BOLD);
        Font regular = new Font(Font.FontFamily.TIMES_ROMAN,14);
        Font title = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD);

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Hand-Over_" + equipment_ID.getText() + ".pdf"));
        document.open();
        document.addTitle("Hand-over of " + equipment_ID.getText());
        document.addKeywords(equipment_ID.getText() + ",Hand-Over");
        document.addAuthor("Test Company IT support");
        document.addCreator("Test Company");


        Paragraph company = new Paragraph(
                "\n " +
                        "Test Company Inc.\n" +
                        "Test Street 75\n" +
                        "04-L50 Test\n\n\n\n\n",font_company);
        company.setAlignment(Element.ALIGN_LEFT);
        document.add(company);

        Paragraph doc_title = new Paragraph("Hand-over of equipment\n\n\n",title);
        doc_title.setAlignment(Element.ALIGN_CENTER);
        document.add(doc_title);
        Paragraph returnNote = new Paragraph("On " + LocalDate.now().format(formatter) +" " +  responsible_user.getValue().split("----")[1] + " is being handed-over the below-mentioned equipment" +
                "by the IT department of Test Company Inc" + ".\n\n\n\n\n",regular);
        returnNote.setAlignment(Element.ALIGN_CENTER);
        document.add(returnNote);
        Paragraph equipmentInformation = new Paragraph(equipment_ID.getText() + " - " + make.getText() + " " + type.getText() + " " + model.getText() +"\n\n", title);
        equipmentInformation.setAlignment(Element.ALIGN_CENTER);
        document.add(equipmentInformation);

        Chunk glue = new Chunk(new VerticalPositionMark());
        PdfPTable table = new PdfPTable(1);
        Phrase p = new Phrase();
        p.add("__________");
        p.add(glue);
        p.add("__________");
        Phrase r = new Phrase();
        r.add("Company");
        r.add(glue);
        r.add("Receiver");
        PdfPCell cellP = new PdfPCell(p);
        PdfPCell cellR = new PdfPCell(r);
        cellP.setBorder(Rectangle.NO_BORDER);
        cellR.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellP);
        table.addCell(cellR);
        document.add(table);
        document.close();
    }
}






