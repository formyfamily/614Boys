package activity;

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

    static int cnt = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        String[] ctype = new String[] {} ;//{"【重磅】有位老人今年已经90岁啦", "【重磅】llx猝死在宿舍", "【重磅】yjn猝死在宿舍", "sdgsdg", "sdgrebhreb", "ewewwgw", "dsbddbsb", "2g3g2g32", "vbsdvasvsdv", "gaewgwagwe", "a", "b", "w", "aw"} ;
        ArrayList<String> arrStr = new ArrayList<String>() ;
        for(int i = 0; i < ctype.length; i ++)
            arrStr.add(ctype[i]) ;
        final NewsAdapter adapter2 = new NewsAdapter(arrStr) ;
        adapter2.resources = getResources() ;
        final RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.main_recyclerView) ;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST)) ;

        final SlideInRightAnimationAdapter newsAdapter = new SlideInRightAnimationAdapter(adapter2) ;
        newsAdapter.setFirstOnly(true);
        newsAdapter.setDuration(1000);
        newsAdapter.setInterpolator(new OvershootInterpolator(0.3f));
        mRecyclerView.setAdapter(newsAdapter) ;

        //NewsProxy newsProxy = new NewsProxy(null) ;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cnt == 0)
                for(int i = 0; i <= 1; i ++)
                    adapter2.add("asgdsgdasgsadg", adapter2.datas.size());
            }
        });
    }
}
