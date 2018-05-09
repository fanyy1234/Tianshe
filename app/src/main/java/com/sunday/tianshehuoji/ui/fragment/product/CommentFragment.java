package com.sunday.tianshehuoji.ui.fragment.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.base.BaseFragment;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.CommentAdapter;
import com.sunday.tianshehuoji.entity.CommentItem;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.widgets.ScrollableHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/15.
 */

public class CommentFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer  {


    @Bind(R.id.list_view)
    ListView listView;

    private CommentAdapter commentAdapter;
    private String shopId;
    private int page=1;
    private List<CommentItem> dataSet=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_comment, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments()!=null){
            shopId=getArguments().getString("shopId");
        }
        commentAdapter=new CommentAdapter(mContext,dataSet);
        listView.setAdapter(commentAdapter);
        getData();

    }

    private void getData(){
        Call<ResultDO<List<CommentItem>>> call= AppClient.getAppAdapter().getCommentList(shopId,page,Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<CommentItem>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<CommentItem>>> call, Response<ResultDO<List<CommentItem>>> response) {
                ResultDO<List<CommentItem>> resultDO=response.body();
                if (resultDO==null){return;}
                if (resultDO.getCode()==0){
                    if (resultDO.getResult()==null){return;}
                    if (page==1){
                        dataSet.clear();
                    }
                    dataSet.addAll(resultDO.getResult());
                    if (resultDO.getResult().size()==20){
                        page++;
                        getData();
                    }
                    commentAdapter.notifyDataSetChanged();
                }else {
                    ToastUtils.showToast(mContext,resultDO.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResultDO<List<CommentItem>>> call, Throwable t) {
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public View getScrollableView() {
        return listView;
    }
}
