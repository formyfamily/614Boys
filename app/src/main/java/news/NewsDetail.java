package news;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/9/7.
 */

class InternetQueryThread3 extends Thread {
    private NewsDetail newsDetail;
    private NewsNLP newsNLP;
    private Activity thisActivity;
    private String id;
    private boolean success = false;
    public InternetQueryThread3(NewsDetail newsDetail, NewsNLP newsNLP, String id,Activity thisActivity){
        this.newsDetail = newsDetail;
        this.newsNLP = newsNLP;
        this.id = id;
        this.thisActivity = thisActivity;
    }
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
            NewsDetail.setThisActivity(thisActivity);
            newsNLP.setByJsonObject(jsonObject);
            newsDetail.setByJsonObject(jsonObject);
            success = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public boolean getSuccess(){return success;}
};

public class NewsDetail extends News {
    private String category;
    private String content;
    private String journal;
    private ArrayList<String> picturesLocal;
    private static Activity thisActivity;
    public static void setThisActivity(Activity activity) {thisActivity = activity;}
    public String getCategory() {return category;}
    public String getContent() {return content;}
    public String getJournal() {return  journal;}
    public ArrayList<String> getPicturesLocal() {return picturesLocal;}
    public void setCategory(String category_) {category = category_;}
    public void setContent(String content_) {content = content_;}
    public void setJournal(String journal_) {journal = journal_;}
    public void setPicturesLocal(ArrayList<String> picturesLocal_) {picturesLocal = picturesLocal_;}
    NewsDetail() {}
    public void setByJsonObject(JSONObject jsonObject) {
        try {
            setClassTag(jsonObject.getString("newsClassTag"));
            setAuthor(jsonObject.getString("news_Author"));
            setCategory(jsonObject.getString("news_Category"));
            String content = jsonObject.getString("news_Content");
            Pattern p = Pattern.compile("[。？！\\.\\?\\!…][\\s]+");
            Matcher m = p.matcher(content);
            int cnt = 0, cur = 0;
            String newContent = new String();
            while(m.find()) {
                cnt++;
                System.out.println("Match number " + cnt);
                System.out.println("start(): " + m.start());
                System.out.println("end(): " + m.end());
                System.out.println("group(): " + m.group());
                newContent = newContent + content.substring(cur, m.start()) + content.substring(m.start(),m.start() + 1) + "<\\br> <\\br>&nbsp;&nbsp;&nbsp;&nbsp;";
                cur = m.end();
            }
            newContent = newContent + content.substring(cur, content.length()) + '\n';
            if (jsonObject.has("persons")) {
                JSONArray persons = jsonObject.getJSONArray("persons");
                for (int i = 0; i < persons.length(); i++) {
                    JSONObject person = persons.getJSONObject(i);
                    String word = person.getString("word");
                    System.out.println("word:" + word);
                    Pattern aPattern = Pattern.compile(word);
                    Matcher aMatcher = aPattern.matcher(newContent);
                    newContent =aMatcher.replaceAll("<a href = \'https://baike.baidu.com/item/" + word + "\'>" + word + "<\\a>");
                    System.out.println(newContent);
                }
            }
            if (jsonObject.has("organizations")) {
                JSONArray organizations = jsonObject.getJSONArray("organizations");
                for (int i = 0; i < organizations.length(); i++) {
                    JSONObject organization = organizations.getJSONObject(i);
                    String word = organization.getString("word");
                    System.out.println("word:" + word);
                    Pattern aPattern = Pattern.compile(word);
                    Matcher aMatcher = aPattern.matcher(newContent);
                    newContent = new String(aMatcher.replaceAll("<a href = \'https://baike.baidu.com/item/" + word + "\'>" + word + "<\\a>"));
                }
            }
            if (jsonObject.has("locations")) {
                JSONArray locations = jsonObject.getJSONArray("locations");
                for (int i = 0; i < locations.length(); i++) {
                    JSONObject location = locations.getJSONObject(i);
                    String word = location.getString("word");
                    System.out.println("word:" + word);
                    Pattern aPattern = Pattern.compile(word);
                    Matcher aMatcher = aPattern.matcher(newContent);
                    newContent = new String(aMatcher.replaceAll("<a href = \'https://baike.baidu.com/item/" + word + "\'>" + word + "<\\a>"));
                }
            }
            setContent(newContent);

            setId(jsonObject.getString("news_ID"));
            setJournal(jsonObject.getString("news_Journal"));
            setSource(jsonObject.getString("news_Source"));
            String timeGet = jsonObject.getString("news_Time");
            //timeGet = timeGet.substring(0, 4) + "-" + timeGet.substring(4, 6) + "-" + timeGet.substring(6, 8);
            setTime(timeGet);

            setTitle(jsonObject.getString("news_Title"));

            setUrl(jsonObject.getString("news_URL"));
            if (jsonObject.getString("news_Video").equals(""))
                setVideo(null);
            else setVideo(jsonObject.getString("news_Video"));
            String pics = jsonObject.getString("news_Pictures");
            String[] picses = pics.split("[;\\s]");

            setPictures(new ArrayList<String>());
            setPicturesLocal(new ArrayList<String>());
            ArrayList<String> pictures = getPictures();
            JSONArray wordScoreArray = jsonObject.getJSONArray("Keywords");
            if (pics.equals("")) {                                                     // Search pictures online when no picture is provided
                ArrayList<String> pictureSearchKeywords = new ArrayList<String>();
                for (int i = 0; i < Math.min(3, wordScoreArray.length()); i++) {
                    JSONObject jsonObject1 = wordScoreArray.getJSONObject(i);
                    pictureSearchKeywords.add(jsonObject1.getString("word"));
                }
                ArrayList<String> searchPictureResults = PictureSearcher.getInstance().addPictureToNews(pictureSearchKeywords);
                picses = new String[searchPictureResults.size()];
                for (int i=0;i<searchPictureResults.size();i++) picses[i] = searchPictureResults.get(i);
                //picses = setPictures(pictureSearchResults);
            }
            int count = 0;
            for (String pic : picses) {
                count ++;
                pictures.add(pic);
                if (getNoPictureMode()) continue;
                final String urlStr = pic;
                final String id = getId();
                DownloadPictureThread thread = new DownloadPictureThread(urlStr,thisActivity,getPicturesLocal(),id,count);
                try {
                    thread.start();
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }// It's too complicated

            /*
            save pics to local and save paths to picturesLocal
             */

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;
    }
    public static NewsDetail getNewsDetailById(final String id) {
        if (NewsDatabase.getInstance().check(id)) {
            return NewsDatabase.getInstance().getNewsDetailById(id);
        } else { //grab it from internet and save it to datebase
            final NewsDetail newsDetail = new NewsDetail();
            final NewsNLP newsNLP = new NewsNLP();
            InternetQueryThread3 thread = new InternetQueryThread3(newsDetail,newsNLP,id,thisActivity);
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            if (thread.getSuccess() == false) return(null);
            NewsDatabase.getInstance().saveNewsDetail(newsDetail);
            NewsDatabase.getInstance().saveNewsNLP(newsNLP);
            return newsDetail;
        }
    }
    public ArrayList<Bitmap> getPictureList() {
        ArrayList<Bitmap> pictureList = new ArrayList<Bitmap>();
        try {
            for (String localUrl : picturesLocal) {
                FileInputStream fis = new FileInputStream(localUrl);
                Bitmap bitmap  = BitmapFactory.decodeStream(fis);
                pictureList.add(bitmap);
            }
            return  pictureList;
        }catch (Exception e) {
            return pictureList;
        }
    }
}
