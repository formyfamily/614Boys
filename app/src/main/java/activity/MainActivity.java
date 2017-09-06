package activity;

import news.* ;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

import static android.R.attr.path;

public class MainActivity extends AppCompatActivity {

    // Static Members.
    static NewsAdapter adapter2 ;
    static RecyclerView newsView ;
    static SlideInRightAnimationAdapter newsAdapter ;
    static NewsProxy newsProxy = NewsProxy.getInstance() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        adapter2 = new NewsAdapter(newsProxy.getDisplayNews()) ;
        adapter2.resources = getResources() ;

        newsView = (RecyclerView)findViewById(R.id.main_recyclerView) ;
        newsView.setLayoutManager(new LinearLayoutManager(this));
        newsView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST)) ;

        newsAdapter =  new SlideInRightAnimationAdapter(adapter2);
        newsAdapter.setFirstOnly(true);
        newsAdapter.setDuration(1000);
        newsAdapter.setInterpolator(new OvershootInterpolator(0.3f));
        newsView.setAdapter(newsAdapter) ;

        //NewsProxy newsProxy = new NewsProxy(null) ;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
