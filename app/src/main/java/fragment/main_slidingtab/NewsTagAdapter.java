package fragment.main_slidingtab;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;
import java.util.TreeSet;

import news.News;

/**
 * @author fyales
 */
public class NewsTagAdapter extends FragmentStatePagerAdapter {

    private String mTabTitle[] = News.classIdTagArray;
    static NewsFragment newsFragments[] = new NewsFragment[13];
    static TreeSet<Integer> selectedTags = new TreeSet<Integer>();
    private Context mContext;
    private FragmentManager fm;
    public int tempTag = 0;
    static private NewsTagAdapter newsTagAdapter;

    static public synchronized NewsTagAdapter getInstance(FragmentManager fm, Context context) {
        if (newsTagAdapter == null)
            newsTagAdapter = new NewsTagAdapter(fm, context);
        return newsTagAdapter;
    }

    static public synchronized NewsTagAdapter resetInstance(FragmentManager fm, Context context) {
        newsTagAdapter = new NewsTagAdapter(fm, context);
        return newsTagAdapter;
    }

    private NewsTagAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fm = fm ;
        this.mContext = context;
        newsFragments[0] = new NewsFragment();
        newsFragments[0].mContext = mContext;
        newsFragments[0].tagId = 0;
        newsFragments[0].update();
        selectedTags.add(0);
    }

    public void addTag(int tagId) {
        selectedTags.add(tagId);
        newsFragments[tagId] = new NewsFragment();
        newsFragments[tagId].mContext = mContext;
        newsFragments[tagId].tagId = tagId;
        newsFragments[tagId].update();
    }

    public void deleteTag(int tagId) {
        selectedTags.remove(tagId);
        newsFragments[tagId] = null ;
    }

    public void resetTag() {
        selectedTags.clear();
        for (int i = 0; i < 13; i++) {
            newsFragments[i] = null ;
        }
        addTag(0);
    }

    public TreeSet<Integer> getTag() {
        return selectedTags;
    }

    @Override
    public Fragment getItem(int position) {
        return newsFragments[(Integer) selectedTags.toArray()[position]];
    }
    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        //do nothing here! no call to super.restoreState(arg0, arg1);
    }
    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE ;
    }

    @Override
    public int getCount() {
        return selectedTags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle[(Integer) selectedTags.toArray()[position]];
    }

    public void update() {
        for(int i = 0; i < getCount(); i ++)
            ((NewsFragment)getItem(i)).update();
        notifyDataSetChanged();
    }
}
