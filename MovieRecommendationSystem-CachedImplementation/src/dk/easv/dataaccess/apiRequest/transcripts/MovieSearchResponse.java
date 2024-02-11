package dk.easv.dataaccess.apiRequest.transcripts;
public class MovieSearchResponse {
    private int id;
    private String poster_path;
    private String name;
    private String backdrop_path;
    private String overview;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MovieSearchResponse{" +
                "id=" + id +
                ", poster_path='" + poster_path + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", overview='" + overview + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null){
            this.title = getName();
        }
        this.title = title;
    }

    public String getbackdrop_path() {
        return backdrop_path;
    }

    public void setbackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getName() {
        return name;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}
