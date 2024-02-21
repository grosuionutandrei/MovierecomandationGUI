package dk.easv.presentation.components.playwindow;
import dk.easv.presentation.model.AppModel;
import dk.easv.presentation.listeners.Displayable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayWindowController implements Initializable, Displayable {

//    private final int MAX_WIDTH = 768;
//    private final int MAX_HEIGHT = 432;
//    private final int MIN_WIDTH = 256;
//    private final int MIN_HEIGHT = 144;
    @FXML
    private HBox embedContainer;
    private AppModel model;
    @FXML
    private VBox videoPlayerContainer;
    @FXML
    private Stage oldStage;
    Stage curentStage;
    Scene currentScene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox.setVgrow(videoPlayerContainer, Priority.ALWAYS);
    }

    @FXML
    private void closePlayWindow(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        WebView webView = (WebView) embedContainer.getChildren().get(0);
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent("");
        stage.close();
    }

    public void setDisplayable(AppModel model) {
        model.setVideoPlayer(this);
        this.model = model;
        setConatinersDimenssions();
    }

    public void getBackWindowDimensions(Stage oldStage) {
        this.oldStage = oldStage;
    }


    @Override
    public void updateView() {
        model.getMediaToBePlayed(this.embedContainer);
        curentStage.show();
        curentStage = (Stage) videoPlayerContainer.getScene().getWindow();
        positionStageCenter(oldStage, curentStage);
//        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500));
//        fadeTransition.setNode(videoPlayerContainer);
//        fadeTransition.setFromValue(0);
//        fadeTransition.setToValue(1);
//        fadeTransition.play();
    }

    private void setConatinersDimenssions() {
        videoPlayerContainer.setPrefWidth(model.getVideoPlayerWindowDimensions()[0]);
        videoPlayerContainer.setPrefHeight(model.getVideoPlayerWindowDimensions()[1]);
        videoPlayerContainer.setMaxWidth(model.getVideoPlayerWindowDimensions()[0]);
        videoPlayerContainer.setMaxHeight(model.getVideoPlayerWindowDimensions()[1]);
        embedContainer.setPrefWidth(model.getVideoPlayerWindowDimensions()[0]);
        embedContainer.setPrefHeight(model.getVideoPlayerWindowDimensions()[1]);
        embedContainer.setMaxWidth(model.getVideoPlayerWindowDimensions()[0]);
        embedContainer.setMaxHeight(model.getVideoPlayerWindowDimensions()[1]);
    }


    private void positionStageCenter(Stage oldStage, Stage newStage) {
        newStage.sizeToScene();
        double oldX = oldStage.getX();
        double oldY = oldStage.getY();
        double oldWidth = oldStage.getWidth();
        double oldHeight = oldStage.getHeight();
        double newX = oldX + (oldWidth / 2) - (newStage.getWidth() / 2);
        double newY = oldY + oldHeight / 3;
        newStage.setX(newX);
        newStage.setY(newY);
    }

    public void getNewStage(Scene scene, Stage stage) {
        currentScene = scene;
        curentStage = stage;
    }


}
