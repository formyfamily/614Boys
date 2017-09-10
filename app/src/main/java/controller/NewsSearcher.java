package controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import news.News;

/**
 * Created by thinkpad on 2017/9/9.
 */

public class NewsSearcher {
    private static NewsSearcher searcher = null;
    private NewsSearcher(){}
    public static synchronized NewsSearcher getInstance() {
        if (searcher == null){
            searcher = new NewsSearcher();
        }
        return(searcher);
    }
    public ArrayList<News> search(int classTag,String keywords) {
        ArrayList<News> results = new ArrayList<News>();
        String urlStr = "http://166.111.68.66:2042/news/action/query/search?keywords="+keywords+"pageSize=20&pageNo=1";
        try {
            URL url = new URL();
        }
        catch (Exception e) {

        }
        finally {
            return(results);
        }
    }
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
                    if (classTagId == 0)
                        url = new URL("http://166.111.68.66:2042/news/action/query/latest?pageNo=" + page + "&pageSize=" + perLoadNum);
                    else url = new URL("http://166.111.68.66:2042/news/action/query/latest?pageNo=" + page + "&pageSize=" + perLoadNum + "&category=" + classTagId);
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