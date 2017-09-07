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
    static LinearLayoutManager layoutManager ;
    static SlideInRightAnimationAdapter newsAdapter ;
    static SwipeRefreshLayout newsRefresh ;
    static NewsProxy newsProxy = NewsProxy.getInstance() ;
    static int lastVisibleItem = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        smallAdapter = new NewsAdapter(newsProxy.getDisplayNews()) ;
        smallAdapter.resources = getResources() ;

        newsView = (RecyclerView)findViewById(R.id.main_recyclerView) ;
        layoutManager = new LinearLayoutManager(this) ;
        newsView.setLayoutManager(layoutManager);
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
        newsView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == smallAdapter.getItemCount()) {
                    smallAdapter.changeMoreStatus(NewsAdapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            newsProxy.moreNews(20) ;
                            smallAdapter.datas = newsProxy.getDisplayNews() ;
                            smallAdapter.changeMoreStatus(NewsAdapter.PULLUP_LOAD_MORE);
                        }
                    }, 500);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView,dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
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
