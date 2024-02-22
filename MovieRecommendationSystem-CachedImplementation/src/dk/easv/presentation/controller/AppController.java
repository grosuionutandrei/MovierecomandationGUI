package dk.easv.presentation.controller;

import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.entities.*;
import dk.easv.exceptions.MoviesException;
import dk.easv.presentation.components.LandingPoster.LandingImageController;
import dk.easv.presentation.components.LandingPoster.LandingPoster;
import dk.easv.presentation.components.moviePosterDisplayPanel.MoviesBanner;
import dk.easv.presentation.model.AppModel;
import dk.easv.presentation.components.poster.Dimensions;
import dk.easv.presentation.components.poster.ImagePoster;
import dk.easv.presentation.components.poster.ImagesControl;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

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
        initializeWidthListener(model);
        loadImages(imagesControlTopMoviesNotSeen, model, postersParent);
        loadImages(imagesControlTopMoviesSeen, model, postersParentMoviesSeen);
        loadImages(topRecomendedMoviesImagesControl, model, recommendedMoviesPostersParent);
        bindbuttonsToResize();
        landingPageContainer.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));
        //landingPosterStackPane.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));
        topRecomendedMovies.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));
        recommendedMoviesPostersParent.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));
        recommendedMoviesPostersParent.setSpacing(15);
        postersParent.setSpacing(15);
        postersParentMoviesSeen.setSpacing(15);
        postersParent.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));
        posterRootParent.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));
        postersParentMoviesSeen.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));
        topMoviesSeen.prefHeightProperty().bind(Dimensions.getInstance().heightProperty().add(20));
        landingImageController = new LandingImageController(model.getObsTopMoviesSimilarUsers());
       // loadLandingPoster();
        loadBannerTest();
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
        double viewportWidth = this.scrollPaneFirstPoster.getViewportBounds().getWidth();
        double contentWidth = this.scrollPaneFirstPoster.getContent().prefWidth(-1);
        double moveNormalized = viewportWidth / (contentWidth - viewportWidth);
        double newHvalue = Math.max(0.0, this.scrollPaneFirstPoster.getHvalue() - moveNormalized);
        this.scrollPaneFirstPoster.setHvalue(newHvalue);
    }


    /**
     * Moves the scrollPane to the right and loads the next images from the  movie list
     */
    public void toTheRight(ActionEvent event) {
        isRightPressed = true;
        this.leftButton.setVisible(true);
        loadImages(imagesControlTopMoviesNotSeen, model, postersParent);
        double viewportWidth = this.scrollPaneFirstPoster.getViewportBounds().getWidth();
        double contentWidth = this.scrollPaneFirstPoster.getContent().prefWidth(-1); // -1 for the height value means compute the pref width for the current height
        double moveNormalized = viewportWidth / (contentWidth - viewportWidth);
        double newHvalue = Math.min(1.0, Math.max(0.0, this.scrollPaneFirstPoster.getHvalue() + moveNormalized));
        this.scrollPaneFirstPoster.setHvalue(newHvalue);
    }

    private void bindButtonsToResize(Button left, Button right) {
        left.prefWidthProperty().bind(Dimensions.getInstance().widthProperty().divide(4));
        left.prefHeightProperty().bind(Dimensions.getInstance().heightProperty());
        right.prefWidthProperty().bind(Dimensions.getInstance().widthProperty().divide(4));
        right.prefHeightProperty().bind(Dimensions.getInstance().heightProperty());
    }


    @FXML
    private void showButtons(MouseEvent mouseEvent) {
        showButtonsOnHover(isRightPressed, leftButton, rightButton);
    }


    @FXML
    private void hideButtons(MouseEvent mouseEvent) {
        this.rightButton.setVisible(false);
        this.leftButton.setVisible(false);
    }

    @FXML
    private void toTheRightNSeen(ActionEvent event) {
        isRightNSeenPressed = true;
        this.leftButtonNSeen.setVisible(true);
        loadImages(imagesControlTopMoviesSeen, model, postersParentMoviesSeen);
        double viewportWidth = this.scrollPaneMoviesSeen.getViewportBounds().getWidth();
        double contentWidth = this.scrollPaneMoviesSeen.getContent().prefWidth(-1); // -1 for the height value means compute the pref width for the current height
        double moveNormalized = viewportWidth / (contentWidth - viewportWidth);
        double newHvalue = Math.min(1.0, Math.max(0.0, this.scrollPaneMoviesSeen.getHvalue() + moveNormalized));
        this.scrollPaneMoviesSeen.setHvalue(newHvalue);
    }

    @FXML
    private void toTheLeftNSeen(ActionEvent event) {
        double viewportWidth = this.scrollPaneMoviesSeen.getViewportBounds().getWidth();
        double contentWidth = this.scrollPaneMoviesSeen.getContent().prefWidth(-1);
        double moveNormalized = viewportWidth / (contentWidth - viewportWidth);
        double newHvalue = Math.max(0.0, this.scrollPaneMoviesSeen.getHvalue() - moveNormalized);
        this.scrollPaneMoviesSeen.setHvalue(newHvalue);
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

    public void loadLandingPoster() throws MoviesException {
        List<MovieSearchResponse> movieData = landingImageController.getProperMoviesForLandingPage();
        LandingPoster landingPoster = new LandingPoster(movieData, true);
        model.addResizable(landingPoster);
        landingPosterStackPane.getChildren().add(landingPoster);
    }

    public void toTheRightRecomended(ActionEvent event) {
        isRightRecommendedPressed = true;
        this.leftButtonRecomended.setVisible(true);
        loadImages(topRecomendedMoviesImagesControl, model, recommendedMoviesPostersParent);
        double viewportWidth = this.scrollPaneRecomendedMovies.getViewportBounds().getWidth();
        double contentWidth = this.scrollPaneRecomendedMovies.getContent().prefWidth(-1); // -1 for the height value means compute the pref width for the current height
        double moveNormalized = viewportWidth / (contentWidth - viewportWidth);
        double newHvalue = Math.min(1.0, Math.max(0.0, this.scrollPaneRecomendedMovies.getHvalue() + moveNormalized));
        this.scrollPaneRecomendedMovies.setHvalue(newHvalue);
    }

    public void toTheLeftRecomended(ActionEvent event) {
        double viewportWidth = this.scrollPaneRecomendedMovies.getViewportBounds().getWidth();
        double contentWidth = this.scrollPaneRecomendedMovies.getContent().prefWidth(-1);
        double moveNormalized = viewportWidth / (contentWidth - viewportWidth);
        double newHvalue = Math.max(0.0, this.scrollPaneRecomendedMovies.getHvalue() - moveNormalized);
        this.scrollPaneRecomendedMovies.setHvalue(newHvalue);
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

    private void loadBannerTest(){
        MoviesBanner moviesBanner =  new MoviesBanner(this.model);
        System.out.println(moviesBanner.getLandingPageRoot());
     //   moviesBanner.initializeMovieBanner();
        landingPageContainer.getChildren().add(moviesBanner.getLandingPageRoot());
    }

}


