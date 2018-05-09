package com.sunday.tianshe.branch.http;

import com.sunday.common.model.ResultDO;
import com.sunday.tianshe.branch.entity.BaseMember;
import com.sunday.tianshe.branch.entity.ChildAccount;
import com.sunday.tianshe.branch.entity.Member;
import com.sunday.tianshe.branch.entity.Product;
import com.sunday.tianshe.branch.entity.Profit;
import com.sunday.tianshe.branch.entity.Record;
import com.sunday.tianshe.branch.entity.RecordDO;
import com.sunday.tianshe.branch.entity.Shop;
import com.sunday.tianshe.branch.entity.Stock;
import com.sunday.tianshe.branch.entity.Version;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by admin on 2016/12/21.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("/mobile/login/sellerLogin")
    Call<ResultDO<Member>> login(@Field("userName")String name, @Field("password")String pwd);

    /**
     * 会员列表
     * @param sellerId
     * @param page
     * @param rows
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/certification/memberList")
    Call<ResultDO<List<Member>>> memberList(@Field("sellerId")Long sellerId,@Field("page")Integer page,
                                                @Field("rows")Integer rows);

    @FormUrlEncoded
    @POST("/mobile/seller/detail/getSellerShopIncomeList")
    Call<ResultDO<List<Shop>>> shopList(@Field("sellerId")Long sellerId, @Field("page")Integer page,
                                        @Field("rows")Integer rows);

    /**
     * 推荐人列表
     * @param page
     * @param rows
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/certification/recList")
    Call<ResultDO<List<BaseMember>>> recList(@Field("page")Integer page, @Field("rows")Integer rows);

    /**
     * 添加会员认证
     * @param sellerId
     * @param type
     * @param realName
     * @param password
     * @param mobile
     * @param idCard
     * @param recId
     * @param arrays
     * @param contractImages
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/certification/save")
    Call<ResultDO> memberAuth(@Field("sellerId")Long sellerId, @Field("type")Integer type,
                              @Field("realName")String realName, @Field("password")String password,
                              @Field("mobile")String mobile, @Field("idCard")String idCard,
                              @Field("recId")Long recId, @Field("idImages")String[] arrays,
                              @Field("contractImages")String[]contractImages,
                              @Field("code")String code);

    /**
     * 添加子账号
     * @param id
     * @param userName
     * @param password
     * @param mobile
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/seller/user/save")
    Call<ResultDO> saveChildAccount(@Field("id")Long id,@Field("sellerId")Long sellerId,
                                    @Field("userName")String userName,
                                    @Field("password")String password,@Field("mobile")String mobile,
                                    @Field("code")String code);

    /**
     * 子账号列表
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/seller/user/sellerUserList")
    Call<ResultDO<List<ChildAccount>>> childAccountList(@Field("id")Long id,
                                                        @Field("sellerId")Long sellerId);

    /**
     * 更改头像
     * @param id
     * @param logo
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/seller/user/update")
    Call<ResultDO> updateInfo(@Field("id")Long id,@Field("logo")String logo);

    /**
     * 更改账号或者密码
     * @param id
     * @param mobile
     * @param pwd
     * @param code
     * @return
     */

    @FormUrlEncoded
    @POST("/mobile/seller/user/updateMobileOrPassword")
    Call<ResultDO> updateMobilOrPwd(@Field("id")Long id,@Field("mobile")String mobile,
                                    @Field("password")String pwd,@Field("code")String code);

    @FormUrlEncoded
    @POST("/mobile/seller/detail/getSellerIncomeBySellerId")
    Call<ResultDO<Profit>> getShopProfit(@Field("sellerId")Long sellerId,
                                         @Field("startTime")String startTime,
                                         @Field("endTime")String endTime);


    @FormUrlEncoded
    @POST("/mobile/shop/income/getShopIncomeByShopId")
    Call<ResultDO<Profit>> getShopProfit2(@Field("shopId")Long shopId,
                                         @Field("startTime")String startTime,
                                         @Field("endTime")String endTime);
    /**
     * 生活馆产品列表
     * @param sellerId
     * @param shopId
     * @param page
     * @param rows
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/seller/detail/getProductList")
    Call<ResultDO<List<Product>>> productList(@Field("sellerId")Long sellerId,@Field("shopId")Long shopId,
                                              @Field("page")Integer page, @Field("rows")Integer rows);

    @FormUrlEncoded
    @POST("/mobile/identifyingCode/sendMobileCode")
    Call<ResultDO> sendValidateCode(@Field("type")int type,@Field("mobile")String mobile);


    @FormUrlEncoded
    @POST("/mobile/seller/user/updateMobileOrPassword")
    Call<ResultDO> findPwd(@Field("mobile")String mobile,@Field("password")String password,
                           @Field("code")String code);
    /**
     * 获取收益明细
     * @param sellerId
     * @param type
     * @param startTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/seller/detail/getSellerShopIncomeOrCostDetail")
    Call<ResultDO<RecordDO>>  getShopProfitRecord(@Field("sellerId")Long sellerId,
                                                  @Field("type")int type,
                                                  @Field("startTime")String startTime,
                                                  @Field("endTime")String endTime,
                                                  @Field("pageNo")Integer pageNo,
                                                  @Field("pageSize")Integer pageSize);

    @FormUrlEncoded
    @POST("/mobile/shop/income/getShopIncomeOrCostDetail")
    Call<ResultDO<RecordDO>>  getShopProfitRecord2(@Field("shopId")Long shopId,
                                                  @Field("type")int type,
                                                  @Field("startTime")String startTime,
                                                  @Field("endTime")String endTime,
                                                  @Field("pageNo")Integer pageNo,
                                                  @Field("pageSize")Integer pageSize);

    /**
     * 删除子账号
     */
    @FormUrlEncoded
    @POST("/mobile/seller/user/delete")
    Call<ResultDO> deleteChildAccount(@Field("id")Long id);

    /**
     * 会员认证
     */
    @FormUrlEncoded
    @POST("/mobile/certification/save2")
    Call<ResultDO> memberAuth(@Field("type")int type,@Field("userName")String userName,
                             @Field("password")String password,
                             @Field("mobile")String mobile,
                             @Field("idCard")String idCard,
                             @Field("recId")Long recId,
                             @Field("idImages")String idImages,
                             @Field("contractImages")String contractImages,
                             @Field("code")String code,
                              @Field("stockTime")String stockTime,
                              @Field("stockPoolsId")Long stockPoolsId,
                              @Field("money")String money);
    @FormUrlEncoded
    @POST("/mobile/seller/user/update")
    Call<ResultDO> updateInfo(@Field("id")Long id,
                              @Field("logo")String logo,
                              @Field("password")String password,
                              @Field("oldPassword")String oldPassword);

    @FormUrlEncoded
    @POST("/mobile/apk/getApkInfo")
    Call<ResultDO<Version>> checkVersion(@Field("versionCode") int versionCode,@Field("type")Integer type);

    @FormUrlEncoded
    @POST("/mobile/seller/user/getById")
    Call<ResultDO<Member>>  getShopInfo(@Field("id")Long id);

    @FormUrlEncoded
    @POST("/mobile/certification/search")
    Call<ResultDO<List<BaseMember>>>  searchMember(@Field("keyword")String keyword);

    @FormUrlEncoded
    @POST("/mobile/certification/stockPoolsList")
    Call<ResultDO<List<Stock>>> stockList(@Field("a")Integer a);

}
