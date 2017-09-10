package assembly.taglistview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.zip.Inflater;

import activity.R;
import controller.NewsReader;
import news.News;
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
        pictures = newsDetail.getPictureList() ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.newspicture_item, parent, false) ;
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