package com.sunday.tianshehuoji.ui.manage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.VoteItem;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.utils.ImgUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/20.
 */

public class VotingDetailActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.vote_img)
    ImageView voteImg;
    @Bind(R.id.vote_title)
    TextView voteTitle;
    @Bind(R.id.voting_time)
    TextView votingTime;
    @Bind(R.id.txt_agree)
    TextView txtAgree;
    @Bind(R.id.txt_disagree)
    TextView txtDisagree;
    @Bind(R.id.webView)
    WebView webView;

    private String voteId;
    private VoteItem voteItem;
    private int appWidth;
    private String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_detail);
        ButterKnife.bind(this);
        voteId=getIntent().getStringExtra("id");
        appWidth= DeviceUtils.getDisplay(mContext).widthPixels;
        memberId= SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"");
        getData();


    }

    private void getData(){
        Call<ResultDO<VoteItem>> call= AppClient.getAppAdapter().getVoteDetail(voteId);
        call.enqueue(new Callback<ResultDO<VoteItem>>() {
            @Override
            public void onResponse(Call<ResultDO<VoteItem>> call, Response<ResultDO<VoteItem>> response) {
                ResultDO<VoteItem> resultDO=response.body();
                if (response.body()==null){return;}
                if (resultDO.getCode()==0){
                    voteItem=resultDO.getResult();
                    updateView();
                }else {
                    ToastUtils.showToast(mContext,resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<VoteItem>> call, Throwable t) {

            }
        });
    }

    private void updateView(){
        if (voteItem==null){return;}
        if (!TextUtils.isEmpty(voteItem.getImage())){
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(voteItem.getImage()))
                    .resize(appWidth,appWidth*3/5)
                    .centerInside()
                    .into(voteImg);
        }

        voteTitle.setText(voteItem.getTitle());
        votingTime.setText(String.format("投票截止：%s", voteItem.getDeadline()));
        txtAgree.setText(String.format("赞成（%d)", voteItem.getAgree()));
        txtDisagree.setText(String.format("反对（%d)", voteItem.getAgainst()));
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadData(voteItem.getContent(), "text/html;charset=UTF-8",null);
    }

    @OnClick(R.id.txt_agree)
    void agree(){
        updateVote(0);
    }

    @OnClick(R.id.txt_disagree)
    void disagree(){
        updateVote(1);
    }

    private void updateVote(final int type){
        if (voteItem==null){return;}
        Call<ResultDO> call = AppClient.getAppAdapter().memberVote(memberId,voteItem.getId(),type);
        call.enqueue(new Callback<ResultDO>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext,"投票成功");
                    if (type == 0) {
                        txtAgree.setText(String.format("赞成（%d)", voteItem.getAgree()+1));
                    } else {
                        txtDisagree.setText(String.format("反对（%d)", voteItem.getAgainst()+1));
                    }
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
