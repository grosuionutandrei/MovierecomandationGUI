package dk.easv.logic;

import dk.easv.dataaccess.DataAccessManager;
import dk.easv.dataaccess.apiRequest.Search;
import dk.easv.dataaccess.apiRequest.services.MultiSearch;
import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.dataaccess.apiRequest.transcripts.VideoData;
import dk.easv.dataaccess.httpRequest.ImageDao;
import dk.easv.dataaccess.httpRequest.MediaDao;
import dk.easv.entities.*;
import dk.easv.exceptions.MoviesException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LogicManager {

    DataAccessManager dataMgr = new DataAccessManager();
    Search search = new Search();
    MediaDao mediaDao = new MediaDao();
    MultiSearch multiSearch = new MultiSearch();
    private ImageDao imageDao = new ImageDao();

    public void reloadAllDataFromStorage() {
        dataMgr.updateCacheFromDisk();
    }

    public Collection<User> getAllUsers() {
        return dataMgr.getAllUsers().values();
    }

    // Gets all rated movies for one user and returns them sorted by avg. best by all users.
    public List<MovieData> getTopAverageRatedMovies(User u) {
        List<MovieData> top = new ArrayList<>();

        for (Rating rating : u.getRatings())
            top.add(rating.getMovie());

        Collections.sort(top, Comparator.comparing(MovieData::getAverageRating).reversed());

        return top;
    }

    // Gets all rated movies for one user and returns them sorted by avg. best by all users.
    public List<MovieData> getTopAverageRatedMoviesUserDidNotSee(User u) {
        List<MovieData> top = new ArrayList<>();
        for (MovieData m : dataMgr.getAllMovies().values()) {
            boolean isSeen = false;
            for (Rating r : u.getRatings())
                if (r.getMovie() == m) {
                    isSeen = true;
                    break;
                }
            if (!isSeen)
                top.add(m);
        }
        Collections.sort(top, Comparator.comparing(MovieData::getAverageRating).reversed());
        return top;
    }

    private double calculateUserSimilarity(User u1, User u2) {
        int count = 0;
        double rsim = 0;
        List<Rating> r1 = u1.getRatings();
        List<Rating> r2 = u2.getRatings();
        for (Rating ur1 : r1) {
            for (Rating ur2 : r2) {
                if (ur1.getMovie() == ur2.getMovie()) {
                    double diff = ur1.getRating() - ur2.getRating();
                    rsim += Math.abs(diff) / 10;
                    count++;
                }
            }
        }
        return 1 - (rsim / count); // 1.0 = 100% identical; 0.0 no similarities
    }

    public List<UserSimilarity> getTopSimilarUsers(User user) {
        List<UserSimilarity> allUsersSimList = new ArrayList<>();
        for (User u : dataMgr.getAllUsers().values()) {
            if (u != user)
                allUsersSimList.add(new UserSimilarity(u, calculateUserSimilarity(user, u)));
        }
        Collections.sort(allUsersSimList, Comparator.comparing(UserSimilarity::getSimilarity).reversed());

        return allUsersSimList;
    }

    public List<TopMovie> getTopMoviesFromSimilarPeople(User u) {
        List<UserSimilarity> userSimList = getTopSimilarUsers(u);
        List<TopMovie> favorites = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int topAmount = 100;
            List<Rating> ratings = userSimList.get(i).getUser().getRatings();
            if (topAmount >= ratings.size())
                topAmount = ratings.size() - 1;
            for (int j = 0; j < topAmount; j++) {
                MovieData m = ratings.get(j).getMovie();
                double rating = ratings.get(j).getRating();

                boolean found = false;
                for (TopMovie topmovie : favorites) {
                    if (topmovie.getMovie() == m) {
                        topmovie.getRawRatings().add(rating);
                        found = true;
                    }
                }
                if (!found) {
                    TopMovie tm = new TopMovie(m);
                    tm.getRawRatings().add(rating);
                    favorites.add(tm);
                }
            }
        }
        favorites.sort(Comparator.comparing(TopMovie::getAverageRating).reversed());
        return favorites;
    }

    public User getUser(String userName) {
        try {
            return dataMgr.getAllUsers().values().stream().filter(u -> u.getName().equals(userName)).findFirst().get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public List<MovieSearchResponse> getAllResponses(String name, String year) throws MoviesException {
        return search.movieResponses(name, year);
    }

    public String processQuerry(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8);
    }

    public void rewriteData() {
        Map<Integer, MovieData> moviesData = dataMgr.getAllMovies();
        dataMgr.rewrite(moviesData.values().stream().toList());
    }


    public void getMediaToBePlayed(VideoData videoData, HBox embededContainer, int[] dimensions) {
        mediaDao.getMedia(videoData.getSite(), embededContainer, videoData.getKey(), dimensions[0], dimensions[1]);
    }


    /**
     * Used for getting data for landing poster
     */
    public List<MovieSearchResponse> getAllResponses(String name) throws MoviesException {
        return multiSearch.parseResponse(name);
    }

    public ObservableList<MovieData> getMovieDataFromSimilarUsers(User loggedUser) {
        ObservableList<MovieData> movieData = FXCollections.observableArrayList();
        this.getTopMoviesFromSimilarPeople(loggedUser).forEach(elem -> movieData.add(elem.getMovie()));
        return movieData;
    }

    public List<MovieSearchResponse> movieSearchResponses(ObservableList<MovieData> movieData) {
        List<MovieSearchResponse> movieSearchResponses = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MultiSearch multiSearch = new MultiSearch();
            MovieSearchResponse movieSearchResponse = null;
            try {
                movieSearchResponse = multiSearch.parseResponse(movieData.get(i).getTitle()).get(0);
            } catch (Exception exception) {
                continue;
            }
            if (!Objects.equals(movieSearchResponse.getTitle(), null) && !Objects.equals(movieSearchResponse.getBackdrop_path(), null)) {
                movieSearchResponses.add(movieSearchResponse);
            }
        }
        return movieSearchResponses;
    }

    public ObservableList<Image> getMoviesPosters(ObservableList<MovieSearchResponse> moviesToDisplay) {
        List<Image> movieImages = new ArrayList<>();
        ObservableList<Image> observableList = FXCollections.observableArrayList();
        for (MovieSearchResponse mvsr : moviesToDisplay) {
            imageDao.getImage(movieImages, mvsr.getBackdrop_path(), true);
        }
        System.out.println(movieImages.size()+"in logic");
        if (!movieImages.isEmpty()) {
            observableList.setAll(movieImages);
            return observableList;
        }
        return observableList;
    }
}
