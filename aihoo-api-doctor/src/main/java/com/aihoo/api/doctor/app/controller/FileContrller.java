package com.aihoo.api.doctor.app.controller;


import com.aihoo.util.OssFileUtils;
import com.aihoo.common.JsonResult;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

@Controller
@RequestMapping("/api/v1/file")
public class FileContrller {

    @PostMapping("/upload")
    @ResponseBody
    public JsonResult upload(MultipartFile file) {
        try {
            if (null == file && file.getSize() == 0) {
                return JsonResult.error("请选择文件");
            }
            String url = OssFileUtils.uploadFile(file);
            JSONObject data = new JSONObject();
            data.put("src", url);
            return JsonResult.ok("操作成功").put("data", data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }
}
