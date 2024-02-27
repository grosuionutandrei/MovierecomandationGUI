package dk.easv.presentation.components.poster;

import dk.easv.presentation.components.poster.data.ImageDao;
import dk.easv.entities.MovieData;
import dk.easv.presentation.listeners.Resizable;
import dk.easv.presentation.model.AppModel;
import dk.easv.presentation.components.ratingPoster.RatingPoster;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


public class ImagePoster extends VBox implements Resizable {
    private final int MAX_WIDTH = 200;
    private final int MAX_HEIGHT = 250;

    private final int MIN_WIDTH = 100;
    private final int MIN_HEIGHT = 100;
    @FXML
    private final ImageView imageView;
    @FXML
    private RatingPoster ratingContainer;
    private final Dimensions dimensions = Dimensions.getInstance(MAX_WIDTH, MAX_HEIGHT);
    private final StackPane posterStack;
    private final MovieData movieData;
    private final AppModel model;
    private PauseTransition pauseTransition;

    public ImagePoster(MovieData movieData, AppModel model) {
        super();
        this.movieData = movieData;
        this.posterStack = new StackPane();
        this.posterStack.setMaxWidth(dimensions.getWidth());
        this.posterStack.getStyleClass().add("posterStack");
        this.imageView = new ImageView();
        this.imageView.getStyleClass().add("image-view");
        this.getStyleClass().add("poster");
        this.setAlignment(Pos.CENTER);
        getImageFromTmdbAPi();
        setDimensions(dimensions.getWidth(), dimensions.getHeight());
        this.posterStack.getChildren().add(this.imageView);
        setOnHover();
        this.getChildren().add(posterStack);
        bindSizeToDimensions();
        this.model = model;
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    private void getImageFromTmdbAPi() {
        ImageDao imageDao = new ImageDao();
        imageDao.getImage(this.imageView, this.movieData.getImageUrl());
    }


    //used for binding the vbox to the dimensions, when the vbox was not resizing
    private void bindSizeToDimensions() {
        this.prefHeightProperty().bind(dimensions.heightProperty());
        this.prefWidthProperty().bind(dimensions.widthProperty());
    }


    public void replaceImage(Image image) {
        this.imageView.setImage(image);
    }


    public void setDimensions(double width, double height) {
        this.imageView.setFitWidth(width);
        this.imageView.setFitHeight(height);
        this.imageView.setPreserveRatio(false);
    }

    /**
     * Resize the images according to the viePort sizes
     *
     * @param x width off the viewport
     * @param y height off the viewport
     */
    @Override
    public void resizeImage(double x, double y) {
        if (x >= 300) {
            double newWidth = (MAX_WIDTH + x) * 0.2;
            double newHeight = (MAX_HEIGHT + x) * 0.2;
            newWidth = Math.min(MAX_WIDTH, Math.max(MIN_WIDTH, newWidth));
            newHeight = Math.min(MAX_HEIGHT, Math.max(MIN_HEIGHT, newHeight));
            dimensions.setWidth(newWidth);
            dimensions.setHeight(newHeight);
            this.imageView.setFitWidth(newWidth);
            this.imageView.setFitHeight(newHeight);
        }
    }


    /**
     * Listener for mouse hover to display the title and ratting off the movies
     */
    private void setOnHover() {
        pauseTransition = new PauseTransition(Duration.seconds(0.5));
        this.posterStack.setOnMouseEntered(e -> {
            pauseTransition.playFromStart();
        });

        pauseTransition.setOnFinished(event -> {
            model.setSelectedMovie(movieData);
            this.ratingContainer = new RatingPoster(model, this.dimensions);
            this.posterStack.getChildren().add(ratingContainer);
            StackPane.setAlignment(ratingContainer, Pos.BOTTOM_CENTER);
        });

        this.posterStack.setOnMouseExited(event -> {
            pauseTransition.stop();
            this.posterStack.getChildren().remove(ratingContainer);
        });
    }


    public void cleanup() {
        if (this.imageView != null) {
            this.imageView.imageProperty().unbind();
        }
        if (this.posterStack != null) {
            this.posterStack.setOnMouseEntered(null);
            this.posterStack.setOnMouseExited(null);
        }
        if (pauseTransition != null) {
            pauseTransition.stop();
        }
        if (this.ratingContainer != null) {
            this.ratingContainer.cleanup();
            this.posterStack.getChildren().remove(this.ratingContainer);
            this.ratingContainer = null;
        }
        this.getChildren().clear();
    }
}
