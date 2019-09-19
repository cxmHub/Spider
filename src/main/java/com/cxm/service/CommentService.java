package com.cxm.service;

import com.cxm.dao.CommentDao;
import com.cxm.pojo.Comment;
import com.cxm.util.MusicEncrypt;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class CommentService implements PageProcessor {

    // 代理隧道验证信息
    private final static String ProxyUser = "7IJY1644910250631011";
    private final static String ProxyPass = "QjumaTA5v5OWXDV1";

    // 代理服务器
    private final static String ProxyHost = "dyn.horocn.com";
    private final static Integer ProxyPort = 50000;


    Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(1000)
            .setDomain("http://music.163.com")
            .setCharset("UTF-8")
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36")
            .addCookie("Cookie", "_iuqxldmzr_=32; _ntes_nnid=828e915b9d457c306abb6be2976d6ea9,1562139068293; _ntes_nuid=828e915b9d457c306abb6be2976d6ea9; WM_TID=3cvYDN0uXrhAQEVQBEIsmpo92u3VvXxo; _ga=GA1.2.1492422079.1562141089; mail_psc_fingerprint=3719fe8aedf4071118f54474ccc281ff; usertrack=CrH1tV04APF5QJF6Aw1qAg==; P_INFO=lr17316033961@163.com|1565252425|0|urs|00&99|bej&1565235123&urs#bej&null#10#0#0|&0|urs|lr17316033961@163.com; WM_NI=WzH2wUzJLOXhm1o3krCtG053N0%2BNF4JdHBjD14tMpN%2F7xIKWXTtpNbnW6B9BB34CmnzJLv06dIccFZbdf2Oje7sSFt5f6geiCdL2UQK6nYIaB%2FDmIoDoJX%2Fx814o5Iimc0k%3D; WM_NIKE=9ca17ae2e6ffcda170e2e6eeb4c73f8697b9baf96e9bbc8ba2c85b838e9f84ee79b5aafdb5dc5c8af0899bc22af0fea7c3b92a95a688d3f65fba9684b0db64afefa1d4ea7a81ac89d1b8398ef0e5afc5408abc88a3e950f3ecbcaef346ede9b8aaca5d9c8cbc91cd6aae89faa3bc4a82edfca4f066ba968497b33ea897ad84c552a2bcaad3ce62a7e9bdb4c580f7bafa88e86381bc84d3d85b89eaa2bacc6b82f18c8ecc6588b186b9ae74fceffc90b843abb3add1ea37e2a3; JSESSIONID-WYYY=5TtTKPaHTpFSUF1%2F%2FwB3p5oYmcHsDe%2FV8vz3ujKR%2FBQ3W7%2F9HFgqTX66T7dl2KAY4Vkf3CfsZfSly7D3%5CIIAf1E7tRjx2o%5COSK5ID8pDQxtw7ANvfRzVgQS0aJXyG4bfIPMYmzyyADDKc8yeY%2FY%2FUqsDNwf3gwF1Fb1%2BKqHZgxdEOZiq%3A1568713102222; MUSIC_U=92cb9234ef65130d68b72b7f8d24691b76f80c4cb69e89c19ee4f1cb3232e7035573979d4fa9d0ea48a176cdf31814827955a739ab43dce1; __remember_me=true; __csrf=f6618e7d1b51e227d863ffc579a27c95; ntes_kaola_ad=1")
            .addHeader("Host", "music.163.com");

    @Override
    public void process(Page page) {


        List<String> contentList = new JsonPathSelector("$.comments.[*].content").selectList(page.getRawText());
        List<String> timeList = new JsonPathSelector("$.comments.[*].time").selectList(page.getRawText());
        List<String> nicknameList = new JsonPathSelector("$.comments.[*].user.nickname").selectList(page.getRawText());


        for (int i = 0; i < contentList.size(); i++) {
            Comment comment = new Comment();
            comment.setComment(filterEmoji(contentList.get(i)));
            comment.setNickname(nicknameList.get(i));
            comment.setTime(timeList.get(i));
            CommentDao commentDao = new CommentDao();
            // 是否存在
            String existComment = commentDao.existComment(comment);
            if (existComment.equals("e")) {
                System.out.println("已经存在， 不插入");
            } else {
                // 不存在再插入
                commentDao.addComment(comment);
            }
        }


    }

    @Override
    public Site getSite() {
        return this.site;
    }

    public static void main(String[] args) {


        String ip = "125.112.36.218";
        int port = 35237;


        try {

//            HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//            httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(ProxyHost,ProxyPort,ProxyUser,ProxyPass)));

            for (int i = 10; i <= 5930; i++) {
                System.err.println("开始查询页数：" + i);
                String url = "https://music.163.com/weapi/v1/resource/comments/R_SO_4_474567580?csrf_token=f6618e7d1b51e227d863ffc579a27c95";
                Request request = new Request(url);
                Map<String, Object> makePostParam = MusicEncrypt.makePostParam("474567580", "false", i);
                request.setRequestBody(HttpRequestBody.form(makePostParam, "utf8"));
                request.setMethod(HttpConstant.Method.POST);
                Spider.create(new CommentService())
//                    .addUrl("https://music.163.com/song?id=474567580")
//                        .setDownloader(httpClientDownloader)
                        .addRequest(request)
                        .thread(3)
                        .run();
                Thread.sleep((new Random().nextInt(1000) + 1000));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 替换emoj表情
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (StringUtils.isNotBlank(source)) {
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
        } else {
            return source;
        }
    }
}
