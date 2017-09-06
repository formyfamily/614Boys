package activity;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kzf on 2017/9/6.
 */
class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>
{
    public ArrayList<String> datas = null;
    public Resources resources = null ;
    public NewsAdapter(ArrayList<String> datas) {
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
        viewHolder.mTextView.setText(datas.get(position));
        viewHolder.mImageView.setImageBitmap(BitmapFactory.decodeResource(resources, R.mipmap.search_icon2));
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

    public void add(String text, int position) {
        datas.add(position, text);
        notifyItemInserted(position);
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public ImageView mSaveIcon;
        public ViewHolder(View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.itemText);
            mImageView = (ImageView) view.findViewById(R.id.itemImage);
            mSaveIcon = (ImageView) view.findViewById(R.id.itemSaveIcon);
        }
    }
}