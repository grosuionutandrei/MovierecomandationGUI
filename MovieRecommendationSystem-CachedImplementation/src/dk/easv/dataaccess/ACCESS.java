package dk.easv.dataaccess;

public enum ACCESS {
TMDB_KEY("Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiZTkyMDM4ODFkZjRiN2IwNDQ1Y2VhYzJmYWJjYzc0NSIsInN1YiI6IjY1YWJlMTI1MWYzZTYwMDBhNGZlYjQ0NCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.w8mGiSsANLtnWrJxJDQ4F0gBud5oLKTAcyqiJqhLr1I");

    public String getValue() {
        return value;
    }

    private String value;
    ACCESS(String value) {
    this.value=value;
    }
}
