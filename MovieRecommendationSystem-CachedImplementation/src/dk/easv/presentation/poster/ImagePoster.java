package dk.easv.presentation.poster;
import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.dataaccess.httpRequest.ImageDao;
import dk.easv.entities.MovieData;
import dk.easv.entities.TopMovie;
import dk.easv.presentation.Resizable;
import dk.easv.presentation.ratingPoster.RatingPoster;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


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

        this.imageView = new ImageView();
        ImageDao imageDao = new ImageDao();
        imageDao.getImage(this.imageView, movieSearchResponse.getbackdrop_path(), true);
        // Commented out for later on when we want to set resizable for landing poster
//        setLandingPosterDimensions(dimensions.getWidth(), dimensions.getHeight());
        setLandingPosterDimensions(Poster_WIDTH, Poster_HEIGHT);

        this.getChildren().addAll(this.imageView);
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
            this.imageView.setFitWidth(dimensions.getWidth());
            this.imageView.setFitHeight(dimensions.getHeight());
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
