package dk.easv.presentation.ratingPoster;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class PlayButton extends MFXButton {

  public PlayButton(){
      super("Play");
      this.setButtonType(ButtonType.RAISED);
      this.getStyleClass().add("playButton");
      this.setText(">");
setOnAction();
  }

  private void setOnAction(){
     this.setOnAction(e->{
         Stage currentStage =  (Stage)((Scene)((Node)e.getSource()).getScene()).getWindow();
         FXMLLoader loader =  new FXMLLoader(getClass().getResource("../playwindow/playWindow.fxml"));
         try {
             Parent root = loader.load();
             Scene scene = new Scene(root);
             Stage modal =  new Stage();
             modal.initOwner(currentStage);
             modal.initModality(Modality.APPLICATION_MODAL);
             modal.initStyle(StageStyle.UNDECORATED);
             modal.setScene(scene);
             modal.show();
         } catch (IOException ex) {
             Alert alarm = new Alert(Alert.AlertType.ERROR,"fxml error from play window");


         }
     });
  }


        }




