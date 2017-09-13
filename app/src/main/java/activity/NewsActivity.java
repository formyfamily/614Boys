package activity;

import fragment.main_newsrecycle.NewsAdapter;
import fragment.news_listview.NewsImageAdapter;
import controller.NewsFavourite;
import controller.NewsReciter;
import controller.NewsSharer;
import controller.NewsRecommender;
import controller.newsActivityLoader.*;
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

import com.bigkoo.svprogresshud.SVProgressHUD;

import java.util.ArrayList;

/**
 * Created by kzf on 2017/9/6.
 */


public class NewsActivity extends AppCompatActivity   {

    public static News news ;
    NewsDetail newsDetail ;
    NewsImageAdapter newsImageAdapter ;
    RecyclerView pictureListView ;
    CollapsingToolbarLayout collapsingToolbar ;
    com.github.clans.fab.FloatingActionButton favouriteButton ;
    com.github.clans.fab.FloatingActionButton reciteButton ;
    com.github.clans.fab.FloatingActionButton shareButton ;
    com.github.clans.fab.FloatingActionButton dislikeButton ;
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
        }
        else {
            favouriteButton.setLabelText("取消收藏");
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
        TextView journalistView = (TextView)findViewById(R.id.news_journalist) ;
        journalistView.setText(newsDetail.getJournal());


        pictureListView = (RecyclerView)findViewById(R.id.news_picture_recycleview) ;
        pictureListView.setLayoutManager(new LinearLayoutManager(this)) ;
        newsImageAdapter = new NewsImageAdapter(NewsActivity.this, newsDetail) ;
        if(newsImageAdapter.getItemCount() == 0)
        {
            ((TextView)findViewById(R.id.news_image_title)).setVisibility(View.GONE);
            ((ImageView)findViewById(R.id.news_image_divider)).setVisibility(View.GONE);
        }
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

        dislikeButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_dislike) ;
        if(DislikeList.getInstance().checkDislike(newsDetail.getTitle()))
            dislikeButton.setLabelText("恢复兴趣");
        else
            dislikeButton.setLabelText("不感兴趣");
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                if (dislikeButton.getLabelText().equals("不感兴趣")) {
                    dislikeButton.setLabelText("恢复兴趣");
                    new SVProgressHUD(NewsActivity.this).showInfoWithStatus("已屏蔽此条新闻");
                    DislikeList.getInstance().addDislike(newsDetail.getTitle());
                }
                else{
                    dislikeButton.setLabelText("不感兴趣");
                    new SVProgressHUD(NewsActivity.this).showInfoWithStatus("已恢复此条新闻");
                    DislikeList.getInstance().removeDislike(newsDetail.getTitle());
                }
            }
        });
    }

    public void onNewsRecommendLoadingFinished(ArrayList<News> newsList)
    {
        Toast.makeText(NewsActivity.this, "Load Recommend Complete", Toast.LENGTH_SHORT).show() ;
        RecyclerView recommendRecyclerView = (RecyclerView)findViewById(R.id.news_recommend_recycleview) ;
        NewsAdapter newsAdapter = new NewsAdapter(getParent(), newsList, 0) ;
        newsAdapter.resources = getResources() ;
        recommendRecyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
        recommendRecyclerView.setAdapter(newsAdapter);
    }
}