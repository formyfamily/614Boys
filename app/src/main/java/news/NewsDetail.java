package news;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
            setContent(jsonObject.getString("news_Content"));
            setId(jsonObject.getString("news_ID"));
            setJournal(jsonObject.getString("news_Journal"));
            setSource(jsonObject.getString("news_Source"));
            setTime(jsonObject.getString("news_Time"));
            setTitle(jsonObject.getString("news_Title"));
            setUrl(jsonObject.getString("news_URL"));
            if (jsonObject.getString("news_Video") == "")
                setVideo(null);
            else setVideo(jsonObject.getString("news_Video"));
            String pics = jsonObject.getString("news_Pictures");
            String[] picses = pics.split("[;\\s]");
            if (pics.equals(""))
                picses = new String[0];

            setPictures(new ArrayList<String>());
            setPicturesLocal(new ArrayList<String>());
            ArrayList<String> pictures = getPictures();
            int count = 0;
            for (String pic : picses) {
                count ++;
                final int count_ = count;
                pictures.add(pic);
                final String urlStr = pic;
                final String id = getId();
                Thread thread = new Thread(){
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
                            String imageName = count_ + ".jpg";
                            File curDir = thisActivity.getExternalFilesDir(null);
                            File targetDir = new File(curDir, "image/" + id);
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
                            getPicturesLocal().add(targetFile.getPath());
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                };
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
    }
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
                        NewsDetail.setThisActivity(thisActivity);
                        newsDetail.setByJsonObject(jsonObject);
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
