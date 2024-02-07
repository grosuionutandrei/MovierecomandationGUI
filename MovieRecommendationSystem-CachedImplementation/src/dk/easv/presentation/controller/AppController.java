package dk.easv.presentation.controller;
import dk.easv.entities.*;
import dk.easv.presentation.model.AppModel;
import dk.easv.presentation.poster.ImagePoster;
import dk.easv.presentation.poster.ImagesControl;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.*;

public class AppController implements Initializable {
//    @FXML
//    private ListView<User> lvUsers;
//    @FXML
//    private ListView<MovieData> lvTopForUser;
//    @FXML
//    private ListView<MovieData> lvTopAvgNotSeen;
//    @FXML
//    private ListView<UserSimilarity> lvTopSimilarUsers;
//    @FXML
//    private ListView<TopMovie> lvTopFromSimilar;
    //String placeholder = "file:///D:///computer_science/sco/compolsory/movie_recomandation_gui/MovieRecommendationSystem-CachedImplementation/data/placeholder.png";
    @FXML
    private HBox postersParent;
    private AppModel model;
    private long timerStartMillis = 0;
    private String timerMsg = "";
    @FXML
    private ScrollPane scrollPaneFirstPoster;
private ImagesControl imagesControl;


    private void startTimer(String message) {
        timerStartMillis = System.currentTimeMillis();
        timerMsg = message;
    }

    private void stopTimer() {
        System.out.println(timerMsg + " took : " + (System.currentTimeMillis() - timerStartMillis) + "ms");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setModel(AppModel model) {
        this.model = model;
 /*       lvUsers.setItems(model.getObsUsers());
        lvTopForUser.setItems(model.getObsTopMovieSeen());
        lvTopAvgNotSeen.setItems(model.getObsTopMovieNotSeen());
        lvTopSimilarUsers.setItems(model.getObsSimilarUsers());
        lvTopFromSimilar.setItems(model.getObsTopMoviesSimilarUsers());*/
        startTimer("Load users");
        model.loadUsers();
        stopTimer();
        model.loadData(model.getObsLoggedInUser());
        imagesControl=new ImagesControl(model.getObsTopMoviesSimilarUsers());
        /*lvUsers.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldUser, selectedUser) -> {
                    startTimer("Loading all data for user: " + selectedUser);
                    model.loadData(selectedUser);
                });*/

        // Select the logged-in user in the listview, automagically trigger the listener above
        //lvUsers.getSelectionModel().select(model.getObsLoggedInUser());


        initializeWidthListener(model);
        loadImages();
     //   modifyData();
        /**  this was used for retrieving data from the tmdb api*/
//        try {
//            int counter = 0;
//            List<MovieData> movies = new ArrayList<>();
//            int batchSize = 100;
//            for (MovieData mov : model.getData().values()) {
//                counter++;
//                List<MovieSearchResponse> responses = model.getResults(mov.getTitle(), String.valueOf(mov.getYear()));
//                if (responses.isEmpty()) {
//                    mov.setImageUrl(placeholder);
//                } else {
//                    String url = responses.get(0).getPoster_path();
//                    int tmdbId=responses.get(0).getId();
//                    mov.setImageUrl(url);
//                    mov.setTmdbId(tmdbId);
//                }
//
//                movies.add(mov);
//                // Process in batches of 100
//                if (counter % batchSize == 0) {
//                    System.out.println(movies.get(counter-2));
//                    writeMovies(model, new ArrayList<>(movies.subList(counter - batchSize, counter)));
//                }
//            }
//            // Process any remaining movies
//            if (!movies.isEmpty() && counter % batchSize != 0) {
//                writeMovies(model, new ArrayList<>(movies.subList(counter - (counter % batchSize), counter)));
//            }
//            System.out.println("All data was appended successfully.");
//        } catch (MoviesException e) {
//            throw new RuntimeException(e);
//        }
    }


/** Ads a listener to the ScrollPane that will update the model viewPort variable, that controls the images resizing
 * @param model represents the model that is holding the observable var
 * */
    private void initializeWidthListener(AppModel model) {
        scrollPaneFirstPoster.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            model.getViewPortWidthProperty().set(newValue.getWidth());
        });
    }

    private static void writeMovies(AppModel model, List<MovieData> movies) {
        System.out.println(movies.size());
        System.out.println(movies.get(0));
        System.out.println("i am here");
        //model.saveData(movies);
    }

    /**Loads initial movies to the scrollPane
     * */
    private void loadImages() {
        for (TopMovie top : imagesControl.getNextBatchMovies()) {
            ImagePoster imagePoster = new ImagePoster(top.getMovie().getTitle(), top.getMovie().getAverageRating(),top.getMovie().getImageUrl());
            model.addResizable(imagePoster);
            postersParent.getChildren().add(imagePoster);
        }
    }
    private void modifyData(){
model.rewriteData();
    }

    /**Moves the scrollPane view to the left */
    public void toTheLeft(ActionEvent event) {
        double viewportWidth = this.scrollPaneFirstPoster.getViewportBounds().getWidth();
        double contentWidth = this.scrollPaneFirstPoster.getContent().prefWidth(-1); // -1 for the height value means compute the pref width for the current height
        double moveNormalized = viewportWidth / (contentWidth - viewportWidth);
        double newHvalue = Math.max(0.0, this.scrollPaneFirstPoster.getHvalue() - moveNormalized);
        this.scrollPaneFirstPoster.setHvalue(newHvalue);
    }


    /**Moves the scrollPane to the right and loads the next images from the  movie list*/
    public void toTheRight(ActionEvent event) {
        loadImages();
        double viewportWidth = this.scrollPaneFirstPoster.getViewportBounds().getWidth();
        double contentWidth = this.scrollPaneFirstPoster.getContent().prefWidth(-1); // -1 for the height value means compute the pref width for the current height
        double moveNormalized = viewportWidth / (contentWidth - viewportWidth);
        double newHvalue = Math.min(1.0, Math.max(0.0, this.scrollPaneFirstPoster.getHvalue() + moveNormalized));
        this.scrollPaneFirstPoster.setHvalue(newHvalue);
    }


}


