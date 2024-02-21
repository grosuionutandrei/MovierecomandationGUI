package dk.easv.presentation.components.ratingPoster;

import dk.easv.dataaccess.apiRequest.transcripts.VideoData;
import dk.easv.dataaccess.httpRequest.Search;
import dk.easv.presentation.model.AppModel;
import dk.easv.presentation.components.playwindow.PlayWindowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
        this.setOnAction(e -> {
            Thread test = new Thread(() -> {
                Search search = new Search();
                Platform.runLater(() -> {
                            List<VideoData> data = new Search().movieResponses(model.getSelectedMovie().getTmdbId());
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
                modal.show();
            } catch (IOException ex) {
                ex.getStackTrace();
                Alert alarm = new Alert(Alert.AlertType.ERROR, "fxml error from play window" + ex.getMessage());
                alarm.show();
            }
        });
    }





}




