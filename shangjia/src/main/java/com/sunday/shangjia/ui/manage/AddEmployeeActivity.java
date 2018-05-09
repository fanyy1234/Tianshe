package com.sunday.shangjia.ui.manage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.imageselector.MultiImageSelectorActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ImageUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.CircleImageView;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.member.utils.UploadUtils;
import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.Image;
import com.sunday.shangjia.entity.Staff;
import com.sunday.shangjia.http.AppClient;
import com.sunday.shangjia.util.ImgUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/21.
 */

public class AddEmployeeActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.employee_logo)
    CircleImageView employeeLogo;
    @Bind(R.id.camera)
    ImageView camera;
    @Bind(R.id.rl_add_photo)
    RelativeLayout rlAddPhoto;
    @Bind(R.id.edit_name)
    ClearEditText editName;
    @Bind(R.id.edit_phone)
    ClearEditText editPhone;
    private String id = "0";

    int appWidth;
    String memberId;
    boolean isEdit=false;
    Staff staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        ButterKnife.bind(this);
        titleView.setText("添加服务人员");
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setText("添加");
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
        memberId= SharePerferenceUtils.getIns(mContext).getString("shopId","");
        isEdit=getIntent().getBooleanExtra("isEdit",false);
        staff= (Staff) getIntent().getSerializableExtra("staff");
        if (isEdit){
            initView();
        }
    }

    private void initView(){
        if (staff==null){return;}
        if (!TextUtils.isEmpty(staff.getLogo())){
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(staff.getLogo()))
                    .into(employeeLogo);
        }
        editName.setText(staff.getName());
        editPhone.setText(staff.getMobile());
        id=staff.getId();
    }

    private final static int REQUEST_CHOOSE_IMAGE = 0x1111;

    @OnClick({R.id.employee_logo, R.id.camera})
    void selectPhoto() {
        intent = new Intent(mContext, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        startActivityForResult(intent, REQUEST_CHOOSE_IMAGE);
    }

    private ArrayList<String> mSelectPath = new ArrayList<>();
    private ArrayList<String> mCompressPath = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        mSelectPath.clear();
        mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
        mCompressPath.clear();
        for (String item : mSelectPath) {
            mCompressPath.add(ImageUtils.getCompressImagePath(item));
        }
        updateImageView();
    }

    private void updateImageView() {
        if (TextUtils.isEmpty(mCompressPath.get(0))) {
            return;
        }
        Picasso.with(mContext)
                .load(new File(mCompressPath.get(0)))
                .placeholder(R.mipmap.logo)
                .error(R.mipmap.logo)
                .resize(appWidth / 5, appWidth / 5)
                .centerInside()
                .into(employeeLogo);

    }

    String saveUrl;

    @OnClick(R.id.rightTxt)
    void add() {
        showLoadingDialog(0);
        if (TextUtils.isEmpty(mCompressPath.get(0))) {
            addStaff();
            return;
        }
        Call<ResultDO<Image>> call = AppClient.getAppAdapter().saveImage(UploadUtils.getRequestBody(mCompressPath.get(0)));
        call.enqueue(new Callback<ResultDO<Image>>() {
            @Override
            public void onResponse(Call<ResultDO<Image>> call, Response<ResultDO<Image>> response) {
                dissMissDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0 && response.body().getResult() != null) {
                    saveUrl = response.body().getResult().getViewUrl();
                    addStaff();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Image>> call, Throwable t) {
                dissMissDialog();
            }
        });


    }

    private void addStaff() {
        String name = editName.getText().toString().trim();
        String mobile = editPhone.getText().toString().trim();
        Call<ResultDO> call=AppClient.getAppAdapter().saveStaff(id,memberId,name,saveUrl,mobile);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,"添加成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });
    }


}
