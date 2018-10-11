package com.xiaohou.wechatsharedemo

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.xiaohou.wechatsharedemo.utlis.Constant
import com.xiaohou.wechatsharedemo.utlis.WeChatUtlis

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btn_wechat_share_friend).setOnClickListener { shareWeChat(false) }
        findViewById<View>(R.id.btn_wechat_share_friend_circle).setOnClickListener { shareWeChat(true) }
        findViewById<View>(R.id.btn_wechat_open_applet).setOnClickListener {         WeChatUtlis.wxOpenApplet() }
        findViewById<View>(R.id.btn_wechat_share_applet).setOnClickListener { shareWeChatApplet() }
    }

    /**
     * 打开小程序
     */
    private fun shareWeChatApplet() {
        Glide.with(this@MainActivity).asBitmap().load(Constant.SHARE_HTML).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                var thumbBmp: Bitmap? = null
                if (resource.height > 100 && resource.width > 100) {
                    thumbBmp = Bitmap.createScaledBitmap(resource, 100, 100, true)
                } else {
                    thumbBmp = resource
                }
                if (thumbBmp!=null){
                    WeChatUtlis.wxShareApplet(thumbBmp,"分享title","分享内容")
                }else{
                    Toast.makeText(this@MainActivity,"获取分享图片失败",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    /**
     * 微信分享
     * @param friendCircle 是否是朋友圈
     */
    private fun shareWeChat(friendCircle: Boolean){
        Glide.with(this@MainActivity).asBitmap().load(Constant.SHARE_HTML).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                var thumbBmp: Bitmap? = null
                if (resource.height > 100 && resource.width > 100) {
                    thumbBmp = Bitmap.createScaledBitmap(resource, 100, 100, true)
                } else {
                    thumbBmp = resource
                }
                if (thumbBmp!=null){
                    WeChatUtlis.wxShare("分享title","分享内容",friendCircle,thumbBmp)
                }else{
                    Toast.makeText(this@MainActivity,"获取分享图片失败",Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

}
