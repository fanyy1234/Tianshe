package com.sunday.tianshehuoji.widgets;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.PixUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.shop.SellerShopProductListBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.R.attr.key;
import static com.baidu.location.i.i.F;

/**
 * Created by 刘涛 on 2016/12/27.
 * <p>
 * 购物明细
 */

public class ProductSettleWindow {

    @Bind(R.id.total_money)
    TextView txt_totalMoney;
    @Bind(R.id.check_detail)
    TextView checkDetail;
    @Bind(R.id.btn_buy)
    TextView btnBuy;

    private RelativeLayout relativeLayout;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    private String totalMoney;

    public ProductSettleWindow(Context context, View.OnClickListener onClickListener) {
        //获取Service
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        relativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.product_buy_layout, null);
        ButterKnife.bind(this, relativeLayout);

        // 设置窗口类型，一共有三种Application windows, Sub-windows, System windows
        // API中以TYPE_开头的常量有23个
        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 设置期望的bitmap格式
        mWindowParams.format = PixelFormat.RGBA_8888;
        int tempHeight = PixUtils.dip2px(context, 55);
        int height = DeviceUtils.getDisplay(context).heightPixels - tempHeight;
        // 以下属性在Layout Params中常见重力、坐标，宽高
        mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowParams.x = 0;
        mWindowParams.y = height;

        mWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowParams.height = tempHeight;

        // 添加指定视图
        mWindowManager.addView(relativeLayout, mWindowParams);
        checkDetail.setOnClickListener(onClickListener);
        btnBuy.setOnClickListener(onClickListener);

    }

    public void onResume() {
        //添加指定视图
        relativeLayout.setVisibility(View.VISIBLE);
    }

    public void onStop() {
        relativeLayout.setVisibility(View.GONE);
    }

    public void destroy() {
        mWindowManager.removeViewImmediate(relativeLayout);
    }

    public void updateView(SparseArray<List<SellerShopProductListBean>> cart) {
        if (cart.size() == 0) {
            txt_totalMoney.setText("￥");
            return;
        }
        int key = 0;
        int number = 0;
        BigDecimal totlePriceNum = new BigDecimal(0);
        for (int i = 0; i < cart.size(); i++) {
            key = cart.keyAt(i);
            List<SellerShopProductListBean> productList = cart.get(key);
            int size = productList.size();
            SellerShopProductListBean item = productList.get(0);
            totlePriceNum = totlePriceNum.add(item.getPrice().multiply(new BigDecimal(size)));
            number += productList.size();
        }
        txt_totalMoney.setText(String.format("￥%s", totlePriceNum.setScale(2, RoundingMode.HALF_UP)));
        totalMoney=txt_totalMoney.getText().toString();
    }

    public String getTotalMoney() {
        return totalMoney;
    }
}
