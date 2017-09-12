package fragment.news_listview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import activity.R;
import controller.NewsReader;
import news.News;
import news.NewsDetail;

/**
 * Created by kzf on 2017/9/9.
 */

/*public class NewsImageAdapter extends BaseAdapter
{
    Context mContext ;
    Resources resources = null ;
    ArrayList<Bitmap> pictures ;

    public NewsImageAdapter(Context context, NewsDetail newsDetail)
    {
        mContext = context ;
        pictures = newsDetail.getPictureList() ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.news_pictureview, parent, false) ;
            final ImageView imageView = (ImageView)convertView.findViewById(R.id.news_picture_view) ;
            imageView.setImageBitmap(pictures.get(position));
        }
        return convertView ;
    }

    @Override
    public int getCount() {return pictures.size() ;}
    @Override
    public long getItemId(int position){return 0 ;}
    @Override
    public View getItem(int position) {return null ;}
}*/

public class NewsImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public Context mContext ;
    public ArrayList<Bitmap> datas = null;
    public Resources resources = null ;

    public NewsImageAdapter(Context context, NewsDetail newsDetail) {
        this.mContext = context ;
        this.datas = newsDetail.getPictureList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_pictureview,viewGroup,false);
        NewsImageAdapter.ItemViewHolder itemViewHolder=new NewsImageAdapter.ItemViewHolder(mContext, view);
        return itemViewHolder;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        NewsImageAdapter.ItemViewHolder ivh = (NewsImageAdapter.ItemViewHolder)viewHolder ;
        ivh.mImageView.setImageBitmap(datas.get(position)) ;
    }

    @Override
    public int getItemCount() {
        return datas.size() ;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView ;
        public ItemViewHolder(Context context, View view){
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.news_picture_view);
        }
    }
}