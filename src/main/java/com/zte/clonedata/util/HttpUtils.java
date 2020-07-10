package com.zte.clonedata.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.model.error.EmBusinessError;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
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
    private HttpUtils(){}

    public static String getJson(String url,String host,String type) throws BusinessException {
        //log.info("即将访问: {}, GET",url);
        //CloseableHttpClient client = initHttpClient();//Spring: 连接池
        HttpClient client = HttpClients.createDefault();//main: 创建一个
        HttpGet httpGet = null;
        HttpResponse response = null;
        String resultJson = null;
        try {
            httpGet = new HttpGet(url);
            RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
            httpGet.setConfig(defaultConfig);
            httpGet.setHeader("Host", host);
            httpGet.setHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:78.0) Gecko/20100101 Firefox/78.0");
            if ("maoyan".equals(type)){
                //httpGet.setHeader("Cookie","__mta=214750760.1592806320774.1593772067196.1593772385742.33; _lxsdk_cuid=172daa762aa4e-095f68aa4d48bd8-4c302c7d-144000-172daa762abc8; _lxsdk=476419A0B44F11EA9736CB82F1E06989E468DF48A90A44F993FD5A067999536C; uuid_n_v=v1; uuid=476419A0B44F11EA9736CB82F1E06989E468DF48A90A44F993FD5A067999536C; Hm_lvt_703e94591e87be68cc8da0da7cbd0be2=1593745810,1593745825,1594003987,1594004433; __mta=214750760.1592806320774.1593772385742.1594004433326.34; mojo-uuid=bfd847c2f22e8b4191e2c3c094fd38a4; recentCis=1; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; _csrf=1fa803620f999baed2764fed46a88e25ae473a7b965a9ed1d02da2f125a83eae; mojo-trace-id=4; mojo-session-id={\"id\":\"f284269393dd920c392802a21ec7ff68\",\"time\":1594003986721}; _lxsdk_s=173220a99a8-224-f08-e10%7C%7C5; Hm_lpvt_703e94591e87be68cc8da0da7cbd0be2=1594004433; lt=2swP42j3bfpYgKlNrDiBIZfxi5YAAAAAAAsAAGYsLtQzd4XZu0zKKiO4BO7WzjGyDcfp650e32d3FOdJBKR8Csl637u0OcN7yO2IAA; lt.sig=JVdlfRd2Bd35Aii3JggmymPaWdY");
            }else if ("douban".equals(type)){
                httpGet.setHeader("Cookie","bid=JY6Z9MgBEUM; _pk_ref.100001.4cf6=%5B%22%22%2C%22%22%2C1594190056%2C%22https%3A%2F%2Fwww.douban.com%2F%22%5D; _pk_id.100001.4cf6=33cccbc7ed3b8084.1592294430.9.1594190503.1593679842.; __utma=30149280.1528111153.1592294430.1593679843.1594190052.9; __utmz=30149280.1594190052.9.2.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utma=223695111.1954929102.1592294430.1593679843.1594190056.9; __utmz=223695111.1594190056.9.2.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/; dbcl2=\"188549964:fkDizA1bvRg\"; push_noty_num=0; push_doumail_num=0; __yadk_uid=oGdqqqPQkm5WnA9ajenYVtpN4BL6ZWSl; ll=\"118318\"; _vwo_uuid_v2=D52F50684BE55ADE5712B0F5A24ACBDF7|1dbe3d140be4213889241951b91c9995; __gads=ID=344007d69055847d:T=1592811923:S=ALNI_MYWPNUH2Ls2UHp_1zth8pM9S-2UgA; __utmv=30149280.18854; ck=aIyW; ap_v=0,6.0; __utmb=30149280.2.10.1594190052; __utmc=30149280; __utmt=1; _pk_ses.100001.4cf6=*; __utmb=223695111.0.10.1594190056; __utmc=223695111");
            }
            response = client.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                resultJson = EntityUtils
                        .toString(response.getEntity(), Contanst.CHARSET);
                return resultJson;
            }else {
                throw new BusinessException(EmBusinessError.HTTP_ERROR,"HttpStatus: ".concat(String.valueOf(code)));
            }
        } catch (ClientProtocolException e) {
            throw new BusinessException(EmBusinessError.HTTP_POOL_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof UnknownHostException){
                throw new BusinessException(EmBusinessError.HTTP_ERROR);
            }else {
                throw new BusinessException(EmBusinessError.IO_ERROR);
            }
        }finally {
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
            if (e instanceof UnknownHostException){
                throw new BusinessException(EmBusinessError.HTTP_ERROR);
            }else {
                throw new BusinessException(EmBusinessError.IO_ERROR,e.getMessage());
            }
        }finally {
            // 释放资源
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                throw new BusinessException(EmBusinessError.IO_ERROR,e.getMessage());
            }
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new BusinessException(EmBusinessError.IO_ERROR,e.getMessage());
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

            while(var9.hasNext()) {
                key = (String)var9.next();
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

        /*String url = "https://movie.douban.com/subject/30463904/?tag=最新&from=gaia";
        String maoyan = HttpUtils.getJson(url, Contanst.DOUBAN_HOST1, "douban");
        System.out.println(1);*/
        byte b = 127;
        System.out.println(Integer.parseInt("2.6"));
    }

}
