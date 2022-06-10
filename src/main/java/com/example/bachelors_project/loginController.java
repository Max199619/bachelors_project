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


    public TextField user_login;
    public PasswordField user_password;
    public Button login_button;
    private String role = "user";
    private String username = "test_user";
    private String password = "test_2022";
    private SqliteConnection sqLite = new SqliteConnection();



    public void Login(ActionEvent event) throws IOException, SQLException {
        Connection conn = connectToDatabase();
        try {
            if (DBUsernameCorrect(conn)) {
                if (Objects.equals(hashPassword(user_password.getText()), getDBPassword(conn)) && Objects.equals(getDBRole(conn),"user")){
                    FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("support space.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(fxmlLoader.load());
                    stage.setScene(scene);
                    stage.show();

                    System.out.println("logged in as user");
                } else if (Objects.equals(hashPassword(user_password.getText()), getDBPassword(conn)) && Objects.equals(getDBRole(conn),"support")){
                    FXMLLoader fxmlLoader = new FXMLLoader(bachelorsProjectApplication.class.getResource("it space.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(fxmlLoader.load());
                    stage.setScene(scene);
                    stage.show();

                    System.out.println("logged in as support");
                } else {
                    System.out.println("Incorrect credentials provided!!");
                    System.out.println("Please check if provided credentials are correct!!");
                }

            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }
        public String hashPassword (String password){
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
            for (String s : charactersInHex) {
                newString = newString + s;
            }
            return newString;

        }

        public Connection connectToDatabase(){
            SqliteConnection sqLite = new SqliteConnection();
            sqLite.databaseName = "user_credentials.sqlite";
            return sqLite.getConnection();
        }

        public boolean DBUsernameCorrect(Connection db) throws SQLException {
            Statement stmt = db.createStatement();
            String sql = "SELECT EXISTS (SELECT 1 FROM user_credentials WHERE username = '" + user_login.getText() + "'";
            ResultSet rs = stmt.executeQuery(sql);
            int usernameIsMatching = 0;
            if (rs.next()){
                usernameIsMatching = rs.getInt(1);
            }
            else{System.out.println("Wrong username input");}
            return usernameIsMatching == 1;
        }

        public String getDBPassword(Connection db) throws SQLException {
            Statement stmt = db.createStatement();
            String sql = "SELECT * FROM user_credentials WHERE username = '" + user_login.getText() + "'";
            ResultSet rs = stmt.executeQuery(sql);
            String dbPassword = null;
            if (rs.next()){
                dbPassword = rs.getString(3);
            }
            else{System.out.println("Couldn't find a username in a database, password won't be checked");}
            return dbPassword;
        }

        public String getDBRole(Connection db) throws SQLException {
            Statement stmt = db.createStatement();
            String sql = "SELECT * FROM user_credentials WHERE username = '" + user_login.getText() + "'";
            ResultSet rs = stmt.executeQuery(sql);
            String dbRole = null;
            if (rs.next()){
                dbRole = rs.getString(4);
            }
            else{System.out.println("Wrong username input");}
            return dbRole;
    }

    }


