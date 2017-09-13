package controller.newsActivityLoader;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;

import activity.NewsActivity;
import news.NewsDetail;

/**
 * Created by kzf on 2017/9/14.
 */

public class NewsDetailLoaderCallBack implements LoaderManager.LoaderCallbacks<NewsDetail> {
    Context mContext ;

    public NewsDetailLoaderCallBack(Context context)
    {
        super() ;
        mContext = context ;
    }
    @Override
    public Loader<NewsDetail> onCreateLoader(int id, Bundle args) {
        NewsDetailLoader newsDetailLoader = new NewsDetailLoader(mContext) ;
        newsDetailLoader.newsId = args.getString("newsId") ;
        return newsDetailLoader ;
    }
    @Override
    public void onLoadFinished(Loader<NewsDetail> loader, NewsDetail data)
    {
        ((NewsActivity)mContext).onNewsDetailLoadingFinished(data);
    }
    @Override
    public void onLoaderReset(Loader<NewsDetail> loader)
    {
    }

}