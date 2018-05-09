package com.sunday.tianshehuoji.ui.fragment.product;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.NoScrollListview;
import com.sunday.member.base.BaseFragment;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.BaseApplication;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.ProductListAdapter;
import com.sunday.tianshehuoji.adapter.ProductTypeAdapter;
import com.sunday.tianshehuoji.entity.Const;
import com.sunday.tianshehuoji.entity.order.OrderConfirm;
import com.sunday.tianshehuoji.entity.shop.SellerShopProductListBean;
import com.sunday.tianshehuoji.entity.shop.ShopDetail;
import com.sunday.tianshehuoji.entity.shop.ShopProduct;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.ui.product.ConfirmBuyActivity;
import com.sunday.tianshehuoji.ui.product.KtvDetailActivity;
import com.sunday.tianshehuoji.ui.product.RoomDetailActivity;
import com.sunday.tianshehuoji.ui.product.SelectRoomActivity;
import com.sunday.tianshehuoji.ui.product.SelectServiceManActivity;
import com.sunday.tianshehuoji.widgets.CartDetailWindow;
import com.sunday.tianshehuoji.widgets.ProductSettleWindow;
import com.sunday.tianshehuoji.widgets.ScrollableHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static java.io.FileDescriptor.in;


/**
 * Created by 刘涛 on 2016/12/15.
 */

public class ProductFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {

    @Bind(R.id.lv_Left)
    ListView lvLeft;
    @Bind(R.id.lv_right)
    ListView lvRight;
    @Bind(R.id.root_layout)
    LinearLayout rootLayout;


    private String shopId;
    private String memberId;
    private String shopType;
    ShopDetail shopDetail;
    private List<ShopProduct> shopProductList = new ArrayList<>();
    private List<SellerShopProductListBean> sellerShopProductList = new ArrayList<>();
    private SparseArray<List<SellerShopProductListBean>> cart = new SparseArray<>();

    private ProductTypeAdapter adapter;
    private ProductListAdapter mAdapter;

    private ProductSettleWindow productSettleWindow;
    private CartDetailWindow cartDetailWindow;

