package com.example.bachelors_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class loginController {

    private Stage stage;
    private Scene scene;

    private Connection db = connectToDatabase();
    public TextField user_login;
    public PasswordField user_password;
    public Button login_button;


    public void Login(ActionEvent event) throws SQLException, IOException {
        Connection conn = connectToDatabase();
        if (DBUsernameCorrect( user_login.getText())) {
            if (passwordCorrect() && (getDBRole(user_login.getText()).equals("user"))) {
                FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("support space.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                supportSpaceController controller_user = fxmlLoader.getController();
                controller_user.setUser_name_and_surname(user_login.getText());
                controller_user.fillRequestList(user_login.getText());
                stage.show();


                System.out.println("logged in as user");
                db.close();
                } else if (passwordCorrect() && (getDBRole(user_login.getText()).equals("support"))) {
                    FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("it space.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(fxmlLoader.load());
                    itSpaceController itSpace = fxmlLoader.getController();
                    itSpace.setUserNameAndSurname(user_login.getText());
                    itSpace.fillRequestList(user_login.getText());
                    stage.setScene(scene);
                    stage.show();

                System.out.println("logged in as support");
                db.close();
            } else {
                System.out.println("Incorrect credentials provided!!");
                System.out.println("Please check if provided credentials are correct!!");
            }
        } else if (Objects.equals(hashPassword(user_password.getText()), getDBPassword(user_login.getText())) && Objects.equals(getDBRole(user_login.getText()), "support")) {
            FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("it space.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();

            System.out.println("logged in as support");
            db.close();
        } else {
            System.out.println("Incorrect credentials provided!!");
            System.out.println("Please check if provided credentials are correct!!");
        }
    }



    public String hashPassword (String password){
        ArrayList<String> charactersInHex = new ArrayList<>();
        char[] charArray = password.toCharArray();
        String newString = "";
        for (char c : charArray) {
            int chToInt = c;
            chToInt *= 4;
            chToInt /= 2;
            chToInt *= 8;
            chToInt /= 4;
            chToInt *= 16;
            chToInt /= 8;
            charactersInHex.add(Integer.toHexString(chToInt * (int) c));
        }
        for (String s : charactersInHex) {
            newString += s ;
        }
        return newString;

    }

    public Connection connectToDatabase(){
        SqliteConnection sqLite = new SqliteConnection();
        sqLite.databaseName = "user_credentials.db";
        return sqLite.getConnection();
    }

    public boolean DBUsernameCorrect(String username) throws SQLException {
        String sql = "SELECT EXISTS (SELECT 1 FROM user_credentials WHERE username = ?)";
        PreparedStatement ps;
        ps = db.prepareStatement(sql);
        ps.setString(1,username);
        ResultSet rs = ps.executeQuery();
        int usernameIsMatching = 0;
        if (rs.next()) {
            usernameIsMatching = rs.getInt(1);
        } else {
            System.out.println("Wrong username input");
        }
        rs.close();
        return usernameIsMatching == 1;

    }




    public String getDBPassword(String username) throws SQLException {
        String dbPassword = "";
        String sql = "SELECT password FROM user_credentials WHERE username = ?";
        PreparedStatement ps;
        ps = db.prepareStatement(sql);
        ps.setString(1,username);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            dbPassword = rs.getString(1);
        }
        rs.close();
        return dbPassword;
    }

    public String getDBRole(String username) throws SQLException {
        String dbRole = "";
        String sql = "SELECT role FROM user_credentials WHERE username = ?";
        PreparedStatement ps;
        ps = db.prepareStatement(sql);
        ps.setString(1,username);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            dbRole = rs.getString(1);
        }
        rs.close();
        return dbRole;
    }

    public boolean passwordCorrect() throws SQLException {
        boolean passwordMatch;
        String dbPassword = getDBPassword(user_login.getText());
        passwordMatch = dbPassword.equals(hashPassword(user_password.getText()));
        return passwordMatch;
    }
}


