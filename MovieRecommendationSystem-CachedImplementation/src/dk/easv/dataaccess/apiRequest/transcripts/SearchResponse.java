package dk.easv.dataaccess.apiRequest.transcripts;

import java.util.List;

public class SearchResponse {
    private int page;
    private List<?> results;

    private int total_pages;
    private int total_results;
    @Override
    public String toString() {
        return "SearchResponse{" +
                "page=" + page +
                ", results=" + results +
                ", totalPages=" + total_pages +
                ", totalResults=" + total_results +
                '}';
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<?> getResults() {
        return results;
    }

    public void setResults(List<?> results) {
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
}
