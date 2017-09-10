package controller;

/**
 * Created by Administrator on 2017/9/10.
 */

import news.News;
import android.content.Context;

import com.bigkoo.svprogresshud.SVProgressHUD;

import cn.sharesdk.onekeyshare.*;

public class NewsSharer {
    private NewsSharer() {} ;
    private static NewsSharer newsSharer = new NewsSharer() ;
    public static NewsSharer getInstance() {return newsSharer ;}

    public void shareNews(Context mContext, News news) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("I am Title");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        //oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段

        //oks.setPlatform(Platform.SHARE_WEBPAGE);
        oks.setText(news.getUrl());
        oks.setUrl(news.getUrl());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        //oks.setUrl("http://sharesdk.cn");
        //oks.setImageUrl("http://img003.21cnimg.com/photos/album/20160808/m600/A3B78A702DF9BF0EE02ADFD5D4F53D54.jpeg");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        //oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(mContext);
        new SVProgressHUD(mContext).showInfoWithStatus("分享提示");
    }
}
