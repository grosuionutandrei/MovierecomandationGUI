package dk.easv.dataaccess;

import dk.easv.entities.Movie;
import dk.easv.entities.MovieData;
import dk.easv.entities.Rating;
import dk.easv.entities.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class DataAccessManager {
    String originalMoviesPath = "MovieRecommendationSystem-CachedImplementation/data/movie_titles.txt";
    String newMovieData = "MovieRecommendationSystem-CachedImplementation/data/movie_data.txt";
    private HashMap<Integer, User> users = new HashMap<>();
    private HashMap<Integer, MovieData> movies = new HashMap<>();
    //private HashMap<Integer,MovieData> moviesWithUrl =  new HashMap<>();
    private List<Rating> ratings = new ArrayList<>();
    //private Map<Integer,MovieData> moviesData = new HashMap<>();

//    public Map<Integer, MovieData> getMoviesData() {
//        loadAllMovieData();
//        return moviesData;
//    }

    // Loads all data from disk and stores in memory
    // For performance, data is only updated if updateCacheFromDisk() is called
    public DataAccessManager() {
        updateCacheFromDisk();
    }

    public Map<Integer, User> getAllUsers() {
        return users;
    }

    public Map<Integer, MovieData> getAllMovies() {
        return movies;
    }

    public List<Rating> getAllRatings() {
        return ratings;
    }


    public void updateCacheFromDisk() {
        loadAllRatings(this.newMovieData);
    }

    private void loadAllMovies(String path) {
        try {
            List<String> movieLines = Files.readAllLines(Path.of(path));
            for (String movieLine : movieLines) {
                String[] split = movieLine.split(",");
                MovieData moviedata = new MovieData(Integer.parseInt(split[0]), split[1], Integer.parseInt(split[2]), split[3], Integer.parseInt(split[4]));
                movies.put(moviedata.getId(), moviedata);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAllMovieData() {
        try {
            List<String> movieLines = Files.readAllLines(Path.of("MovieRecommendationSystem-CachedImplementation/data/movie_titles.txt"));
            for (String movieLine : movieLines) {
                String[] split = movieLine.split(",");
                System.out.println(split.toString());
                MovieData moviedata = new MovieData(Integer.parseInt(split[0]), split[1], Integer.parseInt(split[2]), split[3], Integer.parseInt(split[4]));
                movies.put(moviedata.getId(), moviedata);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAllUsers() {
        try {
            List<String> userLines = Files.readAllLines(Path.of("MovieRecommendationSystem-CachedImplementation/data/users.txt"));
            for (String userLine : userLines) {
                String[] split = userLine.split(",");
                User user = new User(Integer.parseInt(split[0]), split[1]);
                users.put(user.getId(), user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads all ratings, users and movies must be loaded first
    // Users holds a list of ratings and movies holds a list of ratings
    private void loadAllRatings(String moviesPath) {
        loadAllMovies(moviesPath);
        loadAllUsers();
        try {
            List<String> ratingLines = Files.readAllLines(Path.of("MovieRecommendationSystem-CachedImplementation/data/ratings.txt"));
            for (String ratingLine : ratingLines) {
                String[] split = ratingLine.split(",");
                int movieId = Integer.parseInt(split[0]);
                int userId = Integer.parseInt(split[1]);
                int rating = Integer.parseInt(split[2]);
                Rating ratingObj = new Rating(users.get(userId), movies.get(movieId), rating);
                ratings.add(ratingObj);
                users.get(userId).getRatings().add(ratingObj);
                movies.get(movieId).getRatings().add(ratingObj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeDataToFile(List<MovieData> movies) {

    }


    /**
     * saves movies with the url to the text file
     */
    public void saveMovieData(List<MovieData> allData) {
        String fileName = "MovieRecommendationSystem-CachedImplementation/data/movie_data_new.txt";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName),
                StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (MovieData movie : allData) {
                String movieString = movie.toString() + System.lineSeparator();
                writer.write(movieString);
            }
            System.out.println("Data was appended to the CSV file successfully using NIO.");
        } catch (IOException e) {
            System.out.println("An error occurred while appending to the CSV file using NIO.");
            e.printStackTrace();
        }
    }

    public void rewrite(List<MovieData> movies) {

        int counter = 0;
        int batchSize = 100;
        for (MovieData movie : movies) {
            counter++;
            if (movie.getImageUrl().startsWith("file")) {
                movie.setImageUrl("default");
            }
            if (counter % batchSize == 0) {
                System.out.println(movies.get(counter - 2));
                saveMovieData(new ArrayList<>(movies.subList(counter - batchSize, counter)));
            }
        }

        if (!movies.isEmpty() && counter % batchSize != 0) {
            saveMovieData(new ArrayList<>(movies.subList(counter - (counter % batchSize), counter)));
        }

    }


}

