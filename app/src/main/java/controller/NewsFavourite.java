package controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bigkoo.svprogresshud.SVProgressHUD;

import java.util.ArrayList;

import news.DatabaseHelper;
import news.News;
import news.NewsDatabase;

/**
 * Created by kzf on 2017/9/8.
 */

public class NewsFavourite {

    private NewsFavourite() {} ;
    private static NewsFavourite newsFavourite = new NewsFavourite() ;
    public static synchronized NewsFavourite getInstance() {return newsFavourite ;}

    public void favouriteNews(Context mContext, News news)
    {
        if(news.isFavorite()) {
            news.setFavorite(false);
            new SVProgressHUD(mContext).showInfoWithStatus("已取消收藏");
        }
        else {
            news.setFavorite(true);
            new SVProgressHUD(mContext).showInfoWithStatus("收藏成功！");
        }
    }

    public ArrayList<News> getAllFavoriteNews(){
        ArrayList<News> newses = NewsDatabase.getInstance().getAllFavorite();
        return newses ;
    }
}
