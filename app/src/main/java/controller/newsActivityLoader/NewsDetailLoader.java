package controller.newsActivityLoader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.CancellationSignal;

import news.NewsDetail;

/**
 * Created by kzf on 2017/9/14.
 */
public class NewsDetailLoader extends AsyncTaskLoader<NewsDetail> {
    CancellationSignal mCancellationSignal ;
    private NewsDetail mNewsDetail ;
    Context mContext ;
    String newsId ;

    public NewsDetailLoader(Context context) {super(context); mContext = context ;}

    @Override
    public void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Override
    public NewsDetail loadInBackground() {
        //if(isLoadInBackgroundCanceled())
        //    throw new OperationCanceledException();
        if(mNewsDetail == null)
            mNewsDetail = NewsDetail.getNewsDetailById(newsId) ;
        return mNewsDetail ;
    }
}