package dk.easv.presentation.controller;

import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.entities.*;
import dk.easv.exceptions.MoviesException;
import dk.easv.presentation.components.LandingPoster.LandingImageController;
import dk.easv.presentation.components.LandingPoster.LandingPoster;
import dk.easv.presentation.model.AppModel;
import dk.easv.presentation.components.poster.Dimensions;
import dk.easv.presentation.components.LandingPoster.LandingPosterDimensions;
import dk.easv.presentation.components.poster.ImagePoster;
import dk.easv.presentation.components.poster.ImagesControl;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class AppController implements Initializable {

    @FXML
    private MFXButton rightButton;
    @FXML
    private MFXButton leftButton;
    @FXML
    private ScrollPane layoutContainer;
    @FXML
    private HBox landingPageContainer;
    /**
     * left button for the recommended movies
     */
    @FXML
    private MFXButton leftButtonRecomended;

    /**
     * right button for recommended movies
     */
    @FXML
    private MFXButton rightButtonRecomended;

    /**
     * container for the scroll pane that contains the recommended movies for the user
     */
    @FXML
    private HBox topRecomendedMovies;

    /**
     * container for the posters off the recommended movies
     */
    @FXML
    private HBox recommendedMoviesPostersParent;

    /**
     * controls what recommended movies are displayed
     */
    @FXML
    private ImagesControl topRecomendedMoviesImagesControl;

    /**
     * container for the recommended movies posters and navigation buttons
     */
    @FXML
    private ScrollPane scrollPaneRecomendedMovies;

    /**
     * container for the landing page
     */

    private LandingImageController landingImageController;
    @FXML
    private StackPane landingPosterStackPane;

    /**
     * posterRootParent holds the scroll pane with the posters for the movies
     */
    @FXML
    private HBox posterRootParent;
    /**
     * posterParent is holding the posters with the movies
     */
    @FXML
    private HBox postersParent;
    /**
     * topMoviesSeen holds the view for the top movies user have seen
     */
    @FXML
    private HBox topMoviesSeen;

    /**
     * postersParentMovieSeen holds the posters  for seen movies
     */
    @FXML
    private HBox postersParentMoviesSeen;
    @FXML
    private MFXButton rightButtonNSeen;
    @FXML
    private MFXButton leftButtonNSeen;
    @FXML
    private ScrollPane scrollPaneMoviesSeen;
    /**
     * controls the loading off the seen movies
     */
    private ImagesControl imagesControlTopMoviesSeen;
    private AppModel model;
    private long timerStartMillis = 0;
    private String timerMsg = "";
    @FXML
    private ScrollPane scrollPaneFirstPoster;
    private ImagesControl imagesControlTopMoviesNotSeen;
    private boolean isRightPressed = false;
    private boolean isRightNSeenPressed = false;
    private boolean isRightRecommendedPressed = false;

    private LandingPosterDimensions landingPosterDimensions = LandingPosterDimensions.getInstance(1100, 600);

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

    public void setModel(AppModel model) throws MoviesException {
        this.model = model;
        model.loadUsers();
        model.loadData(model.getObsLoggedInUser());
        imagesControlTopMoviesNotSeen = new ImagesControl(model.getObsTopMovieNotSeen());
        imagesControlTopMoviesSeen = new ImagesControl(model.getObsTopMovieSeen());
        topRecomendedMoviesImagesControl = new ImagesControl(model.recommendedMoviesList());
        loadImages(imagesControlTopMoviesNotSeen, model, postersParent);
        loadImages(imagesControlTopMoviesSeen, model, postersParentMoviesSeen);
        loadImages(topRecomendedMoviesImagesControl, model, recommendedMoviesPostersParent);
        bindbuttonsToResize();

        landingPageContainer.prefHeightProperty().bind(LandingPosterDimensions.getInstance().heightProperty().add(20));
        landingPosterStackPane.prefHeightProperty().bind(LandingPosterDimensions.getInstance().heightProperty().add(20));

        topRecomendedMovies.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));

        recommendedMoviesPostersParent.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));
        setSpacingBetweenPosters(15);
        postersParent.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));

        posterRootParent.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));
        postersParentMoviesSeen.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));

        topMoviesSeen.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));
        landingImageController = new LandingImageController(model.getObsTopMoviesSimilarUsers());
        loadLandingPoster();
    }


    private void bindbuttonsToResize() {
        bindButtonsToResize(this.leftButton, this.rightButton);
        bindButtonsToResize(this.leftButtonNSeen, this.rightButtonNSeen);
        bindButtonsToResize(this.leftButtonRecomended, this.rightButtonRecomended);
    }


    /**
     * Ads a listener to the ScrollPane that will update the model viewPort variable, that controls the images resizing
     *
     * @param model represents the model that is holding the observable var
     */
    private void initializeWidthListener(AppModel model) {
        scrollPaneFirstPoster.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            model.getViewPortWidthProperty().set(newValue.getWidth());
        });

    }

    /**
     * Loads initial movies to the scrollPane
     */
    private void loadImages(ImagesControl imagesControl, AppModel model, HBox parent) {
        for (MovieData top : imagesControl.getNextBatchMovies()) {
            ImagePoster imagePoster = new ImagePoster(top, model);
            model.addResizable(imagePoster);
            parent.getChildren().add(imagePoster);
        }
    }


    /**
     * Moves the scrollPane view to the left
     */
    public void toTheLeft(ActionEvent event) {
        double newHvalue = calculateScrollPaneHBarValue(this.scrollPaneFirstPoster, false);
        applySmoothEffect(null, newHvalue, this.scrollPaneFirstPoster);
    }


    /**
     * Moves the scrollPane to the right and loads the next images from the  movie list
     */
    public void toTheRight(ActionEvent event) {
        isRightPressed = true;
        this.leftButton.setVisible(true);
        loadImages(imagesControlTopMoviesNotSeen, model, postersParent);
        double newHvalue = calculateScrollPaneHBarValue(scrollPaneFirstPoster, true);
        applySmoothEffect(null, newHvalue, scrollPaneFirstPoster);
    }

    private void bindButtonsToResize(Button left, Button right) {
        left.prefWidthProperty().bind(Dimensions.getInstance().widthProperty().divide(4));
        left.prefHeightProperty().bind(Dimensions.getInstance().heightProperty());
        right.prefWidthProperty().bind(Dimensions.getInstance().widthProperty().divide(4));
        right.prefHeightProperty().bind(Dimensions.getInstance().heightProperty());
    }

    /**
     * show navigation buttons for the seen movies
     */
    @FXML
    private void showButtons(MouseEvent mouseEvent) {
        showButtonsOnHover(isRightPressed, leftButton, rightButton);
    }


    /**
     * hide navigation buttons for the seen movies
     */
    @FXML
    private void hideButtons(MouseEvent mouseEvent) {
        hideButtonsOnExit(this.rightButton, this.leftButton);
    }

    /**
     * loads the seen movie and move the scroll bar to the right
     */
    @FXML
    private void toTheRightNSeen(ActionEvent event) {
        isRightNSeenPressed = true;
        this.leftButtonNSeen.setVisible(true);
        loadImages(imagesControlTopMoviesSeen, model, postersParentMoviesSeen);
        double newHvalue = calculateScrollPaneHBarValue(this.scrollPaneMoviesSeen, true);
        applySmoothEffect(null, newHvalue, this.scrollPaneMoviesSeen);
    }

    /**
     * loads the seen movie and move the scroll bar to the left
     */
    @FXML
    private void toTheLeftNSeen(ActionEvent event) {
        double newHvalue = calculateScrollPaneHBarValue(this.scrollPaneMoviesSeen, false);
        applySmoothEffect(null, newHvalue, this.scrollPaneMoviesSeen);
    }


    @FXML
    private void showSeenButton(MouseEvent mouseEvent) {
        showButtonsOnHover(isRightNSeenPressed, leftButtonNSeen, rightButtonNSeen);
    }

    @FXML
    private void hideSeenButton(MouseEvent mouseEvent) {
        hideButtonsOnExit(leftButtonNSeen, rightButtonNSeen);
    }


    /**
     * showButtons on hover , if the right is pressed than the left button is showed
     *
     * @param isPressed boolean value that holds the pressed state
     * @param left      the left button than is controlled
     * @param right     the right button that is controlled
     */
    private void showButtonsOnHover(Boolean isPressed, Button left, Button right) {
        if (isPressed) {
            left.setVisible(true);
            right.setVisible(true);
        } else {
            right.setVisible(true);
        }
    }


    /**
     * hide buttons on mouse exit from the stack pane
     *
     * @param left
     * @param right represents the buttons to be hidden
     */
    private void hideButtonsOnExit(Button left, Button right) {
        left.setVisible(false);
        right.setVisible(false);
    }


    /**
     * loads the landing poster component
     */
    public void loadLandingPoster() throws MoviesException {
        List<MovieSearchResponse> movieData = landingImageController.getProperMoviesForLandingPage();
        LandingPoster landingPoster = new LandingPoster(movieData, true, this.model);
        model.addResizable(landingPoster);
        landingPosterStackPane.getChildren().add(landingPoster);
    }

    /**
     * loads the reccomanded movies and move the scroll bar to the right
     */
    public void toTheRightRecomended(ActionEvent event) {
        isRightRecommendedPressed = true;
        this.leftButtonRecomended.setVisible(true);
        loadImages(topRecomendedMoviesImagesControl, model, recommendedMoviesPostersParent);
        double newHvalue = calculateScrollPaneHBarValue(this.scrollPaneRecomendedMovies, true);
        applySmoothEffect(null, newHvalue, this.scrollPaneRecomendedMovies);
    }

    public void toTheLeftRecomended(ActionEvent event) {
        double newHvalue = calculateScrollPaneHBarValue(this.scrollPaneRecomendedMovies, false);
        applySmoothEffect(null, newHvalue, this.scrollPaneRecomendedMovies);
    }


    /**
     * show the navigation buttons for the recommended movies posters when mouse over
     */
    @FXML
    private void showRecommendedButtons(MouseEvent mouseEvent) {
        showButtonsOnHover(isRightRecommendedPressed, leftButtonRecomended, rightButtonRecomended);
    }

    /**
     * hides the navigation buttons for the recommended movies poster when mouse exit
     */
    @FXML
    private void hideRecommendedButtons(MouseEvent mouseEvent) {
        hideButtonsOnExit(leftButtonRecomended, rightButtonRecomended);
    }

    /**
     * Apply a smooth transition animation to the scroll pane
     *
     * @param newHvalue represnts the new value where the scroll pane hbar needs to move
     */
    private void applySmoothEffect(Double millis, double newHvalue, ScrollPane scrollPane) {
        double milliSeconds = 500;
        if (millis != null) {
            milliSeconds = millis;
        }
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(scrollPane.hvalueProperty(), newHvalue);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(milliSeconds), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void setSpacingBetweenPosters(int spacingValue) {
        recommendedMoviesPostersParent.setSpacing(spacingValue);
        postersParent.setSpacing(spacingValue);
        postersParentMoviesSeen.setSpacing(spacingValue);
    }

    private double calculateScrollPaneHBarValue(ScrollPane scrollPane, boolean moveRight) {
        final double imageWidth = 250;
        final double spacing = 15;
        final double totalImageWidth = imageWidth + spacing;
        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double contentWidth = scrollPane.getContent().prefWidth(-1);
        int imagesThatFitInView = (int) Math.floor(viewportWidth / totalImageWidth);
        double moveNormalized = (double) imagesThatFitInView * totalImageWidth / (contentWidth - viewportWidth);
        double newHvalue;
        if (moveRight) {
            newHvalue = Math.min(1.0, scrollPane.getHvalue() + moveNormalized);
        } else {
            newHvalue = Math.max(0.0, scrollPane.getHvalue() - moveNormalized);
        }
        return newHvalue;
    }

}

