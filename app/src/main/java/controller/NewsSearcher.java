package controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import fragment.main_slidingtab.NewsTagFragment;
import news.News;
import news.NewsProxy;

/**
 * Created by thinkpad on 2017/9/9.
 */

public class NewsSearcher {
    private NewsTagFragment newsTagFragment ;
    private static NewsSearcher searcher = null;
    private NewsSearcher() {}

    public static synchronized NewsSearcher getInstance() {
        if (searcher == null) {
            searcher = new NewsSearcher();
        }
        return (searcher);
    }

    public void setNewsTagFragment(NewsTagFragment newsTagFragment) {this.newsTagFragment = newsTagFragment ;}
    public void searchKeyWord(String keyWord) {
        NewsProxy.getInstance().setKeywords(keyWord);
        newsTagFragment.update();
    }
    public void finishSearching() {
        NewsProxy.getInstance().finish_searching();
        newsTagFragment.update() ;
    }

}