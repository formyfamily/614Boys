package controller.newsActivityLoader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.CancellationSignal;

import java.util.ArrayList;

import news.News;

/**
 * Created by kzf on 2017/9/14.
 */
public class NewsRecommendLoader extends AsyncTaskLoader<ArrayList<News>> {
    CancellationSignal mCancellationSignal ;
    private ArrayList<News> recommendNews ;
    Context mContext ;
    News mNews ;

    public NewsRecommendLoader(Context context, News news) {
        super(context);
        mContext = context ;
        mNews = news ;
    }

    @Override
    public void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Override
    public ArrayList<News> loadInBackground() {
        //if(isLoadInBackgroundCanceled())
        //    throw new OperationCanceledException();
        if(recommendNews == null)
            recommendNews = mNews.getRelatedNews() ;
        return recommendNews ;
    }
}

