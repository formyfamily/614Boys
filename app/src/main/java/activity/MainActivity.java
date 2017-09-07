package activity;

import assembly.DividerItemDecoration;
import assembly.NewsAdapter;
import news.* ;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

public class MainActivity extends AppCompatActivity {

    // Static Members.
    static NewsAdapter smallAdapter ;
    static RecyclerView newsView ;
    static SlideInRightAnimationAdapter newsAdapter ;
    static SwipeRefreshLayout newsRefresh ;
    static NewsProxy newsProxy = NewsProxy.getInstance() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        smallAdapter = new NewsAdapter(newsProxy.getDisplayNews()) ;
        smallAdapter.resources = getResources() ;

        newsView = (RecyclerView)findViewById(R.id.main_recyclerView) ;
        newsView.setLayoutManager(new LinearLayoutManager(this));
        newsView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST)) ;

        newsAdapter =  new SlideInRightAnimationAdapter(smallAdapter);
        newsAdapter.setFirstOnly(true);
        newsAdapter.setDuration(1000);
        newsAdapter.setInterpolator(new OvershootInterpolator(0.3f));
        newsView.setAdapter(newsAdapter) ;

        newsRefresh = (SwipeRefreshLayout)findViewById(R.id.news_refresh_layout) ;
        newsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        newsProxy.update() ;
                        smallAdapter.datas = newsProxy.getDisplayNews() ;
                        newsAdapter =  new SlideInRightAnimationAdapter(smallAdapter);
                        newsAdapter.setFirstOnly(true);
                        newsAdapter.setDuration(1000);
                        newsAdapter.setInterpolator(new OvershootInterpolator(0.3f));
                        newsView.setAdapter(newsAdapter) ;
                        newsRefresh.setRefreshing(false);
                    }
                }, 500);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
