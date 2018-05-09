package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.PixUtils;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.widgets.CircleImageView;
import com.sunday.common.widgets.NoScrollGridView;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.CommentItem;
import com.sunday.tianshehuoji.utils.ImgUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.R.attr.width;

/**
 * Created by 刘涛 on 2016/12/15.
 */

public class CommentAdapter extends BaseAdapter {

    private List<CommentItem> data;
    private LayoutInflater layoutInflater;
    private int appWidth;
    private Context mContext;
    private View.OnClickListener onClickListener;

    public CommentAdapter(Context context) {
        this.mContext = context;
    }

    public CommentAdapter(Context mContext, List<CommentItem> data) {
        this.data = data;
        this.mContext=mContext;
        layoutInflater = LayoutInflater.from(mContext);
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        CommentItem item = data.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_comment_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (StringUtils.isMobileNO(item.getMobile())) {
            String a = item.getMobile().substring(0, 3) + "****" + item.getMobile().substring(7, 11);
            viewHolder.userNick.setText(a);
        } else {
            viewHolder.userNick.setText(item.getMobile());
        }
        viewHolder.commentTime.setText(item.getCommentTime());
        viewHolder.commentText.setText(item.getContent());
        viewHolder.ratingBar.setRating(item.getAverage());
        if (!TextUtils.isEmpty(item.getLogo())) {
            Picasso.with(convertView.getContext())
                    .load(ImgUtils.ReplaceStr(item.getLogo()))
                    .error(R.mipmap.login_logo)
                    .resize(appWidth / 6, appWidth / 6)
                    .into(viewHolder.avater);
        }
        if (item.getImages() != null && item.getImages().size() > 0) {
            CommentImageAdapter imageAdapter = new CommentImageAdapter(mContext,item.getImages());
            viewHolder.noScrollGridView.setAdapter(imageAdapter);
            imageAdapter.notifyDataSetChanged();
            imageAdapter.setOnClickListener(onClickListener);
        }
        return convertView;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    static class ViewHolder {
        @Bind(R.id.avator)
        CircleImageView avater;
        @Bind(R.id.user_nick)
        TextView userNick;
        @Bind(R.id.comment_time)
        TextView commentTime;
        @Bind(R.id.rating)
        RatingBar ratingBar;
        @Bind(R.id.comment_text)
        TextView commentText;
        @Bind(R.id.images)
        NoScrollGridView noScrollGridView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
