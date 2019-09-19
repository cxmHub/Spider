package com.cxm.service;

import com.cxm.dao.DontCryDao;
import com.cxm.pojo.DontCry;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * @author cxm
 * @description
 * @date 2019-09-19 14:13
 **/
public class DontCryService implements PageProcessor {

    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(1000)
            .setCharset("UTF-8")
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
            .addCookie("Cookie", "bid=UzKQwLKOZW0; douban-fav-remind=1; ll=\"108288\"; __gads=ID=4c2fbb8616f585b5:T=1568859452:S=ALNI_MZBvBG_kqnO7p-e9CO0xfY75GrdeQ; _vwo_uuid_v2=DD6644B13353A814520E54CB4AC75545C|fb5989a69f0a00f4029f30b830ba6659; __utma=266659602.534805917.1568859662.1568859662.1568859662.1; __utmz=266659602.1568859662.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); trc_cookie_storage=taboola%2520global%253Auser-id%3D4638d706-f77d-42ff-a7a2-c87d64fc902c-tuct4206955; __yadk_uid=JmV1xspRvQ8ERvDz0xLb4eDwmscH5pJH; __utma=30149280.729394809.1564543971.1568859430.1568872382.6; __utmc=30149280; __utmz=30149280.1568872382.6.4.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; ap_v=0,6.0; _pk_ref.100001.afe6=%5B%22%22%2C%22%22%2C1568872454%2C%22https%3A%2F%2Fwww.douban.com%2Fsearch%3Fq%3D%25E8%25AF%25B4%25E5%25A5%25BD%25E4%25B8%258D%25E5%2593%25AD%22%5D; _pk_ses.100001.afe6=*; __utmt=1; ct=y; viewed=\"34815690_34788620_30202016\"; _pk_id.100001.afe6=880db782aa33e4a3.1568859449.2.1568874677.1568860576.; __utmb=30149280.44.9.1568874677148")
            .addHeader("Host", "music.douban.com");

    @Override
    public void process(Page page) {

        // username
        // //*[@id="comments"]/ul/li[1]/div[2]/h3/span[2]/a
        // //*[@id="comments"]/ul/li[2]/div[2]/h3/span[2]/a
        List<String> usernameList = page.getHtml().xpath("//*[@id=\"comments\"]/ul/li/div[2]/h3/span[2]/a/text()").all();
        // content
        // //*[@id="comments"]/ul/li[1]/div[2]/p/span
        // //*[@id="comments"]/ul/li[2]/div[2]/p/span
        List<String> contentList = page.getHtml().xpath("//*[@id=\"comments\"]/ul/li/div[2]/p/span/text()").all();
        // star
        // //*[@id="comments"]/ul/li[1]/div[2]/h3/span[2]/span[1]
        // //*[@id="comments"]/ul/li[2]/div[2]/h3/span[2]/span[1]
        List<String> starList = page.getHtml().xpath("//*[@id=\"comments\"]/ul/li/div[2]/h3/span[2]/span[1]/@title").all();
        // time
        // //*[@id="comments"]/ul/li[1]/div[2]/h3/span[2]/span[2]
        // //*[@id="comments"]/ul/li[2]/div[2]/h3/span[2]/span[2]
        List<String> timeList = page.getHtml().xpath("//*[@id=\"comments\"]/ul/li/div[2]/h3/span[2]/span[2]/text()").all();


        // 开始爬取第749页数据
        for (int i = 4; i < timeList.size(); i++) {
            String username = filterEmoji(usernameList.get(i));
            String content = filterEmoji(contentList.get(i));
            String starText = starList.get(i);
            String time = timeList.get(i);
            int star = 0;
            switch (starText){
                case "很差":
                    star = 1;
                    break;
                case "较差":
                    star = 2;
                    break;
                case "还行":
                    star = 3;
                    break;
                case "推荐":
                    star = 4;
                    break;
                case "力荐":
                    star = 5;
                    break;
            }

            DontCry dontCry = new DontCry();
            dontCry.setUsername(username);
            dontCry.setContent(content);
            dontCry.setStar(star);
            dontCry.setTime(time);
            new DontCryDao().add(dontCry);
        }


    }

    @Override
    public Site getSite() {
        return this.site;
    }


    public static void main(String[] args) {
        String url = "https://music.douban.com/subject/34815690/comments/hot?p=";
        for (int i = 37; i <= 1000; i++) {
            System.out.println("开始爬取第" + i + "页数据");
            Spider.create(new DontCryService())
                    .addUrl(url + i)
                    .thread(2)
                    .run();
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
