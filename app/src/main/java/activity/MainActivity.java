package activity;

import assembly.newsrecycle.NewsAdapter;
import cn.sharesdk.framework.Platform;
import controller.NewsReciter;
import news.* ;
import cn.sharesdk.onekeyshare.*;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import assembly.slidingtab.SlidingTabLayout;
import assembly.slidingtab.NewsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar ;
    ViewPager viewPager ;
    NewsPagerAdapter newsPagerAdapter ;
    SlidingTabLayout slidingTabLayout ;
    ImageButton imageButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        NewsDatabase.getInstance().setThisActivity(MainActivity.this);
        DatabaseHelper.init(this);
        NewsDetail.setThisActivity(MainActivity.this);
        // initialize voice configuration object, used for reading news aloud
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59b214cf");
        NewsReciter.getInstance().init(this);
        // NewsReciter.getInstance().speakText("我是最菜的");  // An example

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        newsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager(), MainActivity.this) ;
        viewPager.setAdapter(newsPagerAdapter);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);
        imageButton = (ImageButton)findViewById(R.id.tab_set_button) ;

        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    imageButton.getDrawable().setAlpha(150);//设置图片透明度0~255，0完全透明，255不透明
                    imageButton.invalidate();
                }
                else {
                    imageButton.getDrawable().setAlpha(255);//还原图片
                    imageButton.invalidate();
                }
                return false;
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TagActivity.class) ;
                TagActivity.newsPagerAdapter = newsPagerAdapter ;
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);//指定Toolbar上的视图文件
        MenuItem item = menu.findItem(R.id.action_search);
        MaterialSearchView searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setMenuItem(item);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {return false;}
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {}
            @Override
            public void onSearchViewClosed() {}
        });
        return true;
    }

    public void createNewNewsActivity(News news)
    {
        //showShare(news);
        Intent intent = new Intent(MainActivity.this, NewsActivity.class) ;
        intent.putExtra("News", news.getId()) ;
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 0) {
            newsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager(), MainActivity.this) ;
            newsPagerAdapter.resetTag();
            boolean tagChecked[] = data.getBooleanArrayExtra("tag_check") ;
            for(int i = 0; i < 12; i ++)
                if(tagChecked[i]) newsPagerAdapter.addTag(i + 1);
            newsPagerAdapter.notifyDataSetChanged();
            viewPager.setAdapter(newsPagerAdapter);
            slidingTabLayout.setViewPager(viewPager);
        }
        else if(requestCode == 1) {
            newsPagerAdapter.update();
        }
    }
}
