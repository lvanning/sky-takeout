package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name = "通用接口")
@RequestMapping("/admin/common")
public class CommonController {
        
        private AliOssUtil aliOssUtil;
        public CommonController(AliOssUtil aliOssUtil) {
                this.aliOssUtil = aliOssUtil;
        }
        
        @PostMapping("/upload")
        public Result<String> upload(MultipartFile file) {
                log.info("上传图片");
                try {
                        String extensionName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                        String objectName = UUID.randomUUID().toString() + extensionName;
                        String url = aliOssUtil.upload(file.getInputStream(), objectName);
                        log.info("图片上传成功，图片url: {}", url);
                        return Result.success(url);
                } catch (IOException e) {
                        log.error("图片上传失败", e);
                        return Result.error(MessageConstant.UPLOAD_FAILED);
                }
        }
        
}
