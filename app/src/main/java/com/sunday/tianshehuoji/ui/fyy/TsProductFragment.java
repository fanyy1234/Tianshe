package com.sunday.tianshehuoji.ui.fyy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunday.common.utils.Constants;
import com.sunday.member.base.BaseFragment;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.BaseApplication;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.MultiTypeAdapter;
import com.sunday.tianshehuoji.model.ClothProduct;
import com.sunday.tianshehuoji.model.SubmitOrderProduct;
import com.sunday.tianshehuoji.model.TiansheMarket;
import com.sunday.tianshehuoji.model.TiansheProduct;
import com.sunday.tianshehuoji.model.Visitable;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fyy on 2018/4/12.
 */

public class TsProductFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    List<Visitable> models = new ArrayList<Visitable>();
    LinearLayoutManager layoutManager;
    MultiTypeAdapter adapter;
    int status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_tianshe_product, null);
        ButterKnife.bind(this, rootView);
        return rootView;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new MultiTypeAdapter(models,getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        if(getArguments()!=null){
            //取出保存的值
            status = getArguments().getInt("status");
            TiansheMarket tiansheMarket = getArguments().getParcelable("productArray");
            JSONArray productArray = (JSONArray)tiansheMarket.getMap().get("productArray");
            getProducts(productArray);
        }
    }

    private void getProducts(JSONArray array){
        int length = array.size();
        if (status==0){
            for (int i = 0;i<length;i++){
                JSONObject productObject = array.getJSONObject(i);
                TiansheProduct product = new TiansheProduct();
                product.setName(productObject.getString("name"));
                product.setNewPrice(productObject.getInteger("price").toString());
                product.setId(productObject.getInteger("id"));
                product.setImg("000");
                models.add(product);
            }
        }
        else if (status==1){
            for (int i = 0;i<length;i++){
                JSONObject productObject = array.getJSONObject(i);
                ClothProduct product = new ClothProduct();
                product.setName(productObject.getString("name"));
                product.setNewPrice(productObject.getInteger("price").toString());
                product.setId(productObject.getInteger("id"));
                product.setImg("000");
                models.add(product);
            }
        }

        adapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
