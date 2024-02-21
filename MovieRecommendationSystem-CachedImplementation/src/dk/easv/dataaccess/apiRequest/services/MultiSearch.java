package dk.easv.dataaccess.apiRequest.services;

import com.google.gson.Gson;
import dk.easv.dataaccess.ACCESS;
import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.dataaccess.apiRequest.transcripts.SearchResponse;
import dk.easv.exceptions.MoviesException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MultiSearch
{
    private String baseURL = "https://api.themoviedb.org/3/search/multi?query=";

    private SearchResponse makeRequest(String query) throws MoviesException {
        HttpRequest httpRequest;
        SearchResponse searchResponse;
        // Sending HTTP Request to API
        try {
            httpRequest = HttpRequest.newBuilder()
                    .uri(new URL(buildURL(query)).toURI())
                    .header("Authorization", ACCESS.TMDB_KEY.getValue())
                    .GET().build()
                    ;
        } catch (URISyntaxException | UnsupportedEncodingException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
        // Parsing incoming response as json
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            searchResponse = gson.fromJson(response.body(), SearchResponse.class);
        } catch (IOException | InterruptedException e) {
            throw new MoviesException(e.getMessage());
        }
        return searchResponse;

    }

    public List<MovieSearchResponse> parseResponse(String query) throws MoviesException {
        SearchResponse searchResponse = makeRequest(query);
        Gson gson = new Gson();
        List<?> map = searchResponse.getResults();

        return map.stream()
                .map(gson::toJson)
                .map(elem -> gson.fromJson(elem, MovieSearchResponse.class))
                .toList();
    }


    /** Build URL from query which is our movie/series name */
    public String buildURL(String query) throws UnsupportedEncodingException {
        // We need to encode URL for in order to get proper results for queries which contains whitespace and special chars.
        String encodedQuery = URLEncoder.encode(query, "UTF-8");
        return baseURL + encodedQuery;
    }


}
