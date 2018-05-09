package com.sunday.shangjia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.widgets.CircleImageView;
import com.sunday.common.widgets.NoScrollGridView;
import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.OrderComment;
import com.sunday.shangjia.util.ImgUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/21.
 */

public class CommentListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<OrderComment> dataSet;
    private int appWidth;
    private View.OnClickListener onClickListener;

    public CommentListAdapter(Context context,List<OrderComment> datas) {
        this.dataSet=datas;
        this.mContext = context;
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_comment_item, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder viewHolder= (ListHolder) holder;
        OrderComment item=dataSet.get(position);
        if (StringUtils.isMobileNO(item.getMobile())) {
            String a = item.getMobile().substring(0, 3) + "****" + item.getMobile().substring(7, 11);
            viewHolder.userNick.setText(a);
        } else {
            viewHolder.userNick.setText(item.getMobile());
        }
        viewHolder.commentTime.setText(item.getCommentTime());
        viewHolder.commentText.setText(item.getContent());
        viewHolder.rating.setRating(item.getAverage());
        if (!TextUtils.isEmpty(item.getLogo())) {
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(item.getLogo()))
                    .error(R.mipmap.login_logo)
                    .resize(appWidth / 6, appWidth / 6)
                    .into(viewHolder.avator);
        }
        if (item.getImages() != null && item.getImages().size() > 0) {
            CommentImageAdapter imageAdapter = new CommentImageAdapter(mContext, item.getImages());
            viewHolder.images.setAdapter(imageAdapter);
            imageAdapter.notifyDataSetChanged();
            imageAdapter.setOnClickListener(onClickListener);
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ListHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.avator)
        CircleImageView avator;
        @Bind(R.id.user_nick)
        TextView userNick;
        @Bind(R.id.rating)
        RatingBar rating;
        @Bind(R.id.comment_time)
        TextView commentTime;
        @Bind(R.id.comment_text)
        TextView commentText;
        @Bind(R.id.images)
        NoScrollGridView images;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
