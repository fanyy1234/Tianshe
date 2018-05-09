package com.sunday.tianshe.branch.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.imageselector.MultiImageSelectorActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ImageUtils;
import com.sunday.common.utils.TimeCount;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.pickerview.TimePickerView;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.entity.Image;
import com.sunday.member.utils.UploadUtils;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.entity.BaseMember;
import com.sunday.tianshe.branch.entity.Stock;
import com.sunday.tianshe.branch.http.ApiClient;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 会员认证
 */
public class MemberAuthActivity extends BaseActivity {
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.ret_name)
    TextView retName;
    @Bind(R.id.radioButtonLeft)
    RadioButton radioButtonLeft;
    @Bind(R.id.radioButtonRight)
    RadioButton radioButtonRight;
    @Bind(R.id.member_name)
    EditText memberName;
    @Bind(R.id.default_pwd)
    EditText defaultPwd;
    @Bind(R.id.mobile)
    EditText mobile;
    @Bind(R.id.validate_code)
    EditText validateCode;
    @Bind(R.id.get_code)
    TextView getCode;
    @Bind(R.id.id_card)
    EditText idCard;
    @Bind(R.id.idcard_left)
    ImageView idcardLeft;
    @Bind(R.id.idcard_right)
    ImageView idcardRight;
    @Bind(R.id.contract_img_left)
    ImageView contractImgLeft;
    @Bind(R.id.contract_img_right)
    ImageView contractImgRight;

    @Bind(R.id.member_level)
    TextView memberLevel;
    @Bind(R.id.member_money)
    TextView memberMoney;
    @Bind(R.id.member_time)
    TextView memberTime;

    private String imgPath1, imgPath2, imgPath3, imgPath4;

    private int imgWidth;
    private TimeCount timeCount;
    List<String> paths = new ArrayList<>(4);
    private SparseArray<String> maps = new SparseArray<>();

    private int type = 2;//会员类型
    private Long recId = -1L;

    private String imgName1,imgName2,imgName3,imgName4;//本地选择图片路径

    private String netImg1,netImg2,netImg3,netImg4;//上传后线上路径

    private List<Stock> stocks = new ArrayList<>(4);
    private  String [] levels = null;
    private TimePickerView timePickerView;
    private String memberTimeStr;
    private Stock stock;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_auth);
        ButterKnife.bind(this);
        imgWidth = DeviceUtils.getDisplay(mContext).widthPixels / 2;
        timeCount = new TimeCount(60000, 1000, getCode);
        //选择CEO还是vip
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==R.id.radioButtonLeft){
                    type = 2;
                }else{
                    type = 1;
                }
                if(stock!=null){
                    if(type==2){
                        memberMoney.setText(stock.ceoMoney);
                    }else{
                        memberMoney.setText(stock.vipMoney);
                    }
                }
            }
        });
        timePickerView = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                memberTimeStr = sdf.format(date);
                memberTime.setText(memberTimeStr);
            }
        });
        timePickerView.setCyclic(false);
        getStockList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0x333:
                    ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (mSelectPath != null && mSelectPath.size() > 0) {
                        imgPath1 = ImageUtils.getCompressImagePath(mSelectPath.get(0));
                        maps.put(0, imgPath1);
                        File file = new File(imgPath1);
                        imgName1 = file.getName();
                        Picasso.with(mContext)
                                .load(file)
                                .resize(imgWidth, imgWidth * 9 / 16)
                                .into(idcardLeft);
                    }
                    break;
                case 0x444:
                    mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (mSelectPath != null && mSelectPath.size() > 0) {
                        imgPath2 = ImageUtils.getCompressImagePath(mSelectPath.get(0));
                        maps.put(1, imgPath2);
                        File file = new File(imgPath2);
                        imgName2 = file.getName();
                        Picasso.with(mContext)
                                .load(file)
                                .resize(imgWidth, imgWidth * 9 / 16)
                                .into(idcardRight);
                    }
                    break;
                case 0x111:
                    mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (mSelectPath != null && mSelectPath.size() > 0) {
                        imgPath3 = ImageUtils.getCompressImagePath(mSelectPath.get(0));
                        maps.put(2, imgPath3);
                        File file = new File(imgPath3);
                        imgName3 = file.getName();
                        Picasso.with(mContext)
                                .load(file)
                                .resize(imgWidth, imgWidth * 9 / 16)
                                .into(contractImgLeft);
                    }
                    break;
                case 0x222:
                    mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (mSelectPath != null && mSelectPath.size() > 0) {
                        imgPath4 = ImageUtils.getCompressImagePath(mSelectPath.get(0));
                        maps.put(3, imgPath4);
                        File file = new File(imgPath4);
                        imgName4 = file.getName();
                        Picasso.with(mContext)
                                .load(file)
                                .resize(imgWidth, imgWidth * 9 / 16)
                                .into(contractImgRight);
                    }
                    break;
                case 0x555:
                    BaseMember member = (BaseMember) data.getSerializableExtra("data");
                    if (member != null) {
                        retName.setText(member.userName);
                        recId = Long.parseLong(member.recId);
                    }
                    break;
            }
        }
    }

    @OnClick(R.id.ret_name)
    void chooseRetMember() {
        intent = new Intent(mContext, RecommondMemberActivity.class);
        startActivityForResult(intent, 0x555);
    }

    @OnClick(R.id.member_level)
    void memberLevel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("会员批次");
        //    指定下拉列表的显示数据
        //    设置一个下拉的列表选择项
        builder.setItems(levels, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                memberLevel.setText(stocks.get(which).name);
                stock = stocks.get(which);
                if(type==2){
                    memberMoney.setText(stock.ceoMoney);
                }else{
                    memberMoney.setText(stock.vipMoney);
                }
            }
        });
        builder.show();
    }

    @OnClick(R.id.get_code)
    void sendValicateCode() {
        sendValidateCode();
    }


    @OnClick(R.id.member_time)
    void memberTime(){
        if(!timePickerView.isShowing()){
            timePickerView.show();
        }

    }


    @OnClick(R.id.idcard_left)
    void idCardLeft() {
        requestCamera(0x333);
    }

    @OnClick(R.id.idcard_right)
    void idCardRight() {
        requestCamera(0x444);
    }


    @OnClick(R.id.contract_img_left)
    void contractImgLeft() {
        requestCamera(0x111);
    }

    @OnClick(R.id.contract_img_right)
    void contractImgRight() {
        requestCamera(0x222);
    }

    @OnClick(R.id.submit_btn)
    void submitBtn() {
        if(judge()){
            uploadImg();
        }
    }

    private void requestCamera(int requestCode) {
        intent = new Intent(mContext, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        startActivityForResult(intent, requestCode);
    }

    private boolean judge(){
        String userNameTxt = memberName.getText().toString();
        String pwd = defaultPwd.getText().toString();
        String mobileTxt =mobile.getText().toString();
        String codeTxt = validateCode.getText().toString();
        String idCardTxt = idCard.getText().toString();
        String memberMoneyStr = memberMoney.getText().toString();
        if(recId==-1){
            ToastUtils.showToast(mContext,"请选择推荐人");
            return false;
        }
        if(stock==null){
            ToastUtils.showToast(mContext,"请选择会员批次");
            return false;
        }
        if(TextUtils.isEmpty(memberMoneyStr)){
            ToastUtils.showToast(mContext,"请输入会员认证金额");
            return false;
        }
        if(TextUtils.isEmpty(memberTimeStr)){
            ToastUtils.showToast(mContext,"请选择推荐时间");
            return false;
        }
        if(TextUtils.isEmpty(userNameTxt)){
            ToastUtils.showToast(mContext,"请输入会员名称");
            return false;
        }
        if(TextUtils.isEmpty(pwd)){
            ToastUtils.showToast(mContext,"请输入会员密码");
            return false;
        }
        if(TextUtils.isEmpty(mobileTxt)){
            ToastUtils.showToast(mContext,"请输入会员手机号");
            return false;
        }
        if(TextUtils.isEmpty(idCardTxt)){
            ToastUtils.showToast(mContext,"请输入会员身份证");
            return false;
        }
        if(maps.size()!=4){
            ToastUtils.showToast(mContext,"请选择上传的图片");
            return false;
        }
        if(TextUtils.isEmpty(codeTxt)){
            ToastUtils.showToast(mContext,"请输入验证码");
            return false;
        }

        return true;
    }

    private void uploadImg() {
        for (int i = 0; i < maps.size(); i++) {
            paths.add(maps.valueAt(i));
        }
        showLoadingDialog(0);
        Call<ResultDO<List<Image>>> call = ApiClient.getMemberAdapter().uploadImgs(UploadUtils.getRequestBody(paths));
        call.enqueue(new Callback<ResultDO<List<Image>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Image>>> call, Response<ResultDO<List<Image>>> response) {
                dissMissDialog();
                ResultDO<List<Image>> resultDO = response.body();
                if (resultDO != null) {
                    if (resultDO.getCode() == 0) {
                        for(Image item:resultDO.getResult()){
                            if(item.getName().equals(imgName1)){
                                netImg1 = item.getViewUrl();
                            }else if(item.getName().equals(imgName2)){
                                netImg2 = item.getViewUrl();
                            }else if(item.getName().equals(imgName3)){
                                netImg3 = item.getViewUrl();
                            }else{
                                netImg4 = item.getViewUrl();
                            }
                        }
                        memberAuth();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Image>>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }


    private void memberAuth(){
        showLoadingDialog(0);
        String userNameTxt = memberName.getText().toString();
        String pwd = defaultPwd.getText().toString();
        String mobileTxt =mobile.getText().toString();
        String codeTxt = validateCode.getText().toString();
        String idCardTxt = idCard.getText().toString();
        String idCardImg = netImg1.concat(",").concat(netImg2);
        String contractImg = netImg3.concat(",").concat(netImg4);
        String memberMoneyStr = memberMoney.getText().toString();
        Call<ResultDO> call = ApiClient.getApiAdapter().memberAuth(type,userNameTxt,pwd,mobileTxt,idCardTxt,recId,idCardImg,contractImg,codeTxt,memberTimeStr,stock.id,memberMoneyStr);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                if(response.body()!=null){
                    if(response.body().getCode()==0){
                        ToastUtils.showToast(mContext,"提交成功,等待审核");
                        intent = new Intent(mContext,MemberAuthSuccessActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        ToastUtils.showToast(mContext,response.body().getMessage());
                    }
                }else{
                    ToastUtils.showToast(mContext,"网络异常");
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
            }
        });
    }


    private void sendValidateCode() {
        String mobileTxt = mobile.getText().toString();
        if (TextUtils.isEmpty(mobileTxt)) {
            ToastUtils.showToast(mContext, "请填写手机号码");
            return;
        }
        Call<ResultDO> call = ApiClient.getApiAdapter().sendValidateCode(1, mobileTxt);
        showLoadingDialog(0);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                if (timeCount != null && response.body() != null && response.body().getCode() == 0) {
                    timeCount.start();
                } else {
                    ToastUtils.showToast(mContext, "发送验证码失败");
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
            }
        });
    }

    private void getStockList(){
        Call<ResultDO<List<Stock>>> call = ApiClient.getApiAdapter().stockList(0);
        call.enqueue(new Callback<ResultDO<List<Stock>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Stock>>> call, Response<ResultDO<List<Stock>>> response) {
                ResultDO<List<Stock>> resultDO = response.body();
                if(resultDO!=null){
                    if(resultDO.getCode()==0){
                        levels = new String[resultDO.getResult().size()];
                        int i = 0;
                        for(Stock item:resultDO.getResult()){
                            stocks.add(item);
                            levels[i] = item.name;
                            i++;
                         }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Stock>>> call, Throwable t) {

            }
        });
    }

}
