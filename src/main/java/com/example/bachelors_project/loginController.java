package com.example.bachelors_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class loginController {

    private Stage stage;
    private Scene scene;


    public TextField user_login;
    public PasswordField user_password;
    public Button login_button;
    private String role = "user";
    private String username = "test_user";
    private String password = "test_2022";




    public void Login(ActionEvent event) throws IOException {
        if (Objects.equals(user_login.getText(), username) && Objects.equals(user_password.getText(), password) &&
                Objects.equals(role, "user")) {

            FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("support space.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();


            System.out.println("logged in as user");
        } else if (Objects.equals(user_login.getText(), username) && Objects.equals(user_password.getText(), password) &&
                Objects.equals(role, "support")) {

            FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("it space.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();

            System.out.println("logged in as user");
        } else {
            System.out.println("Incorrect credentials provided!!");
            System.out.println("Please check if provided credentials are correct!!");
        }
    }

    public String hashPassword(String password){
        ArrayList<String> charactersInHex = new ArrayList<>();
        char[] charArray = password.toCharArray();
        String newString = "";
        for (char c : charArray) {
            int chToInt = (int) c;
            chToInt *= 4;
            chToInt /= 2;
            chToInt *= 8;
            chToInt /= 4;
            chToInt *= 16;
            chToInt /= 8;
            charactersInHex.add(Integer.toHexString(chToInt * (int) c));
            }
        for (String s : charactersInHex){
            newString = newString + s;
        }
        return newString;

    }

}

