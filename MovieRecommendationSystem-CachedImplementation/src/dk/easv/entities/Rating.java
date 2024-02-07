package dk.easv.entities;

public class Rating {
    private User user;
    private MovieData movie;
    private int rating;

    public Rating(User user, MovieData movie, int rating) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MovieData getMovie() {
        return movie;
    }

    public void setMovie(MovieData movie) {
        this.movie = movie;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "rating=" + Math.round(rating*10)/10.0;
    }
}
