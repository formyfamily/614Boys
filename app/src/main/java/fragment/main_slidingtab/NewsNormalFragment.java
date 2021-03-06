package fragment.main_slidingtab;

/**
 * Created by kzf on 2017/9/11.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import activity.R;
import controller.NewsFavourite;
import controller.NewsRecommender;
import fragment.main_newsrecycle.DividerItemDecoration;
import fragment.main_newsrecycle.NewsAdapter;
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
    int typeId ; // 1 -> recommend, 2 -> favourite

    public void setContext(Context context) {mContext = context ;}
    public void setType(int typeId) {this.typeId = typeId ;}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_news_normal_pageview, container, false);

        if(typeId == 1)
            mNewsAdapter = new NewsAdapter(mContext, NewsRecommender.getInstance().getRecommendedNews(), 0);
        else
            mNewsAdapter = new NewsAdapter(mContext, NewsFavourite.getInstance().getAllFavoriteNews(), 0);
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
        if(typeId == 1)
            mNewsAdapter.datas = NewsRecommender.getInstance().getRecommendedNews() ;
        else
            mNewsAdapter.datas = NewsFavourite.getInstance().getAllFavoriteNews() ;
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

