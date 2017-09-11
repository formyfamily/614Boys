package fragment.taglistview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import activity.R;
import news.NewsDetail;

/**
 * Created by kzf on 2017/9/9.
 */

public class NewsImageAdapter extends BaseAdapter
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
            convertView = mInflater.inflate(R.layout.news_picture_item, parent, false) ;
            final ImageView imageView = (ImageView)convertView.findViewById(R.id.news_pictureview) ;
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
}