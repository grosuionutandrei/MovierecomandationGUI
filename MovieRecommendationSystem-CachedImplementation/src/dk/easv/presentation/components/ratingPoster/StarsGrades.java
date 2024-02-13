package dk.easv.presentation.components.ratingPoster;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;

public class StarsGrades extends HBox {
    private double grade;
    @FXML
    private Rating rating;

    public StarsGrades(double grade, VBox parent) {
        super();
        this.grade = grade;
        this.setAlignment(Pos.CENTER_LEFT);
        rating = new Rating();
        rating.setMax(5);
        rating.setRating(grade);
        rating.setDisable(true);
        setPrefDimensions(parent);
        this.getChildren().add(rating);
    }

    private void setPrefDimensions(VBox parent) {
        this.prefWidthProperty().bind(parent.prefWidthProperty());
        this.setMaxWidth(this.getPrefWidth());
        rating.prefWidthProperty().bind(this.widthProperty());
        rating.maxWidthProperty().bind(this.widthProperty());
    }

    public void setGrade(double grade) {
        this.grade = grade;
        rating.setRating(grade);
    }

    public void modifyHeightOfGrades(double width, double height) {
        this.rating.setPrefSize(width, height);
    }
}