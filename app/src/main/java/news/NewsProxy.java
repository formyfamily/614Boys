package news;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/5.
 */


public class NewsProxy {
    private static NewsProxy newsProxy;
    private int size[];
    private int displaySize[];
    private int hasRead[];
    private static final int perLoadNum = 100;
    private static final int perDisplayNum = 20;
    private static final int newsProxyNumSmall = 13;
    private static final int newsProxyNumBig = 26;
    private String keywords = "";
    private Activity thisActivity;
    public Bitmap notFoundBitmap;
    private ArrayList<News> newsAll[];
    private NewsProxy() {}
    public static synchronized NewsProxy getInstance() {
        if (newsProxy == null) {
            newsProxy = new NewsProxy();
            newsProxy.size = new int[newsProxyNumBig];
            newsProxy.displaySize =  new int[newsProxyNumBig];
            newsProxy.hasRead = new int[newsProxyNumBig];
            newsProxy.newsAll = new ArrayList[newsProxyNumBig];
            for (int i = 0 ; i < newsProxyNumBig; i++){
                newsProxy.size[i] = 0;
                newsProxy.displaySize[i] = 0;
                newsProxy.hasRead[i]=0;
                newsProxy.newsAll[i] = new ArrayList<News>();
                if (i < newsProxyNumSmall) newsProxy.update(i);
            }
        }
        return newsProxy;
    }
    public void setThisActivity(Activity activity) {
        thisActivity = activity;
        try {
            InputStream is = thisActivity.getAssets().open("image-not-found.jpg");
            notFoundBitmap = BitmapFactory.decodeStream(is);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int getRealIndex(int classTagId){
        int result = classTagId = classTagId;
        if (!keywords.equals("")) result += newsProxyNumSmall;
        return(result);
    }
    public int getDisplaySize(int classTagId) {
        int index = getRealIndex(classTagId);
        return(displaySize[index]);
    }
    public ArrayList<News> getDisplayNews(int classTagId) {
        if (keywords.equals("")&&(displaySize[classTagId]==0)){   // No network connection, load offline news
            return(NewsDatabase.getInstance().getNewsByClassTag(classTagId));
        }
        ArrayList<News> sublist = new ArrayList<News>();
        int index = getRealIndex(classTagId);
        for (int i = 0; i < displaySize[index]; i++)
            sublist.add(newsAll[index].get(i));
        return sublist;
    }

    private synchronized int addNewsOfPage(int page_, int classTagId_) {
        System.out.println("addNewsOfPage " + page_ );
        int index = getRealIndex(classTagId_);
        InternetQueryThread thread = new InternetQueryThread(keywords,classTagId_,page_,newsAll[index],perLoadNum);
        try {
            thread.start();
            thread.join();
            return(thread.getActualRead());
        } catch (InterruptedException e){
            e.printStackTrace();
            return(0);
        }
    }
    public synchronized void update(int classTagId) {
        /**
         update the newslist to the newest size news
         */
        Log.d("NewsProxy","update()!!!!");
        if (!keywords.equals("")) return;
        if (newsAll[classTagId] != null)
            newsAll[classTagId].clear();
        else newsAll[classTagId] = new ArrayList<News>();
        size[classTagId] = addNewsOfPage(1, classTagId);
        if (size[classTagId] >= perDisplayNum) displaySize[classTagId] = perDisplayNum;
        else displaySize[classTagId] = size[classTagId];
    }
    public void moreNews(int classTagId) {
        int actualReadSize;
        int index = getRealIndex(classTagId);
        if (displaySize[index] + perDisplayNum > size[index]) {
            actualReadSize = addNewsOfPage(hasRead[index] + 1, classTagId);
            size[index] += actualReadSize;
            hasRead[index] += (int)Math.ceil(actualReadSize/perLoadNum);
        }
        if (displaySize[index] + perDisplayNum > size[index]) displaySize[index] = size[index];
        else displaySize[index] += perDisplayNum;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    public String getKeywords() {
        return(keywords);
    }
    public void finish_searching() {
        keywords = "";
        for (int i=newsProxyNumSmall;i<newsProxyNumBig;i++) {
            displaySize[i] = 0;
            size[i] = 0;
            hasRead[i] = 0;
            newsAll[i].clear();
        }
    }
}

