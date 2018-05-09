package com.sunday.tianshehuoji.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.CartDetailAdapter;
import com.sunday.tianshehuoji.entity.shop.SellerShopProductListBean;
import com.sunday.tianshehuoji.ui.fragment.product.ProductFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/1/5.
 */

public class CartDetailWindow extends PopupWindow {

    private CartDetailWindow cartListWindow;
    private Context mContenxt;
    private View contentView;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.hide_detail)
    TextView hideDetail;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.total_money)
    TextView totalMoney;
    private CartDetailAdapter cartListAdapter;
    final ProductSettleWindow productSettleWindow;
    public CartDetailWindow(Context context, SparseArray<List<SellerShopProductListBean>> cartList,
                            View.OnClickListener onClickListener, String money,  ProductSettleWindow window){
        mContenxt =context;
        cartListWindow = this;
        contentView = LayoutInflater.from(mContenxt).inflate(R.layout.cart_detail, null);
        this.productSettleWindow=window;
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        hideDetail.setOnClickListener(onClickListener);
        btnSubmit.setOnClickListener(onClickListener);
        cartListAdapter = new CartDetailAdapter(mContenxt,cartList);
        listView.setAdapter(cartListAdapter);
        totalMoney.setText(money);
        ColorDrawable cd = new ColorDrawable(0x000000);
        setBackgroundDrawable(cd);
        setOutsideTouchable(true);
        setFocusable(true);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOnDismissListener(new OnDismissListener(){

            //在dismiss中恢复透明度
            public void onDismiss(){
                WindowManager.LayoutParams lp=((Activity)mContenxt).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity)mContenxt).getWindow().setAttributes(lp);
                productSettleWindow.onResume();
            }
        });

    }

    public void updateList(String money){
        totalMoney.setText(money);
        cartListAdapter.notifyDataSetChanged();
    }

    public void show(View view){
        if(cartListWindow.isShowing()){
            return ;
        }
        productSettleWindow.onStop();
        //产生背景变暗效果
        WindowManager.LayoutParams lp= ((Activity)mContenxt).getWindow().getAttributes();
        lp.alpha = 0.4f;
        ((Activity)mContenxt).getWindow().setAttributes(lp);
        cartListWindow.showAtLocation(view,
                Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
