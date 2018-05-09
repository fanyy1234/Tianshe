package com.sunday.member.http;

import com.sunday.common.model.ResultDO;
import com.sunday.member.entity.Address;
import com.sunday.member.entity.Ads;
import com.sunday.member.entity.BankAccount;
import com.sunday.member.entity.Image;
import com.sunday.member.entity.MemberDetail;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2016/2/23.
 */
public interface MemberService {
    /******************************************用户相关********************************************/
    /**
     * 注册
     * @param memberId
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/member/regist")
    Call<ResultDO<MemberDetail>> regist(@Field("memberId") String memberId);

    /**
     * 登录
     * @param memberId
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/member/login")
    Call<ResultDO<MemberDetail>> login(@Field("memberId") String memberId);

    /**
     * 更改用户信息
     * @param memberId
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/member/update")
    Call<ResultDO<MemberDetail>> updateInfo(@Field("memberId") String memberId);

    /**
     * 更改用户信息
     * @param memberId
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/member/resetPwd")
    Call<ResultDO<MemberDetail>> resetPwd(@Field("memberId") String memberId);

    /**
     * 更改用户密码
     * @param memberId
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/member/updatePwd")
    Call<ResultDO> updatePwd(@Field("memberId") String memberId);

    /**
     * 获取用户详情
     * @param memberId
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/member/detail")
    Call<ResultDO<MemberDetail>> userDetail(@Field("memberId") String memberId);

    /******************************************收获地址相关*******************************************/

    /**
     * 收获地址列表
     * @param memberId
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/receiveAddress/list")
    Call<ResultDO<List<Address>>> addressList(@Field("memberId") String memberId);


    /**
     * 添加收获地址
     * @param memberId
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/receiveAddress/add")
    Call<ResultDO<List<Address>>> addAddress(@Field("memberId") String memberId);

    /**
     * 添加收获地址
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/receiveAddress/delete")
    Call<ResultDO> deleteAddress(@Field("id") String id);

    /**
     * 设置为默认收货地址
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/receiveAddress/setDefault")
    Call<ResultDO> setDetault(@Field("id") String id);


    /**
     * 更改收获地址
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/receiveAddress/updateAddress")
    Call<ResultDO> updateAddress(@Field("id") String id);

    /**
     * 获取默认收获地址
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/receiveAddress/getDefault")
    Call<ResultDO<Address>> getDefault(@Field("id") String id);

    /******************************************银行卡相关********************************************/
    /**
     * 银行卡列表
     * @param memberId
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/bankAccount/list")
    Call<ResultDO<List<BankAccount>>> bankList(@Field("memberId") String memberId);

    /**
     * 绑定银行卡
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/bankAccount/bind")
    Call<ResultDO> bindBank();

    /**
     * 删除银行卡
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/bankAccount/delete")
    Call<ResultDO> deleteBank();

    /**
     * 更改提现密码
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/bankAccount/updateCashPwd")
    Call<ResultDO> updateCashPwd();

    /**
     * 重置提现密码
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/bankAccount/resetCashPwd")
    Call<ResultDO> resetCashPwd();


    /**
     * 发送验证码
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/tianjie/validateCode/send")
    Call<ResultDO> sendActiveCode(@Field("phone") String phone,@Field("type") int type);

    /**
     * 获取广告
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/adzone/getByAdzoneCode")
    Call<ResultDO<List<Ads>>> getAds(@Field("code") String code,@Field("villageId") int villageId);

    /**
     * 通用上传单图接口
     * @param body
     * @return
     */
    @Multipart
    @POST("/mobile/image/saveImage")
    Call<ResultDO<Image>> uploadSingleImg(@Part("image") RequestBody body);

    /**
     * 通用上传多图接口
     * @param body
     * @return
     */
    @Multipart
    @POST("/mobile/image/saveImages")
    Call<ResultDO<List<Image>>> uploadImgs(@Part("images") RequestBody body);

    /**
     * 通用上传单图接口
     * @param body
     * @return
     */
    @Multipart
    @POST("/mobile/core/imageUpload/ossSaveImage")
    Call<ResultDO<Image>> uploadSingleOssImg(@Part("image") RequestBody body);

    /**
     * 上传推送token
     */
    @Multipart
    @POST("/getui/saveToken")
    Call<ResultDO> saveToken(@Field("userId") String userId,
                             @Field("token") String token,
                             @Field("uutype") String uutype,
                             @Field("type") String type);


}
