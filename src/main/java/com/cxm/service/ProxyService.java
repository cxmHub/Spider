package com.cxm.service;

import com.cxm.dao.ProxyDao;
import com.cxm.pojo.MyProxy;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.List;

/**
 * @author cxm
 * @description
 * @date 2019-09-17 14:27
 **/
public class ProxyService implements PageProcessor {

    // 代理隧道验证信息
    private final static String ProxyUser = "7IJY1644910250631011";
    private final static String ProxyPass = "QjumaTA5v5OWXDV1";

    // 代理服务器
    private final static String ProxyHost = "dyn.horocn.com";
    private final static Integer ProxyPort = 50000;

    Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(1000)
            .setCharset("UTF-8")
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36")
            .addCookie("Cookie", "Province=010; City=010; _ntes_nnid=1da530855269cd2248880ef74b8ed4dd,1542714280735; _ntes_nuid=1da530855269cd2248880ef74b8ed4dd; _iuqxldmzr_=32; __utmz=94650624.1542779633.2.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); WM_TID=n1Ycf2YeAO9BUERQVAc5Pkpl82cBqaRe; vinfo_n_f_l_n3=14f582b0a2927ff7.1.1.1542714280751.1542714574779.1542797535101; __f_=1542933417229; __utmc=94650624; WM_NI=utR1PvQJfANmKL4oDfK%2Bk9pbf%2BF82O0crOTm%2BJUVDXviPku9zFjdVMoxKe%2FUmZPKjVgSDW6H08AH06kLTqq32FOOP4B1TeJouc2ExLvRKm4QlmRyIHb1bTbgllvVJhRdVGo%3D; WM_NIKE=9ca17ae2e6ffcda170e2e6ee88c73fae99aeb9f774a8ef8fb3c44b829e9bbabc6ff3aebc85ca3d86b3fb9ad72af0fea7c3b92ab7ac9ed2d37e9396aaa2cd678994fc90c76e89f5a5daf36aa6f58ad9b145f4bdbeb0d767e9969bb7c754b6b7a08cf833ba9aa685d547ada88586ae5faf86a0ace95ea58d8cb8b544f4e88fa9b33ea9949c92f439f29a81b0d34082ef9c86c965b7bdfea6fb48b68e858eb75bb0b39e9acc44a2efaeb5e16db0b4bab7c96492bc9aa8ee37e2a3; JSESSIONID-WYYY=%2F3r3a4xofssV7b28GPD0%2BRAQo%2BdCwMvcaMGbEUJErn6tgzrCaZwzp9vpE6uzeeEZSC2xYKAoTpy%2Fm6Aw7PB21C1x65vjlHe4Qza5IV%2Fzhs%2F%2BgZaomnYwxIQfahpvphJ0EwMi4f5yY1WI0j%5C6NlomTMwgAMfOj6dsB%2B%2FN3k%2B%2BYxa6YzyO%3A1542969031150; __utma=94650624.734640895.1542779633.1542938657.1542967231.6; __utmb=94650624.6.10.1542967231");

    @Override
    public void process(Page page) {
        String ip1 = "//*[@id=\"ip_list\"]/tbody/tr/td[2]/text()";

        String port1 = "//*[@id=\"ip_list\"]/tbody/tr/td[3]/text()";

        List<String> ipAll = page.getHtml().xpath(ip1).all();
        List<String> portAll = page.getHtml().xpath(port1).all();
        for (int i = 0; i < ipAll.size(); i++) {
            String ip = ipAll.get(i);
            int port = Integer.parseInt(portAll.get(i));
//            boolean check = ProxyCheck.check(ip, port);
//            if(check){
            MyProxy myProxy = new MyProxy();
            myProxy.setIp(ip);
            myProxy.setPort(port);
            new ProxyDao().addProxy(myProxy);
//            }
        }

    }

    @Override
    public Site getSite() {
        return this.site;
    }


    public static void main(String[] args) {


        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("27.203.220.31",8060)));

        try {

            for (int i = 1; i <= 736; i++) {
                System.err.println("开始查询页数：" + i);
                String url = "https://www.xicidaili.com/nt/" + i;
                Request request = new Request(url);
                request.setMethod(HttpConstant.Method.GET);
                Spider.create(new ProxyService())
                        .addRequest(request)
                        .setDownloader(httpClientDownloader)
                        .thread(10)
                        .run();
//                Thread.sleep((new Random().nextInt(1000)));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
