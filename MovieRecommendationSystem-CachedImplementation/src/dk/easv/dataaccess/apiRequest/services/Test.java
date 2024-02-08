package dk.easv.dataaccess.apiRequest.services;

import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.exceptions.MoviesException;

import java.util.List;

public class Test {

    public static void main(String[] args) throws MoviesException {

        MultiSearch multiSearch = new MultiSearch();

        List<?> movieSearchResponses= multiSearch.parseResponse("napoleon");
        MovieSearchResponse movieSearchResponse = (MovieSearchResponse) movieSearchResponses.getFirst();
        System.out.println(movieSearchResponse.getbackdrop_path());
    }
}
