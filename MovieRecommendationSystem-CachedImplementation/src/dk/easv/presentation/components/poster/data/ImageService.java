package dk.easv.presentation.components.poster.data;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.Image;


public class ImageService extends Service<Image> {
    private String imageUri;

    public ImageService(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    protected Task<Image> createTask() {

        return  new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                return new Image(imageUri);
            }
        };
    }
}
