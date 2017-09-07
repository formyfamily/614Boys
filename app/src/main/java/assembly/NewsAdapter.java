package assembly;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import activity.R;
import news.News;

/**
 * Created by kzf on 2017/9/6.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    //上拉加载更多
    public static final int  PULLUP_LOAD_MORE=0;
    //正在加载中
    public static final int  LOADING_MORE=1;
    //下拉刷新
    public static final int  PULLDOWN_REFRESH=0;
    //正在刷新中
    public static final int  FRESHING=1;

    private int load_more_status=0;
    public ArrayList<News> datas = null;
    public Resources resources = null ;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //底部FootView
    private static final int TYPE_REFRESHER = 2;  //顶部RefreshView

    public NewsAdapter(ArrayList<News> datas) {
        this.datas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType==TYPE_ITEM)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
            ItemViewHolder itemViewHolder=new ItemViewHolder(view);
            return itemViewHolder;
        }
        else if(viewType==TYPE_FOOTER)
        {
            View foot_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.footerview,viewGroup,false);
            FootViewHolder footViewHolder=new FootViewHolder(foot_view);
            return footViewHolder;
        }
        //else if(viewType==TYPE_REFRESHER)
        //{
        //}
        return null ;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if(viewHolder instanceof NewsAdapter.ItemViewHolder)
        {
            ItemViewHolder ivh = (ItemViewHolder)viewHolder ;
            News news = datas.get(position) ;
            ivh.mTextView.setText(news.getTitle());
            ivh.mImageView.setImageBitmap(BitmapFactory.decodeResource(resources, R.mipmap.search_icon2));
            String dateString = news.getTime() ;
            ivh.mItemInfo.setText(dateString.substring(0, 4)+'-'+dateString.substring(4, 6)+'-'+dateString.substring(6, 8)) ;
            if (position % 2 == 0) {
                ivh.mTextView.setTextColor(resources.getColor(R.color.colorUnreaded));
                ivh.mSaveIcon.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.star));
            } else {
                ivh.mTextView.setTextColor(resources.getColor(R.color.colorReaded));
                ivh.mSaveIcon.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.filled_star2));
            }
        }
        else if(viewHolder instanceof NewsAdapter.FootViewHolder)
        {
            FootViewHolder fvh=(FootViewHolder)viewHolder;
            switch (load_more_status){
                case PULLUP_LOAD_MORE:
                    fvh.footerText.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    fvh.footerText.setText("正在加载中...");
                    break;
            }
        }
    }
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount())
            return TYPE_FOOTER;
        else
            return TYPE_ITEM;
    }

    public void remove(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }
    public void add(News news, int position) {
        datas.add(position, news);
        notifyItemInserted(position);
    }
    public void changeMoreStatus(int status){
        load_more_status=status;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datas.size()+1;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public ImageView mSaveIcon;
        public TextView mItemInfo ;

        public ItemViewHolder(View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.itemText);
            mImageView = (ImageView) view.findViewById(R.id.itemImage);
            mSaveIcon = (ImageView) view.findViewById(R.id.itemStarIcon);
            mItemInfo = (TextView) view.findViewById(R.id.itemInfo) ;
        }
    }
    public static class FootViewHolder extends  RecyclerView.ViewHolder{
        private TextView footerText;
        public FootViewHolder(View view) {
            super(view);
            footerText=(TextView)view.findViewById(R.id.footer_text);
        }
    }
    public static class RefreshViewHolder extends  RecyclerView.ViewHolder{
        private TextView refreshText;
        public RefreshViewHolder(View view) {
            super(view);
            refreshText=(TextView)view.findViewById(R.id.refresh_text);
        }
    }
}