package assembly.slidingtab;

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
import android.widget.TextView;

import activity.R;
import assembly.DividerItemDecoration;
import assembly.NewsAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import news.NewsProxy;

/**
 * @author fyales
 */
public class NewsFragment extends Fragment {
    private static final String DATA = "data";
    NewsAdapter smallAdapter;
    RecyclerView newsView;
    LinearLayoutManager layoutManager;
    SlideInRightAnimationAdapter newsAdapter;
    SwipeRefreshLayout newsRefresh;
    static NewsProxy newsProxy = NewsProxy.getInstance();
    int lastVisibleItem = 0;
    int tagId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_refresh_layout, container, false);

        lastVisibleItem = 0;
        smallAdapter = new NewsAdapter(newsProxy.getDisplayNews());
        smallAdapter.resources = getResources();
        newsView = (RecyclerView) view.findViewById(R.id.main_recyclerView);
        layoutManager = new LinearLayoutManager(inflater.getContext());
        newsView.setLayoutManager(layoutManager);
        newsView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL_LIST));

        newsAdapter = new SlideInRightAnimationAdapter(smallAdapter);
        newsAdapter.setFirstOnly(true);
        newsAdapter.setDuration(1000);
        newsAdapter.setInterpolator(new OvershootInterpolator(0.3f));
        newsView.setAdapter(newsAdapter);

        newsRefresh = (SwipeRefreshLayout) view.findViewById(R.id.news_refresh_layout);
        newsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        newsProxy.update();
                        smallAdapter.datas = newsProxy.getDisplayNews();
                        newsAdapter = new SlideInRightAnimationAdapter(smallAdapter);
                        newsAdapter.setFirstOnly(true);
                        newsAdapter.setDuration(1000);
                        newsAdapter.setInterpolator(new OvershootInterpolator(0.3f));
                        newsView.setAdapter(newsAdapter);
                        newsRefresh.setRefreshing(false);
                    }
                }, 500);
            }
        });
        newsView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == smallAdapter.getItemCount()) {
                    smallAdapter.changeMoreStatus(NewsAdapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //newsProxy.setClassTagId(tagId);
                            newsProxy.moreNews(20);
                            smallAdapter.datas = newsProxy.getDisplayNews();
                            smallAdapter.changeMoreStatus(NewsAdapter.PULLUP_LOAD_MORE);
                        }
                    }, 500);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
        return newsRefresh;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void update() {
        if(smallAdapter == null) return ;
        //newsProxy.setClassTagId(tagId);
        //newsProxy.update();
        smallAdapter.datas = newsProxy.getDisplayNews();
        smallAdapter.notifyDataSetChanged();
        lastVisibleItem = 0;
        Log.println(Log.INFO, "", "New!" + ((Integer) tagId).toString());
    }
    public static Fragment newInstance(int type){
        Fragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DATA,type);
        fragment.setArguments(bundle);
        return fragment;
    }
}
