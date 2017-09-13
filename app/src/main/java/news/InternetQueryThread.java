package news;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by thinkpad on 2017/9/13.
 */

public class InternetQueryThread extends Thread {
    private String keywords;
    private int classTagId;
    private int page;
    private ArrayList<News> currentList;
    private int perLoadNum;

    public InternetQueryThread(String keywords, int classTagId, int page, ArrayList<News> currentList, int perLoadNum) {
        this.keywords = keywords;
        this.classTagId = classTagId;
        this.page = page;
        this.currentList = currentList;
        this.perLoadNum = perLoadNum;
    }

    private int actualRead = 0;

    public int getActualRead() {
        return actualRead;
    }

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
                    url = new URL("http://166.111.68.66:2042/news/action/query/search?keyword="+keywords+"&pageNo="+ page + "&pageSize=" + perLoadNum);
                else
                    url = new URL("http://166.111.68.66:2042/news/action/query/search?keyword="+keywords+"&pageNo="+ page + "&pageSize=" + perLoadNum + "&category=" + classTagId);
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
            DislikeList dislikeList = DislikeList.getInstance();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                News thisNews = new News(jsonObject);
                if (!dislikeList.checkDislike(thisNews.getTitle())) {
                    currentList.add(new News(jsonObject));
                    actualRead++;
                }
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