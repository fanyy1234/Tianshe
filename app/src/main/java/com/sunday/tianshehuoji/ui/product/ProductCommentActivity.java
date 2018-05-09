package com.sunday.tianshehuoji.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sunday.common.imageselector.MultiImageSelectorActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ImageUtils;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.gridview.NoScrollGridView;
import com.sunday.member.base.BaseActivity;

import com.sunday.member.utils.UploadUtils;
import com.sunday.tianshehuoji.BaseApplication;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.AddCommentImgAdapter;
import com.sunday.tianshehuoji.entity.Image;
import com.sunday.tianshehuoji.http.AppClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/19.
 */

public class ProductCommentActivity extends BaseActivity implements AddCommentImgAdapter.PicCallBack {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.environ_star)
    RatingBar environStar;
    @Bind(R.id.service_star)
    RatingBar serviceStar;
    @Bind(R.id.quality_star)
    RatingBar qualityStar;
    @Bind(R.id.edit_comment)
    EditText editComment;
    @Bind(R.id.images)
    NoScrollGridView gridView;

    private String orderId,memberId,shopId,sellerId;
    private AddCommentImgAdapter adapter;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private ArrayList<String> compressImgPath = new ArrayList<>();
    private List<String> netImages = new ArrayList<>();
    private final int REQUEST_IMAGE = 10;
    int maxNum=6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        titleView.setText("评价");
        orderId=getIntent().getStringExtra("orderId");
        memberId=getIntent().getStringExtra("memberId");
        shopId=getIntent().getStringExtra("shopId");
        sellerId= BaseApplication.getInstance().getSellerId();
        adapter = new AddCommentImgAdapter(mContext, compressImgPath,maxNum, this);
        gridView.setAdapter(adapter);
    }


    @OnClick(R.id.btn_submit)
    void submit(){
        String content=editComment.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            ToastUtils.showToast(mContext, "请填写具体内容", ToastUtils.LENGTH_SHORT);
            return;
        }
        //先上传图片
        showLoadingDialog(0);
        if (mSelectPath != null && mSelectPath.size() > 0) {
            Call<ResultDO<List<Image>>> call = AppClient.getAppAdapter().saveImages(UploadUtils.getRequestBody(compressImgPath));
            call.enqueue(new Callback<ResultDO<List<Image>>>() {
                @Override
                public void onResponse(Call<ResultDO<List<Image>>> call, Response<ResultDO<List<Image>>> response) {
                    dissMissDialog();
                    ResultDO<List<Image>> resultDO = response.body();
                    if (resultDO != null && resultDO.getCode() == 0) {
                        netImages.clear();
                        for (Image item : resultDO.getResult()) {
                            netImages.add(item.getSaveUrl());
                        }
                        submitBack();
                    } else {
                        ToastUtils.showToast(mContext, resultDO.getMessage());
                        dissMissDialog();
                    }
                }

                @Override
                public void onFailure(Call<ResultDO<List<Image>>> call, Throwable t) {
                    dissMissDialog();
                }
            });

        }else{
            submitBack();
        }
    }
    private void submitBack(){
        int score1= (int) environStar.getRating();
        int score2= (int) serviceStar.getRating();
        int score3= (int) qualityStar.getRating();
        String content=editComment.getText().toString().trim();
        Call<ResultDO> call= AppClient.getAppAdapter().saveComment(memberId,sellerId,orderId,shopId,content,StringUtils.listToString(netImages),
                score1,score2,score3);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                ResultDO resultDO=response.body();
                if(resultDO!=null&&resultDO.getCode()==0){
                    ToastUtils.showToast(mContext,"提交成功",ToastUtils.LENGTH_SHORT);
                    finish();
                }else {
                    ToastUtils.showToast(mContext,resultDO.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {

            }
        });
    }

    @Override
    public void addPic() {
        intent = new Intent(mContext, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void delPic(int position) {
        compressImgPath.remove(position);
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE:
                    mSelectPath.clear();
                    mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    compressImgPath.clear();
                    if(mSelectPath!=null){
                        for(String item:mSelectPath){
                            compressImgPath.add(ImageUtils.getCompressImagePath(item));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }

    }

}
