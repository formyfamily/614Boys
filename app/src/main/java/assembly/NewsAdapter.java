package assembly;

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
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>
{
    public ArrayList<News> datas = null;
    public Resources resources = null ;
    public NewsAdapter(ArrayList<News> datas) {
        this.datas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false) ;
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        News news = datas.get(position) ;
        viewHolder.mTextView.setText(news.getTitle());
        viewHolder.mImageView.setImageBitmap(BitmapFactory.decodeResource(resources, R.mipmap.search_icon2));
        String dateString = news.getTime() ;
        viewHolder.mItemInfo.setText(dateString.substring(0, 4)+'-'+dateString.substring(4, 6)+'-'+dateString.substring(6, 8)) ;
        if (position % 2 == 0) {
            viewHolder.mTextView.setTextColor(resources.getColor(R.color.colorUnreaded));
            viewHolder.mSaveIcon.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.star));
        } else {
            viewHolder.mTextView.setTextColor(resources.getColor(R.color.colorReaded));
            viewHolder.mSaveIcon.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.filled_star2));
        }
    }

    public void remove(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }
    public void add(News news, int position) {
        datas.add(position, news);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public ImageView mSaveIcon;
        public TextView mItemInfo ;

        public ViewHolder(View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.itemText);
            mImageView = (ImageView) view.findViewById(R.id.itemImage);
            mSaveIcon = (ImageView) view.findViewById(R.id.itemStarIcon);
            mItemInfo = (TextView) view.findViewById(R.id.itemInfo) ;
        }
    }
}