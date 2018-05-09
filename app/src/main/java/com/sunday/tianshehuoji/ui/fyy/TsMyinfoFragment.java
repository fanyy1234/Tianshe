package com.sunday.tianshehuoji.ui.fyy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunday.member.base.BaseFragment;
import com.sunday.tianshehuoji.R;

import butterknife.ButterKnife;

/**
 * Created by fyy on 2018/4/12.
 */

public class TsMyinfoFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_tianshe_myinfo, null);
        ButterKnife.bind(this, rootView);
        return rootView;

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
