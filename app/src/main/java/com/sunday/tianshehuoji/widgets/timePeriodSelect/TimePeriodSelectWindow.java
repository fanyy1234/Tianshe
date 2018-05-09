package com.sunday.tianshehuoji.widgets.timePeriodSelect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.widgets.ProductSettleWindow;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/1/5.
 */

public class TimePeriodSelectWindow extends PopupWindow {

    @Bind(R.id.time_picker1)
    TimePicker timePicker1;
    @Bind(R.id.time_picker2)
    TimePicker timePicker2;
    private TimePeriodSelectWindow cartListWindow;
    private Context mContenxt;
    private View contentView;

    @Bind(R.id.btn_submit)
    Button btnSubmit;

    private String startPeriod, endPeriod;

    private int startTime, endTime;
    final ProductSettleWindow productSettleWindow;
    private OnSelectListener onSelectListener;

    public TimePeriodSelectWindow(Context context, ProductSettleWindow window) {
        mContenxt = context;
        cartListWindow = this;
        contentView = LayoutInflater.from(mContenxt).inflate(R.layout.select_time_period, null);
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        this.productSettleWindow = window;
        btnSubmit.setOnClickListener(onClickListener);
        TimePickerListener listener = new TimePickerListener();
        timePicker1.setIs24HourView(true);
        timePicker2.setIs24HourView(true);
        startPeriod = String.format("%1d:%2d", timePicker1.getCurrentHour(), timePicker1.getCurrentMinute());
        endPeriod = String.format("%1d:%2d", timePicker2.getCurrentHour(), timePicker2.getCurrentMinute());
        startTime = timePicker1.getChildCount() * 60 + timePicker1.getCurrentMinute();
        endTime = timePicker2.getCurrentHour() * 60 + timePicker2.getCurrentMinute();
        timePicker1.setOnTimeChangedListener(listener);
        timePicker2.setOnTimeChangedListener(listener);
        ColorDrawable cd = new ColorDrawable(0x000000);
        setBackgroundDrawable(cd);
        setOutsideTouchable(true);
        setFocusable(true);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOnDismissListener(new OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) mContenxt).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) mContenxt).getWindow().setAttributes(lp);
                productSettleWindow.onResume();
            }
        });


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_submit:
                    if (startTime >= endTime) {
                        return;
                    }
                    if (TextUtils.isEmpty(startPeriod) || TextUtils.isEmpty(endPeriod)) {
                        return;
                    }
                    if (onSelectListener != null) {
                        onSelectListener.onSelect(startPeriod, endPeriod);
                        dismiss();
                    }
                    break;
            }
        }
    };

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }


    private class TimePickerListener implements TimePicker.OnTimeChangedListener {

        @SuppressLint("DefaultLocale")
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            switch (view.getId()) {
                case R.id.time_picker1:
                    startPeriod = String.format("%1d:%2d", hourOfDay, minute);
                    startTime = hourOfDay * 60 + minute;
                    break;
                case R.id.time_picker2:
                    endPeriod = String.format("%1d:%2d", hourOfDay, minute);
                    endTime = hourOfDay * 60 + minute;
                    break;
            }

        }
    }


    public void show(View view) {
        if (cartListWindow.isShowing()) {
            return;
        }
        productSettleWindow.onStop();
        //产生背景变暗效果
        WindowManager.LayoutParams lp = ((Activity) mContenxt).getWindow().getAttributes();
        lp.alpha = 0.4f;
        ((Activity) mContenxt).getWindow().setAttributes(lp);
        cartListWindow.showAtLocation(view,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    public interface OnSelectListener {
        void onSelect(String startPeriod, String endPeriod);
    }


}