package com.zte.clonedata.util;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * ProjectName: clonedata-com.zte.clonedata.util
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 15:24 2020/5/29
 * @Description:
 */
@Slf4j
@Component
public class JDBCUtils {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    public Connection getCon() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        Connection con = DriverManager.getConnection(url,username,password);
        return con;
    }



    public void delete() throws SQLException, ClassNotFoundException {
        Connection con = getCon();
        String yyyyMMdd = DateTime.now().minusDays(7).toString("yyyyMMdd");
        con.close();
    }
}
