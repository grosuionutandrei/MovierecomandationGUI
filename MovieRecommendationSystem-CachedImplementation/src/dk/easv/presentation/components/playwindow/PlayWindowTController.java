package dk.easv.presentation.components.playwindow;
import dk.easv.exceptions.ExceptionHandler;
import dk.easv.presentation.listeners.Displayable;
import dk.easv.presentation.model.AppModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.io.IOException;

public class PlayWindowTController implements Displayable {

    @FXML
    private StackPane backgroundModal;
    @FXML
    private HBox embedContainer;
    @FXML
    private VBox videoPlayerContainer;
    private AppModel model;

    public PlayWindowTController(AppModel model) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/playWindow2.fxml"));
        try {
            Parent root = loader.load();
            VBox.setVgrow(videoPlayerContainer, Priority.ALWAYS);
            this.backgroundModal.setVisible(false);
           setDisplayable(model);
        } catch (IOException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage(), null);
        }
    }


    @FXML
    private void closeWindow(MouseEvent mouseEvent) {
        WebView webView = (WebView) embedContainer.getChildren().get(0);
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent("");
        this.backgroundModal.setVisible(false);
    }

    @Override
    public void updateView() {
        model.getMediaToBePlayed(this.embedContainer);
        this.backgroundModal.setVisible(true);
    }


    private void setDisplayable(AppModel model) {
        model.setVideoPlayer(this);
        this.model = model;
        setConatinersDimenssions();
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

    public void showModal() {
        this.backgroundModal.setVisible(true);
    }
}
