package controller.newsActivityLoader;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;

import java.util.ArrayList;

import activity.NewsActivity;
import news.News;

/**
 * Created by kzf on 2017/9/14.
 */

public class NewsRecommendLoadingCallback implements LoaderManager.LoaderCallbacks< ArrayList<News> > {
    Context mContext ;
    News mNews ;

    public NewsRecommendLoadingCallback(Context context, News news)
    {
        super() ;
        mNews = news ;
        mContext = context ;
    }
    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        NewsRecommendLoader newsRecommendLoader = new NewsRecommendLoader(mContext, mNews) ;
        return newsRecommendLoader ;
    }
    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data)
    {
        ((NewsActivity)mContext).onNewsRecommendLoadingFinished(data);
    }
    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader)
    {
    }

}
