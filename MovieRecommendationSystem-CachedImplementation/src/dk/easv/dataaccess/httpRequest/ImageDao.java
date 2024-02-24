package dk.easv.dataaccess.httpRequest;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageDao {
    private SimpleObjectProperty<Image> imageProperty ;
   private List<Image> images = new ArrayList<>();
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
            // You can now use the image in your application, e.g., display it in an ImageView
        });
        imageService.setOnFailed(e -> {
            // This is called if the image download fails
            //  ExceptionHandler.displayErrorAlert(imageService.getException().toString(),"Image error");

            imageProperty.set(new Image(defaultImage));
        });
        imageService.start();
    }
    public void getImage(List<Image> images, String imagePath, Boolean isOriginal){
        System.out.println("i am herexxx");

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
            this.images.add(image);
            // You can now use the image in your application, e.g., display it in an ImageView
        });
        imageService.setOnFailed(e -> {
            // This is called if the image download fails
            //  ExceptionHandler.displayErrorAlert(imageService.getException().toString(),"Image error");

           images.add(new Image(defaultImage));
        });
        imageService.start();
    }




}




