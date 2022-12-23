package com.carl.reggie.controller;

import com.carl.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @program: reggie
 * @description: 文件上传和下载
 * @author: Mr.Carl
 * @create: 2022-12-20 19:11
 **/
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /***
     * create time: 2022/12/20 19:12
     * @Description: 文件上传
     * http://localhost:8080/backend/page/demo/upload.html
     * http://localhost:8080/common/upload
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件
        log.info(file.toString());

        //获得原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID生成文件名,防止文件名字重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    /***
     * create time: 2022/12/20 20:00
     * @Description: 文件下载
     * @param: [java.lang.String, javax.servlet.http.HttpServletResponse]
     * @return: void
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流读取内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流,通过输出流将文件写回浏览器,在浏览器展示
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len =  fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }




    }
}
