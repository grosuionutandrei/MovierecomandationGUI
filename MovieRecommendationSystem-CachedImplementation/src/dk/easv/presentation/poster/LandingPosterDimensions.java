package dk.easv.presentation.poster;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class LandingPosterDimensions {
    private DoubleProperty width;
    private DoubleProperty height;
    private static LandingPosterDimensions instance;
    public static LandingPosterDimensions getInstance(double width,double height){
        if(instance==null){
            instance= new LandingPosterDimensions(width,height);

        }
        return instance;
    }



    private LandingPosterDimensions(double width,double height) {
        this.width = new SimpleDoubleProperty(width);
        this.height = new SimpleDoubleProperty(height);
    }



    public double getWidth() {
        return width.get();
    }

    public void setWidth(double width) {
        this.width.set(width);
    }

    public double getHeight() {
        return height.get();
    }

    public void setHeight(double height) {
        this.height.set(height);
    }

    public static LandingPosterDimensions getInstance() {
        return instance;
    }

    public DoubleProperty widthProperty() {
        return width;
    }

    public DoubleProperty heightProperty() {
        return height;
    }
}
