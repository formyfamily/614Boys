package news;

/**
 * Created by Administrator on 2017/9/5.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class News implements Serializable {

    private String langType;
    private String classTag;
    private int classTagId;
    private String author;
    private String id;
    private ArrayList<String> pictures;
    private String source;
    private String time;
    private String title;
    private String url;
    private String video;
    private String intro;
    private Bitmap firstPicture;
    private static HashMap<String, Integer> classTagIdMap = null;
    private void classTagIdMapInit() {
        if (classTagIdMap == null) {
            classTagIdMap = new HashMap<String, Integer>();
            classTagIdMap.put("科技", 1);
            classTagIdMap.put("教育", 2);
            classTagIdMap.put("军事", 3);
            classTagIdMap.put("国内", 4);
            classTagIdMap.put("社会", 5);
            classTagIdMap.put("文化", 6);
            classTagIdMap.put("汽车", 7);
            classTagIdMap.put("国际", 8);
            classTagIdMap.put("体育", 9);
            classTagIdMap.put("财经", 10);
            classTagIdMap.put("健康", 11);
            classTagIdMap.put("娱乐", 12);
        }
    }
    final public static String[] classIdTagArray = {"全部新闻", "科技", "教育", "军事", "国内", "社会", "文化", "汽车", "国际", "体育", "财经", "健康", "娱乐"} ;
    public String getLangType() {return langType;}
    public String getClassTag() {return classTag;}
    public int getClassTagId() {return classTagId;}
    public String getAuthor() {return author;}
    public String getId() {return id;}
    public ArrayList<String> getPictures() {return pictures;}
    public String getSource() {return source;}
    public String getTime() {return time;}
    public String getTitle() {return title;}
    public String getUrl() {return  url;}
    public String getVideo() {return video;}
    public String getIntro() {return intro;}
    public void setLangType(final String langType_) {langType = langType_;}
    public void setClassTag(final String classTag_) {
        classTagIdMapInit();
        classTag = classTag_;
        if (classTagIdMap.containsKey(classTag))
            classTagId = classTagIdMap.get(classTag);
        else
            classTagId = 0;
    }
    public void setAuthor(String author_) {author = author_;}
    public void setId(String id_) {id = id_;}
    public void setPictures(ArrayList<String> pictures_) {pictures = pictures_;}
    public void setSource(String source_) {source = source_;}
    public void setTime(String time_) {time = time_;}
    public void setTitle(String title_) {title = title_;}
    public void setUrl(String url_) {url = url_;}
    public void setVideo(String video_) {video = video_;}
    public void setIntro(String intro_) {intro = intro_;}
    public News() {}
    public News(JSONObject jsonObject){
        classTagIdMapInit();
        try {
            langType = jsonObject.getString("lang_Type");
            classTag = jsonObject.getString("newsClassTag");
            //System.out.println("classTagIdMap : " + classTagIdMap);
            //System.out.println("classTag : " + classTag);
            if (classTagIdMap.containsKey(classTag))
                classTagId = classTagIdMap.get(classTag);
            else
                classTagId = 0;
            author = jsonObject.getString("news_Author");
            id = jsonObject.getString("news_ID");
            String pics = jsonObject.getString("news_Pictures");
            String[] picses = pics.split("[;\\s]");
            pictures = new ArrayList<String>();
            if (pics.equals(""))
                picses = new String[0];
            for (String pic : picses) {
                pictures.add(pic);
            }
            source = jsonObject.getString("news_Source");
            time = jsonObject.getString("news_Time");
            title = jsonObject.getString("news_Title");
            url = jsonObject.getString("news_URL");
            video = jsonObject.getString("news_Video");
            if (video == "")
                video = null;
            intro = jsonObject.getString("news_Intro");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String toString() {
        String out = new String();
        //out = out +
        return "News_toString()";
    }
    public boolean isRead() {
        return NewsDatabase.getInstance().check(id);
    }
    public boolean isFavorite() {
        return NewsDatabase.getInstance().isFavorite(id);
    }
    public void setFavorite(boolean isFavorite_) {
        NewsDatabase.getInstance().setFavorite(id, isFavorite_);
    }
    public Bitmap getFirstPicture() {
        try {
            Bitmap bitmap;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(pictures.get(0));
                        //打开URL对应的资源输入流
                        InputStream is = url.openStream();
                        Bitmap originBitmap = BitmapFactory.decodeStream(is);
                        float width = originBitmap.getWidth();
                        float height = originBitmap.getHeight();
                        // 创建操作图片用的matrix对象
                        Matrix matrix = new Matrix();
                        // 计算宽高缩放率
                        float scaleWidth = ((float) 100) / width;
                        float scaleHeight = ((float) 100) / height;
                        // 缩放图片动作
                        matrix.postScale(scaleWidth, scaleHeight);
                        firstPicture = Bitmap.createBitmap(originBitmap, 0, 0, (int) width,
                                (int) height, matrix, true);
                    } catch (Exception e) {
                        firstPicture = NewsProxy.getInstance().notFoundBitmap;
                    }
                }
            };
            thread.start();
            thread.join();
            return firstPicture;
        } catch (Exception e) {
            return NewsProxy.getInstance().notFoundBitmap;
        }
    }
}
