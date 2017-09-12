package controller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import news.DatabaseHelper;
import news.News;

/**
 * Created by thinkpad on 2017/9/9.
 */

class InternetQueryThread2 extends Thread {
    private String keyword;
    private final int number = 2;
    private ArrayList<News> newses;

    public InternetQueryThread2(String keyword,ArrayList<News> newses) {
        this.keyword = keyword;
        this.newses = newses;
    }


    @Override
    public void run() {
        try {
            URL url;
            url = new URL("http://166.111.68.66:2042/news/action/query/search?keyword="+keyword+"&pageNo="+ 1 + "&pageSize=" + number);
            InputStream is = url.openStream();
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1;) {
                out.append(new String(b, 0, n));
            }
            String json = out.toString();
            JSONObject jsonObject1 = new JSONObject(json);
            JSONArray jsonArray = jsonObject1.getJSONArray("list");
            ArrayList<News> list = new ArrayList<News>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                newses.add(new News(jsonObject));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
};

class WordAndScore implements Comparable<WordAndScore>{
    String word;
    double score;
    public WordAndScore(String word,double score) {
        this.word = word;
        this.score = score;
    }
    @Override
    public int compareTo(WordAndScore other) {   // Reversed order
        double diff = score-other.score;
        if (diff>0) return(-1);
        if (diff<0) return(1);
        return(0);
    }
}

public class NewsRecommender {

    public static NewsRecommender newsRecommender = new NewsRecommender() ;
    private NewsRecommender() {}
    public static NewsRecommender getInstance() {return newsRecommender ;}

    final int keywordsNumber = 10;
    private ArrayList<String> getRecommendedWords() {
        SQLiteDatabase db = DatabaseHelper.getDbHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from NLP",null);
        TreeSet<WordAndScore> nlpData = new TreeSet<WordAndScore>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String word = cursor.getString(cursor.getColumnIndex("word"));
                Double score = cursor.getDouble(cursor.getColumnIndex("score"));
                WordAndScore was = new WordAndScore(word,score);
                nlpData.add(was);
                cursor.moveToNext();
            }
        }
        ArrayList<String> keywords = new ArrayList<String>();
        Iterator<WordAndScore> it = nlpData.iterator();
        for (int i=0;i<keywordsNumber;i++) {
            if (it.hasNext()) {
                keywords.add(it.next().word);
            }
            else break;
        }
        return(keywords);
    }
    public ArrayList<News> getRecommendedNews(){
        ArrayList<String> keywords = getRecommendedWords();
        ArrayList<News> newses = new ArrayList<News>();
        if (keywords.isEmpty()) return(newses);
        for (int i=0;i<keywordsNumber;i++) {
            InternetQueryThread2 thread = new InternetQueryThread2(keywords.get(i),newses);
            thread.start();
            try {
                thread.join();
            }
            catch(Exception e){
                return(newses);
            }
        }
        return(newses);
    }
}
