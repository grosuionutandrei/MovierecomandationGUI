package dk.easv.presentation.components.poster;
import dk.easv.entities.MovieData;
import java.util.ArrayList;
import java.util.List;

public class ImagesControl {
    private List<MovieData> topMovies;
    private int batch = 15;
    private int counterUp = 0;
    private int counterDown = 0;
    private int lastLoadedUp = 0; // Ensure this is correctly updated

    public ImagesControl(List<MovieData> topMovies) {
        this.topMovies = new ArrayList<>(topMovies); // Ensure a deep copy if necessary
    }

    private List<MovieData> getNextBatch() {
        List<MovieData> movies = new ArrayList<>();
        if (counterUp < topMovies.size()) {
            int end = Math.min(counterUp + batch, topMovies.size());
            movies = new ArrayList<>(topMovies.subList(counterUp, end));
            counterUp = end;
            lastLoadedUp = end; // Update lastLoadedUp here
        }
        return movies;
    }

    private List<MovieData> getPreviousBatch() {
        List<MovieData> movies = new ArrayList<>();
        if (lastLoadedUp > batch) {
            int start = Math.max(0, lastLoadedUp - 50); // Adjust this logic if needed
            int end = start + batch;
            counterDown = start; // Adjust counterDown for consistency
            movies = new ArrayList<>(topMovies.subList(start, Math.min(end, topMovies.size())));
            lastLoadedUp = start; // Update lastLoadedUp to reflect the "new" last loaded after going backwards
        }
        return movies;
    }

    public List<MovieData> getNextBatchMovies() {
        return getNextBatch();
    }

    public List<MovieData> getPreviousBatchMovies() {
        return getPreviousBatch();
    }

    public int getLastLoadedUp() {
        return lastLoadedUp; // Corrected method name and variable
    }
    public int getBatch(){
        return this.batch;
    }
}

