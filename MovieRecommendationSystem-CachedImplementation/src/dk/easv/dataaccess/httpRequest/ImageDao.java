package dk.easv.dataaccess.httpRequest;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageDao {
    private SimpleObjectProperty<Image> imageProperty ;
    //private String defaultImagePath ="file:///D:///computer_science/sco/MediPlayer/MovieReloaded/src/resources/default-movie.png";
 //   private String defaultImageUrl="https://ibb.co/F8J7v0z";
    private String noImageUrl = "file:///D:///computer_science/sco/compolsory/movie_recomandation_gui/MovieRecommendationSystem-CachedImplementation/data/placeholder.png";
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
}
