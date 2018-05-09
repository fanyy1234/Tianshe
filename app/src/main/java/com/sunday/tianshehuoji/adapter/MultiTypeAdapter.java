package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.sunday.tianshehuoji.holder.BaseViewHolder;
import com.sunday.tianshehuoji.model.Visitable;
import com.sunday.tianshehuoji.type.TypeFactory;
import com.sunday.tianshehuoji.type.TypeFactoryForList;

import java.util.List;

/**
 * 用于没有下拉刷新的列表
 * Created by fyy on 2018/4/11.
 */

public class MultiTypeAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private TypeFactory typeFactory;
    private List<Visitable> models;
    private Context mContext;

    public MultiTypeAdapter(List<Visitable> models,Context mContext) {
        this.models = models;
        this.typeFactory = new TypeFactoryForList();
        this.mContext = mContext;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View itemView = LayoutInflater.from(context).inflate(viewType,parent,false);
        return typeFactory.createViewHolder(viewType,itemView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setUpView(models.get(position),position,this);
    }

    @Override
    public int getItemCount() {
        if(null == models){
            return  0;
        }
        return models.size();
    }


    @Override
    public int getItemViewType(int position) {
        return models.get(position).type(typeFactory);
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
