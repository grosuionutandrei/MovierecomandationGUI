package dk.easv.dataaccess.httpRequest;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class MediaService extends Service<String> {

    private String mediaPath;
    private double width;
    private double height;

    public MediaService(String mediaPath, double width, double height) {
        this.mediaPath = mediaPath;
        this.width=width;
        this.height=height;
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                String iframe = "<iframe width='"+width+"' height='"+height+"' src="+mediaPath+" title='YouTube video player' frameborder='0' allow='accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share' allowfullscreen></iframe>";
                return iframe;
            }
        };
    }
}
