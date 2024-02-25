package dk.easv.presentation.components.poster.data;

import dk.easv.exceptions.ExceptionHandler;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageDao {
    private SimpleObjectProperty<Image> imageProperty ;
    private String noImageUrl = "file:///D:///computer_science/sco/compolsory/movie_recomandation_gui/MovieRecommendationSystem-CachedImplementation/data/placeholdercar.png";
private String defaultImage ="file:///D:///computer_science/sco/compolsory/movie_recomandation_gui/MovieRecommendationSystem-CachedImplementation/data/default.png";
    public ImageDao()
    {
     this.imageProperty = new SimpleObjectProperty<>(new Image(defaultImage));
    }

    public void getImage(ImageView imageView,String imagePath){
        String path = "https://image.tmdb.org/t/p/w300";
        ImageService imageService = null;

        if(imagePath.startsWith("default")){
            imageService=new ImageService(noImageUrl);
        }else{
            imageService = new ImageService(path+imagePath);
        }

        ImageService finalImageService = imageService;
        imageService.setOnSucceeded(e -> {
             Image image =  finalImageService.getValue();
             imageProperty.set(image);
            imageView.imageProperty().bind(imageProperty);
        });
        imageService.setOnFailed(e -> {
            imageProperty.set(new Image(noImageUrl));
        });
        imageService.start();
    }

    public void getImage(ImageView imageView,String imagePath, Boolean isOriginal){
        String path = "https://image.tmdb.org/t/p/original";
        ImageService imageService = null;
        if(imagePath.startsWith("default")){
            imageService=new ImageService(noImageUrl);
        }else{
            imageService = new ImageService(path+imagePath);
        }

        ImageService finalImageService = imageService;
        imageService.setOnSucceeded(e -> {
            // This is called when the image download is successful
            Image image =  finalImageService.getValue();
            imageProperty.set(image);
            imageView.imageProperty().bind(imageProperty);
        });
        imageService.setOnFailed(e -> {
            // This is called if the image download fails
              ExceptionHandler.displayErrorAlert("Error occured when downloading images","Image error");

            imageProperty.set(new Image(defaultImage));
        });
        imageService.start();
    }
}




