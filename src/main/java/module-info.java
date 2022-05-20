module com.example.bachelors_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bachelors_project to javafx.fxml;
    exports com.example.bachelors_project;
}