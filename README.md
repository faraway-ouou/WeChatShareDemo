一直在搞微信分享但是从未搞过分享小程序，今天搞了一下还真的是学习了。
此项目是kotlin和Java混写的（初学Kotlin可能用法不恰当的欢迎指正，嘿嘿...........)

> 这里着重说一下微信分享小程序、APP小程序互跳

1、微信分享小程序[(微信官网)](https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317340&token=ae4e632e7dcc175a183ce11346e0dfcd98399d82&lang=zh_CN)

```java
WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
miniProgramObj.webpageUrl = "https://www.baidu.com/"; // 兼容低版本的网页链接
miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
miniProgramObj.userName = "gh_xxxxxxxxxx";     // 小程序原始id
miniProgramObj.path = "/pages/xxxx/xxxx";            //小程序页面路径 
WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
msg.title = "Title";                    // 小程序消息title
msg.description = "Desc";               // 小程序消息desc
msg.thumbData = getThumb();             // 小程序消息封面图片，小于128k
SendMessageToWX.Req req = new SendMessageToWX.Req();
req.transaction = buildTransaction("webpage");
req.message = msg;
req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前支持会话
api.sendReq(req);

```
2、APP打开小程序

```java
IWXAPI api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID);
WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
req.userName = "gh_e30198451645"; // 填小程序原始id
//传参
req.path = "pages/index/userLogin/userLogin";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
//如需传参跳转的话参考如下
//req.path = "pages/xxx/xxx?key=value&key=value";  
req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
api.sendReq(req);
```
WXEntryActivity中配置
```java
public void onResp(BaseResp resp) {
    if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
        WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
        String extraData =launchMiniProResp.extMsg; // 对应JsApi navigateBackApplication中的extraData字段数据
    }
}

```
3、小程序返回App[（微信官网）](https://developers.weixin.qq.com/miniprogram/dev/api/launchApp.html)

```wxml
<button open-type="launchApp" app-parameter="wechat" binderror="launchAppError">打开APP</button>
```
温馨提示：小程序打开APP，前提是你是从APP内打开小程序，才能返回打开APP，不然是不支持的，也就是说让APP给小程序导流，然后再将流量返回到APP的能力。
