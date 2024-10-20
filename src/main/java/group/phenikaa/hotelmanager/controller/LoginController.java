package group.phenikaa.hotelmanager.controller;

import group.phenikaa.hotelmanager.HotelApplication;
import group.phenikaa.hotelmanager.api.info.impl.customer.Session;
import group.phenikaa.hotelmanager.api.info.impl.customer.User;
import group.phenikaa.hotelmanager.asm.database.LoginDatabase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static group.phenikaa.hotelmanager.api.utility.Utility.showAlert;

public class LoginController implements Initializable {

    @FXML private Button minimize_btn;
    @FXML private Button enter_btn;
    @FXML private Button register_btn;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private TextField passwordVisible;
    @FXML private Button show_hide;
    @FXML private ImageView eye_icon;

    private boolean isPasswordVisible = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enter_btn.setDisable(true);
        register_btn.setDisable(true);

        passwordVisible.setManaged(false);
        passwordVisible.setVisible(false);

        ChangeListener<String> textFieldChangeListener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean isBothFieldsFilled = !username.getText().isEmpty() && (!password.getText().isEmpty() || !passwordVisible.getText().isEmpty());
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
        passwordVisible.textProperty().addListener(textFieldChangeListener);

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isPasswordVisible) {
                passwordVisible.setText(newValue);
            }
        });

        passwordVisible.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isPasswordVisible) {
                password.setText(newValue);
            }
        });
    }

    @FXML
    void getEnterScene() {
        try {
            User loggedInUser = LoginDatabase.login(username.getText(), password.getText());

            if (loggedInUser != null) {
                Session.setCurrentUser(loggedInUser);
                HotelApplication.switchToMenuScene();
            } else {
                throw new Exception("Invalid username or password.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Login error", "An error occurred while accessing the database.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Login failed", e.getMessage());
        }
    }

    @FXML
    void registerAccount() {
        try {
            User newCustomer = new User(username.getText(), password.getText());
            LoginDatabase.register(newCustomer);  // This now handles SQL exceptions

            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Account Created Successfully.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Registration error", "An error occurred while saving to the database.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Registration failed", e.getMessage());
        }
    }

    @FXML
    void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            password.setVisible(false);
            password.setManaged(false);
            passwordVisible.setVisible(true);
            passwordVisible.setManaged(true);

            eye_icon.setImage(new Image(getClass().getResourceAsStream("/assets/textures/hide.png")));
        } else {
            password.setVisible(true);
            password.setManaged(true);
            passwordVisible.setVisible(false);
            passwordVisible.setManaged(false);

            eye_icon.setImage(new Image(getClass().getResourceAsStream("/assets/textures/show.png")));
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
