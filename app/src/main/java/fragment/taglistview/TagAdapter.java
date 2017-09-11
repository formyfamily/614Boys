package fragment.taglistview;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.TreeSet;

import activity.R;
import news.News;

/**
 * Created by kzf on 2017/9/9.
 */

public class TagAdapter extends BaseAdapter
{
    public Context mContext ;
    public boolean tagChecked[] = new boolean[13] ;
    public Resources resources = null ;

    public TagAdapter(Context context, TreeSet<Integer> selectedTags)
    {
        this.mContext = context ;
        for(int i = 1; i <= 12; i ++)
            if(selectedTags.contains(i))
                tagChecked[i-1] = true ;
            else tagChecked[i-1] = false ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.tag_item, parent, false) ;
            final CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.tag_checkbox) ;
            checkBox.setChecked(tagChecked[position]);
            checkBox.setText(News.classIdTagArray[position+1]);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tagChecked[position] = !tagChecked[position] ;
                    checkBox.setChecked(tagChecked[position]);
                }
            }) ;
        }
        return convertView ;
    }

    @Override
    public int getCount() {return 12 ;}
    @Override
    public long getItemId(int position){return 0 ;}
    @Override
    public View getItem(int position) {return null ;}
}