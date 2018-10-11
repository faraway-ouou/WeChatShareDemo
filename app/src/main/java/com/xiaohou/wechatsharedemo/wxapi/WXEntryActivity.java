package com.xiaohou.wechatsharedemo.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaohou.wechatsharedemo.WechatActivity;
import com.xiaohou.wechatsharedemo.utlis.Constant;

/**
 * 创建时间: 2018/7/18
 * 作者: JinzhiHou
 * E-mail: 605322850@qq.com
 * 描述: WXEntryActivity
 **/
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    private IWXAPI wxAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wxAPI = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);
        wxAPI.registerApp(Constant.WX_APP_ID);

        try {
            boolean result = wxAPI.handleIntent(getIntent(), this);
            if (!result) {
                Log.e(TAG, "参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        wxAPI.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq arg0) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//登陆
            SendAuth.Resp authResp = (SendAuth.Resp) resp;
//            getAccessToken(authResp.code);
            //微信登陆自行解决哈
        } else if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
//                    ToastUtils.showShort("分享成功");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL://分享取消
//                    ToastUtils.showShort("分享失败");
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED://分享被拒绝
//                    ToastUtils.showShort("分享取消");
                    break;
            }
        }else  if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            //小程序返回app
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            String extraData =launchMiniProResp.extMsg; // 对应JsApi navigateBackApplication中的extraData字段数据
            //这里返回标识可以自行和小程序商量统一
            if (extraData.equals("wechat")) {
                startActivity(new Intent(WXEntryActivity.this,WechatActivity.class));
            }
        }
        finish();
    }
}