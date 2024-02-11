package dk.easv.dataaccess.apiRequest.services;

import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.exceptions.MoviesException;
import java.net.HttpURLConnection;

import java.net.*;


import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

public class Test {

    public static void main(String[] args) throws MoviesException, UnsupportedEncodingException, URISyntaxException, MalformedURLException {

        MultiSearch multiSearch = new MultiSearch();


        List<?> movieSearchResponses= multiSearch.parseResponse("Prison Break");
        MovieSearchResponse movieSearchResponse = (MovieSearchResponse) movieSearchResponses.getFirst();
        System.out.println(movieSearchResponse.getbackdrop_path());
    }
}
