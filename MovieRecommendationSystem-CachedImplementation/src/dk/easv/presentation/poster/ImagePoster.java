package dk.easv.presentation.poster;
import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.dataaccess.httpRequest.ImageDao;
import dk.easv.entities.MovieData;
import dk.easv.presentation.Resizable;
import dk.easv.presentation.ratingPoster.RatingPoster;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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


public class ImagePoster extends VBox implements Resizable {
    private final int WIDTH = 200;
    private final int HEIGHT = 250;

    private final int Poster_WIDTH = 1100;
    private final int Poster_HEIGHT = 600;
    @FXML
    private Label title;
    @FXML
    private ImageView imageView;
    @FXML
    private RatingPoster ratingContainer;
    private Dimensions dimensions = Dimensions.getInstance(WIDTH, HEIGHT);
    private LandingPosterDimensions landingPosterDimensions = LandingPosterDimensions.getInstance(Poster_WIDTH, Poster_HEIGHT);


    private StackPane posterStack;

    /** Loads small size image posters */

    public ImagePoster(MovieData movieData) {
        super();
        this.setSpacing(10);
       // this.title = new Label(title);
        this.posterStack = new StackPane();
        this.imageView = new ImageView();
        this.getStyleClass().add("poster");
        this.setAlignment(Pos.CENTER);
       // resizeLabel(this.title);
        ImageDao imageDao = new ImageDao();
        imageDao.getImage(this.imageView, movieData.getImageUrl() );
        setDimensions(dimensions.getWidth(), dimensions.getHeight());
//        RatingPoster ratingPoster = new RatingPoster(rating);
//        ratingContainer = new HBox(ratingPoster.getRating());
        this.ratingContainer=new RatingPoster(movieData.getAverageRating(),movieData.getTitle(),dimensions.getWidth(),dimensions.getHeight());
        this.posterStack.getChildren().add(this.imageView);
        this.posterStack.getChildren().add(ratingContainer);
        this.getChildren().addAll( posterStack);
    }


    /** Used for getting data for landing poster */
    public ImagePoster(MovieSearchResponse movieSearchResponse, Boolean isLandingPoster) {
        super();
        // StackPane to stack nodes on image
        StackPane innerStackPane = new StackPane();

        this.imageView = new ImageView();
        ImageDao imageDao = new ImageDao();
        imageDao.getImage(this.imageView, movieSearchResponse.getBackdrop_path(), true);
        /** Commented out for later on when we want to set resizable for landing poster */
//        imageView.fitWidthProperty().bind(innerStackPane.widthProperty());
//        imageView.fitHeightProperty().bind(innerStackPane.heightProperty());
//
        setLandingPosterDimensions(landingPosterDimensions.getWidth(), landingPosterDimensions.getHeight());
//        setLandingPosterDimensions(Poster_WIDTH, Poster_HEIGHT);


        // Rectangle on image
        Rectangle rectangle = new Rectangle();

        // Rectangle coloring
        LinearGradient paint = new LinearGradient(
                0.6905, 0.3095, 0.6905, 0.9238, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, new Color(0.0784, 0.0745, 0.0745, 0.02)),
                new Stop(1.0, new Color(0.0784, 0.0745, 0.0745, 1.0)));
        rectangle.setFill(paint);

        // Binding rectangle dimensions to main image
        rectangle.widthProperty().bind(imageView.fitWidthProperty());
        rectangle.heightProperty().bind(imageView.fitHeightProperty());


        // This vbox contains all the nodes on top of the landing image (except rectangles)
        VBox allLabelsOverPicture = new VBox(30);
        allLabelsOverPicture.setAlignment(Pos.CENTER_LEFT);


        // set title label for landing poster
        Label titleLabel = new Label(movieSearchResponse.getTitle());
        titleLabel.setFont(Font.font("DejaVu Sans", FontWeight.BOLD, 50));
        titleLabel.setStyle("-fx-text-fill: #ddd7d7");


        // set description label for landing poster
        Label descLabel = new Label(movieSearchResponse.getOverview());
        descLabel.setFont(Font.font("Hack", FontWeight.BOLD, 17));
        descLabel.setStyle("-fx-text-fill: #a1a1a1");
        descLabel.setMaxWidth(400);
        descLabel.setMaxHeight(100);
        descLabel.setWrapText(true);

        /* HBox for buttons */
        HBox buttonsForPoster = new HBox(20);
        Button watchNowButton = new Button("Watch Online");
        Button moreButton = new Button("More");

        // Sizing the buttons, NOTE: needs to be resizable on future
        watchNowButton.setPrefSize(140, 40);
        moreButton.setPrefSize(80, 40);

        // Addressing some designs for CSS
        watchNowButton.getStyleClass().add("watch-online-button");  // giving class identity for css usage
        moreButton.getStyleClass().add("more-button");              // giving class identity for css usage
        buttonsForPoster.getStylesheets().add("dk/easv/presentation/poster/LandingPoster.css"); // addressing css file



        /* Adding children to parents */
        buttonsForPoster.getChildren().addAll(watchNowButton, moreButton); // Adding buttons to HBox
        allLabelsOverPicture.getChildren().addAll(titleLabel, descLabel, buttonsForPoster); // Adding title label, description label and HBox buttons to VBox
        innerStackPane.getChildren().addAll(imageView, rectangle, allLabelsOverPicture); // adding imageView, then rectangle on top of that, and at the end, spawning all text labels and buttons


        // Some extra spices
        StackPane.setMargin(allLabelsOverPicture, new Insets(0, 0, 0, 80)); // set some padding for vbox which is inside StackPane


        this.getChildren().addAll(innerStackPane);
    }


    public void replaceImage(Image image) {
        this.imageView.setImage(image);
    }


    public void setDimensions(double width, double height) {
        this.imageView.setFitWidth(width);
        this.imageView.setFitHeight(height);
        this.imageView.setPreserveRatio(false);
    }

    public void setLandingPosterDimensions(double width, double height){
        this.imageView.setFitWidth(width);
        this.imageView.setFitHeight(height);
        this.imageView.setPreserveRatio(true);
    }

    @Override
    public void resizeImage(double x, double y) {
        if (x >= 300) {
            dimensions.setWidth((WIDTH + x) * 0.2);

            if (dimensions.getWidth() > WIDTH) {
                dimensions.setWidth(WIDTH);
                dimensions.setHeight(HEIGHT);
                return;
            }
            dimensions.setHeight((HEIGHT + x) * 0.2);
        }

    }

    private void resizeLabel(Label label) {
        // Enable text overflow handling to use ellipsis
        label.setWrapText(false); // Ensure wrapping is disabled for ellipsis to work
        label.setTextOverrun(javafx.scene.control.OverrunStyle.ELLIPSIS);
    }


    private void setOnHover(){

    }
}
