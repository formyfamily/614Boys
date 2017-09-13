package activity;

import fragment.main_newsrecycle.NewsAdapter;
import fragment.news_listview.NewsImageAdapter;
import controller.NewsFavourite;
import controller.NewsReciter;
import controller.NewsSharer;
import controller.NewsRecommender;
import news.* ;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kzf on 2017/9/6.
 */

class NewsDetailLoader extends AsyncTaskLoader<NewsDetail> {
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
class NewsRecommendLoader extends AsyncTaskLoader<ArrayList<News>> {
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

class NewsDetailLoaderCallBack implements LoaderManager.LoaderCallbacks<NewsDetail> {
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
class NewsRecommendLoadingCallback implements LoaderManager.LoaderCallbacks< ArrayList<News> > {
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

public class NewsActivity extends AppCompatActivity   {

    public static News news ;
    NewsDetail newsDetail ;
    NewsImageAdapter newsImageAdapter ;
    RecyclerView pictureListView ;
    CollapsingToolbarLayout collapsingToolbar ;
    com.github.clans.fab.FloatingActionButton favouriteButton ;
    com.github.clans.fab.FloatingActionButton reciteButton ;
    com.github.clans.fab.FloatingActionButton shareButton ;
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Bundle bundle = new Bundle() ;
        bundle.putString("newsId", getIntent().getStringExtra("News"));
        getLoaderManager().initLoader(0, bundle, new NewsDetailLoaderCallBack(this)) ;
        getLoaderManager().initLoader(1, bundle, new NewsRecommendLoadingCallback(this, news)) ;

        Toolbar toolbar = (Toolbar) findViewById(R.id.news_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(news.getTitle());
        TextView titleView = (TextView)findViewById(R.id.news_title) ;
        titleView.setText(news.getTitle());

        TextView infoView = (TextView)findViewById(R.id.news_info) ;
        String dateString = news.getTime() ;
        infoView.setText(news.getSource() + "    " + dateString) ;
    }
    void setFavouriteButtonState(boolean is_favourite)
    {
        if(!is_favourite) {
            favouriteButton.setLabelText("收藏新闻");
            favouriteButton.setColorNormal(getResources().getColor(R.color.colorFavourite));
            favouriteButton.setColorPressed(getResources().getColor(R.color.colorFavoriteLight));
        }
        else {
            favouriteButton.setLabelText("取消收藏");
            favouriteButton.setColorNormal(getResources().getColor(R.color.colorFavourite));
            favouriteButton.setColorPressed(getResources().getColor(R.color.colorFavoriteLight));
        }
    }

    public void onNewsDetailLoadingFinished(NewsDetail newsDetail_)
    {
        Toast.makeText(NewsActivity.this, "Load Complete", Toast.LENGTH_SHORT).show() ;
        newsDetail = newsDetail_ ;
        if(newsDetail == null) {
            Log.println(Log.INFO, "", "Wrong! Null");
        }

        ImageView titleImage = (ImageView)findViewById(R.id.news_firstimage) ;
        try {
            titleImage.setImageBitmap(newsDetail.getPictureList().get(0));
        } catch (Exception e) {}

        TextView textView = (TextView)findViewById(R.id.news_text) ;
        textView.setText(Html.fromHtml(newsDetail.getContent()));
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        textView.setLayoutParams(layoutParams);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setClickable(true);


        pictureListView = (RecyclerView)findViewById(R.id.news_picture_recycleview) ;
        pictureListView.setLayoutManager(new LinearLayoutManager(this)) ;
        newsImageAdapter = new NewsImageAdapter(NewsActivity.this, newsDetail) ;
        pictureListView.setAdapter(newsImageAdapter);

        favouriteButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_favourite) ;
        setFavouriteButtonState(newsDetail.isFavorite());
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFavourite.getInstance().favouriteNews(NewsActivity.this, newsDetail);
                setFavouriteButtonState(newsDetail.isFavorite());
            }
        });

        reciteButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_voice) ;
        reciteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                NewsReciter reciter1 = NewsReciter.getInstance();
                if (reciter1.hasStarted) {
                    reciter1.stopSpeaking();
                    reciteButton.setLabelText("语音播放");
                }
                else {
                    reciter1.speakText(newsDetail.getTitle()+'\n'+newsDetail.getRealContent());
                    reciteButton.setLabelText("停止播放");
                }
                reciter1.hasStarted = !reciter1.hasStarted;
            }
        });
        shareButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_share) ;
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                NewsSharer.getInstance().shareNews(NewsActivity.this, newsDetail) ;
            }
        });
    }

    public void onNewsRecommendLoadingFinished(ArrayList<News> newsList)
    {
        Toast.makeText(NewsActivity.this, "Load Recommend Complete", Toast.LENGTH_SHORT).show() ;
        RecyclerView recommendRecyclerView = (RecyclerView)findViewById(R.id.news_recommend_recycleview) ;
        NewsAdapter newsAdapter = new NewsAdapter(NewsActivity.this, newsList, 0) ;
        newsAdapter.resources = getResources() ;
        recommendRecyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
        recommendRecyclerView.setAdapter(newsAdapter);
    }
}