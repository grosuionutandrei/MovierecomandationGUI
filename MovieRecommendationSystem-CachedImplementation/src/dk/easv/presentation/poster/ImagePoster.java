package dk.easv.presentation.poster;
import dk.easv.dataaccess.httpRequest.ImageDao;
import dk.easv.presentation.Resizable;
import dk.easv.presentation.ratingPoster.RatingPoster;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ImagePoster extends VBox implements Resizable {
    private final int WIDTH =300;
    private final int HEIGHT = 400;
    @FXML
    private Label title;
    @FXML
    private ImageView imageView;
    @FXML
    private HBox ratingContainer;

    public ImagePoster(String title,double rating,String imagePath) {
        super();
        this.setSpacing(10);
        this.title= new Label(title);
        this.imageView =  new ImageView();
        this.getStyleClass().add("poster");
        this.setAlignment(Pos.CENTER);
        resizeLabel(this.title);
        ImageDao imageDao =  new ImageDao();
        imageDao.getImage(this.imageView,imagePath);
        setDimensions(WIDTH,HEIGHT);
        RatingPoster ratingPoster = new RatingPoster(rating);
        ratingContainer=new HBox(ratingPoster.getRating());
        this.getChildren().addAll(this.title,this.imageView,this.ratingContainer);
    }


    public void replaceImage(Image image){
    this.imageView.setImage(image);
    }



    public void setDimensions(double width, double height) {
        this.imageView.setFitWidth(width);
        this.imageView.setFitHeight(height);
        this.imageView.setPreserveRatio(false);
    }

//    public double getPrefWidth() {
//        return this.getFitWidth();
//    }


    @Override
    public void resizeImage(double x, double y) {
        if(x>=300){
            double resizableWidth = (WIDTH+x)*0.2;
            if(resizableWidth>300){
                return;
            }
            double resizableHeight =(HEIGHT+x)*0.2;
            this.imageView.setFitWidth(resizableWidth);
            this.imageView.setFitHeight(resizableHeight);
        }
    }

    private void resizeLabel(Label label){
        // Enable text overflow handling to use ellipsis
        label.setWrapText(false); // Ensure wrapping is disabled for ellipsis to work
        label.setTextOverrun(javafx.scene.control.OverrunStyle.ELLIPSIS);

    }
}
