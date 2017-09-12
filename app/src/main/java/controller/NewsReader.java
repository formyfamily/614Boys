package controller;

import android.content.Context;

import activity.MainActivity;
import fragment.main_newsrecycle.NewsAdapter;
import news.* ;
/**
 * Created by kzf on 2017/9/7.
 */

public class NewsReader {
    private NewsReader() {} ;
    private static NewsReader newsReader = new NewsReader() ;

    public static NewsReader getInstance() {return newsReader ;}
    public void readNews(final News news, final Context mContext, final NewsAdapter newsAdapter)
    {
        if (NewsDetail.getNewsDetailById(news.getId()) != null) {
            newsAdapter.notifyDataSetChanged();
            ((MainActivity) mContext).createNewNewsActivity(news);
        }
    }
}
