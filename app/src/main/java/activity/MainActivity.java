package activity;

import controller.NewsReciter;
import controller.NewsSearcher;
import fragment.main_newsrecycle.NewsAdapter;
import fragment.main_slidingtab.MainAdapter;
import fragment.main_slidingtab.MainTagEntity;
import fragment.main_slidingtab.NewsNormalFragment;
import fragment.main_slidingtab.NewsTagFragment;
import controller.NewsRecommender;
import news.* ;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import fragment.main_slidingtab.NewsTagAdapter;

public class MainActivity extends AppCompatActivity {

    static boolean nightMode = false ;
    static boolean noPictureMode = false ;
    public static Context mContext ;

    Toolbar toolbar ;
    CommonTabLayout commonTabLayout ;
    SlidingTabLayout slidingTabLayout ;
    ArrayList<Fragment> fragmentList ;
    ArrayList<CustomTabEntity> tabEntityList ;
    NewsTagFragment newsTagFragment ;
    NewsNormalFragment favouriteFragment, recommendFragment;
    NewsTagAdapter newsTagAdapter ;
    MainAdapter mainAdapter ;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*getSupportFragmentManager().findFragmentById(R.id.main_tab_content) ;
        getSupportFragmentManager().putFragment(outState, "newsTagFragment", newsTagFragment );
        getSupportFragmentManager().putFragment(outState, "favouriteFragment", favouriteFragment );
        getSupportFragmentManager().putFragment(outState, "recommendFragment", recommendFragment );*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // -- --- -- --- Some initialization
        android.app.FragmentManager fm = getFragmentManager() ;
        NewsDatabase.getInstance().setThisActivity(MainActivity.this);
        DatabaseHelper.init(this);
        NewsDetail.setThisActivity(MainActivity.this);
        // initialize voice configuration object, used for reading news aloud
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59b214cf");
        NewsReciter.getInstance().init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction();
        mContext = MainActivity.this ;

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
        nightModeBotton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        nightModeBotton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        break;
                    case MotionEvent.ACTION_UP:
                        TextView nightText = (TextView)findViewById(R.id.night_mode_text) ;
                        nightModeBotton.setBackgroundColor(getResources().getColor(R.color.white));
                        if(nightMode == false){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            new SVProgressHUD(MainActivity.this).showInfoWithStatus("已打开夜间模式");
                            recreate();
                            nightText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        }
                        else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            new SVProgressHUD(MainActivity.this).showInfoWithStatus("已关闭夜间模式");
                            recreate();
                            nightText.setTextColor(getResources().getColor(R.color.textColor_svprogresshuddefault_msg));
                        }
                        nightMode = !nightMode ;
                        break;
                }
                return false;
            }
        });
        final LinearLayout noPictureModeBotton = (LinearLayout)findViewById(R.id.nopicture_mode_button) ;
        noPictureModeBotton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        noPictureModeBotton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        break;
                    case MotionEvent.ACTION_UP:
                        TextView noPictureText = (TextView)findViewById(R.id.nopicture_mode_text) ;
                        noPictureModeBotton.setBackgroundColor(getResources().getColor(R.color.white));
                        if(noPictureMode == false){
                            noPictureText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            new SVProgressHUD(MainActivity.this).showInfoWithStatus("已打开无图模式");
                        }
                        else {
                            noPictureText.setTextColor(getResources().getColor(R.color.textColor_svprogresshuddefault_msg));
                            new SVProgressHUD(MainActivity.this).showInfoWithStatus("已关闭无图模式");
                        }
                        noPictureMode = !noPictureMode ;
                        break;
                }
                return false;
            }
        });


        // -- --- -- --- Setup SlidingTab


        if (savedInstanceState != null) {
            List<Fragment> fragList = getSupportFragmentManager().getFragments() ;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            for(int i = 0; i < fragList.size(); i ++)
                fragmentTransaction.remove(fragList.get(i)) ;
            fragmentTransaction.commit();
        }
        newsTagFragment = new NewsTagFragment();
        favouriteFragment = new NewsNormalFragment() ;
        recommendFragment = new NewsNormalFragment() ;
        newsTagFragment.setContext(MainActivity.this) ;
        favouriteFragment.setContext(MainActivity.this) ;
        recommendFragment.setContext(MainActivity.this) ;
        newsTagAdapter = new NewsTagAdapter(getSupportFragmentManager(), MainActivity.this) ;
        newsTagFragment.setAdapter(newsTagAdapter);
        newsTagFragment.update();
        NewsSearcher.getInstance().setNewsTagFragment(newsTagFragment);
        fragmentList = new ArrayList<Fragment>() ;
        fragmentList.add(newsTagFragment) ;
        fragmentList.add(favouriteFragment) ;
        fragmentList.add(recommendFragment) ;


        tabEntityList = new ArrayList<CustomTabEntity>() ;
        tabEntityList.add(new MainTagEntity("全部新闻", 0, 0)) ;
        tabEntityList.add(new MainTagEntity("新闻推荐", 0, 0)) ;
        tabEntityList.add(new MainTagEntity("收藏夹", 0, 0)) ;

        String[] mTabTitles = {"全部新闻","新闻推荐","收藏夹"} ;
        commonTabLayout = (CommonTabLayout)findViewById(R.id.main_tab_layout) ;
        commonTabLayout.setTabData(tabEntityList, MainActivity.this, R.id.main_tab_content, fragmentList) ;
        /*slidingTabLayout = (SlidingTabLayout)findViewById(R.id.main_tab_layout) ;
        ViewPager viewPager = (ViewPager)findViewById(R.id.main_viewpager) ;
        viewPager.setAdapter(mainAdapter);
        slidingTabLayout.setViewPager(viewPager, mTabTitles);
        slidingTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if(position == 1) favouriteFragment.update();
            }
            @Override
            public void onTabReselect(int position) {

            }
        });*/
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
                NewsSearcher.getInstance().searchKeyWord(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {return false;}
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //NewsSearcher.getInstance().searchKeyWord("aewgewbawbablablablahah12y12985y1512"); // a magic random string which will filter all news
            }
            @Override
            public void onSearchViewClosed() {
                NewsSearcher.getInstance().finishSearching();
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 0) {
            newsTagAdapter = new NewsTagAdapter(getSupportFragmentManager(), MainActivity.this) ;
            newsTagAdapter.resetTag();
            boolean tagChecked[] = data.getBooleanArrayExtra("tag_check") ;
            for(int i = 0; i < 12; i ++)
                if(tagChecked[i]) newsTagAdapter.addTag(i + 1);
            newsTagAdapter.notifyDataSetChanged();
            newsTagFragment.setAdapter(newsTagAdapter);
      }
        else if(requestCode == 1) {
            newsTagAdapter.update();
        }
    }

    public void createNewNewsActivity(News news)
    {
        Intent intent = new Intent(MainActivity.this, NewsActivity.class) ;
        intent.putExtra("News", news.getId()) ;
        startActivityForResult(intent, 1);
    }
    public void createNewTagActivity(NewsTagAdapter newsTagAdapter)
    {
        Intent intent = new Intent(MainActivity.this, TagActivity.class) ;
        TagActivity.newsTagAdapter = newsTagAdapter;
        startActivityForResult(intent, 0);
    }
}
