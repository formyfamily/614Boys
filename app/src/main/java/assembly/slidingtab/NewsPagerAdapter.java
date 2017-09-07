package assembly.slidingtab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.lang.reflect.Array;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author fyales
 */
public class NewsPagerAdapter extends FragmentPagerAdapter {


    private String mTabTitle[] = new String[]{"科技", "教育", "军事", "国内", "社会", "文化", "汽车", "国际", "体育", "财经", "健康", "娱乐"};
    static NewsFragment newsFragments[] = new NewsFragment[12] ;
    static TreeSet<Integer> selectedTags = new TreeSet<Integer>() ;
    private Context mContext;

    public NewsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        for(int i = 0; i < 12; i ++) {
            newsFragments[i] = new NewsFragment();
            newsFragments[i].mContext = mContext ;
            newsFragments[i].tagId = i+1 ;
            newsFragments[i].update();
            if(i%4 == 0) selectedTags.add(i);
        }
    }

    public void addTab(int tabId) {selectedTags.add(tabId) ;}
    public void deleteTab(int tabId) {selectedTags.remove(tabId) ;}

    @Override
    public Fragment getItem(int position) {
        return newsFragments[(Integer)selectedTags.toArray()[position]] ;
    }

    @Override
    public int getCount() {
        return selectedTags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return  mTabTitle[(Integer)selectedTags.toArray()[position]];
    }
}
