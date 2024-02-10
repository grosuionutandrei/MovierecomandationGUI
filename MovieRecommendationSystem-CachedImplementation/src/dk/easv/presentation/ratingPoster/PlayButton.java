package dk.easv.presentation.ratingPoster;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.scene.paint.Paint;

public class PlayButton extends MFXButton {

  public PlayButton(){
      super("Play");
      this.setButtonType(ButtonType.RAISED);
      this.getStyleClass().add("playButton");
      this.setText(">");

  }

        }




