package dk.easv.presentation.components.moviePosterDisplayPanel;
import dk.easv.dataaccess.httpRequest.ImageDao;
import dk.easv.exceptions.ExceptionHandler;
import dk.easv.presentation.listeners.Resizable;
import dk.easv.presentation.model.BridgeInterface;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.io.IOException;

public class MoviesBanner  implements Resizable {
    private final int POSTER_WIDTH = 1100;
    private final int POSTER_HEIGHT = 600;
    private static final double MIN_WIDTH = 300; // Example minimum width
    private static final double MIN_HEIGHT = 200;
    LandingPosterDimensions landingPosterDimensions =  LandingPosterDimensions.getInstance(POSTER_WIDTH,POSTER_HEIGHT);
    @FXML
    private StackPane bigPosterParent;
    @FXML
    private ImageView bigPosterImage;
    @FXML
    private VBox detailsContainer;
    @FXML
    private Label movieTitle;
    @FXML
    private Label movieDescription;
    @FXML
    private HBox buttonsContainer;
    @FXML
    private MFXButton watchMovie;
    @FXML
    private MFXButton moreMovieInfo;
    private final MoviesBannerModel moviesBannerModel;
    private  ObservableList<Image> posterImages;
    private int movieIndex=0;
    private PauseTransition pauseTransition = new PauseTransition(Duration.seconds(10));
    public MoviesBanner(BridgeInterface bridgeInterface) {
        this.moviesBannerModel = new MoviesBannerModel(bridgeInterface);

        FXMLLoader loader  = new FXMLLoader(getClass().getResource("MoviesBanner.fxml"));
        try {
            bigPosterParent = loader.load();
            initializeMovieBanner();
        } catch (IOException e) {
           e.printStackTrace();
            ExceptionHandler.displayErrorAlert(e.getMessage(),"Poster error");
        }
    }
    @Override
    public void resizeImage(double x, double y) {

    }


    public StackPane getLandingPageRoot(){
        return this.bigPosterParent;
    }


    private void loadImagesInMemory(){
     posterImages= moviesBannerModel.getMoviePosters();
    }

    public void setLandingPosterDimensions(double width, double height) {
        this.bigPosterImage.setFitWidth(width);
        this.bigPosterImage.setFitHeight(height);
        this.bigPosterImage.setPreserveRatio(false);
    }

    private void setImage(Image image) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500));
        fadeOut.setNode(bigPosterImage);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0.3);
        fadeOut.play();
        this.bigPosterImage.setImage(image);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500));
        fadeIn.setNode(bigPosterImage);
        fadeIn.setFromValue(0.3);
        fadeIn.setToValue(1);
        fadeIn.play();
    }


    private void rotate (){
        pauseTransition.setOnFinished((event -> {
           if(movieIndex==9){
               movieIndex=0;
           }
            setImage(posterImages.get(movieIndex+=1));
            rotate();
        }));
        pauseTransition.play();
    }

    private void getImage(ImageView image, String uri) {
        ImageDao imageDao = new ImageDao();
        imageDao.getImage(image, uri, true);
    }


    public void initializeMovieBanner(){
        loadImagesInMemory();
        if(this.posterImages!= null && !this.posterImages.isEmpty()){
            setLandingPosterDimensions(landingPosterDimensions.getWidth(), landingPosterDimensions.getHeight() );
            this.bigPosterImage.setImage(posterImages.get(0));
            rotate();
        }else {
            System.out.println("not here");
        }
        posterImages.addListener((ListChangeListener<Image>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    // Respond to the change in size
                    // For example, print the new size of the list
                    System.out.println("The size of the images list is now: " + posterImages.size());
                }
            }
        });

    }

}
// load the images one by one and when it need s to be changed save it in the list and than check if is in the list, just retrieve , map will bw=e fastser