package com.sunday.tianshehuoji.http;

import com.sunday.common.model.ResultDO;
import com.sunday.tianshehuoji.entity.Account;
import com.sunday.tianshehuoji.entity.Address;
import com.sunday.tianshehuoji.entity.Bonus;
import com.sunday.tianshehuoji.entity.CardType;
import com.sunday.tianshehuoji.entity.CartTotal;
import com.sunday.tianshehuoji.entity.CashAccount;
import com.sunday.tianshehuoji.entity.CashDetail;
import com.sunday.tianshehuoji.entity.CashRecord;
import com.sunday.tianshehuoji.entity.City;
import com.sunday.tianshehuoji.entity.CommentItem;
import com.sunday.tianshehuoji.entity.Image;
import com.sunday.tianshehuoji.entity.Img;
import com.sunday.tianshehuoji.entity.Income;
import com.sunday.tianshehuoji.entity.Notice;
import com.sunday.tianshehuoji.entity.OrderResult;
import com.sunday.tianshehuoji.entity.Profit;
import com.sunday.tianshehuoji.entity.ProfitRecord;
import com.sunday.tianshehuoji.entity.Room;
import com.sunday.tianshehuoji.entity.ShareProfit;
import com.sunday.tianshehuoji.entity.Staff;
import com.sunday.tianshehuoji.entity.Version;
import com.sunday.tianshehuoji.entity.Vote;
import com.sunday.tianshehuoji.entity.VoteItem;
import com.sunday.tianshehuoji.entity.order.Order;
import com.sunday.tianshehuoji.entity.order.OrderConfirm;
import com.sunday.tianshehuoji.entity.order.OrderDetail;
import com.sunday.tianshehuoji.entity.order.OrderTotal;
import com.sunday.tianshehuoji.entity.shop.IndexShop;
import com.sunday.tianshehuoji.entity.shop.MemberSize;
import com.sunday.tianshehuoji.entity.shop.Seller;
import com.sunday.tianshehuoji.entity.shop.ShopDetail;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import static android.R.attr.id;
import static android.R.attr.password;
import static com.sunday.tianshehuoji.R.mipmap.phone;

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
    @POST("/mobile/login/memberLogin")
    Call<ResultDO<Account>> login(@Field("mobile") String phone, @Field("password") String password);

    /**
     * 获取会员信息
     */
    @FormUrlEncoded
    @POST("/mobile/member/getMemberById")
    Call<ResultDO<Account>> getMemberById(@Field("memberId") String memberId);

    /**
     * 个人资料修改
     */
    @FormUrlEncoded
    @POST("/mobile/member/save")
    Call<ResultDO<Account>> saveMember(@Field("id") String id,@Field("logo")String logo,@Field("password")String password,
                                       @Field("oldPassword")String oldPassword);




    /**
     * 生活馆列表
     */
    @POST("/mobile/seller/sellerList")
    Call<ResultDO<List<Seller>>> getSellerList();

    /**
     * 经纬度获取生活馆的首页商铺信息
     */
    @FormUrlEncoded
    @POST("/mobile/shop/getIndexShopByLatLon")
    Call<ResultDO<IndexShop>> getIndexShopByLatLon(@Field("latitude") String latitude, @Field("longitude") String longitude);

    /**
     * id获取生活馆的首页商铺信息
     */
    @FormUrlEncoded
    @POST("/mobile/shop/getIndexShopBySellerId")
    Call<ResultDO<IndexShop>> getIndexShopBySellerId(@Field("sellerId") String sellerId);


    /**
     * id获取商铺产品详情
     */
    @FormUrlEncoded
    @POST("/mobile/shop/product/getProductListBySellerIdAndShopId")
    Call<ResultDO<ShopDetail>> getShopProductDetail(@Field("shopId") String shopId);

    /**
     * id获取商铺产品评价列表
     */
    @FormUrlEncoded
    @POST("/mobile/comment/commentList")
    Call<ResultDO<List<CommentItem>>> getCommentList(@Field("id") String id,@Field("page")int page,@Field("rows")int rows);

    /**
     * 评价订单
     */
    @FormUrlEncoded
    @POST("/mobile/comment/save")
    Call<ResultDO> saveComment(@Field("memberId") String memberId,@Field("sellerId")String sellerId,
                               @Field("orderNo")String orderId,@Field("sellerShopId")String sellerShopId,
                               @Field("content")String content,@Field("images")String images,
                               @Field("health")Integer health,@Field("service")Integer service,
                               @Field("quality")Integer quality);



    /**
     * 购物车确认订单
     */
    @FormUrlEncoded
    @POST("/mobile/shop/cart/toAddCart")
    Call<ResultDO<OrderConfirm>> OrderConfirm(@Field("memberId") String memberId, @Field("shopId")String shopId, @Field("chooseServiceName")String chooseServiceName,
                                              @Field("chooseWaiterName")String chooseWaiterName,@Field("startTime")String startTime,
                                              @Field("endTime")String endTime,@Field("leaveTime")String leaveTime,@Field("shopType")String shopType,
                                              @Field("productArr")String productArr);


    /**
     * 创建订单
     */
    @FormUrlEncoded
    @POST("/mobile/order/createOrder")
    Call<ResultDO<String>> createOrder(@Field("cartId") Integer cartId, @Field("linkMobile")String linkMobile,
                                       @Field("linkName")String linkName,@Field("desc")String desc,@Field("type")Integer type,
                                       @Field("addressId") Integer addressId,@Field("sizeId") Integer sizeId);


    /**
     * 订单列表
     */
    @FormUrlEncoded
    @POST("/mobile/order/getOrderByMemberIdAndSellerId")
    Call<ResultDO<OrderTotal>> getOrderList(@Field("memberId") String memberId, @Field("sellerId")String sellerId,
                                            @Field("status")Integer status, @Field("pageNo")int pageNo, @Field("pageSize")Integer pageSize);


    /**
     * 订单详情
     */
    @FormUrlEncoded
    @POST("/mobile/order/getOrderDetailByOrderNo")
    Call<ResultDO<OrderDetail>> getOrderDetail(@Field("orderNo")String orderNo);

    /**
     * 订单核销二维码
     */
    @FormUrlEncoded
    @POST("/mobile/order/getMemberTwoCode")
    Call<ResultDO<String>> getMemberTwoCode(@Field("orderNo")String orderNo);


    /**
     * 账户列表
     */
    @FormUrlEncoded
    @POST("/mobile/withdraw/list")
    Call<ResultDO<List<CashAccount>>> getAccountList(@Field("memberId")String memberId);

    /**
     * 绑定银行卡
     */
    @FormUrlEncoded
    @POST("/mobile/withdraw/save")
    Call<ResultDO> saveAccount(@Field("id") String id, @Field("memberId") String memberId,
                               @Field("bankId") String bankId, @Field("account") String account,
                               @Field("accountName") String accountName);


    /**
     * 申请提现
     */
    @FormUrlEncoded
    @POST("/mobile/withdraw/doWithdraw")
    Call<ResultDO> applyCash(@Field("memberId") String memberId, @Field("withdrawsMoney") String withdrawsMoney, @Field("name") String name,
                             @Field("bankName") String bankName,@Field("bankNo")String bankNo);

    /**
     * id获取银行具体信息
     */
    @FormUrlEncoded
    @POST("/mobile/bank/getById")
    Call<ResultDO<CardType>> getBankDetail(@Field("id") String id);


    /**
     * 获取所有银行信息
     */
    @POST("/mobile/bank/list")
    Call<ResultDO<List<CardType>>> getBankList();


    /**
     * 删除银行卡
     */
    @FormUrlEncoded
    @POST("/mobile/withdraw/delete")
    Call<ResultDO> delAccount(@Field("id") String id);


    /**
     * 提现列表
     */
    @FormUrlEncoded
    @POST("/mobile/withdraw/getMemberWithdrawsRecords")
    Call<ResultDO<CashDetail>> getCashRecords(@Field("memberId")String memberId, @Field("pageNo")Integer page, @Field("pageSize")Integer rows);


    /**
     * 收益记录
     */
    @FormUrlEncoded
    @POST("/mobile/member/getMemberIncomeRecords")
    Call<ResultDO<Profit>> getProfit(@Field("memberId")String memberId, @Field("pageNo")Integer page, @Field("pageSize")Integer rows,
                                    @Field("startTime")String startTime,@Field("endTime")String endTime);


    /**
     * 收益统计
     *
     * type 1：分红 2：推荐
     */
    @FormUrlEncoded
    @POST("/mobile/member/getDividends")
    Call<ResultDO<Income>> getDividends(@Field("memberId")String memberId, @Field("type")Integer type);

    /**
     * 会员分红收益记录
     *
     */
    @FormUrlEncoded
    @POST("/mobile/member/getMemberRecRecords")
    Call<ResultDO<Profit>> getMemberRecRecords(@Field("memberId")String memberId, @Field("pageNo")Integer page, @Field("pageSize")Integer rows,
                                               @Field("startTime")String startTime,@Field("endTime")String endTime,@Field("type")int type);

    /**
     * 会员推荐收益记录
     *
     */
    @FormUrlEncoded
    @POST("/mobile/member/getRecStatistics")
    Call<ResultDO<ShareProfit>> getRecStatistics(@Field("memberId")String memberId, @Field("pageNo")Integer page, @Field("pageSize")Integer rows,
                                                    @Field("startTime")String startTime, @Field("endTime")String endTime);



    /**
     * 会员分红收益
     *
     */
    @FormUrlEncoded
    @POST("/mobile/member/getMemberBonusById")
    Call<ResultDO<Bonus>> getMemberBonusById(@Field("memberId")String memberId);

    /**
     * 会员消费记录
     *
     */
    @FormUrlEncoded
    @POST("/mobile/member/consumeDetail")
    Call<ResultDO<List<ProfitRecord>>> getConsumeDetail(@Field("memberId")String memberId,@Field("pageNo")Integer pageNo,
                                                        @Field("pageSize")Integer pageSize);


    /**
     * 留言反馈
     */
    @FormUrlEncoded
    @POST("/mobile/feedback/save")
    Call<ResultDO> saveFeedBack(@Field("body")String body);


    /**
     * 投票列表
     */
    @FormUrlEncoded
    @POST("/mobile/vote/getVoteList")
    Call<ResultDO<Vote>> getVoteList(@Field("pageNo")Integer page, @Field("pageSize")Integer rows);

    /**
     * 投票
     */
    @FormUrlEncoded
    @POST("/mobile/vote/memberVote")
    Call<ResultDO> memberVote(@Field("memberId")String memberId, @Field("voteId")String voteId,@Field("state")Integer state);


    /**
     * 投票详情
     */
    @FormUrlEncoded
    @POST("/mobile/vote/getDetailById")
    Call<ResultDO<VoteItem>> getVoteDetail(@Field("voteId")String voteId);

    /**
     * 通告
     */
    @FormUrlEncoded
    @POST("/mobile/info/notice")
    Call<ResultDO<Notice>> getNotice(@Field("sellerId")String sellerId);


    /**
     * 资讯
     *type 1:轮播图 2：文章
     */
    @FormUrlEncoded
    @POST("/mobile/info/information")
    Call<ResultDO<List<Img>>> getInformation(@Field("type")int type);


    /**
     * 文章详情
     *
     */
    @FormUrlEncoded
    @POST("/mobile/info/getById")
    Call<ResultDO<Img>> getInfoDetail(@Field("id")String id);

    /**
     * 选择房型
     *
     */
    @FormUrlEncoded
    @POST("/mobile/spa/room/shopUserList")
    Call<ResultDO<List<Room>>> getRoomList(@Field("shopId")String shopId);


    /**
     * 选择美容师
     */
    @FormUrlEncoded
    @POST("/mobile/shop/staff/staffList")
    Call<ResultDO<List<Staff>>> getStaffList(@Field("sellerChooseShopId") String sellerChooseShopId, @Field("page")Integer page, @Field("rows")Integer rows);

    /**
     * 发送验证码
     */
    @FormUrlEncoded
    @POST("/mobile/identifyingCode/sendMobileCode")
    Call<ResultDO> sendMobileCode(@Field("type") int type, @Field("mobile")String mobile);

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

    @FormUrlEncoded
    @POST("/mobile/apk/getApkInfo")
    Call<ResultDO<Version>> checkVersion(@Field("type")Integer type,@Field("versionCode") int versionCode);





   //下面的是fyy新增接口************************************************************************

    /**
     * 注册账号
     */
    @FormUrlEncoded
    @POST("/mobile/login/memberRegister")
    Call<ResultDO> memberRegister(@Field("mobile")String mobile,
                                  @Field("password")String password,
                                  @Field("yzm")String code);

    /**
     * 充值 type=1支付宝2微信
     */
    @FormUrlEncoded
    @POST("/mobile/order/recharge")
    Call<ResultDO> recharge(@Field("memberId")int memberId,
                            @Field("money")int money,
                            @Field("type")int type);

    /**
     * id获取商铺产品详情
     */
    @FormUrlEncoded
    @POST("/mobile/shop/product/getProductListBySellerIdAndShopId")
    Call<ResultDO> getTiansheShopDetail(@Field("shopId") String shopId);

    /**
     * 增加或修改收货信息
     * addressId修改时传
     */
    @FormUrlEncoded
    @POST("/mobile/address/saveAddress")
    Call<ResultDO> saveAddress(@Field("memberId")int memberId,
                               @Field("name")String name,
                               @Field("mobile")String mobile,
                               @Field("address")String address,
                               @Field("addressId")Integer addressId);

    /**
     * 删除收货信息
     */
    @FormUrlEncoded
    @POST("/mobile/address/deleteAddress")
    Call<ResultDO> deleteAddress(@Field("addressId")int addressId);
    /**
     * 设置默认收货地址
     */
    @FormUrlEncoded
    @POST("/mobile/address/setDefault")
    Call<ResultDO> setDefault(@Field("memberId")int memberId,@Field("addressId")int addressId);
    /**
     * 获取默认收货地址
     */
    @FormUrlEncoded
    @POST("/mobile/address/getDefault")
    Call<ResultDO<Address>> getDefault(@Field("memberId")int memberId);
    /**
     * 获取收货人列表
     */
    @FormUrlEncoded
    @POST("/mobile/address/addressList")
    Call<ResultDO<List<Address>>> addressList(@Field("memberId")int memberId);

    /**
     * 添加用户身体尺寸
     */
    @FormUrlEncoded
    @POST("/mobile/memberSize/addSize")
    Call<ResultDO> addSize(@Field("memberId")int memberId,
                           @Field("name")String name,
                           @Field("yichang")String yichang,
                           @Field("jiankuan")String jiankuan,
                           @Field("xiongwei")String xiongwei,
                           @Field("yaowei")String yaowei,
                           @Field("tunwe")String tunwe,
                           @Field("xiuchang")String xiuchang);
    /**
     * 更新用户身体尺寸
     */
    @FormUrlEncoded
    @POST("/mobile/memberSize/updateSize")
    Call<ResultDO> updateSize(@Field("id")int id,
                           @Field("name")String name,
                           @Field("yichang")String yichang,
                           @Field("jiankuan")String jiankuan,
                           @Field("xiongwei")String xiongwei,
                           @Field("yaowei")String yaowei,
                           @Field("tunwe")String tunwe,
                           @Field("xiuchang")String xiuchang);
    /**
     * 获取用户身体尺寸list
     */
    @GET("/mobile/memberSize/list")
    Call<ResultDO<List<MemberSize>>> sizeList(@Query("memberId") int id);
    /**
     * 删除用户身体尺寸
     */
    @GET("/mobile/memberSize/delSize")
    Call<ResultDO> delSize(@Query("id") int id);

    /**
     * 所有省份
     */
    @POST("/mobi/cart/AhGetProvinces")
    Call<ResultDO<List<City>>> getProvinces();

    /**
     * 所有城市
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AhGetCitys")
    Call<ResultDO<List<City>>> getCitys(@Field("provinceId") Integer provinceId);

    /**
     *所有区县
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AhGetDistricts")
    Call<ResultDO<List<City>>> getDistricts(@Field("cityId") Integer cityId);

    /**
     * 获取默认收货地址
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AgGetDefault")
    Call<ResultDO<Address>> getDefaultAddr(@Field("memberId") long memberId);

    /**
     * 创建服装订单
     */
    @FormUrlEncoded
    @POST("/mobile/order/createOrder")
    Call<ResultDO<String>> createClothOrder(@Field("cartId") Integer cartId, @Field("linkMobile")String linkMobile,
                                       @Field("linkName")String linkName,@Field("desc")String desc,
                                       @Field("addressId")Integer addressId,@Field("sizeId")Integer sizeId,
                                            @Field("type")Integer type);
    /**
     * 购物车列表
     */
    @FormUrlEncoded
    @POST("/mobile/shop/cart/getCart")
    Call<ResultDO<List<CartTotal>>> getCartList(@Field("memberId") Integer memberId);
    /**
     * 更新购物车
     */
    @FormUrlEncoded
    @POST("/mobile/shop/cart/updateCart")
    Call<ResultDO> updateCart(@Field("cartItemId") Integer cartItemId,@Field("num") Integer num);
    /**
     * 获取天奢商城订单列表
     */
    @FormUrlEncoded
    @POST("/mobile/order/getMarketOrder")
    Call<ResultDO<List<com.sunday.tianshehuoji.entity.Order>>> getMarketOrder(@Field("memberId") Integer memberId,@Field("pageNo") Integer pageNo,
                                  @Field("status") Integer status,@Field("pageSize") Integer pageSize);
    /**
     * 取消
     */
    @FormUrlEncoded
    @POST("/mobile/order/cancelOrder")
    Call<ResultDO> cancelOrder(@Field("orderId") Integer orderId);
    /**
     * POST /mobile/order/getOrderDetail 根据订单id，获取订单详情
     */
    @FormUrlEncoded
    @POST("/mobile/order/getOrderDetail")
    Call<ResultDO<OrderResult>> getOrderDetailByOrderId(@Field("orderId") Integer orderId);
    /**
     * 完成订单支付type1 支付宝2 微信 3余额
     */
    @FormUrlEncoded
    @POST("/mobile/order/payOrder")
    Call<ResultDO> payOrder(@Field("orderId") Integer orderId,@Field("type") Integer type);
}
