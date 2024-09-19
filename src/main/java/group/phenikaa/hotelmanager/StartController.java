package group.phenikaa.hotelmanager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML
    private Button enter_btn;

    @FXML
    private Button exit_btn;

    @FXML
    private Button minimize_btn;

    @FXML
    void getEnterScene() {
        try {
            HotelApplication.switchToMenuScene();
        } catch (IOException e) {
            e.printStackTrace();
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

    }
}

