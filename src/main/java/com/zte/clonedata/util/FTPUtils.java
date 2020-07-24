package com.zte.clonedata.util;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;
import java.util.Properties;

import static com.zte.clonedata.contanst.ChangeRunningContanst.SLEEP_FTP_SPAN_TIME;
import static com.zte.clonedata.contanst.RunningContanst.*;

/**
 * ProjectName: clonedata-com.zte.clonedata.util
 *
 <dependency>
 <groupId>com.jcraft</groupId>
 <artifactId>jsch</artifactId>
 <version>0.1.55</version>
 </dependency>
 *
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 9:49 2020/6/2
 * @Description:
 */
@Slf4j
public class FTPUtils {
    private ChannelSftp sftp = null;
    private Session sshSession = null;

    /**
     * 连接sftp服务器
     * @return ChannelSftp sftp连接实例
     */
    public ChannelSftp connect() {
        if (!isopen || StringUtils.isEmpty(FTP_ADDRESS)){//FTP上传
            return null;
        }
        log.info("-->sftp连接开始>>>>>> " + FTP_ADDRESS + ":" + FTP_PORT + " >>>username=" + FTP_USERNAME);
        JSch jsch = new JSch();
        try {
            sshSession = jsch.getSession(FTP_USERNAME, FTP_ADDRESS, FTP_PORT);
            sshSession.setPassword(FTP_PASSWORD);
            Properties properties = new Properties();
            properties.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(properties);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            log.info(" ftp Connected to " + FTP_ADDRESS + ":" + FTP_PORT);
        } catch (JSchException e) {
            throw new RuntimeException("sftp连接失败", e);
        }
        return sftp;
    }
    /**
     * 上传文件
     */
    public void uploadFile(List<File> fileList){
        uploadFile(fileList,true);
    }
    /**
     * 上传文件
     */
    public void uploadFile(List<File> fileList,boolean isClose){
        if (!isopen || StringUtils.isEmpty(FTP_ADDRESS)){//FTP上传
            return;
        }
        log.info(">>>>>>>>>uploadFile--ftp上传文件开始>>>>>>>>>>>>>");
        long start = System.currentTimeMillis();
        if (sftp == null){
            connect();
        }
        try {
            sftp.cd(FTP_BASEPATH);
        } catch (SftpException e) {
            try {
                sftp.mkdir(FTP_BASEPATH);
                sftp.cd(FTP_BASEPATH);
            } catch (SftpException e1) {
                log.error("ftp创建文件路径失败，路径为" + FTP_BASEPATH);
                throw new RuntimeException("ftp创建文件路径失败" + FTP_BASEPATH);
            }
        }
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            InputStream inputStream =null;
            try {
                inputStream = new FileInputStream(file);
                String filename = file.getName();
                sftp.put(inputStream, filename);
                Thread.sleep(SLEEP_FTP_SPAN_TIME);
            } catch (SftpException e) {
                log.error("sftp异常-->", e);
            } catch (FileNotFoundException e) {
                log.error("FileInputStream-->", e);
            } catch (InterruptedException e) {
                log.error("sleep-->", e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.info("Close stream error." + e.getMessage());
                    }
                }
            }
        }
        //关闭资源
        if (isClose){
            disconnect();
        }
        log.info(">>>>>>>>>uploadFile--ftp上传文件结束,用时: {}>>>>>>>>>>>>>",System.currentTimeMillis()-start);
    }

    /**
     * 关闭连接
     */
    public void disconnect() {
        if (this.sftp != null) {
            if (this.sftp.isConnected()) {
                this.sftp.disconnect();
                this.sftp = null;
                log.info("sftp 连接已关闭！");
            }
        }
        if (this.sshSession != null) {
            if (this.sshSession.isConnected()) {
                this.sshSession.disconnect();
                this.sshSession = null;
                log.info("sshSession 连接已关闭！");
            }
        }
    }


}
