package com.example.bachelors_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class supportSpaceApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(loginApplication.class.getResource("support space.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bachelors Project");
        stage.setScene(scene);
        stage.show();
    }
}
