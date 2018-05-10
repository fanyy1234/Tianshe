package com.sunday.shangjia.http;

import com.sunday.common.model.ResultDO;
import com.sunday.shangjia.entity.Account;
import com.sunday.shangjia.entity.ChildAccount;
import com.sunday.shangjia.entity.CommentScore;
import com.sunday.shangjia.entity.Image;
import com.sunday.shangjia.entity.OrderComment;
import com.sunday.shangjia.entity.OrderDetail;
import com.sunday.shangjia.entity.OrderTotal;
import com.sunday.shangjia.entity.Product;
import com.sunday.shangjia.entity.Profit;
import com.sunday.shangjia.entity.ProfitTotal;
import com.sunday.shangjia.entity.Staff;
import com.sunday.shangjia.entity.Version;


import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

//import static u.aly.av.I;

/**
 * Created by 刘涛 on 2016/12/13.
 */

public interface AppService {


    /**
     * 单张图片上传
     */
    @Multipart
    @POST("/mobile/image/saveImage")
    Call<ResultDO<Image>> saveImage(@Part("image") RequestBody requestBody);

    /**
     * 多张图片上传
     */
    @Multipart
    @POST("/mobile/image/saveImages")
    Call<ResultDO<List<Image>>> saveImages(@Part("images") RequestBody requestBody);

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/mobile/login/shopLogin")
    Call<ResultDO<Account>> login(@Field("userName") String userName, @Field("password") String password);

    /**
     * 订单列表
     */
    @FormUrlEncoded
    @POST("/mobile/shop/order/getOrderByShopId")
    Call<ResultDO<OrderTotal>> getOrderList(@Field("shopId") String shopId, @Field("search") String search,
                                            @Field("status") Integer status,@Field("pageNo")Integer pageNo,@Field("pageSize")Integer pageSize);

    /**
     * 订单详情
     */
    @FormUrlEncoded
    @POST("/mobile/order/getOrderDetailByOrderNo")
    Call<ResultDO<OrderDetail>> getOrderDetail(@Field("orderNo")String orderNo);


    /**
     * 核销码核销
     */
    @FormUrlEncoded
    @POST("/mobile/shop/order/orderVerificationCancelNo")
    Call<ResultDO> orderVerificationCancelNo(@Field("cancelNo") String cancelNo);

    /**
     * 核销码核销
     */
    @FormUrlEncoded
    @POST("/mobile/shop/order/orderVerificationNo")
    Call<ResultDO> orderVerificationNo(@Field("orderNo") String orderNo);



    /**
     * 评价列表//id 待修正
     */
    @FormUrlEncoded
    @POST("/mobile/shop/comment/commentList")
    Call<ResultDO<List<OrderComment>>> getCommentList(@Field("id") String id);

    /**
     * 评分//id 待修正
     */
    @FormUrlEncoded
    @POST("/mobile/shop/comment/score")
    Call<ResultDO<CommentScore>> getScore(@Field("id") String id);

    /**
     * 商铺收益统计
     */
    @FormUrlEncoded
    @POST("/mobile/shop/income/getShopIncomeByShopId")
    Call<ResultDO<ProfitTotal>> getProfitTotal(@Field("shopId") String id,@Field("startTime")String startTime,
                                             @Field("endTime")String endTime);


    /**
     * 商铺收益明细
     */
    @FormUrlEncoded
    @POST("/mobile/shop/income/getShopIncomeOrCostDetail")
    Call<ResultDO<Profit>> getProfit(@Field("shopId") String id, @Field("startTime")String startTime,
                                          @Field("endTime")String endTime ,@Field("type")int type,
                                     @Field("pageNo")int pageNo,@Field("pageSize")Integer pageSize);
    /**
     * 获取会员信息
     */
    @FormUrlEncoded
    @POST("/mobile/shop/user/getById")
    Call<ResultDO<Account>> getMemberById(@Field("id") String memberId);

