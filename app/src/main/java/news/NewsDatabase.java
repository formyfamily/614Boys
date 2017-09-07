package news;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Iterator;

import activity.MainActivity;

/**
 * Created by Administrator on 2017/9/7.
 */

public class NewsDatabase {
    private static NewsDatabase newsDatabase;
    private NewsDatabase() {}
    private String databasePath = "";
    public static synchronized NewsDatabase getInstance() {
        if (newsDatabase == null) {
            newsDatabase = new NewsDatabase();
        }
        return newsDatabase;
    }
    boolean check(String id) {
        DatabaseHelper dbHelper = new DatabaseHelper(null,"local.db");
        SQLiteDatabase for_search = dbHelper.getReadableDatabase();
        String[] argList=new String[1];
        argList[0]=id;
        Cursor cursor = for_search.query("newsHistory",new String[]{"id","classTagId","source","title"},"id=?",argList,null,null,null);
        if (cursor.moveToNext()) return(true);
        else return(false);
    }
    NewsDetail getNewsDetailById(String id) {
        if (!check(id))
            return null;
        NewsDetail newsDetail = new NewsDetail();
        /*
        newsDetail.setCategory("");
        newsDetail.setContent("");
        newsDetail.setJournal("");
        newsDetail.setPicturesLocal(new ArrayList<String>());
        newsDetail.setPictures(new ArrayList<String>());
        newsDetail.setAuthor("");
        newsDetail.setLangType("");
        newsDetail.setClassTag("");//have set classtagid inside
        newsDetail.setId(id);
        newsDetail.setIntro("");
        newsDetail.setSource("");
        newsDetail.setTime("");
        newsDetail.setTitle("");
        newsDetail.setUrl("");
        newsDetail.setVideo("");
        */
        DatabaseHelper dbHelper = new DatabaseHelper(null, "local.db");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] argList = new String[1];
        argList[0] = id;
        Cursor cursor = db.query("newsHistory", new String[]{"id","category","content","journal","picturesLocal",
        "pictures","author","langType","classTag","intro","source","time","title","url","video"}, "id=?", argList, null, null, null);
        if (cursor.moveToNext()){
            newsDetail.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            newsDetail.setContent (cursor.getString(cursor.getColumnIndex("content")));
            newsDetail.setJournal(cursor.getString(cursor.getColumnIndex("journal")));
            newsDetail.setPicturesLocal(new ArrayList<String>());
            //newsDetail.setPicturesLocal(cursor.getString(cursor.getColumnIndex("picturesLocal")));  // Unfinished
            String[] picturesS = cursor.getString(cursor.getColumnIndex("pictures")).split(";,;");
            ArrayList<String> picturesA = new ArrayList<String>();
            for (int i=0;i<picturesS.length;i++) picturesA.add(picturesS[i]);
            newsDetail.setPictures(picturesA);
            newsDetail.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            newsDetail.setLangType(cursor.getString(cursor.getColumnIndex("langType")));
            newsDetail.setClassTag(cursor.getString(cursor.getColumnIndex("classTag")));
            //newsDetail.set id(cursor.getString(cursor.getColumnIndex("")));
            newsDetail.setIntro(cursor.getString(cursor.getColumnIndex("intro")));
            newsDetail.setSource(cursor.getString(cursor.getColumnIndex("source")));
            newsDetail.setTime(cursor.getString(cursor.getColumnIndex("time")));
            newsDetail.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            newsDetail.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            newsDetail.setVideo(cursor.getString(cursor.getColumnIndex("video")));
            return newsDetail;
        }
        else return(null);
    }
    void saveNewsDetail(NewsDetail newsDetail) {

        DatabaseHelper dbHelper = new DatabaseHelper(null, "local.db");
        String curId = newsDetail.getId();
        boolean alreadyExists = check(curId);
        if (alreadyExists) return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("category", newsDetail.getCategory());
        cv.put("content", newsDetail.getContent());
        cv.put("journal", newsDetail.getJournal());
        cv.put("picturesLocal", newsDetail.getPicturesLocal().toString());         // Unfinished
        ArrayList<String> picturesA = newsDetail.getPictures();
        String picturesS = "";
        Iterator<String> it = picturesA.iterator();
        while (it.hasNext()){
            picturesS += it.next()+";,:";
        }
        if (!picturesS.equals("")) picturesS = picturesS.substring(0,picturesS.length()-3);
        cv.put("pictures", picturesS);
        cv.put("author", newsDetail.getAuthor());
        cv.put("langType", newsDetail.getLangType());
        cv.put("classTag", newsDetail.getClassTag());
        cv.put("id", newsDetail.getId());
        cv.put("intro", newsDetail.getIntro());
        cv.put("source", newsDetail.getSource());
        cv.put("time", newsDetail.getTime());
        cv.put("title", newsDetail.getTitle());
        cv.put("url", newsDetail.getUrl());
        cv.put("video", newsDetail.getVideo());
        db.insert("newsHistory", null, cv);
    }
}
