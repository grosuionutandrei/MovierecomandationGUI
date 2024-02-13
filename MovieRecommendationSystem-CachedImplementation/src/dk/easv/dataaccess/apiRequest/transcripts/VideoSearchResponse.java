package dk.easv.dataaccess.apiRequest.transcripts;

import java.util.List;

public class VideoSearchResponse {
private int id;
private List<?> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<?> getResults() {
        return results;
    }

    public void setResults(List<?> results) {
        this.results = results;
    }
}
