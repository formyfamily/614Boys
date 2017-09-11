package activity;

import fragment.taglistview.NewsImageAdapter;
import controller.NewsFavourite;
import controller.NewsReciter;
import controller.NewsSharer;
import news.* ;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by kzf on 2017/9/6.
 */

public class NewsActivity extends AppCompatActivity {
    NewsDetail newsDetail ;
    CollapsingToolbarLayout collapsingToolbar ;
    com.github.clans.fab.FloatingActionButton favouriteButton ;

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

        /*fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        TextView titleView = (TextView)findViewById(R.id.news_title) ;
        titleView.setText(newsDetail.getTitle());
        TextView textView = (TextView)findViewById(R.id.news_text) ;
        textView.setText(newsDetail.getContent());
        TextView infoView = (TextView)findViewById(R.id.news_info) ;
        String dateString = newsDetail.getTime() ;
        infoView.setText(newsDetail.getSource()+"   "+dateString.substring(0, 4)+'-'+dateString.substring(4, 6)+'-'+dateString.substring(6, 8));

        ListView pictureListView = (ListView)findViewById(R.id.news_picture_listview) ;
        pictureListView.setAdapter(new NewsImageAdapter(NewsActivity.this, newsDetail));

        favouriteButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_favourite) ;
        setFavouriteButtonState(newsDetail.isFavorite());
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFavourite.getInstance().favouriteNews(NewsActivity.this, newsDetail);
                setFavouriteButtonState(newsDetail.isFavorite());
            }
        });

        final com.github.clans.fab.FloatingActionButton reciteButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_voice) ;
        reciteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                NewsReciter reciter1 = NewsReciter.getInstance();
                if (reciter1.hasStarted) {
                    reciter1.stopSpeaking();
                    reciteButton.setLabelText("语音播放");
                }
                else {
                    reciter1.speakText(newsDetail.getTitle()+'\n'+newsDetail.getContent());
                    reciteButton.setLabelText("停止播放");
                }
                reciter1.hasStarted = !reciter1.hasStarted;
            }
        });
        final com.github.clans.fab.FloatingActionButton shareButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_share) ;
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