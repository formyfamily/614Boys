package activity;

import controller.NewsFavourite;
import news.* ;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by kzf on 2017/9/6.
 */

public class NewsActivity extends AppCompatActivity {
    NewsDetail newsDetail ;
    static CollapsingToolbarLayout collapsingToolbar ;
    static FloatingActionButton fab ;

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
        infoView.setText(newsDetail.getSource()+"   "+newsDetail.getTime());

        com.github.clans.fab.FloatingActionButton favouriteButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_favourite) ;
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFavourite.getInstance().favouriteNews(NewsActivity.this, newsDetail);
            }
        });
    }
}