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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        SQLiteDatabase for_search = DatabaseHelper.getDbHelper().getReadableDatabase();
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
        SQLiteDatabase db = DatabaseHelper.getDbHelper().getReadableDatabase();
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
            newsDetail.setId(id);
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

        String newsId = newsDetail.getId();
        boolean alreadyExists = check(newsId);
        if (alreadyExists) return;
        SQLiteDatabase db = DatabaseHelper.getDbHelper().getWritableDatabase();

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
        db.close();
    }

    public boolean isFavorite(String id){
        SQLiteDatabase db = DatabaseHelper.getDbHelper().getReadableDatabase();
        Cursor cursor = db.query("favorite", new String[]{"id"}, "id=?", new String[]{id}, null, null, null);
        if (cursor.moveToNext()) return(true);
        return(false);
    }

    public void setFavorite(String id,boolean isfavorite_){
        SQLiteDatabase db = DatabaseHelper.getDbHelper().getWritableDatabase();
        Cursor cursor = db.query("favorite", new String[]{"id"}, "id=?", new String[]{id}, null, null, null);
        ContentValues values = new ContentValues();
        if (cursor.moveToNext()){
            db.delete("favorite","id=?",new String[]{id});
        }
        else {
            values.put("id", id);
            db.insert("favorite", null, values);
        }
    }

    public void saveNewsNLP(NewsNLP newsNLP) {
        SQLiteDatabase db = DatabaseHelper.getDbHelper().getWritableDatabase();
        HashMap<String,Double> scores = newsNLP.getScores();
        Iterator<HashMap.Entry<String,Double>> it = scores.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String,Double> thisEntry = it.next();
            String word = thisEntry.getKey();
            double score = thisEntry.getValue();
            Cursor cursor = db.query("NLP",new String[]{"word","score"},"word=?",new String[]{word},null,null,null);
            if (cursor.moveToNext()) {
                ContentValues values = new ContentValues();
                values.put("score",cursor.getDouble(cursor.getColumnIndex("score"))+score);
                db.update("NLP",values,"word=?",new String[]{word});
            }
            else {
                ContentValues values = new ContentValues();
                values.put("word",word);
                values.put("score",score);
                db.insert("NLP",null,values);
            }
        }
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
