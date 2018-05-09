package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.Room;
import com.sunday.tianshehuoji.entity.Staff;
import com.sunday.tianshehuoji.utils.ImgUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/1/4.
 */

public class SelectServiceManAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Staff> dataSet;
    private int appWidth;
    private int selectIndex=-1;
    private View.OnClickListener onClickListener;

    public SelectServiceManAdapter(Context context, List<Staff> datas) {
        this.mContext = context;
        this.dataSet = datas;
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_select_room_item, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder = (ListHolder) holder;
        Staff item = dataSet.get(position);
        if (!TextUtils.isEmpty(item.getLogo())) {
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(item.getLogo()))
                    .placeholder(R.mipmap.default_img)
                    .error(R.mipmap.default_img)
                    .resize(appWidth / 5, appWidth / 5)
                    .centerInside()
                    .into(listHolder.productImage);
        }
        if (selectIndex == position) {
            listHolder.imgSelect.setImageResource(R.mipmap.ic_checked_button);
        } else {
            listHolder.imgSelect.setImageResource(R.mipmap.ic_unchecked_button);
        }
        listHolder.productTitle.setText(item.getName());
        listHolder.productContent.setText(item.getMobile());
        listHolder.imgSelect.setOnClickListener(onClickListener);
        listHolder.imgSelect.setTag(position);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ListHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.img_select)
        ImageView imgSelect;
        @Bind(R.id.product_image)
        ImageView productImage;
        @Bind(R.id.product_title)
        TextView productTitle;
        @Bind(R.id.product_content)
        TextView productContent;
        @Bind(R.id.rl_select_room)
        RelativeLayout rlSelectRoom;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}