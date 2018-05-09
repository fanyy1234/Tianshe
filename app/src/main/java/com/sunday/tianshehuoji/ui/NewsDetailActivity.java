package com.sunday.tianshehuoji.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.sunday.member.base.BaseActivity;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.Img;
import com.sunday.tianshehuoji.utils.URLImageGetter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/1/6.
 */

public class NewsDetailActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.content)
    TextView content;

    Img item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        item= (Img) getIntent().getSerializableExtra("img");
        titleView.setText(item.getTitle());

        content.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
        content.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
        URLImageGetter imageGetter = new URLImageGetter(this,content);
        content.setText(Html.fromHtml(item.getContent(), imageGetter, null));
    }


}
