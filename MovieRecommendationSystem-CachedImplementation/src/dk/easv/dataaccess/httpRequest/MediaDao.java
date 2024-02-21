package dk.easv.dataaccess.httpRequest;

import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

public class MediaDao {
    String baseUrlYoutube = "https://www.youtube.com/embed/";
    String baseUrlDefault = "https://youtu.be/2tm61CNECmU";

    public MediaDao() {
    }

    public void getMedia(String site, HBox embedContainer, String key, double width, double height) {
        MediaService mediaService = null;
        if (site.equals("YouTube")) {
            mediaService = new MediaService(baseUrlYoutube + key, width, height);
        } else if (site.equals("default")) {
            mediaService = new MediaService(baseUrlYoutube + key, width, height);
        }

        WebView webView = new WebView();
        MediaService mediaService1 = mediaService;
        mediaService.setOnSucceeded(e -> {
            System.out.println(mediaService1.getValue());
            System.out.println("alooo");
            // HTML content with iframe
            String htmlContent = "<html><body>" +
                    mediaService1.getValue() +
                    "</body></html>";
            webView.getEngine().loadContent(htmlContent, "text/html");
            webView.getStyleClass().add("webContainer");
            embedContainer.getChildren().clear();
            embedContainer.getChildren().add(webView);
            System.out.println(key);
        });
        mediaService.setOnFailed(e -> {
            System.out.println("Failed");
        });
        mediaService.start();
    }
}
