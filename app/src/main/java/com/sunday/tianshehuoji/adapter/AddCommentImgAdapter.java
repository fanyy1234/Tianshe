package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.tianshehuoji.R;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/1/5.
 */

public class AddCommentImgAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<String> dataSet;
    private int imgWidth;
    private int maxNum;
    private PicCallBack picCallBack;



    public AddCommentImgAdapter(Context context, ArrayList<String> data, int maxNum, PicCallBack callBack) {
        this.mContext = context;
        this.dataSet = data;
        this.imgWidth = DeviceUtils.getDisplay(mContext).widthPixels / 4;
        this.picCallBack = callBack;
        this.maxNum = maxNum;
    }



    public void add(ArrayList<String> mSelectPath) {
        this.dataSet.clear();
        this.dataSet.addAll(mSelectPath);
    }


    public void clear() {
        this.dataSet.clear();
    }

    public void addImg(String img) {
        this.dataSet.add(img);
    }

    public ArrayList<String> getDataSet() {
        return this.dataSet;
    }


    @Override
    public int getCount() {
        if (dataSet.size() == maxNum) {
            return dataSet.size();
        } else {
            return dataSet.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_pic_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == dataSet.size()) {
            viewHolder.imgSelect.setTag(position);
            viewHolder.imgSelect.setOnClickListener(this);
            Picasso.with(mContext)
                    .load(R.mipmap.pic_add)
                    .placeholder(R.mipmap.default_img)
                    .error(R.mipmap.default_img)
                    .resize(imgWidth, imgWidth)
                    .centerInside()
                    .into(viewHolder.imgSelect);
            viewHolder.imgDel.setVisibility(View.GONE);
        } else {
            Picasso.with(mContext)
                    .load(new File(dataSet.get(position)))
                    .placeholder(R.mipmap.default_img)
                    .error(R.mipmap.default_img)
                    .resize(imgWidth, imgWidth)
                    .centerInside()
                    .into(viewHolder.imgSelect);
            viewHolder.imgDel.setVisibility(View.VISIBLE);
            viewHolder.imgDel.setOnClickListener(this);
            viewHolder.imgDel.setTag(position);
        }


        return convertView;


    }

    @Override
    public void onClick(View v) {
        final int position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.imgSelect:
                if (position == dataSet.size()) {
                    picCallBack.addPic();
                }
                break;
            case R.id.imgDel:
                if (position != dataSet.size()) {
                    picCallBack.delPic(position);
                }
                break;
        }
    }


    static class ViewHolder {
        @Bind(R.id.imgSelect)
        ImageView imgSelect;
        @Bind(R.id.imgDel)
        ImageView imgDel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            imgDel.setVisibility(View.GONE);
        }

    }

    public interface PicCallBack {
        void addPic();
        void delPic(int position);
    }
}