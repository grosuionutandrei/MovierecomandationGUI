package dk.easv.presentation.model;

import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.dataaccess.apiRequest.transcripts.VideoData;
import dk.easv.entities.*;
import dk.easv.exceptions.MoviesException;
import dk.easv.logic.LogicManager;
import dk.easv.logic.ViewLogic;
import dk.easv.presentation.listeners.Resizable;
import dk.easv.presentation.listeners.Displayable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class AppModel {

    LogicManager logic = new LogicManager();
    ViewLogic viewLogic = new ViewLogic();
    // Models of the data in the view
    private final ObservableList<User> obsUsers = FXCollections.observableArrayList();
    private final ObservableList<MovieData> obsTopMovieSeen = FXCollections.observableArrayList();
    private final ObservableList<MovieData> obsTopMovieNotSeen = FXCollections.observableArrayList();
    private final ObservableList<UserSimilarity> obsSimilarUsers = FXCollections.observableArrayList();
    private final ObservableList<TopMovie> obsTopMoviesSimilarUsers = FXCollections.observableArrayList();

    private final SimpleObjectProperty<User> obsLoggedInUser = new SimpleObjectProperty<>();


    public void loadUsers() {
        obsUsers.clear();
        obsUsers.addAll(logic.getAllUsers());
    }

    public void loadData(User user) {
        obsTopMovieSeen.clear();
        obsTopMovieSeen.addAll(logic.getTopAverageRatedMovies(user));

        obsTopMovieNotSeen.clear();
        obsTopMovieNotSeen.addAll(logic.getTopAverageRatedMoviesUserDidNotSee(user));

        obsSimilarUsers.clear();
        obsSimilarUsers.addAll(logic.getTopSimilarUsers(user));

        obsTopMoviesSimilarUsers.clear();
        obsTopMoviesSimilarUsers.addAll(logic.getTopMoviesFromSimilarPeople(user));
    }

    public ObservableList<User> getObsUsers() {
        return obsUsers;
    }

    public ObservableList<MovieData> getObsTopMovieSeen() {
        return obsTopMovieSeen;
    }

    public ObservableList<MovieData> getObsTopMovieNotSeen() {
        return obsTopMovieNotSeen;
    }


    public ObservableList<UserSimilarity> getObsSimilarUsers() {
        return obsSimilarUsers;
    }

    public ObservableList<TopMovie> getObsTopMoviesSimilarUsers() {
        return obsTopMoviesSimilarUsers;
    }

    public User getObsLoggedInUser() {
        return obsLoggedInUser.get();
    }

    public SimpleObjectProperty<User> obsLoggedInUserProperty() {
        return obsLoggedInUser;
    }

    public void setObsLoggedInUser(User obsLoggedInUser) {
        this.obsLoggedInUser.set(obsLoggedInUser);
    }

    private final DoubleProperty viewPortWidth = new SimpleDoubleProperty();
    private final List<Resizable> resizables;
    private SimpleObjectProperty<VideoData> videoData = new SimpleObjectProperty<>();
    private Displayable videoPlayer;

    /**
     * Holds the current movie that is being hover over
     */
    private MovieData selectedMovie;


    public AppModel() {
        resizables = new ArrayList<>();
        addChangeListener();
        addObjectListener();
    }


    public DoubleProperty getViewPortWidthProperty() {
        return viewPortWidth;
    }

    public boolean loginUserFromUsername(String userName) {
        User u = logic.getUser(userName);
        obsLoggedInUser.set(u);
        if (u == null)
            return false;
        else
            return true;
    }

    private String processQuery(String text) {
        return logic.processQuerry(text);
    }

    public List<MovieSearchResponse> getResults(String name, String year) throws MoviesException {
        String nameProcessed = processQuery(name);
        String yearProcessed = processQuery(year);
        return logic.getAllResponses(nameProcessed, yearProcessed);
    }

    public void rewriteData() {
        logic.rewriteData();
    }

    private void addChangeListener() {
        this.viewPortWidth.addListener((observable, oldValue, newValue) -> {
            Double newSize = (Double) newValue;
            resizeItems(newSize);
        });
    }

    private void resizeItems(Double newSize) {
        for (Resizable res : resizables) {
            res.resizeImage(newSize, 0);
        }
    }

    public void addResizable(Resizable resizable) {
        this.resizables.add(resizable);
    }


    public VideoData getVideoData() {
        return videoData.get();
    }

    public SimpleObjectProperty<VideoData> videoDataProperty() {
        return videoData;
    }

    public void setVideoData(VideoData videoData) {
        this.videoData.set(videoData);
    }

    private void addObjectListener() {
        this.videoData.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.videoPlayer.updateView();
                System.out.println(newValue);
            }
        });
    }

    public void setVideoPlayer(Displayable videoPlayer) {
        this.videoPlayer = videoPlayer;
    }

    public MovieData getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(MovieData selectedMovie) {
        this.selectedMovie = selectedMovie;
    }

    public void getMediaToBePlayed(HBox embededContainer) {
        int[] dimensions = getVideoPlayerWindowDimensions();

        logic.getMediaToBePlayed(this.videoData.get(), embededContainer, dimensions);
    }

    public int[] getVideoPlayerWindowDimensions() {
        return viewLogic.viewPortBasedSizeAspectRatio((int) viewPortWidth.get(), (int) viewPortWidth.get());
    }
}




