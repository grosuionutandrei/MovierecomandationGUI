package dk.easv.presentation.ratingPoster;

import io.github.palexdev.materialfx.controls.base.MFXLabeled;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RatingPoster extends VBox {
    @FXML
    private Label movieTitle;
    @FXML
    private Label rating;
    @FXML
    private HBox playButtons;

    public RatingPoster(double rating, String title, double width, double height) {
        this.movieTitle = new Label(title);
        this.rating = new Label(String.valueOf(rating));
        this.playButtons = new HBox();
        this.getStylesheets().add("RatingPoster.css");
        this.getStyleClass().add("customInfo");
        this.setPrefWidth(width);
        this.setPrefHeight(height);
        this.setAlignment(Pos.BOTTOM_CENTER);
        System.out.println("I am ");
    }
}
