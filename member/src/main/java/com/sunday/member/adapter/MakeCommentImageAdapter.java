package com.sunday.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.sunday.member.R;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MakeCommentImageAdapter extends BaseAdapter {
    private List<Object> dataSet;
    private Context mContext;
    private View.OnClickListener onClickListener;
    private LayoutInflater layoutInflater;
    public MakeCommentImageAdapter(Context context, List<Object> data){
        this.mContext = context;
        this.dataSet = data;
        layoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView =layoutInflater.inflate(R.layout.grid_image_item,null);
        ImageView imageView  = ButterKnife.findById(convertView, R.id.image);
        ImageView deleteBtn = ButterKnife.findById(convertView,R.id.delete_image);
        Object item = dataSet.get(position);
        if(item instanceof Integer){
            Picasso.with(mContext)
                    .load((Integer)item)
                    .into(imageView);
        }else if(item instanceof String){
            File file = new File((String)item);
            Picasso.with(mContext)
                    .load(file)
                    .into(imageView);
        }
        imageView.setTag(position);
        deleteBtn.setTag(position);
        deleteBtn.setTag(R.id.action0,imageView);
        imageView.setOnClickListener(onClickListener);
        deleteBtn.setOnClickListener(onClickListener);
        if(position==dataSet.size()-1){
            deleteBtn.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
