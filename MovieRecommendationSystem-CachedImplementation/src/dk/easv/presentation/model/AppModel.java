package dk.easv.presentation.model;

import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.entities.*;
import dk.easv.exceptions.MoviesException;
import dk.easv.logic.LogicManager;
import dk.easv.presentation.Resizable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppModel {

    LogicManager logic = new LogicManager();
    // Models of the data in the view
    private final ObservableList<User>  obsUsers = FXCollections.observableArrayList();
    private final ObservableList<MovieData> obsTopMovieSeen = FXCollections.observableArrayList();
    private final ObservableList<MovieData> obsTopMovieNotSeen = FXCollections.observableArrayList();
    private final ObservableList<UserSimilarity>  obsSimilarUsers = FXCollections.observableArrayList();
    private final ObservableList<TopMovie> obsTopMoviesSimilarUsers = FXCollections.observableArrayList();

    private final SimpleObjectProperty<User> obsLoggedInUser = new SimpleObjectProperty<>();

    public void loadUsers(){
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

    private final DoubleProperty viewPortWidth =  new SimpleDoubleProperty();
    private final List<Resizable> resizables ;

    public AppModel(){
        resizables= new ArrayList<>();
        addChangeListener();
    }



    public DoubleProperty getViewPortWidthProperty() {
        return viewPortWidth;
    }

    public boolean loginUserFromUsername(String userName) {
        User u = logic.getUser(userName);
        obsLoggedInUser.set(u);
        if (u==null)
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
        return logic.getAllResponses(nameProcessed,yearProcessed);
    }

    /** Used for getting data for landing poster */
    public List<MovieSearchResponse> getResults(String name) throws MoviesException {
        String nameProcessed = processQuery(name);
        return logic.getAllResponses(nameProcessed);
    }

    public void rewriteData(){
        logic.rewriteData();
    }

    private void addChangeListener(){
        this.viewPortWidth.addListener((observable, oldValue, newValue) -> {
            Double newSize = (Double)newValue;
                resizeItems(newSize);
        });
    }
    private void resizeItems(Double newSize) {
        for(Resizable res : resizables){
            res.resizeImage(newSize,0);
        }
    }

    public void addResizable(Resizable resizable){
        this.resizables.add(resizable);
    }
}




