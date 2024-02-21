package dk.easv.presentation.components.LandingPoster;

import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.dataaccess.httpRequest.ImageDao;
import dk.easv.presentation.listeners.Resizable;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class LandingPoster extends StackPane implements Resizable {
    @FXML
    private ImageView imageView;
    @FXML
    private Rectangle rectangle;
    @FXML
    private VBox allLabelsOverPicture;
    @FXML
    private HBox buttonsForPoster;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descLabel;
    @FXML
    private Button watchNowButton;
    @FXML
    private Button moreButton;

    @FXML
    private StackPane innerStackPane;
    private final int Poster_WIDTH = 1100;
    private final int Poster_HEIGHT = 600;

    private static final double MIN_WIDTH = 300; // Example minimum width
    private static final double MIN_HEIGHT = 200;
    private List<FadeTransition> fades = new ArrayList<>();
    private int imageIndex = 0;
    private int fadeIndex = 0;

    private LandingPosterDimensions landingPosterDimensions = LandingPosterDimensions.getInstance(Poster_WIDTH, Poster_HEIGHT);

    private PauseTransition pauseTransition = new PauseTransition(Duration.seconds(10));

    private List<StackPane> stackPaneList = new ArrayList<>();


    public LandingPoster(List<MovieSearchResponse> movieSearchResponseList, Boolean isLandingPoster) {

        super();
        // get all the movies
        List<MovieSearchResponse> movieList = new ArrayList<>(movieSearchResponseList);
        // set all the movies
        setAllMovies(movieList);

    }

    private void setAllMovies(List<MovieSearchResponse> movieList) {

        // set up all movies
        for (MovieSearchResponse movie : movieList) {

            // set movie on StackPane
            StackPane stackPane = setMovie(movie);

            // set visibility to false, otherwise everything will be shown in the same time
            stackPane.setVisible(false);

            // set fade transitions
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), stackPane); // set up fade in
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), stackPane); // set fade out
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            // fade in is set for even numbers and fade out is set for odd numbers
            fades.add(fadeIn);
            fades.add(fadeOut);

            stackPaneList.add(stackPane);
            this.getChildren().add(stackPane);
        }

        // make the first image visible
        stackPaneList.getFirst().setVisible(true);

        // call fade transition function
        callFade(fadeIndex);

    }

    private StackPane setMovie(MovieSearchResponse movie) {
        // StackPane to stack everything on image
        StackPane innerStackPane = new StackPane();

        // setting up movie components
        setImage(movie);
        setLandingPosterDimensions(landingPosterDimensions.getWidth(), landingPosterDimensions.getHeight());
        setRectangle();
        setParentVBox();
        setTitle(movie);
        setDescription(movie);
        setButtonsHBox();
        setButtons();

        /* Adding children to parents */
        buttonsForPoster.getChildren().addAll(watchNowButton, moreButton); // buttons to HBox
        allLabelsOverPicture.getChildren().addAll(titleLabel, descLabel, buttonsForPoster); // title , description and buttons to VBox
        innerStackPane.getChildren().addAll(imageView, rectangle, allLabelsOverPicture); // adding imageView, then rectangle on top of that, and at the end, spawning vbox which contains all text labels and buttons
        return innerStackPane;
    }


    public void setLandingPosterDimensions(double width, double height) {
        this.imageView.setFitWidth(width);
        this.imageView.setFitHeight(height);
        this.imageView.setPreserveRatio(false);
    }

    private void setImage(MovieSearchResponse movie) {
        this.imageView = new ImageView();
        getImage(this.imageView, movie.getBackdrop_path());

    }


    private void getImage(ImageView image, String uri) {
        ImageDao imageDao = new ImageDao();
        imageDao.getImage(image, uri, true);
    }


    /**
     * Vertical rectangle on the poster image
     */
    private void setRectangle() {
        this.rectangle = new Rectangle();
        // Rectangle coloring
        LinearGradient paint = new LinearGradient(
                0.6905, 0.3095, 0.6905, 0.9238, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, new Color(0.0784, 0.0745, 0.0745, 0.02)),
                new Stop(1.0, new Color(0.0784, 0.0745, 0.0745, 1.0)));
        rectangle.setFill(paint);

        // Binding rectangle dimensions to main image
        rectangle.widthProperty().bind(imageView.fitWidthProperty());
        rectangle.heightProperty().bind(imageView.fitHeightProperty());
    }

    private void setParentVBox() {
        this.allLabelsOverPicture = new VBox();
        allLabelsOverPicture.setSpacing(30);
        allLabelsOverPicture.setAlignment(Pos.CENTER_LEFT);
        StackPane.setMargin(allLabelsOverPicture, new Insets(0, 0, 0, 80)); // set some padding for vbox which is inside StackPane

    }

    private void setTitle(MovieSearchResponse movie) {
        this.titleLabel = new Label();
        titleLabel.setText(movie.getTitle());
        titleLabel.setFont(Font.font("DejaVu Sans", FontWeight.BOLD, 50));
        titleLabel.setStyle("-fx-text-fill: #ddd7d7");


    }

    private void setDescription(MovieSearchResponse movie) {
        this.descLabel = new Label();
        descLabel.setText(movie.getOverview());
        descLabel.setFont(Font.font("Hack", FontWeight.BOLD, 17));
        descLabel.setStyle("-fx-text-fill: #a1a1a1");
        descLabel.setMaxWidth(400);
        descLabel.setMaxHeight(100);
        descLabel.setWrapText(true);

    }

    private void setButtonsHBox() {
        this.buttonsForPoster = new HBox();
        buttonsForPoster.setSpacing(20);
        buttonsForPoster.getStylesheets().add("dk/easv/presentation/components/LandingPoster/LandingPoster.css"); // addressing css file

    }

    private void setButtons() {
        this.watchNowButton = new Button("Watch Online");
        this.moreButton = new Button("More");

        // Sizing the buttons, NOTE: needs to be resizable on future
        watchNowButton.setPrefSize(140, 40);
        moreButton.setPrefSize(80, 40);

        // Addressing some designs for CSS
        watchNowButton.getStyleClass().add("watch-online-button");  // giving class identity for css usage
        moreButton.getStyleClass().add("more-button");              // giving class identity for css usage

    }

    private void callFade(int index) {
        // fade in
        fades.get(index).play();


        pauseTransition.setOnFinished(event -> {
            // fade out
            fades.get(fadeIndex + 1).play();

            // make next picture visible and same time run fade in for it
            imageIndex++;
            try {
                stackPaneList.get(imageIndex).setVisible(true);
            } catch (Exception e) {
                imageIndex = 0;
                fadeIndex = 0;
                callFade(fadeIndex);
            }
            // some calculation
            fadeIndex = imageIndex * 2;

            // recursive function call
            callFade(fadeIndex);
        });
        pauseTransition.play();
    }

    @Override
    public void resizeImage(double x, double y) {
        if (x >= 300) {
            double newWidth = (Poster_WIDTH + x) * 0.2;
            double newHeight = (Poster_HEIGHT + x) * 0.2;
            newWidth = Math.min(Poster_WIDTH, Math.max(MIN_WIDTH, newWidth));
            newHeight = Math.min(Poster_HEIGHT, Math.max(MIN_HEIGHT, newHeight));
            landingPosterDimensions.setWidth(newWidth);
            landingPosterDimensions.setHeight(newHeight);
            this.imageView.setFitWidth(newWidth);
            this.imageView.setFitHeight(newHeight);
        }
    }
}
