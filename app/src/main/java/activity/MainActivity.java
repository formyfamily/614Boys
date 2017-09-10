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

    private void showShare(News news) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("I am Title");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        //oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段

        //oks.setPlatform(Platform.SHARE_WEBPAGE);
        oks.setText(news.getUrl());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
       // oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        //oks.setUrl("http://sharesdk.cn");
        //oks.setImageUrl("http://img003.21cnimg.com/photos/album/20160808/m600/A3B78A702DF9BF0EE02ADFD5D4F53D54.jpeg");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        //oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
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
