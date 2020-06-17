package com.zte.clonedata.util;

import com.alibaba.fastjson.JSONObject;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.model.error.EmBusinessError;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
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
    private HttpUtils(){}

    public static String getJson(String url,String host) throws BusinessException {
        //log.info("即将访问: {}, GET",url);
        //CloseableHttpClient client = initHttpClient();//Spring: 连接池
        HttpClient client = HttpClients.createDefault();//main: 创建一个
        HttpGet httpGet = null;
        HttpResponse response = null;
        String resultJson = null;
        try {
            httpGet = new HttpGet(url);
            httpGet.setHeader("Host", host);
            httpGet.setHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11");
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
        while (true) {
            Random random = new Random();
            int i = random.nextInt(10000);
            String json = HttpUtils.getJson("https://movie.douban.com/j/new_search_subjects?sort=U&range=0,10&tags=%E7%94%B5%E5%BD%B1&start="+i+"&countries=%E4%B8%AD%E5%9B%BD%E5%A4%A7%E9%99%86&", Contanst.DOUBAN_HOST1);
            System.out.println(json);
            if (json.contains("IP")){
                break;
            }
            System.out.println(">>>>");
            Thread.sleep(2000);
        }
    }
}
