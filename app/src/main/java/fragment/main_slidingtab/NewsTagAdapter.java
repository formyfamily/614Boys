package fragment.main_slidingtab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.TreeSet;

import news.News;

/**
 * @author fyales
 */
public class NewsTagAdapter extends FragmentPagerAdapter {


    private String mTabTitle[] = News.classIdTagArray;
    static NewsFragment newsFragments[] = new NewsFragment[13];
    static TreeSet<Integer> selectedTags = new TreeSet<Integer>();
    private Context mContext;

    public NewsTagAdapter(FragmentManager fm, Context context) {
        super(fm);
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
        newsFragments[tagId] = null;
    }

    public void resetTag() {
        selectedTags.clear();
        for (int i = 0; i < 13; i++)
            newsFragments[i] = null;
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
