package group.phenikaa.hotelmanager.impl.controller;

import group.phenikaa.hotelmanager.HotelApplication;
import group.phenikaa.hotelmanager.api.info.impl.customer.Session;
import group.phenikaa.hotelmanager.api.info.impl.customer.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import group.phenikaa.hotelmanager.impl.data.LoginData;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static group.phenikaa.hotelmanager.api.utility.Utility.showAlert;

public class LoginController implements Initializable {

    @FXML private Button minimize_btn;
    @FXML private Button enter_btn;
    @FXML private Button register_btn;
    @FXML private TextField username;
    @FXML private TextField password;

    private LoginData loginData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginData = new LoginData();

        enter_btn.setDisable(true);
        register_btn.setDisable(true);

        ChangeListener<String> textFieldChangeListener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean isBothFieldsFilled = !username.getText().isEmpty() && !password.getText().isEmpty();
            enter_btn.setDisable(!isBothFieldsFilled);
            register_btn.setDisable(!isBothFieldsFilled);

            if (isBothFieldsFilled) {
                enter_btn.setStyle("-fx-background-color: #48B9FDBF;");
            } else {
                enter_btn.setStyle("-fx-background-color: grey;");
            }
        };

        username.textProperty().addListener(textFieldChangeListener);
        password.textProperty().addListener(textFieldChangeListener);
    }

    @FXML
    void getEnterScene() {
        try {
            User loggedInUser = loginData.login(username.getText(), password.getText());

            if (loggedInUser != null) {
                Session.setCurrentUser(loggedInUser);
                HotelApplication.switchToMenuScene();
            } else {
                throw new Exception("Invalid username or password.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while accessing the database.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", e.getMessage());
        }
    }

    @FXML
    void registerAccount() {
        try {
            User newCustomer = new User(username.getText(), password.getText());
            loginData.register(newCustomer);  // This now handles SQL exceptions

            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Account created successfully.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "An error occurred while saving to the database.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", e.getMessage());
        }
    }

    @FXML
    void onExit() {
        System.exit(0);
    }

    @FXML
    void onMinimize() {
        var stage = (Stage) minimize_btn.getScene().getWindow();
        stage.setIconified(true);
    }
}
