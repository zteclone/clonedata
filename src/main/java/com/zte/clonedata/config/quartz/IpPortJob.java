package com.zte.clonedata.config.quartz;

import com.zte.clonedata.contanst.JobContanst;
import com.zte.clonedata.dao.IpProxyMapper;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.model.IpProxy;
import com.zte.clonedata.util.DateUtils;
import com.zte.clonedata.util.HttpUtils;
import com.zte.clonedata.util.SpringContextUtil;
import com.zte.clonedata.util.UUIDUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ProjectName: clonedata-com.zte.clonedata.config.quartz
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 10:49 2020/7/17
 *
 * @Description:  获取代理ip任务
 *
 *  从网站获取一些地址和端口,进行测试
 *
 *  此类有3次重试机会,只需要通过两次
 *  httpclient有3次重试机会
 */
@Slf4j
public class IpPortJob  implements Job {

    private static IpProxyMapper ipProxyMapper;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ExecutorService exe = Executors.newCachedThreadPool();
        //7云
        this.get7yun(exe);
        String url = "https://movie.douban.com/tag/#/?sort=U&range=0,10&tags=%E7%94%B5%E5%BD%B1";
        String json = HttpUtils.getJson(
                "http://www.89ip.cn/tqdl.html?api=1&num=10&port=&address=&isp=",
                "www.89ip.cn", HttpType.DETAIL);
        json = json.substring(json.lastIndexOf("</script>")+9,json.lastIndexOf("更好用的代理ip请访问")).replaceAll("\\n","");
        String[] split = json.split("<br>");
        for (String s : split) {
            String[] split1 = s.split(":");
            HttpHost proxy = new HttpHost(split1[0],Integer.parseInt(split1[1]) );
            int success = 0;
            for (int i = 0; i < 3; i++) {
                long start = System.currentTimeMillis();
                boolean b = HttpUtils.testGet(url, JobContanst.DOUBAN_HOST1,proxy);
                if (System.currentTimeMillis() - start < 100000 && b){
                    success++;
                }
            }
            if (success > 1) {
                insert(proxy.getHostName(),proxy.getPort(),"HTTP");
            }
        }
        exe.shutdown();
        while (true) {
            if (exe.isTerminated()) {
                break;
            }
            Thread.sleep(10000);
        }
        log.info("更新ip库成功");
    }


    private void get7yun(ExecutorService exe){
        exe.execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                String url = "https://movie.douban.com/tag/#/?sort=U&range=0,10&tags=%E7%94%B5%E5%BD%B1";
                Random random = new Random();
                String html = HttpUtils.getJson(
                        "https://www.7yip.cn/free/?action=china&page=".concat(String.valueOf(random.nextInt(10)+1)),
                        "www.7yip.cn", HttpType.DETAIL);
                Document parse = Jsoup.parse(html);
                Elements trs = parse.select("tr");
                for (int i = 1; i < trs.size(); i++) {
                    Element element = trs.get(i);
                    if (Integer.parseInt(element.child(5).text().split("\\.")[0])<30){
                        String ip = element.child(0).text();
                        int port = Integer.parseInt(element.child(1).text());
                        String httpType = element.child(3).text();
                        HttpHost proxy = new HttpHost(ip,port,httpType);
                        int success = 0;
                        for (int j = 0; j < 3; j++) {
                            long start = System.currentTimeMillis();
                            boolean b = HttpUtils.testGet(url, JobContanst.DOUBAN_HOST1,proxy);
                            if (b && System.currentTimeMillis() - start < 100000){
                                success++;
                            }
                        }
                        if (success > 1) {
                            insert(ip,port,httpType);
                        }
                    }
                }
            }
        });
    }

    private void insert(String ip,int port,String httpType){
        if (ipProxyMapper == null){
            ipProxyMapper = SpringContextUtil.getBean(IpProxyMapper.class);
        }
        IpProxy ipProxy = new IpProxy();
        ipProxy.setId(UUIDUtils.get());
        ipProxy.setIp(ip);
        ipProxy.setPort(port);
        ipProxy.setUpdDt(DateUtils.getNowYYYYMMDDHHMMSS());
        ipProxy.setErrorCount(0);
        ipProxy.setExecuteCount(0);
        ipProxy.setHttpType(httpType.trim());
        ipProxy.setIsOld(0);
        ipProxyMapper.insert(ipProxy);
    }

}