    private String startTime;
    private String endTime;
    private String jsonresult = "";
    private OrderConfirm orderConfirm;
    MyOnclickListener onClickListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_product, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            shopId = getArguments().getString("shopId");
            shopType = getArguments().getString("shopType");
        }
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
        onClickListener = new MyOnclickListener();
        productSettleWindow = new ProductSettleWindow(mContext, onClickListener);
        cartDetailWindow = new CartDetailWindow(mContext, cart, onClickListener, productSettleWindow.getTotalMoney(), productSettleWindow);
        adapter = new ProductTypeAdapter(mContext, shopProductList);
        lvLeft.setAdapter(adapter);
        mAdapter = new ProductListAdapter(mContext, sellerShopProductList, cart);
        lvRight.setAdapter(mAdapter);
        getData();
        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectIndex(position);
                sellerShopProductList.clear();
                sellerShopProductList.addAll(shopProductList.get(position).getSellerShopProductList());
                adapter.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setOnClickListener(onClickListener);
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SellerShopProductListBean item = (SellerShopProductListBean) parent.getAdapter().getItem(position);
                if (item.getHaveDetail() == 1) {
                    intent = new Intent(mContext, RoomDetailActivity.class);
                    intent.putExtra("detail", item);
                    if (cart.get(Integer.parseInt(item.getId())) != null) {
                        intent.putExtra("num", cart.get(Integer.parseInt(item.getId())).size());
                    }
                    startActivityForResult(intent, 0x111);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        SellerShopProductListBean item = (SellerShopProductListBean) data.getSerializableExtra("detail");
        int num = data.getIntExtra("num", 0);
        updateCart(item, num);

    }

    //更新购物
    private void updateCart(SellerShopProductListBean item, int num) {
        int productId = Integer.parseInt(item.getId());
        List<SellerShopProductListBean> productList = cart.get(productId);
        if (productList != null) {
            int oldSize = productList.size();
            if (oldSize == num) {
                return;
            }
            if (num == 0) {
                cart.remove(productId);
            } else if (oldSize < num) {
                for (int i = 0; i < (num - oldSize); i++) {
                    cart.get(productId).add(item);
                }
            } else if (oldSize > num) {
                for (int i = 0; i < (oldSize - num); i++) {
                    cart.get(productId).remove(0);
                }
            }
        } else if (productList == null && num > 0) {
            List<SellerShopProductListBean> tempProductList = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                tempProductList.add(item);
            }
            cart.put(productId, tempProductList);
        }
        productSettleWindow.updateView(cart);
        cartDetailWindow.updateList(productSettleWindow.getTotalMoney());
        mAdapter.notifyDataSetChanged();
    }

    private void clearCart(){
        cart.clear();
        productSettleWindow.updateView(cart);
        cartDetailWindow.updateList(productSettleWindow.getTotalMoney());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BaseApplication.getClearCartFlag()==1){
            clearCart();
            BaseApplication.setClearCartFlag(0);
        }
    }

    public void getData() {
        Call<ResultDO<ShopDetail>> call = AppClient.getAppAdapter().getShopProductDetail(shopId);
        call.enqueue(new Callback<ResultDO<ShopDetail>>() {
            @Override
            public void onResponse(Call<ResultDO<ShopDetail>> call, Response<ResultDO<ShopDetail>> response) {
                ResultDO<ShopDetail> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() != null) {
                        shopDetail = resultDO.getResult();
                        updateView();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResultDO<ShopDetail>> call, Throwable t) {

            }
        });
    }

    private class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add:
                    SellerShopProductListBean item = (SellerShopProductListBean) v.getTag();
                    //更新适配器显示
                    addProduct(item);
                    mAdapter.notifyDataSetChanged();
                    break;
                case R.id.delete:
                    SellerShopProductListBean item1 = (SellerShopProductListBean) v.getTag();
                    //更新适配器显示
                    subProduct(item1);
                    mAdapter.notifyDataSetChanged();
                    break;
                case R.id.check_detail:
                    if (cart.size() == 0) {
                        return;
                    }
                    //明细
                    cartDetailWindow.show(rootView);
                    break;
                case R.id.btn_buy:
                    confirmBuy();
                    break;
                case R.id.btn_submit:
                    confirmBuy();
                    break;
                case R.id.hide_detail:
                    cartDetailWindow.dismiss();
                    productSettleWindow.onResume();
                    break;
            }
        }
    }

    //结算
    private void confirmBuy() {
        if (cart.size() == 0) {
            return;
        }
        //spa 美容美发 美甲单独选择房型 美容师
        if (shopType.equals(Const.TYPE4)) {
            getJson();
            intent = new Intent(mContext, SelectRoomActivity.class);
            intent.putExtra("shopId", shopDetail.getShopId());
            intent.putExtra("shopType", shopType);
            intent.putExtra("shopName", shopDetail.getShopName());
            intent.putExtra("cart", jsonresult);
            startActivity(intent);
        } else if (shopType.equals(Const.TYPE9) || shopType.equals(Const.TYPE10)) {
            getJson();
            intent = new Intent(mContext, SelectServiceManActivity.class);
            intent.putExtra("shopId", shopDetail.getShopId());
            intent.putExtra("shopType", shopType);
            intent.putExtra("shopName", shopDetail.getShopName());
            intent.putExtra("cart", jsonresult);
            startActivity(intent);
        } else {
            confirmOrder();
        }
    }

    //添加product
    private void addProduct(SellerShopProductListBean item) {
        if (cart.get(Integer.parseInt(item.getId())) != null) {
            cart.get(Integer.parseInt(item.getId())).add(item);
        } else {
            List<SellerShopProductListBean> tempProductList = new ArrayList<>();
            tempProductList.add(item);
            cart.put(Integer.parseInt(item.getId()), tempProductList);
        }
        //更新计算金额
        productSettleWindow.updateView(cart);
        cartDetailWindow.updateList(productSettleWindow.getTotalMoney());
    }

    //移除product
    private void subProduct(SellerShopProductListBean item1) {
        List<SellerShopProductListBean> tempList = cart.get(Integer.parseInt(item1.getId()));
        tempList.remove(0);
        if (tempList.size() == 0) {
            cart.remove(Integer.parseInt(item1.getId()));
        }
        //更新计算金额
        productSettleWindow.updateView(cart);
        cartDetailWindow.updateList(productSettleWindow.getTotalMoney());
    }


    //确认订单
    private void confirmOrder() {
        //时间段
        if (checkTime()) {
            getJson();
            orderConfirm();
        }
    }

    private boolean checkTime() {
        startTime = ((KtvDetailActivity) getActivity()).getStartTime();
        endTime = ((KtvDetailActivity) getActivity()).getEndTime();
        if (shopType.equals(Const.TYPE2) || shopType.equals(Const.TYPE11) ||
                shopType.equals(Const.TYPE3) || shopType.equals(Const.TYPE7)) {
            if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
                ToastUtils.showToast(mContext, "请选择时间");
                return false;
            }
        }
        return true;
    }

    private void getJson() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (int i = 0; i < cart.size(); i++) {
            List<SellerShopProductListBean> productList = cart.get(cart.keyAt(i));
            int size = productList.size();
            SellerShopProductListBean item = productList.get(0);
            try {
                jsonObject = new JSONObject();
                jsonObject.put("productId", item.getId());
                jsonObject.put("num", size);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        jsonresult = jsonArray.toString();
    }

    private void orderConfirm() {
        Call<ResultDO<OrderConfirm>> call = AppClient.getAppAdapter().OrderConfirm(memberId, shopId, null, null, startTime, endTime,
                null, shopType, jsonresult);
        call.enqueue(new Callback<ResultDO<OrderConfirm>>() {
            @Override
            public void onResponse(Call<ResultDO<OrderConfirm>> call, Response<ResultDO<OrderConfirm>> response) {
                ResultDO<OrderConfirm> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    orderConfirm = resultDO.getResult();
                    intent = new Intent(mContext, ConfirmBuyActivity.class);
                    intent.putExtra("shopType", shopType);
                    intent.putExtra("shopName", shopDetail.getShopName());
                    intent.putExtra("orderConfirm", orderConfirm);
                    mContext.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<OrderConfirm>> call, Throwable t) {

            }
        });
    }

    private void updateView() {
        shopProductList.clear();
        sellerShopProductList.clear();
        if (shopDetail.getProduct()!=null){
            shopProductList.addAll(shopDetail.getProduct());
            sellerShopProductList.addAll(shopProductList.get(0).getSellerShopProductList());
        }
        adapter.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public View getScrollableView() {
        return lvRight;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void showWindow() {
        productSettleWindow.onResume();
    }

    public void hideWindow() {
        productSettleWindow.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        productSettleWindow.destroy();
    }

    public ProductSettleWindow getProductSettleWindow(){
        return productSettleWindow;
    }

}
