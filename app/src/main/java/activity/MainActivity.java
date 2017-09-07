package activity;

import assembly.NewsAdapter;
import news.* ;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import assembly.slidingtab.SlidingTabLayout;
import assembly.slidingtab.NewsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    // Static Members.
    static NewsAdapter smallAdapter ;
    static RecyclerView newsView ;
    static LinearLayoutManager layoutManager ;
    static SlideInRightAnimationAdapter newsAdapter ;
    static SwipeRefreshLayout newsRefresh ;
    static NewsProxy newsProxy = NewsProxy.getInstance() ;
    static int lastVisibleItem = 0 ;
    static public MainActivity current_Activity ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        current_Activity = this ;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new NewsPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/
    }

    public void createNewNewsActivity(News news)
    {
        Intent intent = new Intent(MainActivity.this, NewsActivity.class) ;
        intent.putExtra("News", news) ;
        startActivity(intent);
    }

}
