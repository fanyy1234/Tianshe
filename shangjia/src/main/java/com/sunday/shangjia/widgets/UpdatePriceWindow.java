package com.sunday.shangjia.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.sunday.shangjia.R;


/**
 * Created by 刘涛 on 2017/1/3.
 */

public class UpdatePriceWindow extends Dialog {


    private Context context;
    private String title;
    private String buttonLeftText;
    private String buttonRightText;
    private UpdatePriceWindow.ClickListenerInterface clickListenerInterface;
    EditText tvMessage;


    public UpdatePriceWindow(Context context, String title,
                       String buttonLeftText, String buttonRightText) {
        super(context, com.sunday.common.R.style.UIAlertViewStyle);

        this.context = context;
        this.title = title;
        this.buttonLeftText = buttonLeftText;
        this.buttonRightText = buttonRightText;
    }

    public interface ClickListenerInterface {
        public void doLeft();

        public void doRight();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        inite();
    }

    public void inite() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.update_price_window, null);
        setContentView(view);

        tvMessage= (EditText) view.findViewById(com.sunday.common.R.id.tvMessage);
        TextView tvLeft = (TextView) view.findViewById(com.sunday.common.R.id.tvBtnLeft);
        TextView tvRight = (TextView) view.findViewById(com.sunday.common.R.id.tvBtnRight);
        TextView tvTitle = (TextView) view.findViewById(com.sunday.common.R.id.tvTitle);

        if ("".equals(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }
        tvLeft.setText(buttonLeftText);
        tvRight.setText(buttonRightText);

        tvLeft.setOnClickListener(new clickListener());
        tvRight.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();

        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(UpdatePriceWindow.ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    public String getPrice(){
        if (tvMessage!=null){
            return tvMessage.getText().toString().trim();
        }else {
            return null;
        }
    }

    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            String  tag = (String)v.getTag();
            switch (tag) {
                case "tvBtnLeft":
                    clickListenerInterface.doLeft();
                    break;
                case "tvBtnRight":
                    clickListenerInterface.doRight();
                    break;

                default:
                    break;
            }
        }
    }
}
