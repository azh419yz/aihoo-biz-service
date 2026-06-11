package com.aihoo.api.doctor.common.utils;


import com.alibaba.fastjson2.JSON;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by john on 16/9/7.
 */
public class ResponseUtil {


    public static void responeJsonStr(Integer statusCode, HttpServletResponse response, Object object) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf8");
            response.setStatus(statusCode);
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(object));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
