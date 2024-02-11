package dk.easv.presentation.playwindow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayWindowController implements Initializable {



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(" i am here");
    }



    @FXML
    private void closePlayWindow(javafx.event.ActionEvent event) {
        Stage stage = (Stage)((Scene)((Node)event.getSource()).getScene()).getWindow();
        System.out.println("I am closing the stage");
        stage.close();
    }
}
