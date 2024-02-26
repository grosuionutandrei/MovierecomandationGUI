package dk.easv.dataaccess.httpRequest;
import com.google.gson.Gson;
import dk.easv.dataaccess.ACCESS;
import dk.easv.dataaccess.apiRequest.transcripts.VideoData;
import dk.easv.dataaccess.apiRequest.transcripts.VideoSearchResponse;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SearchVideo {
    String baseUrl = "https://api.themoviedb.org/3/movie/";
    String endUrl = "/videos?language=en-US";

    private VideoSearchResponse searchRequest(int id) {
        VideoSearchResponse searchResponse = null;
        HttpRequest getRequest = null;
        try {
            getRequest = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + id + endUrl))
                    .header("accept", "application/json")
                    .header("Authorization", ACCESS.TMDB_KEY.getValue())
                    .GET().build();
        } catch (URISyntaxException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error from the api");
        }
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            searchResponse = gson.fromJson(response.body(), VideoSearchResponse.class);
        } catch (IOException | InterruptedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error from the api converting to object from json");
        }
        return searchResponse;
    }

    public List<VideoData> movieResponses(int id) {
        VideoSearchResponse searchResponse = searchRequest(id);
         Gson gson = new Gson();
        List<?> map = searchResponse.getResults();
        if(map!=null){
            return map.stream().map(gson::toJson).map(elem -> gson.fromJson(elem, VideoData.class)).toList();
        }
        return null;
    }
}
