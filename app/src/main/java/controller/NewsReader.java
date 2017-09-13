package controller;

import android.content.Context;

import com.bigkoo.svprogresshud.SVProgressHUD;

import activity.MainActivity;
import activity.NewsActivity;
import fragment.main_newsrecycle.NewsAdapter;
import news.* ;
/**
 * Created by kzf on 2017/9/7.
 */

public class NewsReader {
    private NewsReader() {} ;
    private static NewsReader newsReader = new NewsReader() ;

    public static NewsReader getInstance() {return newsReader ;}
    public void readNews(final News news, Context mContext, final NewsAdapter newsAdapter)
    {
        newsAdapter.notifyDataSetChanged();
        if(mContext == null)
            mContext = MainActivity.mContext ;
        NewsActivity.news = news ;
        ((MainActivity) mContext).createNewNewsActivity(news);
    }
}
