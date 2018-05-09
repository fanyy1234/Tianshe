package com.sunday.member.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.widget.EditText;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.R;
import com.sunday.member.base.BaseActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/2/23.
 */
public class RegistStepTwoActivity extends BaseActivity {

    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private SmsObserver smsObserver;
    private String mobile ;
    private EditText validateCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_regist_step_two);
        mobile = getIntent().getStringExtra("mobile");

        TextView userMobile = ButterKnife.findById(this,R.id.mobile_txt);
        validateCode = ButterKnife.findById(this,R.id.validate_code);
        userMobile.setText(String.format(mContext.getString(R.string.mobile_cn),mobile));
        smsObserver = new SmsObserver(this, smsHandler);
        getContentResolver().registerContentObserver(SMS_INBOX, true,smsObserver);
    }

    public Handler smsHandler = new Handler() {
        // TODO
        public void handleMessage(android.os.Message msg) {
            System.out.println("smsHandler 执行了.....");
        }
    };


    public void getSmsFromPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[] { "body","address","person"};// "_id", "address",
        String where = " date >  " + (System.currentTimeMillis() - 10 * 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return;
        if (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));// 手机号
            String name = cur.getString(cur.getColumnIndex("person"));// 联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));

            System.out.println(">>>>>>>>>>>>>>>>手机号：" + number);
            System.out.println(">>>>>>>>>>>>>>>>联系人姓名列表：" + name);
            System.out.println(">>>>>>>>>>>>>>>>短信的内容：" + body);

            // 这里我是要获取自己短信服务号码中的验证码~~
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]{4}");
            Matcher matcher = pattern.matcher(body);//String body="测试验证码2346ds";
            if (matcher.find()) {
                String res = matcher.group().substring(0, 4);// 获取短信的内容
                validateCode.setText(res);
                System.out.println(res);
            }
        }
    }


    class SmsObserver extends ContentObserver {

        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            // read phone state用于获取 imei 设备信息
            boolean readSmsPermission =
                    getPackageManager().checkPermission(Manifest.permission.READ_SMS, getPackageName()) == PackageManager.PERMISSION_GRANTED;

            if (Build.VERSION.SDK_INT >= 23 && !readSmsPermission) {
                String permission = Manifest.permission.READ_SMS;
                if(ActivityCompat.checkSelfPermission(RegistStepTwoActivity.this, permission)
                        == PackageManager.PERMISSION_GRANTED) {
                    getSmsFromPhone();
                } else {
                    PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(RegistStepTwoActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE}, new PermissionsResultAction() {
                                @Override
                                public void onGranted() {
                                    getSmsFromPhone();
                                }
                                @Override
                                public void onDenied(String permission) {
                                    ToastUtils.showToast(mContext,"没有权限");
                                }
                            });
                }

            }else{
                 getSmsFromPhone();
            }
        }
    }
}
