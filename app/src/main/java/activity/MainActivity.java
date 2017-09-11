package activity;

import controller.NewsReciter;
import news.* ;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import fragment.slidingtab.SlidingTabLayout;
import fragment.slidingtab.NewsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    boolean nightMode = false ;
    boolean noPictureMode = false ;
    Toolbar toolbar ;
    ViewPager viewPager ;
    NewsPagerAdapter newsPagerAdapter ;
    SlidingTabLayout slidingTabLayout ;
    ImageButton imageButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // -- --- -- --- Some initialization
        NewsDatabase.getInstance().setThisActivity(MainActivity.this);
        DatabaseHelper.init(this);
        NewsDetail.setThisActivity(MainActivity.this);
        // initialize voice configuration object, used for reading news aloud
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59b214cf");
        NewsReciter.getInstance().init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // -- --- -- --- Setup ToolBar
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);


        // -- --- -- --- Setup DrawerLayout
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();//初始化状态
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT) ;
            }
        }) ;

        // -- --- -- --- Setup Menu Botton Click Events

        final LinearLayout nightModeBotton = (LinearLayout)findViewById(R.id.night_mode_button) ;
        nightModeBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nightModeText = (TextView)findViewById(R.id.night_mode_text) ;
                if(nightMode == false)
                    nightModeText.setTextColor(getResources().getColor(R.color.colorPrimary));
                else nightModeText.setTextColor(getResources().getColor(R.color.textColor_svprogresshuddefault_msg));
                nightMode = !nightMode ;
            }
        });
        final LinearLayout noPictureModeBotton = (LinearLayout)findViewById(R.id.nopicture_mode_button) ;
        noPictureModeBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView noPictureText = (TextView)findViewById(R.id.nopicture_mode_text) ;
                if(noPictureMode == false)
                    noPictureText.setTextColor(getResources().getColor(R.color.colorPrimary));
                else noPictureText.setTextColor(getResources().getColor(R.color.textColor_svprogresshuddefault_msg));
                noPictureMode = !noPictureMode ;
            }
        });

        // -- --- -- --- Setup SlidingTab
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        newsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager(), MainActivity.this) ;
        viewPager.setAdapter(newsPagerAdapter);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);

        // -- --- -- --- Setup TagActivity Button
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

    public void createNewNewsActivity(News news)
    {
        //showShare(news);
        Intent intent = new Intent(MainActivity.this, NewsActivity.class) ;
        intent.putExtra("News", news.getId()) ;
        startActivityForResult(intent, 1);
    }

}
