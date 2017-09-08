package news;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
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
    private Activity thisActivity;
    private String splitStr=";,;";
    public static synchronized NewsDatabase getInstance() {
        if (newsDatabase == null) {
            newsDatabase = new NewsDatabase();
        }
        return newsDatabase;
    }
    public void setThisActivity(Activity activity) { thisActivity = activity;}
    public boolean check(String id) {
        DatabaseHelper dbHelper = new DatabaseHelper(thisActivity, "local.db");
        SQLiteDatabase for_search = dbHelper.getReadableDatabase();
        String[] argList=new String[1];
        argList[0]=id;
        Cursor cursor = for_search.query("newsHistory",new String[]{"id"},"id=?",argList,null,null,null);
        if (cursor.moveToNext()) return(true);
        else return(false);
    }
    public NewsDetail getNewsDetailById(String id) {
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
        DatabaseHelper dbHelper = new DatabaseHelper(thisActivity, "local.db");
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
            String[] picturesS = cursor.getString(cursor.getColumnIndex("pictures")).split(splitStr);  // ";,;" used to distinguish two items
            ArrayList<String> picturesA = new ArrayList<String>();
            for (int i=0;i<picturesS.length;i++) picturesA.add(picturesS[i]);
            newsDetail.setPictures(picturesA);
            String[] picturesLocalS = cursor.getString(cursor.getColumnIndex("picturesLocal")).split(splitStr);
            ArrayList<String> picturesLocalA = new ArrayList<String>();
            for (int i=0;i<picturesLocalS.length;i++) picturesLocalA.add(picturesLocalS[i]);
            newsDetail.setPicturesLocal(picturesLocalA);
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
    public void saveNewsDetail(NewsDetail newsDetail) {

        DatabaseHelper dbHelper = new DatabaseHelper(thisActivity, "local.db");
        String newsId = newsDetail.getId();
        boolean alreadyExists = check(newsId);
        if (alreadyExists) return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("category", newsDetail.getCategory());
        cv.put("content", newsDetail.getContent());
        cv.put("journal", newsDetail.getJournal());
        ArrayList<String> picturesA = newsDetail.getPictures();
        ArrayList<String> picturesB = newsDetail.getPicturesLocal();
        String picturesS = "";
        String picturesLocalS="";
        Iterator<String> it1 = picturesA.iterator();
        Iterator<String> it2 = picturesB.iterator();
        while (it1.hasNext()){
            String currentURL = it1.next();
            String currentURLLocal = it2.next();
            picturesS += currentURL + splitStr;
            picturesLocalS += currentURLLocal + splitStr;   // newsId is used to seperate images in different folders.
        }
        if (!picturesS.equals("")) {
            picturesS = picturesS.substring(0, picturesS.length() - 3);
            picturesLocalS = picturesLocalS.substring(0, picturesS.length() - 3);
        }
        cv.put("pictures", picturesS);
        cv.put("picturesLocal",picturesLocalS);
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

    public boolean isFavorite(String id){
        DatabaseHelper dbHelper = new DatabaseHelper(null, "local.db");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] argList = new String[1];
        argList[0] = id;
        Cursor cursor = db.query("favorite", new String[]{"id","isfavorite"}, "id=?", argList, null, null, null);
        if (cursor.moveToNext()) {
            int result = cursor.getColumnIndex("isfavorite");
            if (result==1) return(true);
            else return(false);
        }
        return(false);
    }

    public void setFavorite(String id,boolean isfavorite_){
        int isfavorite = 0;
        if (isfavorite_) isfavorite = 1;
        DatabaseHelper dbHelper = new DatabaseHelper(null, "local.db");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] argList = new String[1];
        argList[0] = id;
        Cursor cursor = db.query("favorite", new String[]{"id","isfavorite"}, "id=?", argList, null, null, null);
        ContentValues values = new ContentValues();
        values.put("isfavorite",isfavorite);
        if (cursor.moveToNext()){
            db.update("favorite",values,"id=?",argList);
        }
        values.put("id",id);
        db.insert("favorite",null,values);
    }
/*
    private String downloadPicture(final String urlStr,final String id){
        new Thread(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    //创建一个url对象
                    URL url=new URL(urlStr);
                    //打开URL对应的资源输入流
                    InputStream is= url.openStream();
                    //把InputStream转化成ByteArrayOutputStream
                    ByteArrayOutputStream baos =new ByteArrayOutputStream();
                    byte[] buffer =new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) > -1 ) {
                        baos.write(buffer, 0, len);
                    }
                    baos.flush();
                    is.close();//关闭输入流
                    //将ByteArrayOutputStream转化成InputStream
                    is=new ByteArrayInputStream(baos.toByteArray());
                    baos.close();
                    //打开手机文件对应的输出流
                    String imageName="bye.jpg";
                    File curDir = thisActivity.getExternalFilesDir(null);
                    File targetDir = new File(curDir, "image/"+id);
                    targetDir.mkdirs();
                    File targetFile = new File(targetDir,imageName);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    byte[]buff=new byte[1024];
                    int count=0;
                    //将URL对应的资源下载到本地
                    while ((count=is.read(buff))>0) {
                        fos.write(buff, 0, count);
                    }
                    fos.flush();
                    //关闭输入输出流
                    is.close();
                    fos.close();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }
    */
}
