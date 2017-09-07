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
    private int size;
    private int displaySize;
    private ArrayList<News> newsAll;
    private NewsProxy() {}
    public static synchronized NewsProxy getInstance() {
        if (newsProxy == null) {
            newsProxy = new NewsProxy();
            newsProxy.size = 500;
            newsProxy.displaySize = 20;
            newsProxy.update();
            //newsProxy.tryUrl();
        }
        return newsProxy;
    }
    public int getDisplaySize() {
        return  displaySize;
    }
    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
    }
    public ArrayList<News> getDisplayNews() {
        ArrayList<News> sublist = new ArrayList<News>();
        for (int i = 0; i < displaySize; i++)
            sublist.add(newsAll.get(i));
        return sublist;

        // It's too complicated

    }
    private synchronized void addNewsOfPage(int page_) {
        System.out.println("addNewsOfPage " + page_ );
        final int page = page_;
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://166.111.68.66:2042/news/action/query/latest?pageNo=" + page + "&pageSize=500");
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
                        newsAll.add(new News(jsonObject));
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
    public void update() {
        /**
         update the newslist to the newest size news
         */
        Log.d("NewsProxy","update()!!!!");
        if (newsAll != null)
            newsAll.clear();
        else newsAll = new ArrayList<News>();
        for (int i = 0; i < size; i += 500) {
            addNewsOfPage(i / 500 + 1);
        }
    }
    public void moreNews(int num) {
        while (displaySize + num > size) {
            size += 500;
            addNewsOfPage(size / 500);
        }
        displaySize += num;
    }
}

