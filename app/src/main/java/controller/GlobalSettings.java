package controller;

/**
 * Created by thinkpad on 2017/9/13.
 */

public class GlobalSettings {
    private static boolean noPictureMode = false;
    public static void setNoPictureMode(boolean noPictureMode_){
        noPictureMode = noPictureMode_;
    }
    public static boolean getNoPictureMode(){
        return(noPictureMode);
    }

    private static boolean nightMode = false;
    public static void setNightMode(boolean nightMode_){
        nightMode = nightMode_;
    }
    public static boolean getNightMode(){
        return(nightMode);
    }

    private static boolean imageSearch = true;
    public static boolean getImageSearch(){
        return(imageSearch);
    }
    public static void setImageSearch(boolean imageSearch_){
        imageSearch = imageSearch_;
    }

    private static boolean recommendRelated = true;
    public static boolean getRecommendRelated(){
        return(recommendRelated);
    }
    public static void setRecommendRelated(boolean recommendRelated_){
        recommendRelated = recommendRelated_;
    }
}
