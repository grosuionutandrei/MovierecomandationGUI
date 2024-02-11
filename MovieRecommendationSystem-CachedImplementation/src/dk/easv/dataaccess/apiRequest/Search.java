package dk.easv.dataaccess.apiRequest;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import dk.easv.dataaccess.ACCESS;
import dk.easv.dataaccess.apiRequest.transcripts.MovieSearchResponse;
import dk.easv.dataaccess.apiRequest.transcripts.SearchResponse;
import dk.easv.exceptions.MoviesException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Search {
    String baseUrl = "https://api.themoviedb.org/3/search/movie?query=";
    String endUrl = "&include_adult=false&language=en-US&";
    String releaseYear="primary_release_year=";
    String page = "&page=1";

    private SearchResponse searchRequest(String name,String year ) throws MoviesException {
        SearchResponse searchResponse;
        HttpRequest getRequest = null;
        try {
            getRequest = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + name + endUrl + releaseYear + year +page))
                    .header("Authorization", ACCESS.TMDB_KEY.getValue())
                    .GET().build();
        } catch (URISyntaxException e) {
            throw new MoviesException(e.getMessage());
        }
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            searchResponse = gson.fromJson(response.body(), SearchResponse.class);
        } catch (IOException | InterruptedException e) {
            throw new MoviesException(e.getMessage());
        }
        return searchResponse;
    }

    public List<MovieSearchResponse> movieResponses(String query,String year) throws MoviesException {
        SearchResponse searchResponse = searchRequest(query,year);
        Gson gson = new Gson();
        List<?> map = searchResponse.getResults();
        LinkedTreeMap<String, Object> mapMovie = new LinkedTreeMap<>();
        return map.stream().map(gson::toJson).map(elem -> gson.fromJson(elem, MovieSearchResponse.class)).toList();

//     return searchResponse.getResults().stream().map(elem->new Gson().fromJson((Reader) map,MovieSearchResponse.class)).toList();
    }


}
