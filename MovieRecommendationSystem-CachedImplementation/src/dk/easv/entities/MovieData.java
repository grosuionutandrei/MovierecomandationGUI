package dk.easv.entities;


import java.util.ArrayList;
import java.util.List;

public class MovieData {

    private int id;
    private String title;
    private int year;
    private List<Rating> ratings;
    private String imageUrl;

    private String backdropUrl;
    private int tmdbId;
    public MovieData(int id, String title, int year, String imageUrl, int tmdbId) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.imageUrl=imageUrl;
        this.tmdbId= tmdbId;
        this.ratings = new ArrayList<>();
    }


    public MovieData(int id, String title, int year, String imageUrl, String backdropUrl, int tmdbId) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.imageUrl=imageUrl;
        this.backdropUrl = backdropUrl;
        this.tmdbId= tmdbId;
        this.ratings = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return  id +","+ title + ","+ year + "," + imageUrl+","+tmdbId;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }
    public List<Rating> getRatings() {
        return ratings;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }
    public double getAverageRating(){
        double sum = 0;
        for (Rating r: ratings){
            sum+=r.getRating();
        }
        if(ratings.size()==0)
            return 0;
        return sum/ratings.size();
    }
}









