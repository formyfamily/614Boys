package activity;

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
        newsDetail = NewsDetail.getNewsDetailById(this, getIntent().getStringExtra("News")) ;
        if(newsDetail == null) {
            Log.println(Log.INFO, "", "Wrong! Null");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.news_toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.app_name));

        /*fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        TextView textView = (TextView)findViewById(R.id.news_text) ;
        textView.setText(newsDetail.getContent());
    }
}