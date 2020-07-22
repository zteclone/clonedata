package com.zte.clonedata.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * ProjectName: clonedata-com.zte.clonedata
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 9:51 2020/7/21
 * @Description:
 */
@Controller
public class ErrorWeb implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request){
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode >= 400){
            return "访问错误";
        }

        return "访问错误 ";
    }
}
