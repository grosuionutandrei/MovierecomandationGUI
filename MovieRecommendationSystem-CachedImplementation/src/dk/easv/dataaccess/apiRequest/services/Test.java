package dk.easv.dataaccess.apiRequest.services;

import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.exceptions.MoviesException;

import java.net.*;


import java.io.UnsupportedEncodingException;
import java.util.List;

public class Test {

    public static void main(String[] args) throws MoviesException, UnsupportedEncodingException, URISyntaxException, MalformedURLException {

        MultiSearch multiSearch = new MultiSearch();


        List<?> movieSearchResponses= multiSearch.parseResponse("Demon Warrior");
        System.out.println(movieSearchResponses.size());
        MovieSearchResponse movieSearchResponse = (MovieSearchResponse) movieSearchResponses.getFirst();
        System.out.println(movieSearchResponse.getTitle() + movieSearchResponse.getBackdrop_path());
    }
}
