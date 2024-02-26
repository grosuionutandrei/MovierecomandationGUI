package dk.easv.presentation.components.poster;
import dk.easv.entities.MovieData;
import java.util.ArrayList;
import java.util.List;

public class ImagesControl {
    private List<MovieData> topMovies;

    private int batch = 15;
    private int counter = 0;

    public ImagesControl(List<MovieData> topMovies) {
        this.topMovies = topMovies;
    }
    private List<MovieData> getNextBatch() {
        List<MovieData> movie = new ArrayList<>();
        if (this.counter <= this.topMovies.size()) {
            movie = this.topMovies.subList(counter, Math.min((counter + batch), this.topMovies.size()));
            counter += batch;
            return movie;
        }
        return movie;
    }
    public List<MovieData> getNextBatchMovies(){
        return getNextBatch();
    }

}
