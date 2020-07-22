package com.zte.clonedata.util;

import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.dao.IpProxyMapper;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.model.IpProxy;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.model.error.EmBusinessError;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
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

    private static IpProxyMapper ipProxyMapper;

    public static boolean testGet(String url, String host, HttpHost proxy) throws BusinessException {
        CloseableHttpClient client = initHttpClient();
        HttpGet httpGet = null;
        HttpResponse response = null;
        Random random = new Random();
        int useragent = random.nextInt(USER_AGENT_LENGTH);
        try {
            httpGet = new HttpGet(url);
            RequestConfig defaultConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .setProxy(proxy)
                    .setConnectTimeout(20000)
                    .setSocketTimeout(20000)
                    .setConnectionRequestTimeout(3000)
                    .build();
            httpGet.setConfig(defaultConfig);
            httpGet.setHeader("Host", host);
            httpGet.setHeader("User-Agent", USER_AGENT[useragent]);
            response = client.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
             return false;
        } finally {
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                throw new BusinessException(EmBusinessError.IO_ERROR);
            }
        }
    }

    public static String getJson(String url, String host, HttpType httpType) throws BusinessException {
        log.debug("即将访问: {}, GET", url);
        long start = System.currentTimeMillis();
        CloseableHttpClient client = initHttpClient();//Spring: 连接池
        //HttpClient client = HttpClients.createDefault();//main: 创建一个
        HttpGet httpGet = null;
        HttpResponse response = null;
        String resultJson = null;
        Random random = new Random();
        IpProxy ipProxy = null;
        boolean b = false;
        int useragent = random.nextInt(USER_AGENT_LENGTH);
        try {
            httpGet = new HttpGet(url);
            RequestConfig defaultConfig = null;
            if (HttpType.DETAIL.equals(httpType)) {
                defaultConfig = getRequestConfigBuilder().build();
            } else {
                int i = random.nextInt(10);
                HttpHost httpHost = null;
                if (i < 3 && RunningContanst.IS_PROXY) {
                    if (ipProxyMapper == null) {
                        ipProxyMapper = SpringContextUtil.getBean(IpProxyMapper.class);
                    }
                    ipProxy = ipProxyMapper.selectRandOne();
                    if (ipProxy != null) {
                        httpHost = new HttpHost(ipProxy.getIp(), ipProxy.getPort(),ipProxy.getHttpType());
                    }
                }
                defaultConfig = getRequestConfigBuilder().setProxy(httpHost).build();
            }
            httpGet.setConfig(defaultConfig);
            httpGet.setHeader("Host", host);
            httpGet.setHeader("User-Agent", USER_AGENT[useragent]);
            if (HttpType.DOUBAN_TY.equals(httpType) || HttpType.DOUBAN_MY.equals(httpType)) {
                //httpGet.setHeader("Referer","https://movie.douban.com/tag/");
            } else if (HttpType.MAOYAN_MV.equals(httpType)) {
                httpGet.setHeader("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
                //httpGet.setHeader("Cookie","bid=JY6Z9MgBEUM; _pk_ref.100001.4cf6=%5B%22%22%2C%22%22%2C1594190056%2C%22https%3A%2F%2Fwww.douban.com%2F%22%5D; _pk_id.100001.4cf6=33cccbc7ed3b8084.1592294430.9.1594190503.1593679842.; __utma=30149280.1528111153.1592294430.1593679843.1594190052.9; __utmz=30149280.1594190052.9.2.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utma=223695111.1954929102.1592294430.1593679843.1594190056.9; __utmz=223695111.1594190056.9.2.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/; dbcl2=\"188549964:fkDizA1bvRg\"; push_noty_num=0; push_doumail_num=0; __yadk_uid=oGdqqqPQkm5WnA9ajenYVtpN4BL6ZWSl; ll=\"118318\"; _vwo_uuid_v2=D52F50684BE55ADE5712B0F5A24ACBDF7|1dbe3d140be4213889241951b91c9995; __gads=ID=344007d69055847d:T=1592811923:S=ALNI_MYWPNUH2Ls2UHp_1zth8pM9S-2UgA; __utmv=30149280.18854; ck=aIyW; ap_v=0,6.0; __utmb=30149280.2.10.1594190052; __utmc=30149280; __utmt=1; _pk_ses.100001.4cf6=*; __utmb=223695111.0.10.1594190056; __utmc=223695111");
            }
            response = client.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                if (!HttpType.DETAIL.equals(httpType) && ipProxy != null) {
                    ipProxyMapper.updateExecuteCount(ipProxy.getId());
                }
                resultJson = EntityUtils
                        .toString(response.getEntity(), Contanst.CHARSET);
                b = true;
                return resultJson;
            } else {
                if (!HttpType.DETAIL.equals(httpType) && ipProxy != null) {
                    ipProxyMapper.updateErrorCount(ipProxy.getId());
                }
                throw new BusinessException(EmBusinessError.HTTP_ERROR, "HttpStatus: ".concat(String.valueOf(code)));
        }
        } catch (ClientProtocolException e) {
            throw new BusinessException(EmBusinessError.HTTP_POOL_ERROR);
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                throw new BusinessException(EmBusinessError.HTTP_ERROR);
            } else if (e instanceof ConnectTimeoutException || e instanceof ConnectException) {
                if (!HttpType.DETAIL.equals(httpType) && ipProxy != null) {
                    ipProxyMapper.updateErrorCount(ipProxy.getId());
                }
                throw new BusinessException(EmBusinessError.HTTP_RESULT_OVERTIME);
            } else {
                throw new BusinessException(EmBusinessError.IO_ERROR);
            }
        } finally {
            try {
                if (response != null) EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                throw new BusinessException(EmBusinessError.IO_ERROR);
            }
            /**
             * 删除代理IP
             *   1. 访问时长大于100秒
             *   2. 年轻代 代理(TODO 后续发生老年代代理HTTP长连接再处理,暂时不处理)
             */
            if (!b && (System.currentTimeMillis() - start)>100000 && ipProxy != null && ipProxy.getIsOld() == 0){
                SpringContextUtil.getBean(IpProxyMapper.class).deleteByPrimaryKey(ipProxy.getId());
            }
        }
    }

    public static void picGetFileSave(String url, OutputStream outputStream) throws BusinessException {
        log.debug("即将访问: {}, GET",url);
        CloseableHttpClient client = initHttpClient();//Spring: 连接池
        HttpGet httpGet = null;
        HttpResponse response = null;
        try {
            httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpGet.setConfig(getRequestConfigBuilder().build());
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
            try {
                if (response != null) EntityUtils.consume(response.getEntity());
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

    public static RequestConfig.Builder getRequestConfigBuilder() throws BusinessException {
        RequestConfig.Builder requestConfigBuilder = SpringContextUtil.getBean("requestConfigBuilder");
        if (requestConfigBuilder == null) {
            log.error("获取连接池配置异常");
            throw new BusinessException(EmBusinessError.HTTP_POOL_ERROR, "获取连接池配置异常");
        } else {
            return requestConfigBuilder;
        }
    }

    public static void main(String[] args) throws BusinessException, InterruptedException {

    }

}
