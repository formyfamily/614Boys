package news;

import java.util.ArrayList;

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
        return false;
    }
    NewsDetail getNewsDetailById(String id) {
        if (!check(id))
            return null;
        NewsDetail newsDetail = new NewsDetail();
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
        return newsDetail;
    }
    void saveNewsDetail(NewsDetail newsDetail) {
        return;
    }
}
