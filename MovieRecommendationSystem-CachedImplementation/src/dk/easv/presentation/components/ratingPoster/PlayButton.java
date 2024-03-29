package dk.easv.presentation.components.ratingPoster;

import dk.easv.dataaccess.apiRequest.transcripts.VideoData;
import dk.easv.exceptions.ExceptionHandler;
import dk.easv.presentation.model.AppModel;
import dk.easv.presentation.components.playwindow.PlayWindowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class PlayButton extends MFXButton {
    private AppModel model;

    public PlayButton(AppModel model) {
        super("Play");
        this.model = model;
        this.setButtonType(ButtonType.RAISED);
        this.getStyleClass().add("playButton");
        this.setText(">");
        setOnAction();
    }

    private void setOnAction() {
        this.setOnMouseClicked(e -> {
            Thread test = new Thread(() -> {
                List<VideoData> data = model.getVideoData(model.getSelectedMovie().getTmdbId());
                Platform.runLater(() -> {
                            if (data != null && (!data.isEmpty())) {
                                model.setVideoData(data.get(0));
                            } else {
                                VideoData videoData = new VideoData();
                                videoData.setKey("2tm61CNECmU");
                                videoData.setSite("default");
                                model.setVideoData(videoData);
                            }
                        }
                );
            });
            test.start();
            Stage currentStage = (Stage) ((Scene) ((Node) e.getSource()).getScene()).getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../playwindow/playWindow.fxml"));
            try {
                model.coords[0] = e.getScreenX();
                model.coords[1] = e.getScreenY();
                Parent root = loader.load();
                PlayWindowController playWindowController = loader.getController();
                playWindowController.setDisplayable(model);
                playWindowController.getBackWindowDimensions(currentStage);
                Scene scene = new Scene(root);
                Stage modal = new Stage();
                modal.initOwner(currentStage);
                modal.initModality(Modality.APPLICATION_MODAL);
                modal.initStyle(StageStyle.UNDECORATED);
                modal.setScene(scene);
                playWindowController.getNewStage(scene, modal);
            } catch (IOException ex) {
                ex.getStackTrace();
                ExceptionHandler.displayErrorAlert(ex.getMessage(), null);
            }
        });
    }

    public void cleanUp() {
        this.setOnMouseClicked(null);
    }
}




