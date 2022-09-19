module com.example.bachelors_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires itextpdf;


    opens com.example.bachelors_project to javafx.fxml;
    exports com.example.bachelors_project;
}