package group.phenikaa.hotelmanager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML private Button minimize_btn;
    @FXML private Button enter_btn;
    @FXML private TextField username;
    @FXML private TextField password;

    protected final String USERNAME = "1";
    protected final String PASSWORD = "1";

    @FXML
    void getEnterScene() throws IOException {
        if (username.getText().equals(USERNAME) && password.getText().equals(PASSWORD)) {
            HotelApplication.switchToMenuScene();
        } else {
            HotelController.showAlert(Alert.AlertType.ERROR, "Something went wrong", "Please try again");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enter_btn.setDisable(true);

        ChangeListener<String> textFieldChangeListener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean isBothFieldsFilled = !username.getText().isEmpty() && !password.getText().isEmpty();
            enter_btn.setDisable(!isBothFieldsFilled);

            if (isBothFieldsFilled) {
                enter_btn.setStyle("-fx-background-color: #48B9FDBF;");
            } else {
                enter_btn.setStyle("-fx-background-color: grey;");
            }
        };

        username.textProperty().addListener(textFieldChangeListener);
        password.textProperty().addListener(textFieldChangeListener);
    }
}

