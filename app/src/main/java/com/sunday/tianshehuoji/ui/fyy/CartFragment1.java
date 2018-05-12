package com.sunday.tianshehuoji.ui.fyy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.UIAlertView;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseFragment;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.CartAdapter1;
import com.sunday.tianshehuoji.entity.CartItem;
import com.sunday.tianshehuoji.entity.CartListItem;
import com.sunday.tianshehuoji.entity.CartPay;
import com.sunday.tianshehuoji.entity.CartTotal;
import com.sunday.tianshehuoji.http.AppClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/9/17.
 */

public class CartFragment1 extends BaseFragment {

    @Bind(R.id.left_btn)
    ImageView leftBtn;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.login_not_img)
    ImageView loginNotImg;
    @Bind(R.id.login_not_dec)
    TextView loginNotDec;
    @Bind(R.id.text_login)
    TextView textLogin;
    @Bind(R.id.login_not_layout)
    RelativeLayout invisibleLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.checkbox_select_all)
    ImageView checkboxSelectAll;
    @Bind(R.id.btn_buy)
    TextView btnBuy;
    @Bind(R.id.total_money)
    TextView txt_totalMoney;
    @Bind(R.id.text_total_money)
    TextView textTotalMoney;
    @Bind(R.id.linearlayout)
    LinearLayout visibleLayout;
    @Bind(R.id.empty_layout)
    EmptyLayout emptyLayout;


    private CartAdapter1 adapter;
    private final static String EDIT = "编辑";
    private final static String FINISH = "完成";

    private int memberId;
    //private List<CartItem> dataSet = new ArrayList<>();
    private boolean selectAll = false;//是否全选，包括预热
    private int availableNum;//减去卖完的购物项
    private boolean isFromDetail;
    private boolean isLogin;
    private List<CartListItem> dataSet = new ArrayList<>();
    private List<CartItem> buyProducts = new ArrayList<>();


    public static CartFragment1 newInstance(boolean isLogin, boolean isFromDetail) {
        CartFragment1 fragment = new CartFragment1();
        Bundle args = new Bundle();
        args.putBoolean("isLogin", isLogin);
        args.putBoolean("isFromDetail", isFromDetail);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart1, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            isFromDetail = getArguments().getBoolean("isFromDetail");
            isLogin = getArguments().getBoolean("isLogin");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        if (!isLogin) {
            invisibleLayout.setVisibility(View.VISIBLE);
            visibleLayout.setVisibility(View.GONE);
        } else {
            invisibleLayout.setVisibility(View.GONE);
            visibleLayout.setVisibility(View.VISIBLE);
            memberId= Integer.parseInt(SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,""));
            rightTxt.setVisibility(View.VISIBLE);
            rightTxt.setText(EDIT);
            emptyLayout.setVisibility(View.GONE);
            getData();
        }
        if (isFromDetail) {
            leftBtn.setVisibility(View.VISIBLE);
            leftBtn.setImageResource(R.mipmap.ic_back);
        } else {
            leftBtn.setVisibility(View.GONE);
        }
        titleView.setText("购物车");
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        adapter = new CartAdapter1(mContext, dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider)
                .build());
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
            }
        });
        adapter.setOnClickListener(onClickListener);
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        checkboxSelectAll.setOnClickListener(onClickListener);


    }

    private int selectList = 0;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.list_select_all:
                    int p = (int) v.getTag();
                    switch (rightTxt.getText().toString()) {
                        case EDIT:
                            if (dataSet.get(p).getType() == 1) {
                                return;
                            } else {
                                if (dataSet.get(p).isSelected()) {
                                    dataSet.get(p).setSelected(false);
                                    for (int i = 0; i < dataSet.get(p).getCartItemList().size(); i++) {
                                        dataSet.get(p).getCartItemList().get(i).setSelect(false);
                                    }
                                } else {
                                    dataSet.get(p).setSelected(true);
                                    for (int i = 0; i < dataSet.get(p).getCartItemList().size(); i++) {
                                        dataSet.get(p).getCartItemList().get(i).setSelect(true);
                                    }
                                }
                                checkListSelectAll(p);
                                adapter.notifyItemChanged(p);
                                updatePrice();
                            }
                            break;
                        case FINISH:
                            int p1 = (int) v.getTag();
                            if (dataSet.get(p1).isSelected()) {
                                dataSet.get(p1).setSelected(false);
                                for (int i = 0; i < dataSet.get(p1).getCartItemList().size(); i++) {
                                    dataSet.get(p1).getCartItemList().get(i).setSelect(false);
                                }

                            } else {
                                dataSet.get(p1).setSelected(true);
                                for (int i = 0; i < dataSet.get(p1).getCartItemList().size(); i++) {
                                    dataSet.get(p1).getCartItemList().get(i).setSelect(true);
                                }
                            }
                            checkListSelectAll(p1);
                            adapter.notifyDataSetChanged();
                            updatePrice();
                            break;
                    }
                    break;

                case R.id.ivCheckGood:
                    int childP = (int) v.getTag();
                    int parentP = (int) v.getTag(R.id.ivCheckGood);
                    if (dataSet.get(parentP).getType() == 1 && rightTxt.getText().toString().equals(EDIT)) {
                        return;
                    }
                    CartItem cartItem = dataSet.get(parentP).getCartItemList().get(childP);
                    dataSet.get(parentP).getCartItemList().get(childP).setSelect(!cartItem.isSelect());
                    //adapter.notifyDataSetChanged();
                    checkListSelectAll(parentP);
                    adapter.notifyItemChanged(parentP);
                    updatePrice();
                    break;
                case R.id.ivAdd:
                    int childP1 = (int) v.getTag();
                    int parentP1 = (int) v.getTag(R.id.ivAdd);
                    int num = dataSet.get(parentP1).getCartItemList().get(childP1).getNum();
                    num++;
                    dataSet.get(parentP1).getCartItemList().get(childP1).setNum(num);
                    adapter.notifyDataSetChanged();
                    updateCart(parentP1, childP1, num);
                    break;
                case R.id.ivReduce:
                    int childP2 = (int) v.getTag();
                    int parentP2 = (int) v.getTag(R.id.ivReduce);
                    int num1 = dataSet.get(parentP2).getCartItemList().get(childP2).getNum();
                    //num1++;
                    if (num1 > 1) {
                        num1--;
                        dataSet.get(parentP2).getCartItemList().get(childP2).setNum(num1);
                        adapter.notifyDataSetChanged();
                        updateCart(parentP2, childP2, num1);
                    }
                    break;
                case R.id.delete:
                    final int childP3 = (int) v.getTag();
                    final int parentP3 = (int) v.getTag(R.id.delete);
                    final UIAlertView alertView = new UIAlertView(mContext, "温馨提示", "确认删除吗", "取消", "确定");
                    alertView.show();
                    alertView.setClicklistener(new UIAlertView.ClickListenerInterface() {
                        @Override
                        public void doLeft() {
                            alertView.dismiss();
                        }

                        @Override
                        public void doRight() {
                            deleteCart(parentP3, childP3);
                            alertView.dismiss();
                        }
                    });

                    break;

                case R.id.checkbox_select_all:
                    selectAll = !selectAll;
                    checkboxSelectAll.setImageResource(selectAll ? R.mipmap.ic_select : R.mipmap.ic_select_no);
                    if (dataSet != null && dataSet.size() > 0) {
                        for (int i = 0; i < dataSet.size(); i++) {
                            dataSet.get(i).setSelected(selectAll);
                            if (dataSet.get(i).getCartItemList() != null && !dataSet.get(i).getCartItemList().isEmpty()) {
                                for (int j = 0; j < dataSet.get(i).getCartItemList().size(); j++) {
                                    dataSet.get(i).getCartItemList().get(j).setSelect(selectAll);
                                }
                            }
                        }
                    }
                    updatePrice();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void checkSelectAll() {
        int num = 0;
        if (dataSet != null && !dataSet.isEmpty()) {
            for (CartListItem item : dataSet) {
                if (item.isSelected()) {
                    num++;
                }
            }
            if (num == dataSet.size()) {
                selectAll = true;
                checkboxSelectAll.setSelected(true);
                checkboxSelectAll.setImageResource(R.mipmap.ic_select);
            } else {
                selectAll = false;
                checkboxSelectAll.setSelected(false);
                checkboxSelectAll.setImageResource(R.mipmap.ic_select_no);
            }
        }

    }

    private void checkListSelectAll(int parentP) {
        //判断局部是否全选
        int partNum = 0;
        for (CartItem item : dataSet.get(parentP).getCartItemList()) {
            if (item.isSelect())
                partNum++;
        }
        if (partNum == dataSet.get(parentP).getCartItemList().size()) {
            dataSet.get(parentP).setSelected(true);

        } else {
            if (dataSet.get(parentP).isSelected()) {

            }
            dataSet.get(parentP).setSelected(false);
        }
        checkSelectAll();

    }

    //更新购物项的单个数量
    private void updateCart(final int parentP, final int childP, int num) {
        Call<ResultDO> call = AppClient.getAppAdapter().updateCart(dataSet.get(parentP).getCartItemList().get(childP).getId(), num);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                ResultDO resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    //如果是选中的数量有变化重新结算
                    if (dataSet.get(parentP).getCartItemList().get(childP).isSelect()) {
                        updatePrice();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                ToastUtils.showToast(mContext, "网络错误");
            }
        });

    }

    private List<String> cartIds = new ArrayList<>();

    //更新结算
    private int totalBuyNum = 0;
    private HashSet<String> typeNameList = new HashSet<>();//统计选中商品的种类
    private void updatePrice() {
        buyProducts.clear();
        typeNameList.clear();
        if (dataSet != null && !dataSet.isEmpty()) {
            for (int i = 0; i < dataSet.size(); i++) {
                if (dataSet.get(i).getType() != 1) {
                    buyProducts.addAll(dataSet.get(i).getCartItemList());
                }
            }
        }
        BigDecimal totalMoney = BigDecimal.ZERO;
        int selectNum = 0;
        if (buyProducts.isEmpty()) {
            txt_totalMoney.setText("￥0.00");
            btnBuy.setText("结算");
            return;
        }
        for (CartItem cartItem : buyProducts) {
            if (cartItem.isSelect() ) {
                selectNum += cartItem.getNum();
                totalMoney = totalMoney.add(BigDecimal.valueOf(cartItem.getPrice()).multiply(BigDecimal.valueOf(cartItem.getNum())));
            }
        }
        BigDecimal countMoney = totalMoney;
        totalBuyNum = selectNum;

        //productNum.setText(SpannalbeStringUtils.setTextColor("共", getResources().getColor(R.color.black_6)));
        //productNum.append(SpannalbeStringUtils.setTextColor("" + totalNum, getResources().getColor(R.color.colorPrimary)));
        //productNum.append(SpannalbeStringUtils.setTextColor("件", getResources().getColor(R.color.black_6)));
        txt_totalMoney.setText(String.format("¥%s", countMoney.setScale(2, RoundingMode.HALF_UP)));
        btnBuy.setText("结算(" + totalBuyNum + ")");

    }

    private void deleteCart(final int parentP, final int childP) {
        showLoadingDialog(0);
        Call<ResultDO> call = AppClient.getAppAdapter().updateCart(dataSet.get(parentP).getCartItemList().get(childP).getId(),0);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                if (isFinish || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    getData();
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }


    //  boolean isNotActiveProSelected = false;

    @OnClick({R.id.left_btn, R.id.btn_buy, R.id.text_login, R.id.rightTxt})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                getActivity().finish();
                break;
            case R.id.rightTxt:
                switch (rightTxt.getText().toString()) {
                    case EDIT:
                        rightTxt.setText(FINISH);
                        //编辑状态
                        adapter.setEditType(true);
                        adapter.notifyDataSetChanged();
                        break;
                    case FINISH:
                        rightTxt.setText(EDIT);
                        //非编辑状态
                        adapter.setEditType(false);
                        adapter.notifyDataSetChanged();
                        break;
                }
                break;
            case R.id.btn_buy:
                //结算跳转订单
//                if (buyProducts.isEmpty()) {
//                    ToastUtils.showToast(mContext, "请选择要购买的产品");
//                    return;
//                }
//                cartIds.clear();
//                for (CartItem cartItem : buyProducts) {
//                    if (cartItem.isSelect()) {
//                        cartIds.add(String.valueOf(cartItem.getId()));
//                        String typeName = cartItem.getTypeName();
//                        typeNameList.add(typeName);
//                    }
//                }
//                if (!cartIds.isEmpty()) {
                    if (dataSet==null||dataSet.size()==0){
                        ToastUtils.showToast(mContext, "购物车是空的");
                    }
                    else {
                        Intent intent = new Intent(getActivity(),ComitOrderActivity.class);
                        startActivity(intent);
                    }

//                }
//                else {
//                    ToastUtils.showToast(mContext, "请选择要购买的产品");
//                }

                break;
            case R.id.text_login:
                break;
        }

    }


    private void payCart() {
//        showLoadingDialog(0);
//        Call<ResultDO<CartPay>> call = AppClient.getAppAdapter().payCartNew(StringUtils.listToString(cartIds), memberId, null, null);
//        call.enqueue(new Callback<ResultDO<CartPay>>() {
//            @Override
//            public void onResponse(Call<ResultDO<CartPay>> call, Response<ResultDO<CartPay>> response) {
//                dissMissDialog();
//                if (isFinish) {
//                    return;
//                }
//                ResultDO<CartPay> resultDO = response.body();
//                if (resultDO == null) {
//                    return;
//                }
//                if (resultDO.getCode() == 0) {
//                    Intent intent;
//                    intent = new Intent(mContext, OrderConfirmActivity.class);
//                    intent.putExtra("cartPay", resultDO.getResult());
//                    intent.putExtra("cartIds", StringUtils.listToString(cartIds));
//                    //intent.putExtra("isActive",isActiveProSelected?1:0);
//                    startActivity(intent);
//                }else {
//                    ToastUtils.showToast(mContext,response.body().getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResultDO<CartPay>> call, Throwable t) {
//                dissMissDialog();
//                ToastUtils.showToast(mContext,R.string.network_error);
//            }
//        });
    }

    public void getData() {
        showLoadingDialog(0);
        Call<ResultDO<List<CartTotal>>> call = AppClient.getAppAdapter().getCartList(memberId);
        call.enqueue(new Callback<ResultDO<List<CartTotal>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<CartTotal>>> call, Response<ResultDO<List<CartTotal>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                ptrFrame.refreshComplete();
                ResultDO<List<CartTotal>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null||resultDO.getResult().size()==0) {
                        return;
                    }
                    dataSet.clear();
                    if (resultDO.getResult().get(0).getCartItem() != null && !resultDO.getResult().get(0).getCartItem().isEmpty()) {
                        CartListItem cartListItem = new CartListItem();
                        cartListItem.setTypeName("天奢商城");
                        cartListItem.setType(0);
                        cartListItem.setSelected(false);
                        cartListItem.setCartItemList(resultDO.getResult().get(0).getCartItem());
                        dataSet.add(cartListItem);
                        for (int i = 0; i < resultDO.getResult().get(0).getCartItem().size(); i++) {
                            resultDO.getResult().get(0).getCartItem().get(i).setSelect(true);
                        }
                    }

                    txt_totalMoney.setText("￥0.00");
                    btnBuy.setText("结算");
                    if (dataSet.size() > 0) {
                        visibleLayout.setVisibility(View.VISIBLE);
                        emptyLayout.setVisibility(View.GONE);

                    } else {
                        emptyLayout.setVisibility(View.VISIBLE);
                        visibleLayout.setVisibility(View.GONE);
                        emptyLayout.setErrorType(EmptyLayout.NODATA);
                        emptyLayout.setNoDataContent("您的购物车是空的");
                    }
                    selectAll = false;
                    checkboxSelectAll.setImageResource(R.mipmap.ic_select_no);
                    adapter.notifyDataSetChanged();
                    updatePrice();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<CartTotal>>> call, Throwable t) {
                dissMissDialog();
                ptrFrame.refreshComplete();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
            getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

