package com.zte.clonedata.contanst;

import com.zte.clonedata.service.DataTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * ProjectName: clonedata-com.zte.clonedata.contanst
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 16:28 2020/6/16
 * @Description:
 */
@Configuration
public class RunningContanst {

    /**
     * 豆瓣 id
     */
    public static String TYPE_DOUBAN_ID;
    /**
     * 豆瓣 id
     */
    public static String TYPE_MAOYAN_ID;
    /**
     * Common
     */
    @Value("${project.baseUrl}")
    public String BASEURL;
    /**
     * IP代理开关
     */
    public static boolean IS_OPEN_IP_PROXY = false;
    /**
     * IP代理比例
     */
    public static int PROXY_PPROPORTION = 0;
    /**
     * FTP相关
     */
    public static String FTP_ADDRESS;
    public static int FTP_PORT;
    public static String FTP_USERNAME;
    public static String FTP_PASSWORD;
    public static String FTP_BASEPATH;
    public static boolean isopen;

    @Autowired
    private DataTypeService dataTypeService;
    @PostConstruct
    public void init(){
        TYPE_DOUBAN_ID = dataTypeService.getId(JobContanst.DOUBAN);
        TYPE_MAOYAN_ID = dataTypeService.getId(JobContanst.MAOYAN);
    }


    @Value("${proxy.isOpenIpProxy:false}")
    public void setIsOpenIpProxy(boolean isOpenIpProxy){
        RunningContanst.IS_OPEN_IP_PROXY = isOpenIpProxy;
    }
    @Value("${proxy.proxypProportion:0}")
    public void setProxyPproportion(int i){
        RunningContanst.PROXY_PPROPORTION = i;
    }
    @Value("${ftp.host:}")
    public void setFtpAddress(String address){
        RunningContanst.FTP_ADDRESS = address;
    }
    @Value("${ftp.port:0}")
    public void setFtpPort(int i){
        RunningContanst.FTP_PORT = i;
    }
    @Value("${ftp.username:}")
    public void setFtpUsername(String username){
        RunningContanst.FTP_USERNAME = username;
    }
    @Value("${ftp.password:}")
    public void setFtpPassword(String password){
        RunningContanst.FTP_PASSWORD = password;
    }
    @Value("${ftp.savepath:}")
    public void setFtpBasepath(String path){
        RunningContanst.FTP_BASEPATH = path;
    }
    @Value("${ftp.isopen:false}")
    public void setIsopen(boolean b){
        RunningContanst.isopen = b;
    }
}
