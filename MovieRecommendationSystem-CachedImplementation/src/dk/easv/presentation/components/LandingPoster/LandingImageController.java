package dk.easv.presentation.components.LandingPoster;

import dk.easv.dataaccess.apiRequest.services.MultiSearch;
import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.entities.MovieData;
import dk.easv.entities.TopMovie;
import dk.easv.exceptions.MoviesException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LandingImageController {

    private List<TopMovie> topMovies;
    private final List<MovieData> movie = new ArrayList<>();
    private final List<MovieSearchResponse> movies = new ArrayList<>();

    public LandingImageController(List<TopMovie> topMovies){
        this.topMovies = topMovies;

    }

    private List<MovieSearchResponse> getTopTenMovies() throws MoviesException {

        for (int i=0; movies.size()<10; i++){
            MultiSearch multiSearch = new MultiSearch();
            MovieSearchResponse movieSearchResponse = null;
            try {
                movieSearchResponse = multiSearch.parseResponse(topMovies.get(i).getTitle()).get(0);
            } catch (Exception exception){
                continue;
            }
            if (!Objects.equals(movieSearchResponse.getTitle(), null) && !Objects.equals(movieSearchResponse.getBackdrop_path(), null)){
                movies.add(movieSearchResponse);
            }

        }
        return movies;
    }



    public List<MovieSearchResponse> getProperMoviesForLandingPage() throws MoviesException {

        return getTopTenMovies();
    }
}
