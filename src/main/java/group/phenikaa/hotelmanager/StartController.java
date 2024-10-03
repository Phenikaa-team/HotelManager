package group.phenikaa.hotelmanager;

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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static group.phenikaa.hotelmanager.HotelApplication.USER;
import static group.phenikaa.hotelmanager.api.utility.Utility.showAlert;

public class StartController implements Initializable {

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
    void getEnterScene() throws IOException {
        List<User> user = loginData.load(USER);

        for (User info : user) {
            if (info.getUsername().equals(username.getText()) && info.getPassword().equals(password.getText())) {
                User loggedInUser = new User(username.getText(), password.getText());
                Session.setCurrentUser(loggedInUser);
                HotelApplication.switchToMenuScene();
                return;
            }
        }

        showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
    }

    @FXML
    void registerAccount() {
        List<User> user = loginData.load(USER);

        for (User info : user) {
            if (info.getUsername().equals(username.getText())) {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Username already taken.");
                return;
            }
        }

        User newCustomer = new User(username.getText(), password.getText());
        user.add(newCustomer);
        loginData.save(user, USER);

        showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Account created successfully.");
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
