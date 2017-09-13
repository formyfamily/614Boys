package activity;

import fragment.news_listview.NewsImageAdapter;
import controller.NewsFavourite;
import controller.NewsReciter;
import controller.NewsSharer;
import controller.NewsRecommender;
import news.* ;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;

/**
 * Created by kzf on 2017/9/6.
 */

public class NewsActivity extends AppCompatActivity {
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

        newsDetail = NewsDetail.getNewsDetailById(getIntent().getStringExtra("News")) ;
        if(newsDetail == null) {
            Log.println(Log.INFO, "", "Wrong! Null");
        }
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
        collapsingToolbar.setTitle(newsDetail.getTitle());
        ImageView titleImage = (ImageView)findViewById(R.id.news_firstimage) ;
        try {
            titleImage.setImageBitmap(newsDetail.getPictureList().get(0));
        } catch (Exception e) {}

        TextView titleView = (TextView)findViewById(R.id.news_title) ;
        titleView.setText(newsDetail.getTitle());
        TextView textView = (TextView)findViewById(R.id.news_text) ;
        textView.setText(Html.fromHtml(newsDetail.getContent()));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setClickable(true);
        TextView infoView = (TextView)findViewById(R.id.news_info) ;
        String dateString = newsDetail.getTime() ;
        infoView.setText(newsDetail.getSource() + "    " + dateString) ;

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
}