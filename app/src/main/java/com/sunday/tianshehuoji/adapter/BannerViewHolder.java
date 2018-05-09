package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.widgets.banner.holder.Holder;

import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.Ads;
import com.sunday.tianshehuoji.entity.Img;
import com.sunday.tianshehuoji.utils.ImgUtils;

/**
 * Created by 刘涛 on 2016/12/14.
 */

public class BannerViewHolder  implements Holder<Object> {

    private Context mContext;
    private int width;
    private ImageView imageView;
    private boolean isNeedFull;
    private View.OnClickListener onClickListener;

    @Override
    public View createView(Context context) {
        this.mContext = context;
        width = DeviceUtils.getDisplay(mContext).widthPixels;
        imageView = new ImageView(mContext);
        if (isNeedFull) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Object data) {
        if (data instanceof Img) {
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(((Img) data).getImage()))
                    .resize(width / 2, width / 2)
                    .error(R.mipmap.icon)
                    .into(imageView);
            imageView.setTag(data);
            imageView.setOnClickListener(onClickListener);
        }else if (data instanceof String){
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(data.toString()))
                    .resize(width / 2, width / 2)
                    .error(R.mipmap.default_img)
                    .into(imageView);
            imageView.setTag(data);
            imageView.setOnClickListener(onClickListener);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setNeedFull(boolean needFull) {
        isNeedFull = needFull;
    }
}
