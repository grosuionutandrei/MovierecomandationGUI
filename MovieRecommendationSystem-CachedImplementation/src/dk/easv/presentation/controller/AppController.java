package dk.easv.presentation.controller;
import dk.easv.entities.*;
import dk.easv.presentation.model.AppModel;
import dk.easv.presentation.poster.ImagePoster;
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
    String placeholder = "file:///D:///computer_science/sco/compolsory/movie_recomandation_gui/MovieRecommendationSystem-CachedImplementation/data/placeholder.png";
    @FXML
    private HBox postersParent;
    private AppModel model;
    private long timerStartMillis = 0;
    private String timerMsg = "";
    @FXML
    private ScrollPane scrollPaneFirstPoster;



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
        /*lvUsers.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldUser, selectedUser) -> {
                    startTimer("Loading all data for user: " + selectedUser);
                    model.loadData(selectedUser);
                });*/

        // Select the logged-in user in the listview, automagically trigger the listener above
        //lvUsers.getSelectionModel().select(model.getObsLoggedInUser());







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

    private static void writeMovies(AppModel model, List<MovieData> movies) {
        System.out.println(movies.size());
        System.out.println(movies.get(0));
        System.out.println("i am here");
        //model.saveData(movies);
    }

    private void loadImages() {
        for (TopMovie top : model.getObsTopMoviesSimilarUsers()) {
            ImagePoster imagePoster = new ImagePoster(top.getMovie().getTitle(), top.getMovie().getAverageRating(),top.getMovie().getImageUrl());
            postersParent.getChildren().add(imagePoster);
        }
    }
    private void modifyData(){
model.rewriteData();
    }

    public void toTheLeft(ActionEvent event) {
        double viewportWidth = this.scrollPaneFirstPoster.getViewportBounds().getWidth();
        double contentWidth = this.scrollPaneFirstPoster.getContent().prefWidth(-1); // -1 for the height value means compute the pref width for the current height
        double moveInPixels = 400;
        double moveNormalized = moveInPixels / (contentWidth - viewportWidth);
        double newHvalue = Math.max(0.0, this.scrollPaneFirstPoster.getHvalue() - moveNormalized);
        this.scrollPaneFirstPoster.setHvalue(newHvalue);
    }

    public void toTheRight(ActionEvent event) {
        double viewportWidth = this.scrollPaneFirstPoster.getViewportBounds().getWidth();
        double contentWidth = this.scrollPaneFirstPoster.getContent().prefWidth(-1); // -1 for the height value means compute the pref width for the current height
        double moveInPixels = 400;
        double moveNormalized = moveInPixels / (contentWidth - viewportWidth);
        double newHvalue = Math.min(1.0, Math.max(0.0, this.scrollPaneFirstPoster.getHvalue() + moveNormalized));
        this.scrollPaneFirstPoster.setHvalue(newHvalue);
    }


}


