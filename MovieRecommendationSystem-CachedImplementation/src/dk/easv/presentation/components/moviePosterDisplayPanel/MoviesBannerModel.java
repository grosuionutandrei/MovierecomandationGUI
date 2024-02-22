package dk.easv.presentation.components.moviePosterDisplayPanel;
import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.entities.MovieData;
import dk.easv.logic.LogicManager;
import dk.easv.presentation.model.BridgeInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class MoviesBannerModel {

    private ObservableList<MovieSearchResponse> moviesToDisplay;


    private ObservableList<MovieData> topMovies;
    private LogicManager logicManager;
    private BridgeInterface bridgeInterface;

    public MoviesBannerModel(BridgeInterface bridgeInterface) {
        logicManager =  new LogicManager();
        moviesToDisplay= FXCollections.observableArrayList();
        topMovies =  FXCollections.observableArrayList();
       this.bridgeInterface = bridgeInterface;
        populateTopMovies();
        populateMoviesToDisplay();
    }

    private void populateTopMovies(){
        this.topMovies.setAll(logicManager.getMovieDataFromSimilarUsers(bridgeInterface.getLoggedUser()));
    }

    private void populateMoviesToDisplay(){
        this.moviesToDisplay.setAll(logicManager.movieSearchResponses(topMovies));
    }

    public ObservableList<MovieSearchResponse> getMoviesToDisplay() {
        return moviesToDisplay;
    }

    public ObservableList<Image> getMoviePosters(){
        return logicManager.getMoviesPosters(moviesToDisplay);
    }




}
