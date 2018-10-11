package com.xiaohou.wechatsharedemo.utlis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.xiaohou.wechatsharedemo.BaseApp;

import java.io.ByteArrayOutputStream;

public class WeChatUtlis {
    private static final int IMAGE_SIZE = 32768;
    private static byte[] result;
    private static Bitmap map;
    private static Bitmap bitmap;

    /**
     * 微信分享
     * @param title
     * @param desc
     * @param friendsCircle
     * @param imgUrl
     */
    public static void wxShare( String title, String desc, boolean friendsCircle, Bitmap imgUrl) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "https://blog.csdn.net/qq_33722930";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        byte[] bm = bitmapBytes(imgUrl, true);
        msg.thumbData = bm;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = friendsCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        BaseApp.mWxApi.sendReq(req);
    }

    /**
     * bitmap转字节流
     *
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bitmapBytes(final Bitmap bmp, final boolean needRecycle) {
        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                //F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }


    /**
     * 微信分享小程序
     *
     * @param imgUrl
     * @param title
     * @param desc
     */
    public static void wxShareApplet(Bitmap imgUrl, String title, String desc) {
        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = "https://www.baidu.com/"; // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = Constant.WX_APPLET_ID;     // 小程序原始id
        miniProgramObj.path = Constant.WX_APPLET_PATH;            //小程序页面路径
        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
        msg.title = title;                    // 小程序消息title
        msg.description = desc;               // 小程序消息desc
        byte[] bm = bitmapBytes(imgUrl, true);
        msg.thumbData = bm;             // 小程序消息封面图片，小于128k
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前支持会话
        BaseApp.mWxApi.sendReq(req);
    }

    /**
     * APP打开小程序
     */

    public static void wxOpenApplet() {
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = Constant.WX_APPLET_ID; // 填小程序原始id
        //传参
        req.path = Constant.WX_APPLET_PATH;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        //如需传参跳转的话参考如下
        //req.path = Constant.WX_APPLET_PATH+"?key=value&key=value";
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        BaseApp.mWxApi.sendReq(req);
    }
}
