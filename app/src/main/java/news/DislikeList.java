package news;

import java.util.ArrayList;

import controller.TextHelper;

/**
 * Created by thinkpad on 2017/9/13.
 */

public class DislikeList {
    private static DislikeList myself = null;
    private static ArrayList<String> dislikeList = new ArrayList<String>();
    private DislikeList(){}
    public static DislikeList getInstance(){
        if (myself == null) {
            myself = new DislikeList();
            dislikeList = NewsDatabase.getInstance().getAllDislike();
        }
        return(myself);
    }
    public void addDislike(String title){
        dislikeList.add(title);
        NewsDatabase.getInstance().addDislike(title);
    }
    public void removeDislike(String title){
        dislikeList.remove(title);
        NewsDatabase.getInstance().removeDislike(title);
    }
    public ArrayList<String> getAllDislike(){
        return(dislikeList);
    }
    public boolean checkDislike(String title){
        for (String s:dislikeList){
            if (TextHelper.similarNews(s,title))
                return(true);
        }
        return(false);
    }
}
