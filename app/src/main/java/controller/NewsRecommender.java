package controller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import news.DatabaseHelper;

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
    final int keywordsNumber = 10;
    public ArrayList<String> getRecommendedWords() {
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
}
