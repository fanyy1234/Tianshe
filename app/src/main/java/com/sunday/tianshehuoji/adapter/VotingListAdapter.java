package com.sunday.tianshehuoji.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.VoteItem;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.ui.manage.VotingDetailActivity;
import com.sunday.tianshehuoji.utils.ImgUtils;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/20.
 */

public class VotingListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<VoteItem> dataSet;
    private int appWidth;
    private String memberId;

    public VotingListAdapter(Context context, List<VoteItem> voteItems) {
        this.mContext = context;
        this.dataSet = voteItems;
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_voting_item, null);
        return new ListHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder viewHolder = (ListHolder) holder;
        VoteItem item = dataSet.get(position);
        if (!TextUtils.isEmpty(item.getImage())) {
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(item.getImage()))
                    .resize(appWidth / 5, appWidth / 5)
                    .into(viewHolder.votingImg);
        }
        viewHolder.votingTitle.setText(item.getTitle());
        viewHolder.votingTime.setText(String.format("投票截止：%s", item.getDeadline()));
        viewHolder.txtAgree.setText(String.format("赞成（%d)", item.getAgree()));
        viewHolder.txtDisagree.setText(String.format("反对（%d)", item.getAgainst()));
        viewHolder.txtAgree.setTag(position);
        viewHolder.txtDisagree.setTag(position);
        viewHolder.rlVoting.setTag(position);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.voting_img)
        ImageView votingImg;
        @Bind(R.id.voting_title)
        TextView votingTitle;
        @Bind(R.id.voting_time)
        TextView votingTime;
        @Bind(R.id.view)
        View view;
        @Bind(R.id.rl_voting)
        RelativeLayout rlVoting;
        @Bind(R.id.txt_agree)
        TextView txtAgree;
        @Bind(R.id.txt_disagree)
        TextView txtDisagree;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rlVoting.setOnClickListener(this);
            txtAgree.setOnClickListener(this);
            txtDisagree.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int p = (int) v.getTag();
            VoteItem item = dataSet.get(p);
            switch (v.getId()) {
                case R.id.rl_voting:
                    Intent intent = new Intent(mContext, VotingDetailActivity.class);
                    intent.putExtra("id", item.getId());
                    mContext.startActivity(intent);
                    break;
                case R.id.txt_agree:
                    updateVote(item.getId(), 0, p);
                    break;
                case R.id.txt_disagree:
                    updateVote(item.getId(),1,p);
                    break;
            }
        }
    }

    private void updateVote(String id, final int type, final int position) {
        Call<ResultDO> call = AppClient.getAppAdapter().memberVote(memberId, id, type);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (type == 0) {
                        dataSet.get(position).setAgree(dataSet.get(position).getAgree() + 1);
                    } else {
                        dataSet.get(position).setAgainst(dataSet.get(position).getAgainst() + 1);
                    }
                    notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }
}
