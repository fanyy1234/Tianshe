package com.sunday.member.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sunday.common.utils.Constants;
import com.sunday.member.R;
import com.sunday.member.entity.ApkInfo;
import com.sunday.member.utils.SharePerferenceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by wang on 2016/4/11.
 */
public class UpdateDialog extends Dialog implements View.OnClickListener{
    TextView updateContent;
    CheckBox updateCheck;
    Button updateOk,updateCancle;
    private OnUpdateClickListener onUpdateClickListener;
    public UpdateDialog(final Context context, ApkInfo apkInfo) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.umeng_update_dialog);
        updateContent = ButterKnife.findById(this,R.id.umeng_update_content);
        updateCheck = ButterKnife.findById(this,R.id.umeng_update_id_check);
        updateOk = ButterKnife.findById(this,R.id.umeng_update_id_ok);
        updateCancle = ButterKnife.findById(this,R.id.umeng_update_id_cancel);
        updateOk.setOnClickListener(this);
        updateCancle.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        updateContent.setText(apkInfo.getFileLogs());
        updateCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharePerferenceUtils.getIns(context).putBoolean(Constants.IGNOREVERSION,true);
                }else{
                    SharePerferenceUtils.getIns(context).putBoolean(Constants.IGNOREVERSION,false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.umeng_update_id_ok){
            dismiss();
            if(onUpdateClickListener!=null){
                onUpdateClickListener.onClick();
            }
        }else if(v.getId()==R.id.umeng_update_id_cancel){
            dismiss();

        }
    }

    public void setOnUpdateClickListener(OnUpdateClickListener onUpdateClickListener) {
        this.onUpdateClickListener = onUpdateClickListener;
    }

    public interface OnUpdateClickListener{
        public void onClick();
    }
}
