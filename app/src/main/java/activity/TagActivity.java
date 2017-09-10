package activity;

import assembly.slidingtab.NewsFragment;
import assembly.slidingtab.NewsPagerAdapter;
import assembly.slidingtab.SlidingTabLayout;
import assembly.taglistview.TagAdapter;
import controller.NewsFavourite;
import news.* ;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by kzf on 2017/9/6.
 */

public class TagActivity extends AppCompatActivity {

    static NewsPagerAdapter newsPagerAdapter ;
    TagAdapter tagAdapter ;
    ListView listView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tags_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tagAdapter = new TagAdapter(this, newsPagerAdapter.getTag()) ;
        listView = (ListView)findViewById(R.id.tags_listview) ;
        listView.setAdapter(tagAdapter) ;

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle data = new Bundle();
                data.putBooleanArray("tag_check", tagAdapter.tagChecked); ;
                intent.putExtras(data) ;
                setResult(0, intent);
                finish();
            }
        });
    }
}