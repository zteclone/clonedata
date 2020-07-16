package com.zte.clonedata.util;

import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.model.error.EmBusinessError;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.*;

/**
 * ProjectName: clonedata-com.zte.clonedata.util
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 18:03 2020/5/28
 * @Description:
 */
@Slf4j
public class HttpUtils {
    private HttpUtils() {
    }

    private static final String[] USER_AGENT = {
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:78.0) Gecko/20100101 Firefox/78.0"
            , "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36"
            , "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; Touch; rv:11.0) like Gecko"
            , "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.18363"
    };
    private static final Integer USER_AGENT_LENGTH = USER_AGENT.length;

    public static String getJson(String url, String host, HttpType httpType) throws BusinessException {
        //log.info("即将访问: {}, GET",url);
        CloseableHttpClient client = initHttpClient();//Spring: 连接池
        //HttpClient client = HttpClients.createDefault();//main: 创建一个
        HttpGet httpGet = null;
        HttpResponse response = null;
        String resultJson = null;
        Random random = new Random();
        int useragent = random.nextInt(USER_AGENT_LENGTH);
        try {
            httpGet = new HttpGet(url);
            RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
            httpGet.setConfig(defaultConfig);
            httpGet.setHeader("Host", host);
            httpGet.setHeader("User-Agent", USER_AGENT[useragent]);
            if (httpType.equals(HttpType.DOUBAN)) {
                //httpGet.setHeader("Referer","https://movie.douban.com/tag/");
            } else if (httpType.equals(HttpType.MAOYAN_DETAIL)) {
                //httpGet.setHeader("Referer",url);
            } else if (httpType.equals(HttpType.MAOYAN)) {
                //httpGet.setHeader("Cookie","bid=JY6Z9MgBEUM; _pk_ref.100001.4cf6=%5B%22%22%2C%22%22%2C1594190056%2C%22https%3A%2F%2Fwww.douban.com%2F%22%5D; _pk_id.100001.4cf6=33cccbc7ed3b8084.1592294430.9.1594190503.1593679842.; __utma=30149280.1528111153.1592294430.1593679843.1594190052.9; __utmz=30149280.1594190052.9.2.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utma=223695111.1954929102.1592294430.1593679843.1594190056.9; __utmz=223695111.1594190056.9.2.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/; dbcl2=\"188549964:fkDizA1bvRg\"; push_noty_num=0; push_doumail_num=0; __yadk_uid=oGdqqqPQkm5WnA9ajenYVtpN4BL6ZWSl; ll=\"118318\"; _vwo_uuid_v2=D52F50684BE55ADE5712B0F5A24ACBDF7|1dbe3d140be4213889241951b91c9995; __gads=ID=344007d69055847d:T=1592811923:S=ALNI_MYWPNUH2Ls2UHp_1zth8pM9S-2UgA; __utmv=30149280.18854; ck=aIyW; ap_v=0,6.0; __utmb=30149280.2.10.1594190052; __utmc=30149280; __utmt=1; _pk_ses.100001.4cf6=*; __utmb=223695111.0.10.1594190056; __utmc=223695111");
            }
            response = client.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                resultJson = EntityUtils
                        .toString(response.getEntity(), Contanst.CHARSET);
                return resultJson;
            } else {
                throw new BusinessException(EmBusinessError.HTTP_ERROR, "HttpStatus: ".concat(String.valueOf(code)));
            }
        } catch (ClientProtocolException e) {
            throw new BusinessException(EmBusinessError.HTTP_POOL_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof UnknownHostException) {
                throw new BusinessException(EmBusinessError.HTTP_ERROR);
            } else {
                throw new BusinessException(EmBusinessError.IO_ERROR);
            }
        } finally {
            // 释放资源
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                throw new BusinessException(EmBusinessError.IO_ERROR);
            }
        }
    }

    public static void picGetFileSave(String url, OutputStream outputStream) throws BusinessException {
        //log.info("即将访问: {}, GET",url);
        CloseableHttpClient client = initHttpClient();//Spring: 连接池
        //HttpClient client = HttpClients.createDefault();//main: 创建一个
        HttpGet httpGet = null;
        HttpResponse response = null;
        String resultJson = null;
        try {
            httpGet = new HttpGet(url);
            httpGet.setHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
            httpGet.setHeader("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            response = client.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                response.getEntity().writeTo(outputStream);
            }
        } catch (ClientProtocolException e) {
            throw new BusinessException(EmBusinessError.HTTP_POOL_ERROR);
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                throw new BusinessException(EmBusinessError.HTTP_ERROR);
            } else {
                throw new BusinessException(EmBusinessError.IO_ERROR, e.getMessage());
            }
        } finally {
            // 释放资源
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                throw new BusinessException(EmBusinessError.IO_ERROR, e.getMessage());
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new BusinessException(EmBusinessError.IO_ERROR, e.getMessage());
                }
            }
        }
    }


    public static Map<String, String> doGet(String url, Map<String, String> param, Integer timeoutSecond, String lid) throws Exception {
        Map<String, String> resultMap = new HashMap();
        CloseableHttpClient httpclient = initHttpClient();
        String resultString = "";
        CloseableHttpResponse response = null;
        URIBuilder builder = new URIBuilder(url);
        String key;
        if (param != null) {
            Iterator var9 = param.keySet().iterator();

            while (var9.hasNext()) {
                key = (String) var9.next();
                builder.addParameter(key, param.get(key));
            }
        }

        URI uri = builder.build();
        HttpGet httpGet;
        if (timeoutSecond != null) {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeoutSecond).build();
            httpGet = new HttpGet(uri);
            httpGet.setConfig(requestConfig);
        } else {
            httpGet = new HttpGet(uri);
        }

        try {
            httpGet.setHeader(LegalPersonIdHeader(lid));
            response = httpclient.execute(httpGet);
        } catch (Exception var12) {
            resultMap.put("STATUS", "FAIL");
            resultMap.put("MESSAGE", "调用失败, 可能原因: ".concat(var12.getMessage()));
            return resultMap;
        }

        resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        resultMap.put("MESSAGE", resultString);
        if (response.getStatusLine().getStatusCode() == 200) {
            resultMap.put("STATUS", "SUCCESS");
        } else {
            resultMap.put("STATUS", "FAIL");
        }

        response.close();
        return resultMap;
    }

    public static Header LegalPersonIdHeader(String lid) {
        return new BasicHeader("remarkHeader-cms", lid);
    }

    public static CloseableHttpClient initHttpClient() throws BusinessException {
        CloseableHttpClient closeableHttpClient = SpringContextUtil.getBean("httpClient");
        if (closeableHttpClient == null) {
            log.error("连接池获取异常");
            throw new BusinessException(EmBusinessError.HTTP_POOL_ERROR);
        } else {
            return closeableHttpClient;
        }
    }

    public static void main(String[] args) throws BusinessException, InterruptedException {

        System.out.println(Integer.parseInt(""));
    }

}
