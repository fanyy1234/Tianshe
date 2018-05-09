package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.PixUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.utils.ImgUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;



/**
 * Created by 刘涛 on 2016/12/15.
 */

public class CommentImageAdapter  extends BaseAdapter {
    private Context mContext;
    private List<String> dataSet = new ArrayList<>();
    private int appWidth;
    private View.OnClickListener onClickListener;

    public CommentImageAdapter(Context context, List<String> images) {
        this.mContext = context;
        this.dataSet = images;
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
    }

    public CommentImageAdapter(Context context) {
        this.mContext = context;
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
    }

    public void setDataSet(List<String> images) {
        this.dataSet = images;
    }


    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String item = dataSet.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_img_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext)
                .load(ImgUtils.ReplaceStr(item))
                .error(R.mipmap.default_img)
                .resize(appWidth/5, appWidth/5)
                .centerCrop()
                .into(viewHolder.image);
        viewHolder.image.setOnClickListener(onClickListener);
        viewHolder.image.setTag(R.id.tag_first, dataSet);
        viewHolder.image.setTag(R.id.tag_second, position);
        return convertView;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;

    }

    class ViewHolder {
        @Bind(R.id.img)
        ImageView image;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
