package news;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/7.
 */

public class NewsDetail extends News {
    private String category;
    private String content;
    private String journal;
    private ArrayList<String> picturesLocal;
    public String getCategory() {return category;}
    public String getContent() {return content;}
    public String getJournal() {return  journal;}
    public ArrayList<String> getPicturesLocal() {return picturesLocal;}
    public void setCategory(String category_) {category = category_;}
    public void setContent(String content_) {content = content_;}
    public void setJournal(String journal_) {journal = journal_;}
    public void setPicturesLocal(ArrayList<String> picturesLocal_) {picturesLocal = picturesLocal_;}
    NewsDetail() {}
    public static NewsDetail getNewsDetailById(final String id) {
        if (NewsDatabase.getInstance().check(id)) {
            return NewsDatabase.getInstance().getNewsDetailById(id);
        } else { //grab it from internet and save it to datebase
            final NewsDetail newsDetail = new NewsDetail();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://166.111.68.66:2042/news/action/query/detail?newsId=" + id);
                        InputStream is = url.openStream();
                        StringBuffer out = new StringBuffer();
                        byte[] b = new byte[4096];
                        for (int n; (n = is.read(b)) != -1;) {
                            out.append(new String(b, 0, n));
                        }
                        String json = out.toString();
                        JSONObject jsonObject = new JSONObject(json);

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
            NewsDatabase.getInstance().saveNewsDetail(newsDetail);
            return newsDetail;
        }

    }

}
