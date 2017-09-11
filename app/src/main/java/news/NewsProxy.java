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
    private static final int perLoadNum = 20;
    private static final int perDisplayNum = 20;
    private static final int newsProxyNum = 13;
    private String keywords = "";
    private int searchSize = 0;
    private int searchDisplaySize = 0;
    private ArrayList<News> newsSearch;
    private Activity thisActivity;
    public Bitmap notFoundBitmap;
    private ArrayList<News> newsAll[];
    private NewsProxy() {}
    public static synchronized NewsProxy getInstance() {
        if (newsProxy == null) {
            newsProxy = new NewsProxy();
            newsProxy.size = new int[newsProxyNum];
            newsProxy.displaySize =  new int[newsProxyNum];
            newsProxy.newsAll = new ArrayList[newsProxyNum];
            newsProxy.newsSearch = new ArrayList<News>();
            for (int i = 0 ; i < newsProxyNum; i++){
                newsProxy.size[i] = perLoadNum;
                newsProxy.displaySize[i] = perDisplayNum;
                newsProxy.update(i);
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
    public int getDisplaySize(int classTagId) {
        if (keywords.equals("")) return  displaySize[classTagId];
        else return(searchDisplaySize);
    }
    public void setDisplaySize(int classTagId, int displaySize) {
        if (keywords.equals("")) this.displaySize[classTagId] = displaySize;
        else searchDisplaySize = displaySize;
    }
    public ArrayList<News> getDisplayNews(int classTagId) {
        ArrayList<News> sublist = new ArrayList<News>();
        System.out.printf("displaysize[%d] = %d size[%d] = %d \n", classTagId ,displaySize[classTagId], classTagId, size[classTagId]);
        System.out.printf("now size[%d] = %d \n", classTagId, newsAll[classTagId].size());
       // try {
        if (keywords.equals("")) {
            for (int i = 0; i < displaySize[classTagId]; i++)
                sublist.add(newsAll[classTagId].get(i));
        }
        else{
            for (int i = 0; i < searchDisplaySize; i++)
                sublist.add(newsSearch.get(i));
        }
       // } catch (IndexOutOfBoundsException e) {
          //  e.printStackTrace();
      //  }
        return sublist;

        // It's too complicated

    }
    private synchronized void addNewsOfPage(int page_, int classTagId_) {
        System.out.println("addNewsOfPage " + page_ );
        final int page = page_;
        final int classTagId = classTagId_;
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url;
                    if (keywords.equals("")) {
                        if (classTagId == 0)
                            url = new URL("http://166.111.68.66:2042/news/action/query/latest?pageNo=" + page + "&pageSize=" + perLoadNum);
                        else
                            url = new URL("http://166.111.68.66:2042/news/action/query/latest?pageNo=" + page + "&pageSize=" + perLoadNum + "&category=" + classTagId);
                    }
                    else{
                        url = new URL("http://166.111.68.66:2042/news/action/query/search?keyword="+keywords+"&pageNo="+ page + "&pageSize=" + perLoadNum);
                    }
                    InputStream is = url.openStream();
                    StringBuffer out = new StringBuffer();
                    byte[] b = new byte[4096];
                    for (int n; (n = is.read(b)) != -1;) {
                        out.append(new String(b, 0, n));
                    }
                    String json = out.toString();
                    JSONObject jsonObject1 = new JSONObject(json);
                    JSONArray jsonArray = jsonObject1.getJSONArray("list");
                    ArrayList<News> currentList;
                    if (keywords.equals("")) currentList = newsAll[classTagId];
                    else currentList = newsSearch;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        currentList.add(new News(jsonObject));
                    }
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public synchronized void update(int classTagId) {
        /**
         update the newslist to the newest size news
         */
        Log.d("NewsProxy","update()!!!!");
        if (newsAll[classTagId] != null)
            newsAll[classTagId].clear();
        else newsAll[classTagId] = new ArrayList<News>();
        size[classTagId] = perLoadNum;
        displaySize[classTagId] = 20;
        for (int i = 0; i < size[classTagId]; i += perLoadNum) {
            addNewsOfPage(i / perLoadNum + 1, classTagId);
        }
    }
    public void moreNews(int classTagId) {
        if (keywords.equals("")) {
            while (displaySize[classTagId] + perDisplayNum > size[classTagId]) {
                size[classTagId] += perLoadNum;
                addNewsOfPage(size[classTagId] / perLoadNum, classTagId);
            }
            displaySize[classTagId] += perDisplayNum;
        }
        else{
            while (searchDisplaySize + perDisplayNum > searchSize) {
                searchSize += perLoadNum;
                addNewsOfPage(searchSize / perLoadNum, classTagId);
            }
            searchDisplaySize += perDisplayNum;
        }
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    public String getKeywords() {
        return(keywords);
    }
    public void finish_searching() {
        keywords = "";
        searchDisplaySize = 0;
        searchSize = 0;
    }
}

