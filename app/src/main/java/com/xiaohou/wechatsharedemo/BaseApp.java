package com.xiaohou.wechatsharedemo;

import android.app.Application;
import android.content.Context;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaohou.wechatsharedemo.utlis.Constant;

/**
 * 作者: JinzhiHou
 * E-mail: 605322850@qq.com
 * Blog: www.xiaohoutongxue.cn
 * 描述: BaseApp
 **/
public class BaseApp extends Application {

    public static IWXAPI mWxApi;
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        registerToWX();
        mContext = this;
    }
    private void registerToWX() {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(Constant.WX_APP_ID);
    }
}
