package dk.easv.logic;

public class ViewLogic {

    private final int MAX_WIDTH = 768;
    private final int MAX_HEIGHT = 432;
    public ViewLogic() {
    }

    public int[] viewPortBasedSizeAspectRatio(int width,int height){
     int[] dimensions= {MAX_WIDTH,MAX_HEIGHT};
     if(width<MAX_WIDTH){
         double aspectRatio = 16d/9d;
         double widthSize = Math.min(width,(width*aspectRatio));
         double heightSize = Math.min(height,height/aspectRatio);
         dimensions[0]=(int)widthSize;
         dimensions[1]=(int) heightSize;
     }

    return dimensions;
    }
}
