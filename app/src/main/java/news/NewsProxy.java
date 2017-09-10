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
    private static final int perLoadNum = 100;
    private static final int perDisplayNum = 20;
    private static final int newsProxyNum = 26;          // 13-25 used for searching
    private ArrayList<News> newsAll[];
    private String keywords="";               // used for searching and recommendation
    private NewsProxy() {}
    public static synchronized NewsProxy getInstance() {
        if (newsProxy == null) {
            newsProxy = new NewsProxy();
            newsProxy.size = new int[newsProxyNum];
            newsProxy.displaySize =  new int[newsProxyNum];
            newsProxy.newsAll = new ArrayList[newsProxyNum];
            for (int i = 0 ; i < newsProxyNum; i++){
                newsProxy.size[i] = perLoadNum;
                newsProxy.displaySize[i] = perDisplayNum;
                newsProxy.update(i);
            }
        }
        return newsProxy;
    }
    public int getDisplaySize(int classTagId) {
        return  displaySize[classTagId];
    }
    public void setDisplaySize(int classTagId, int displaySize) {
        this.displaySize[classTagId] = displaySize;
    }
    public ArrayList<News> getDisplayNews(int classTagId) {
        ArrayList<News> sublist = new ArrayList<News>();
        System.out.printf("displaysize[%d] = %d size[%d] = %d \n", classTagId ,displaySize[classTagId], classTagId, size[classTagId]);
        System.out.printf("now size[%d] = %d \n", classTagId, newsAll[classTagId].size());
        for (int i = 0; i < displaySize[classTagId]; i++)
            sublist.add(newsAll[classTagId].get(i));
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
                        if (classTagId == 0)
                            url = new URL("http://166.111.68.66:2042/news/action/query/search?keywords=" +
                                    keywords + "&pageNo=" + page + "&pageSize=" + perLoadNum);
                        else url = new URL("http://166.111.68.66:2042/news/action/query/search?keywords=" +
                                keywords + "&pageNo=" + page + "&pageSize=" + perLoadNum+"&category="+classTagId);
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
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        newsAll[classTagId].add(new News(jsonObject));
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
        int proxyIndex = classTagId;
        if (!keywords.equals("")) proxyIndex += 13;
        if (newsAll[proxyIndex] != null)
            newsAll[proxyIndex].clear();
        else newsAll[proxyIndex] = new ArrayList<News>();
        size[proxyIndex] = perLoadNum;
        displaySize[proxyIndex] = 20;
        for (int i = 0; i < size[proxyIndex]; i += perLoadNum) {
            addNewsOfPage(i / perLoadNum + 1, classTagId);
        }
    }
    public void moreNews(int classTagId) {
        int proxyIndex = classTagId;
        if (!keywords.equals("")) proxyIndex += 13;
        while (displaySize[proxyIndex] + perDisplayNum > size[proxyIndex]) {
            size[proxyIndex] += perLoadNum;
            addNewsOfPage(size[proxyIndex] / perLoadNum, classTagId);
        }
        displaySize[proxyIndex] += perDisplayNum;
    }
    public String getKeywords() {
        return(keywords);
    }
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}

