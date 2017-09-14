package controller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeSet;

import news.DatabaseHelper;
import news.InternetQueryThread;
import news.News;

/**
 * Created by thinkpad on 2017/9/9.
 */

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

    final int keywordsNumber = 12;
    final int perLoadNumber = 4;
    final double probabilityThreshold = 0.5;
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
            if (Math.random()>probabilityThreshold) continue;
            InternetQueryThread thread = new InternetQueryThread(keywords.get(i),0,1,newses,perLoadNumber);
            thread.start();
            try {
                thread.join();
            }
            catch(Exception e){
                return(newses);
            }
        }
        newses = deduplicate(newses);
        Collections.shuffle(newses);
        return(newses);
    }

    private ArrayList<News> deduplicate(ArrayList<News> newses){
        int size = newses.size();
        ArrayList<News> result = new ArrayList<News>();
        for (int i=0;i<size;i++){
            boolean repeat = false;
            News thisNews = newses.get(i);
            for (int j=0;j<i;j++){
                if (TextHelper.sameNews(thisNews.getTitle(),newses.get(j).getTitle())){
                    repeat = true;
                    break;
                }
            }
            if (repeat == false){
                result.add(thisNews);
            }
        }
        return(result);
    }
}
