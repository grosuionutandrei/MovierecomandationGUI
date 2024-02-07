package dk.easv.presentation.poster;


import dk.easv.entities.TopMovie;

import java.util.ArrayList;
import java.util.List;

public class ImagesControl {
    private List<TopMovie> topMovies;

    private int batch = 7;
    private int counter = 0;

    public ImagesControl(List<TopMovie> topMovies) {
        this.topMovies = topMovies;
    }


    private List<TopMovie> getNextBatch() {
        List<TopMovie> movie = new ArrayList<>();

        if (this.counter <= this.topMovies.size()) {
            movie = this.topMovies.subList(counter, Math.min((counter + batch), this.topMovies.size()));
            counter += batch;
            return movie;
        }
        return movie;
    }


    public List<TopMovie> getNextBatchMovies(){
        return getNextBatch();
    }

}
