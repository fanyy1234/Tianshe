package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.widgets.banner.ConvenientBanner;
import com.sunday.common.widgets.banner.holder.ViewHolderCreator;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.Ads;
import com.sunday.tianshehuoji.entity.Img;
import com.sunday.tianshehuoji.entity.News;
import com.sunday.tianshehuoji.ui.NewsDetailActivity;
import com.sunday.tianshehuoji.ui.WebViewActivity;
import com.sunday.tianshehuoji.utils.ImgUtils;
import com.sunday.tianshehuoji.utils.URLImageGetter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/14.
 */

public class CommunityFragApdapter  extends RecyclerView.Adapter {


    private final static int TYPE_MAIN = 1;
    private final static int TYPE_LIST = 2;


    private Context mContext;
    private LayoutInflater inflater;
    private int appWidth;

    private List<Img> adsList;
    private List<Img> newsList;


    public CommunityFragApdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        appWidth = mContext.getResources().getDisplayMetrics().widthPixels;

    }

    public CommunityFragApdapter(Context context, List<Img> adsList, List<Img> newsList) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        appWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        this.adsList = adsList;
        this.newsList = newsList;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_MAIN:
                View view = inflater.inflate(R.layout.layout_view_pager, null);
                return new MainHolder(view);
            case TYPE_LIST:
                view = inflater.inflate(R.layout.list_news_item, null);
                RecyclerView.LayoutParams layoutParams1 = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(layoutParams1);
                return new ListHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       if (position == 0) {
            MainHolder mainHolder = (MainHolder) holder;
            if (adsList.size() > 0) {
                mainHolder.viewPager.setPages(mainHolder.viewHolderCreator, adsList)
                        .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
                mainHolder.viewPager.notifyDataSetChanged();
            }
        } else {
            ListHolder listHolder = (ListHolder) holder;
            Img product = newsList.get(position - 1);
            if (!TextUtils.isEmpty(product.getImage())) {
                Picasso.with(mContext)
                        .load(ImgUtils.ReplaceStr(product.getImage()))
                        .placeholder(R.mipmap.default_img)
                        .resize(appWidth /5, appWidth / 5)
                        .centerInside()
                        .into(listHolder.newsImg);
            }
            listHolder.newsTitle.setText(product.getTitle());
            URLImageGetter imageGetter = new URLImageGetter(mContext,listHolder.newsSummary);
            listHolder.newsSummary.setText(Html.fromHtml(product.getContent(), imageGetter, null));
            listHolder.rlIndexProduct.setTag(product);


        }


    }

    @Override
    public int getItemCount() {
        return newsList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_MAIN;
        } else {
            return TYPE_LIST;
        }


    }

    public class MainHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.view_pager)
        ConvenientBanner viewPager;


        BannerViewHolder bannerViewHolder;
        ViewHolderCreator viewHolderCreator;


        public MainHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            viewPager.startTurning(3000);
            bannerViewHolder = new BannerViewHolder();
            bannerViewHolder.setNeedFull(true);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(appWidth, appWidth * 3 / 5);
            viewPager.setLayoutParams(params);
            viewHolderCreator = new ViewHolderCreator<BannerViewHolder>() {

                @Override
                public BannerViewHolder createHolder() {
                    return bannerViewHolder;
                }
            };
            bannerViewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.news_img)
        ImageView newsImg;
        @Bind(R.id.news_title)
        TextView newsTitle;
        @Bind(R.id.news_summary)
        TextView newsSummary;
        @Bind(R.id.rl_index_product)
        RelativeLayout rlIndexProduct;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rlIndexProduct.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_index_product:
                    Img item = (Img) v.getTag();
                    Intent intent = new Intent(mContext, NewsDetailActivity.class);
                    intent.putExtra("img", item);
                    mContext.startActivity(intent);
            }
        }
    }


}
