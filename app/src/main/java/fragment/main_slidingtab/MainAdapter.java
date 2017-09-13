package fragment.main_slidingtab;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import java.util.TreeSet;

import activity.MainActivity;
import activity.R;
import controller.NewsSearcher;
import news.News;
import news.NewsProxy;

/**
 * Created by kzf on 2017/9/13.
 */

public class MainAdapter extends FragmentPagerAdapter {

    NewsTagFragment newsTagFragment ;
    NewsNormalFragment favouriteFragment, recommendFragment ;
    private Context mContext;

    public MainAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }
    public void setNewsTagFragment(NewsTagFragment newsTagFragment) {this.newsTagFragment = newsTagFragment ;}
    public void setFavouriteFragment(NewsNormalFragment favouriteFragment) {this.favouriteFragment = favouriteFragment ;}
    public void setRecommendFragment(NewsNormalFragment recommendFragment) {this.recommendFragment = recommendFragment ;}

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return newsTagFragment ;
            case 1:
                return recommendFragment ;
            case 2:
                return favouriteFragment ;
        }
        return null ;
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void update() {
        newsTagFragment.update(); ;
        recommendFragment.update();
        favouriteFragment.update();
        notifyDataSetChanged();
    }
}
