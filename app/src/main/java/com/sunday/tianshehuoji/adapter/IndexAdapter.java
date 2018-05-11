package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.utils.PixUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.MarqueeView;
import com.sunday.common.widgets.banner.ConvenientBanner;
import com.sunday.common.widgets.banner.holder.ViewHolderCreator;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.Const;
import com.sunday.tianshehuoji.entity.Img;
import com.sunday.tianshehuoji.entity.Notice;
import com.sunday.tianshehuoji.entity.shop.IndexShop;
import com.sunday.tianshehuoji.entity.shop.SellerShop;
import com.sunday.tianshehuoji.ui.fyy.ClothingMakeActivity;
import com.sunday.tianshehuoji.ui.fyy.ComitClothOrderActivity;
import com.sunday.tianshehuoji.ui.fyy.TiansheMarketActivity;
import com.sunday.tianshehuoji.ui.product.KtvDetailActivity;
import com.sunday.tianshehuoji.utils.ImgUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/14.
 */

public class IndexAdapter extends RecyclerView.Adapter {


    private final static int TYPE_BANNER = 0;
    private final static int TYPE_NEWS = 1;
    private final static int TYPE_LIST = 2;


    private Context mContext;
    private LayoutInflater inflater;
    private int appWidth;
    private int imgWidth;
    private List<Notice> noticeList;
    private List<Img> imgList;
    private List<SellerShop> sellerShopList;


    public IndexAdapter(Context context, List<Img> imgs, List<Notice> notices, List<SellerShop> sellerShops) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        appWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        imgWidth = PixUtils.dip2px(mContext, 60);
        this.imgList = imgs;
        this.noticeList = notices;
        this.sellerShopList = sellerShops;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BANNER:
                View view = inflater.inflate(R.layout.layout_view_pager, null);
                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(appWidth, appWidth * 3 / 5);
                layoutParams.setMargins(0, 0, 0, 15);
                view.setLayoutParams(layoutParams);
                return new BannerHolder(view);
            case TYPE_NEWS:
                view = inflater.inflate(R.layout.index_news, null);
                return new NewsHolder(view);
            case TYPE_LIST:
                view = inflater.inflate(R.layout.index_list_product_item, null);
                RecyclerView.LayoutParams layoutParams1 = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams1.setMargins(5,5,5,5);
                view.setLayoutParams(layoutParams1);
                return new ListHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position == 0) {
            if (imgList.size() > 0) {
                BannerHolder bannerHolder = (BannerHolder) holder;
                bannerHolder.viewPager.setPages(bannerHolder.viewHolderCreator, imgList)
                        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                        .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
                bannerHolder.viewPager.notifyDataSetChanged();
            }
        } else if (position == 1) {
            NewsHolder newsHolder = (NewsHolder) holder;
            List<String> adsList = new ArrayList<String>();
            for (int i = 0; i < noticeList.size(); i++) {
                adsList.add(noticeList.get(i).getContent());
            }
            newsHolder.contentAdsList.startWithList(adsList);
        } else {
            ListHolder listHolder= (ListHolder) holder;
            SellerShop item=sellerShopList.get(position-2);
            if (!TextUtils.isEmpty(item.getIndexLogo()))
                Log.d("img",ImgUtils.ReplaceStr(item.getIndexLogo()));
            Picasso.with(mContext)
            .load(ImgUtils.ReplaceStr(item.getIndexLogo()))
            .resize(appWidth/5,appWidth/5)
            .centerInside()
            .into(listHolder.categoryMainImg);
            if (!TextUtils.isEmpty(item.getIndexMinLogo())){
                Picasso.with(mContext)
                        .load(ImgUtils.ReplaceStr(item.getIndexMinLogo()))
                        .resize(appWidth/6,appWidth/6)
                        .centerCrop()
                        .into(listHolder.categoryMinImg);
            }
            listHolder.indexTitle.setText(item.getName());
            listHolder.categoryLayout.setTag(item);
        }

    }

    @Override
    public int getItemCount() {
        return sellerShopList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        } else if (position == 1) {
            return TYPE_NEWS;
        } else return TYPE_LIST;
    }

    public class BannerHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.view_pager)
        ConvenientBanner viewPager;


        BannerViewHolder bannerViewHolder;
        ViewHolderCreator viewHolderCreator;


        public BannerHolder(View itemView) {
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


    public class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.contentAdsList)
        MarqueeView contentAdsList;

        public NewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }

        }
    }

    public class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.index_check_more)
        TextView indexCheckMore;
        @Bind(R.id.index_title)
        TextView indexTitle;
        @Bind(R.id.category_main_img)
        ImageView categoryMainImg;
        @Bind(R.id.category_layout)
        RelativeLayout categoryLayout;
        @Bind(R.id.category_min_img)
        ImageView categoryMinImg;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            categoryLayout.setOnClickListener(this);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) categoryMainImg.getLayoutParams();
            layoutParams.width = appWidth / 2;
            layoutParams.height = appWidth / 2;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.category_layout:
                    SellerShop sellerShop= (SellerShop) v.getTag();
                    String type = sellerShop.getType();
                    if (type.equals("12")){
                        Intent intent=new Intent(mContext, TiansheMarketActivity.class);
                        intent.putExtra("shopName",sellerShop.getName());
                        intent.putExtra("shopId",sellerShop.getId());
                        intent.putExtra("shopType",sellerShop.getType());
                        mContext.startActivity(intent);
                    }
                    else if(type.equals("13")){
                        Intent intent=new Intent(mContext, ClothingMakeActivity.class);
                        intent.putExtra("shopName",sellerShop.getName());
                        intent.putExtra("shopId",sellerShop.getId());
                        intent.putExtra("shopType",sellerShop.getType());
                        mContext.startActivity(intent);
                    }
                    else {
                        Intent intent=new Intent(mContext, KtvDetailActivity.class);
                        intent.putExtra("shopName",sellerShop.getName());
                        intent.putExtra("shopId",sellerShop.getId());
                        intent.putExtra("shopType",sellerShop.getType());
                        mContext.startActivity(intent);
                    }

                    break;
            }

        }
    }

}
