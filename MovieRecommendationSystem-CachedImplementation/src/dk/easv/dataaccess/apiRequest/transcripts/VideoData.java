package dk.easv.dataaccess.apiRequest.transcripts;

public class VideoData {

    private String key;
    private String site;

    @Override
    public String toString() {
        return "VideoData{" +
                "key='" + key + '\'' +
                ", site='" + site + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
