package dk.easv.presentation.controller;
import dk.easv.entities.*;
import dk.easv.presentation.model.AppModel;
import dk.easv.presentation.components.poster.Dimensions;
import dk.easv.presentation.components.poster.ImagePoster;
import dk.easv.presentation.components.poster.ImagesControl;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.*;

public class AppController implements Initializable {
    public MFXButton rightButton;
    public MFXButton leftButton;
    @FXML
    private HBox posterRootParent;
    @FXML
    private HBox postersParent;
    private AppModel model;
    private long timerStartMillis = 0;
    private String timerMsg = "";
    @FXML
    private ScrollPane scrollPaneFirstPoster;
    private ImagesControl imagesControl;

    private boolean isRightPressed= false;

    private void startTimer(String message) {
        timerStartMillis = System.currentTimeMillis();
        timerMsg = message;
    }

    private void stopTimer() {
        System.out.println(timerMsg + " took : " + (System.currentTimeMillis() - timerStartMillis) + "ms");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setModel(AppModel model) {
        this.model = model;
        startTimer("Load users");
        model.loadUsers();
        stopTimer();
        model.loadData(model.getObsLoggedInUser());
        imagesControl=new ImagesControl(model.getObsTopMovieNotSeen());
        initializeWidthListener(model);
        loadImages();
        bindButtonsToResize();
        postersParent.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));
        posterRootParent.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));
    }


/** Ads a listener to the ScrollPane that will update the model viewPort variable, that controls the images resizing
 * @param model represents the model that is holding the observable var
 * */
    private void initializeWidthListener(AppModel model) {
        scrollPaneFirstPoster.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            model.getViewPortWidthProperty().set(newValue.getWidth());
        });

    }


    /**Loads initial movies to the scrollPane
     * */
    private void loadImages() {
        for (MovieData top : imagesControl.getNextBatchMovies()) {
            ImagePoster imagePoster = new ImagePoster(top,model);
            model.addResizable(imagePoster);
            postersParent.getChildren().add(imagePoster);
        }
    }
    private void modifyData(){
model.rewriteData();
    }

    /**Moves the scrollPane view to the left */
    public void toTheLeft(ActionEvent event) {
        double viewportWidth = this.scrollPaneFirstPoster.getViewportBounds().getWidth();
        double contentWidth = this.scrollPaneFirstPoster.getContent().prefWidth(-1); // -1 for the height value means compute the pref width for the current height
        double moveNormalized = viewportWidth / (contentWidth - viewportWidth);
        double newHvalue = Math.max(0.0, this.scrollPaneFirstPoster.getHvalue() - moveNormalized);
        this.scrollPaneFirstPoster.setHvalue(newHvalue);
    }


    /**Moves the scrollPane to the right and loads the next images from the  movie list*/
    public void toTheRight(ActionEvent event) {
        isRightPressed=true;
        this.leftButton.setVisible(true);
        loadImages();
        double viewportWidth = this.scrollPaneFirstPoster.getViewportBounds().getWidth();
        double contentWidth = this.scrollPaneFirstPoster.getContent().prefWidth(-1); // -1 for the height value means compute the pref width for the current height
        double moveNormalized = viewportWidth / (contentWidth - viewportWidth);
        double newHvalue = Math.min(1.0, Math.max(0.0, this.scrollPaneFirstPoster.getHvalue() + moveNormalized));
        this.scrollPaneFirstPoster.setHvalue(newHvalue);
    }

    private void bindButtonsToResize(){
        this.leftButton.prefWidthProperty().bind(Dimensions.getInstance().widthProperty().divide(4));
        this.leftButton.prefHeightProperty().bind(Dimensions.getInstance().heightProperty());
        this.rightButton.prefWidthProperty().bind(Dimensions.getInstance().widthProperty().divide(4));
        this.rightButton.prefHeightProperty().bind(Dimensions.getInstance().heightProperty());
    }


    public void showButtons(MouseEvent mouseEvent) {
     if(isRightPressed){
         this.leftButton.setVisible(true);
         this.rightButton.setVisible(true);
     }else{
         this.rightButton.setVisible(true);
     }
    }
    public void hideButtons(MouseEvent mouseEvent) {
        this.rightButton.setVisible(false);
        this.leftButton.setVisible(false);
    }
}


