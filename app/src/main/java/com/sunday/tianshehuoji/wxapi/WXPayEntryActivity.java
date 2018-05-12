package com.sunday.tianshehuoji.wxapi;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sunday.common.utils.ToastUtils;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshehuoji.BaseApplication;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.ui.fyy.ComitClothOrderActivity;
import com.sunday.tianshehuoji.ui.fyy.ComitOrderActivity;
import com.sunday.tianshehuoji.ui.manage.ReChargeActivity;
import com.sunday.tianshehuoji.ui.product.BuyOrderActivity;
import com.sunday.tianshehuoji.ui.product.ConfirmBuyActivity;
import com.sunday.tianshehuoji.ui.product.OrderPayActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d("微信支付", "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode == 0){
				if (BaseApplication.getPayFlag()==1) {//钱包充值
					ReChargeActivity.reChargeActivity.finish();
					finish();
				}else if(BaseApplication.getPayFlag()==2){//订单支付
					BaseApplication.setClearCartFlag(1);
					finish();
					BuyOrderActivity.buyOrderActivity.finish();
					ConfirmBuyActivity.confirmBuyActivity.finish();
				}else if(BaseApplication.getPayFlag()==3){//天奢商城
					finish();
					BuyOrderActivity.buyOrderActivity.finish();
					ComitOrderActivity.comitOrderActivity.finish();
				}else if(BaseApplication.getPayFlag()==4){//服装定制
					finish();
					BuyOrderActivity.buyOrderActivity.finish();
					ComitClothOrderActivity.comitClothOrderActivity.finish();
				}else if(BaseApplication.getPayFlag()==5){//我的订单
					finish();
					OrderPayActivity.orderPayActivity.finish();
				}
			}
			else if(resp.errCode == -1){
				Toast.makeText(getApplicationContext(), "支付失败", ToastUtils.LENGTH_LONG).show();
			}
			else if(resp.errCode == -2){
				Toast.makeText(getApplicationContext(), "支付已取消", ToastUtils.LENGTH_LONG).show();
				finish();
			}
		}
	}
}