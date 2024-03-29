package dk.easv.presentation.components.ratingPoster;
import dk.easv.presentation.model.AppModel;
import dk.easv.presentation.components.poster.Dimensions;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
public class RatingPoster extends VBox {
    @FXML
    private Label movieTitle;
    @FXML
    private HBox playControl;
    @FXML
    private HBox grades;
    @FXML
    private PlayButton playButton;

    public RatingPoster( AppModel model, Dimensions dimensions) {
        this.movieTitle = new Label(model.getSelectedMovie().getTitle());
        this.grades = new StarsGrades(model.getSelectedMovie().getAverageRating(),this);
        this.playControl= new HBox();
        this.playButton =  new PlayButton(model);
        this.playControl.setAlignment(Pos.CENTER);
        playControl.getChildren().add(this.playButton);
        setStyleSettings();
        setPreMaxDimensions(dimensions);
        this.setAlignment(Pos.BOTTOM_CENTER);
        this.setVisible(true);
        hideRating(dimensions);
    }

    private void setStyleSettings() {
        this.getStylesheets().add("dk/easv/presentation/components/ratingPoster/RatingPoster.css");
        this.getStyleClass().add("customInfo");
    }

    /**sets the dimensions off the rating poster to be half  hight off the poster */
    private void setPreMaxDimensions(Dimensions dimensions) {
        this.setPrefWidth(dimensions.getWidth());
        this.setPrefHeight(dimensions.getHeight()/2);
        this.setMaxWidth(dimensions.getWidth());
        this.setMaxHeight(dimensions.getHeight()/2);
    }

    /**used to determine to hide  the star grading
     * @param dimensions an object that holds the dimensions off the image view*/
    private void hideRating(Dimensions dimensions) {
        if(dimensions.getWidth()>180){
            this.getChildren().addAll(movieTitle, playControl,grades);
        }else{
            this.getChildren().addAll(movieTitle, playControl);
        }
    }

    public void cleanup() {
        this.grades=null;
        this.playButton.cleanUp();
        this.playButton=null;
    }
}
