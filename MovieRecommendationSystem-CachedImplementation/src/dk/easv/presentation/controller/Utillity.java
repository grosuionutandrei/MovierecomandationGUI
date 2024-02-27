package dk.easv.presentation.controller;

import dk.easv.presentation.components.poster.ImagePoster;
import dk.easv.presentation.components.poster.ImagesControl;
import dk.easv.presentation.model.AppModel;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Utillity {

    /**
     * showButtons on hover , if the right is pressed than the left button is showed
     *
     * @param isPressed boolean value that holds the pressed state
     * @param left      the left button than is controlled
     * @param right     the right button that is controlled
     */
    public static void showButtonsOnHover(Boolean isPressed, Button left, Button right) {
        if (isPressed) {
            left.setVisible(true);
            right.setVisible(true);
        } else {
            right.setVisible(true);
        }
    }


    /**
     * hide buttons on mouse exit from the stack pane
     *
     * @param left
     * @param right represents the buttons to be hidden
     */
    public static void hideButtonsOnExit(Button left, Button right) {
        left.setVisible(false);
        right.setVisible(false);
    }


    public static double calculateScrollPaneHBarValue(ScrollPane scrollPane, boolean moveRight) {
        final double imageWidth = 250;
        final double spacing = 15;
        final double totalImageWidth = imageWidth + spacing;
        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double contentWidth = scrollPane.getContent().prefWidth(-1);
        int imagesThatFitInView = (int) Math.floor(viewportWidth / totalImageWidth);
        double moveNormalized = (double) imagesThatFitInView * totalImageWidth / (contentWidth - viewportWidth);
        double newHvalue;
        if (moveRight) {
            newHvalue = Math.min(1.0, scrollPane.getHvalue() + moveNormalized);
        } else {
            newHvalue = Math.max(0.0, scrollPane.getHvalue() - moveNormalized);
        }
        return newHvalue;
    }


    /**
     * Configures the scroll animation
     * @param toRight       navigate right or left
     * @param millis        duration for the scroll animatiopn
     * @param newHValue     the value where the scroll pane hbar  needs to be positioned
     * @param scrollPane    the scroll pane parent
     * @param left          the left navigational button
     * @param right         the right navigational button
     * @param imagesControl the object that controls the image data
     * @param model         model that holds the movie data
     * @param parent        the container off the images
     */
    public static void configureScrollAnimation( boolean toRight,Double millis, double newHValue, ScrollPane scrollPane, Button left, Button right, ImagesControl imagesControl, AppModel model, HBox parent) {
        Timeline timeline = applySmoothEffect(millis, newHValue, scrollPane);
        timeline.setOnFinished(event -> onScrollAnimationComplete(toRight,left, right, imagesControl, model, parent));
        timeline.play();
    }


    /**
     * Apply a smooth transition animation to the scroll pane
     *
     * @param newHvalue represnts the new value where the scroll pane hbar needs to move
     */
    private static Timeline applySmoothEffect(Double millis, double newHvalue, ScrollPane scrollPane) {
        double milliSeconds = 500;
        if (millis != null) {
            milliSeconds = millis;
        }
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(scrollPane.hvalueProperty(), newHvalue);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(milliSeconds), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        return timeline;
    }


    /**
     * Sets up the events that will be called when the animation completes
     *
     * @param leftButton    the left navigational button
     * @param rightButton   the right navigational button
     * @param imagesControl the object that controls the image data
     * @param model         model that holds the movie data
     * @param parent        the container off the images
     */
    private static void onScrollAnimationComplete(boolean toRight,Button leftButton, Button rightButton, ImagesControl imagesControl, AppModel model, HBox parent) {
        Platform.runLater(() -> {
            leftButton.setVisible(true);
            loadMovies(toRight,imagesControl, model, parent);
            if(toRight){
                rightButton.setDisable(false);
            }else{
                leftButton.setDisable(false);
            }

        });
    }

    /**
     * Loads the next  batch movies
     *
     * @param imagesControl the object that controls the image data
     * @param model         model that holds the movie data
     * @param parent        the container off the images
     */
    private static void loadMovies(boolean toRight, ImagesControl imagesControl, AppModel model, HBox parent) {
        if(toRight){
            loadImages(imagesControl, model, parent);
        }else{
            loadImagesFromFront(imagesControl,model,parent);
        }

    }

    /**
     * Loads initial movies to the scrollPane
     */
    private static void loadImages(ImagesControl imagesControl, AppModel model, HBox parent) {
        System.out.println(parent.getChildren().size() + " size before");
        List<Node> kids = parent.getChildren();
        List<Node> toRemovePosters = new ArrayList<>();
        List<ImagePoster> toAddPosters = new ArrayList<>();
        if (kids.size() > 100) {
            toRemovePosters.addAll(kids.subList(0, kids.size() - 50));
        }

        Platform.runLater(() -> {
            for (Node node : toRemovePosters) {
                if (node instanceof ImagePoster) {
                    ImagePoster poster = (ImagePoster) node;
                    poster.cleanup();
                }
            }
            parent.getChildren().removeAll(toRemovePosters);
        });

        imagesControl.getNextBatchMovies().forEach((elem) -> {
            ImagePoster imagePoster = new ImagePoster(elem, model);
            model.addResizable(imagePoster);
            toAddPosters.add(imagePoster);
        });
        Platform.runLater(() -> parent.getChildren().addAll(toAddPosters));
        //System.out.println(imagesControl.getNextBatchMovies().size() + "size after");
        System.out.println(parent.getChildren().size() + "after");
        System.out.println(parent.getChildren().size());
    }

    public static void loadImagesFromFront(ImagesControl imagesControl, AppModel model, HBox parent) {
        if(imagesControl.getLastLoadedUp()<=imagesControl.getBatch()){
            System.out.println("smaller");
            return;
        }
        List<Node> kids = parent.getChildren();
        List<Node> toRemovePosters = new ArrayList<>();
        List<ImagePoster> toAddPosters = new ArrayList<>();
        System.out.println(imagesControl.getLastLoadedUp());
        if(kids.size()>100){
            toRemovePosters.addAll(kids.subList(kids.size()-50,kids.size()));
        }
                Platform.runLater(() -> {
            for (Node node : toRemovePosters) {
                if (node instanceof ImagePoster poster) {
                    poster.cleanup();
                }
            }
            parent.getChildren().removeAll(toRemovePosters);
        });

        imagesControl.getPreviousBatchMovies().forEach((elem) -> {
            ImagePoster imagePoster = new ImagePoster(elem, model);
            model.addResizable(imagePoster);
            toAddPosters.add(imagePoster);
        });
        Platform.runLater(() -> {
            for (int i = toAddPosters.size() - 1; i >= 0; i--) {
                parent.getChildren().add(0, toAddPosters.get(i));
            }
        });

    }

}
