    /**
     * 个人资料修改
     */
    @FormUrlEncoded
    @POST("/mobile/shop/user/update")
    Call<ResultDO<Account>> saveMember(@Field("id") String id,@Field("logo")String logo,@Field("password")String password,
                                       @Field("oldPassword")String oldPassword);

    /**
     * 添加 修改员工
     */
    @FormUrlEncoded
    @POST("/mobile/shop/staff/save")
    Call<ResultDO> saveStaff(@Field("id") String id,@Field("sellerChooseShopId")String sellerChooseShopId,
                             @Field("name")String name,@Field("logo")String logo,@Field("mobile")String mobile);

    /**
     * 删除员工
     */
    @FormUrlEncoded
    @POST("/mobile/shop/staff/delete")
    Call<ResultDO> deleteStaff(@Field("id") String id);


    /**
     * 员工列表
     */
    @FormUrlEncoded
    @POST("/mobile/shop/staff/staffList")
    Call<ResultDO<List<Staff>>> getStaffList(@Field("sellerChooseShopId") String sellerChooseShopId,@Field("page")Integer page,@Field("rows")Integer rows);


    /**
     * 添加 修改子帐号
     */
    @FormUrlEncoded
    @POST("/mobile/shop/user/save")
    Call<ResultDO> saveChildAccount(@Field("id") String id,@Field("shopUserId")String shopUserId,
                             @Field("sellerChooseShopId")String sellerChooseShopId,@Field("userName")String userName,
                             @Field("password")String password,@Field("mobile")String mobile,@Field("logo")String logo,
                             @Field("type")int type,@Field("oldPassword")String oldPassword);
    /**
     * 子帐号列表
     */
    @FormUrlEncoded
    @POST("/mobile/shop/user/shopUserList")
    Call<ResultDO<List<ChildAccount>>> getChildAccount(@Field("id") String id, @Field("page")Integer page, @Field("rows")Integer rows);

    /**
     * 删除子帐号
     */
    @FormUrlEncoded
    @POST("/mobile/shop/user/delete")
    Call<ResultDO> deleteChildAccount(@Field("id") String id);

    /**
     * 产品列表
     */
    @FormUrlEncoded
    @POST("/mobile/shopProduct/getProductList")
    Call<ResultDO<List<Product>>> getProductList(@Field("sellerId") String sellerId, @Field("shopId")String shopId,
                                                 @Field("show")Integer show,@Field("page")Integer page,
                                                 @Field("rows")Integer rows);

    /**
     * 删除产品
     */
    @FormUrlEncoded
    @POST("/mobile/shopProduct/delete")
    Call<ResultDO> deleteProduct(@Field("id") String id);

    /**
     * 产品定价
     */
    @FormUrlEncoded
    @POST("/mobile/shopProduct/updatePrice")
    Call<ResultDO> updatePrice(@Field("id") String id,@Field("price")String price);


    /**
     * 上下架产品
     */
    @FormUrlEncoded
    @POST("/mobile/shopProduct/updateShow")
    Call<ResultDO> updateShowProduct(@Field("id") String id,@Field("show")Integer show);


    /**
     * 更新
     */
    @FormUrlEncoded
    @POST("/mobile/member/updateMobileOrPassword")
    Call<ResultDO> updateMobileOrPassword(@Field("id")String id,@Field("mobile")String mobile,
                                          @Field("password")String password, @Field("code")String code);

    /**
     * 根据验证码，验证手机号
     */
    @FormUrlEncoded
    @POST("/mobile/identifyingCode/checkCodeByMobile")
    Call<ResultDO> checkCodeByMobile(@Field("mobile")String mobile,@Field("code")String code,
                                     @Field("type")int type);
    /**
     * 发送验证码
     */
    @FormUrlEncoded
    @POST("/mobile/identifyingCode/sendMobileCode")
    Call<ResultDO> sendMobileCode(@Field("type") int type, @Field("mobile")String mobile);

    @FormUrlEncoded
    @POST("/mobile/apk/getApkInfo")
    Call<ResultDO<Version>> checkVersion(@Field("type")Integer type,@Field("versionCode") int versionCode);

}
