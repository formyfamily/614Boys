package controller;

import android.content.Context;

import com.bigkoo.svprogresshud.SVProgressHUD;

import news.News;

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
}
