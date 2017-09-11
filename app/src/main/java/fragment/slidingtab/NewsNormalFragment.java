package fragment.slidingtab;

/**
 * Created by kzf on 2017/9/11.
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import activity.MainActivity;
import activity.R;
import fragment.DividerItemDecoration;
import fragment.newsrecycle.NewsAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import news.News;
import news.NewsProxy;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;

import activity.R;
import fragment.DividerItemDecoration;
import fragment.newsrecycle.NewsAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import news.NewsProxy;

/**
 * @author fyales
 */
public class NewsNormalFragment extends Fragment {
    private static final String DATA = "data";
    static NewsProxy newsProxy = NewsProxy.getInstance();

    NewsAdapter mNewsAdapter;
    RecyclerView newsView;
    LinearLayoutManager layoutManager;
    Context mContext ;

    public void setContext(Context context) {mContext = context ;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_news_normal_pageview, container, false);

        mNewsAdapter = new NewsAdapter(mContext, newsProxy.getDisplayNews(0));
        mNewsAdapter.resources = getResources();
        newsView = (RecyclerView) view.findViewById(R.id.main_recyclerView);
        layoutManager = new LinearLayoutManager(inflater.getContext());
        newsView.setLayoutManager(layoutManager);
        newsView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL_LIST));
        newsView.setAdapter(mNewsAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void update() {
        if(mNewsAdapter == null) return ;
        mNewsAdapter.datas = newsProxy.getDisplayNews(0);
        mNewsAdapter.notifyDataSetChanged();
    }
    public static Fragment newInstance(int type){
        Fragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DATA,type);
        fragment.setArguments(bundle);
        return fragment;
    }
}

