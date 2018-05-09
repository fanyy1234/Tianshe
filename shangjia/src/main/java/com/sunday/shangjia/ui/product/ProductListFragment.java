package com.sunday.shangjia.ui.product;


import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.UIAlertView;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseFragment;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.shangjia.R;
import com.sunday.shangjia.adapter.ProductListAdapter;
import com.sunday.shangjia.entity.Product;
import com.sunday.shangjia.http.AppClient;
import com.sunday.shangjia.ui.MainActivity;
import com.sunday.shangjia.ui.fragment.ProductManageFragment;
import com.sunday.shangjia.ui.fragment.ProfitManageFragment;
import com.sunday.shangjia.widgets.UpdatePriceWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/1/3.
 */

public class ProductListFragment extends BaseFragment {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_layout)
    EmptyLayout emptyLayout;

    private LinearLayoutManager linearLayoutManager;
    private ProductListAdapter adapter;
    private int type;//1:上架产品 2:下架产品
    private int pageNo=1;
    private List<Product> dataSet=new ArrayList<>();
    private String shopId;
    private String sellerId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_common, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shopId= SharePerferenceUtils.getIns(mContext).getString("shopId","");
        sellerId=SharePerferenceUtils.getIns(mContext).getString("sellerId","");
        if (getArguments()!=null){
            type=getArguments().getInt("type");
        }
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ProductListAdapter(mContext,dataSet);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider)
                .build());
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green,
                R.color.material_blue, R.color.material_yellow);
        handlerRecylerView();
        emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNo=1;
                getData();
            }
        });
        getData();
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product item= (Product) v.getTag();
                switch (v.getId()){
                    case R.id.btn1:
                        if (item.getShow()==1){
                            updateShowProduct(item,2);
                        }else {
                            showDeleteWindow(item);
                        }
                        break;
                    case R.id.btn2:
                        if (item.getShow()==1){
                            showUpdatePriceWindow(item);
                        }else {
                            updateShowProduct(item,1);
                        }
                        break;
                }
            }
        });

    }

    //上下架产品
    private void updateShowProduct(Product item,int show){
        Call<ResultDO> call=AppClient.getAppAdapter().updateShowProduct(item.getId(),show);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,"操作成功");
                    pageNo=1;
                    getData();
                    ((ProductManageFragment)getActivity().getSupportFragmentManager().getFragments().get(1)).setUpdate(true);

                }else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {

            }
        });
    }

    private void showUpdatePriceWindow(final Product item){
        final UpdatePriceWindow view=new UpdatePriceWindow(mContext, "修改价格" , "取消" , "确定");
        view.show();
        view.setClicklistener(new UpdatePriceWindow.ClickListenerInterface() {
            @Override
            public void doLeft() {
                view.dismiss();
            }
            @Override
            public void doRight() {
                view.dismiss();
                if (TextUtils.isEmpty(view.getPrice())){
                    return;
                }
                updatePrice(item,view.getPrice());
            }
        });

    }

    private void showDeleteWindow(final Product item){
        final UIAlertView view = new UIAlertView(mContext , "温馨提示" , "确定要删除产品吗" , "取消" , "确定");
        view.setClicklistener(new UIAlertView.ClickListenerInterface() {
            @Override
            public void doLeft() {
                view.dismiss();
            }
            @Override
            public void doRight() {
                view.dismiss();
                deleteProduct(item);
            }
        });
        view.show();
    }

    private void deleteProduct(Product item){
        Call<ResultDO> call=AppClient.getAppAdapter().deleteProduct(item.getId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,"操作成功");
                    pageNo=1;
                    getData();
                }else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {

            }
        });
    }

    private void updatePrice(Product item,String price){
        Call<ResultDO> call=AppClient.getAppAdapter().updatePrice(item.getId(),price);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,"操作成功");
                    pageNo=1;
                    getData();
                }else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {

            }
        });
    }




    private boolean isCanLoadMore = false;
    private int lastVisibleItem;


    private void handlerRecylerView() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                getData();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 3 == adapter.getItemCount() && isCanLoadMore == true) {
                    getData();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });


    }


    public void getData(){
        showLoadingDialog(0);
        Call<ResultDO<List<Product>>> call = AppClient.getAppAdapter().getProductList(sellerId,shopId,type,pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<Product>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Product>>> call, Response<ResultDO<List<Product>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                swipeRefreshLayout.setRefreshing(false);
                ResultDO<List<Product>> resultDO = response.body();
                if (resultDO != null && resultDO.getCode() == 0) {
                    if (pageNo == 1) {
                        dataSet.clear();
                    }
                    dataSet.addAll(resultDO.getResult());
                    if (resultDO.getResult().size()== 20) {
                        pageNo++;
                        isCanLoadMore = true;
                    }
                    if (dataSet.size() > 0) {
                        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    } else {
                        emptyLayout.setErrorType(EmptyLayout.NODATA);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ResultDO<List<Product>>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
