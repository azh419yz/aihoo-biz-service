package com.aihoo.api.doctor.common;

import com.aihoo.upload.UploadUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: sunjianbo
 * @date: 2019/8/8 18:58
 */
@RestController
@RequestMapping("/api/v1/upload")
public class UploadController {

    /**
     * 上传文件
     *
     * @param imgFile
     * @return
     */
    @PostMapping("/uploadFile")
    public Map<String, Object> uploadManyFile(@RequestParam(value = "imgFile", required = false) MultipartFile[] imgFile) {
        Map<String, Object> result = new HashMap<>();
        if (null == imgFile && imgFile.length == 0) {
            result.put("success", false);
            result.put("message", "文件必传");
            return result;
        }
        StringBuffer sb = new StringBuffer();
        for (MultipartFile multipartFile : imgFile) {
            String path = UploadUtils.uploadFile(multipartFile);
            if (sb.toString().isEmpty()) {
                sb.append(path);
            } else {
                sb.append(",").append(path);
            }
        }
        result.put("success", true);
        result.put("data", sb.toString());
        return result;
    }
}
