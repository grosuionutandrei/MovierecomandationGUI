package dk.easv.presentation.components.moviePosterDisplayPanel;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


// not used, to implement later
public class MoviePosterDisplayPanel extends StackPane {

    @FXML
    private ScrollPane scrollPanePanel;
    @FXML
    private HBox posterParent;
    @FXML
    private MFXButton rightButton;
    @FXML
    private MFXButton leftButton;


    public MoviePosterDisplayPanel() {
        this.scrollPanePanel = new ScrollPane();
        this.posterParent= new HBox();
    }

    //not used
}
