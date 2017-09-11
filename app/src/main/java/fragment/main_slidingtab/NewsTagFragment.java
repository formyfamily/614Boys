package fragment.main_slidingtab;

/**
 * Created by kzf on 2017/9/11.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import activity.MainActivity;
import activity.R;
import news.NewsProxy;

import android.widget.ImageButton;

/**
 * @author fyales
 */
public class NewsTagFragment extends Fragment {
    private static final String DATA = "data";
    static NewsProxy newsProxy = NewsProxy.getInstance();

    ViewPager mViewPager ;
    ImageButton imageButton ;
    NewsTagAdapter newsTagAdapter ;
    SlidingTabLayout slidingTabLayout ;
    Context mContext ;
    FragmentManager fm ;

    public void setContext(Context context) {mContext = context ;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_news_tag_pageview, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        imageButton = (ImageButton) view.findViewById(R.id.tab_set_button) ;
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
                    ((MainActivity)mContext).createNewTagActivity(newsTagAdapter) ;
                }
        });

        slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        updateAdapter();
        return view ;
    }

    public void setAdapter(NewsTagAdapter newsTagAdapter)
    {
        this.newsTagAdapter = newsTagAdapter ;
        updateAdapter();
    }

    void updateAdapter()
    {
        if(mViewPager == null)
            mViewPager = new ViewPager(mContext) ;
        mViewPager.setAdapter(newsTagAdapter);
        if(slidingTabLayout != null)
            slidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void update() {
        newsTagAdapter.update();
    }

    public static Fragment newInstance(int type){
        Fragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DATA,type);
        fragment.setArguments(bundle);
        return fragment;
    }
}
